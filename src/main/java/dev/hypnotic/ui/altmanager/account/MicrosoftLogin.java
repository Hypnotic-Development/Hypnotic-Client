/*
* Copyright (C) 2022 Hypnotic Development
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package dev.hypnotic.ui.altmanager.account;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import net.minecraft.util.Util;

//Credits (mostly) to meteor client (https://github.com/MeteorDevelopment/meteor-client)
public class MicrosoftLogin {
    public static class LoginData {
        public String mcToken;
        public String newRefreshToken;
        public String uuid, username;

        public LoginData() {}

        public LoginData(String mcToken, String newRefreshToken, String uuid, String username) {
            this.mcToken = mcToken;
            this.newRefreshToken = newRefreshToken;
            this.uuid = uuid;
            this.username = username;
        }

        public boolean isGood() {
            return mcToken != null;
        }
    }

    private static final String CLIENT_ID = "538a3bf8-38e9-49b3-8dec-019f73f89fdb";
    private static final int PORT = 9675;

    private static HttpServer server;
    private static Consumer<String> callback;

    public static void getRefreshToken(Consumer<String> callback) {
        MicrosoftLogin.callback = callback;

        startServer();
        Util.getOperatingSystem().open("https://login.live.com/oauth20_authorize.srf?client_id=" + CLIENT_ID + "&response_type=code&redirect_uri=http://127.0.0.1:" + PORT + "&scope=XboxLive.signin%20offline_access&prompt=select_account");
    }

    public static LoginData login(String refreshToken) {
        // Refresh access token
        AuthTokenResponse res = Http.post("https://login.live.com/oauth20_token.srf")
            .bodyForm("client_id=" + CLIENT_ID + "&refresh_token=" + refreshToken + "&grant_type=refresh_token&redirect_uri=http://127.0.0.1:" + PORT)
            .sendJson(AuthTokenResponse.class);

        if (res == null) return new LoginData();

        String accessToken = res.access_token;
        refreshToken = res.refresh_token;

        // XBL
        XblXstsResponse xblRes = Http.post("https://user.auth.xboxlive.com/user/authenticate")
            .bodyJson("{\"Properties\":{\"AuthMethod\":\"RPS\",\"SiteName\":\"user.auth.xboxlive.com\",\"RpsTicket\":\"d=" + accessToken + "\"},\"RelyingParty\":\"http://auth.xboxlive.com\",\"TokenType\":\"JWT\"}")
            .sendJson(XblXstsResponse.class);

        if (xblRes == null) return new LoginData();

        // XSTS
        XblXstsResponse xstsRes = Http.post("https://xsts.auth.xboxlive.com/xsts/authorize")
            .bodyJson("{\"Properties\":{\"SandboxId\":\"RETAIL\",\"UserTokens\":[\"" + xblRes.Token + "\"]},\"RelyingParty\":\"rp://api.minecraftservices.com/\",\"TokenType\":\"JWT\"}")
            .sendJson(XblXstsResponse.class);

        if (xstsRes == null) return new LoginData();

        // Minecraft
        McResponse mcRes = Http.post("https://api.minecraftservices.com/authentication/login_with_xbox")
            .bodyJson("{\"identityToken\":\"XBL3.0 x=" + xblRes.DisplayClaims.xui[0].uhs + ";" + xstsRes.Token + "\"}")
            .sendJson(McResponse.class);

        if (mcRes == null) return new LoginData();

        // Check game ownership
        GameOwnershipResponse gameOwnershipRes = Http.get("https://api.minecraftservices.com/entitlements/mcstore")
            .bearer(mcRes.access_token)
            .sendJson(GameOwnershipResponse.class);

        if (gameOwnershipRes == null || !gameOwnershipRes.hasGameOwnership()) return new LoginData();

        // Profile
        ProfileResponse profileRes = Http.get("https://api.minecraftservices.com/minecraft/profile")
            .bearer(mcRes.access_token)
            .sendJson(ProfileResponse.class);

        if (profileRes == null) return new LoginData();

        return new LoginData(mcRes.access_token, refreshToken, profileRes.id, profileRes.name);
    }

    private static void startServer() {
        if (server != null) return;
        try {
            server = HttpServer.create(new InetSocketAddress("127.0.0.1", PORT), 0);

            server.createContext("/", new Handler());
            server.setExecutor(Executors.newSingleThreadExecutor());
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void stopServer() {
        if (server == null) return;

        server.stop(0);
        server = null;

        callback = null;
    }

    private static class Handler implements HttpHandler {
        @Override
        public void handle(HttpExchange req) throws IOException {
            if (req.getRequestMethod().equals("GET")) {
                // Login
				List<NameValuePair> query = URLEncodedUtils.parse(req.getRequestURI(), StandardCharsets.UTF_8);

                boolean ok = false;

                for (NameValuePair pair : query) {
                    if (pair.getName().equals("code")) {
                        handleCode(pair.getValue());

                        ok = true;
                        break;
                    }
                }

                if (!ok) writeText(req, "<body style=\"font-family: sans-serif;\"><h1 style=\"color: #ff0098;\">Hypnotic Client</h1><p>Failed to authenticate with Microsoft.</p><body>");
                else writeText(req, "<body style=\"font-family: sans-serif;\"><h1 style=\"color: #ff0098;\">Hypnotic Client</h1><p>Successfully authenticated with Microsoft.<br>You may now close this page.</p></body>");
            }

            stopServer();
        }

        private void handleCode(String code) {
            AuthTokenResponse res = Http.post("https://login.live.com/oauth20_token.srf")
                .bodyForm("client_id=" + CLIENT_ID + "&code=" + code + "&grant_type=authorization_code&redirect_uri=http://127.0.0.1:" + PORT)
                .sendJson(AuthTokenResponse.class);

            if (res == null) callback.accept(null);
            else callback.accept(res.refresh_token);
        }

        private void writeText(HttpExchange req, String text) throws IOException {
            OutputStream out = req.getResponseBody();

            req.sendResponseHeaders(200, text.length());

            out.write(text.getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();
        }
    }

    private static class AuthTokenResponse {
        public String access_token;
        public String refresh_token;
    }

    private static class XblXstsResponse {
        public String Token;
        public DisplayClaims DisplayClaims;

        private static class DisplayClaims {
            private Claim[] xui;

            private static class Claim {
                private String uhs;
            }
        }
    }

    private static class McResponse {
        public String access_token;
    }

    private static class GameOwnershipResponse {
        private Item[] items;

        private static class Item {
            private String name;
        }

        private boolean hasGameOwnership() {
            boolean hasProduct = false;
            boolean hasGame = false;

            for (Item item : items) {
                if (item.name.equals("product_minecraft")) hasProduct = true;
                else if (item.name.equals("game_minecraft")) hasGame = true;
            }

            return hasProduct && hasGame;
        }
    }

    private static class ProfileResponse {
        public String id;
        public String name;
    }
}
