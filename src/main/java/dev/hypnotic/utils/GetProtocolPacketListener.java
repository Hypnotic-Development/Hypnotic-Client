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
package dev.hypnotic.utils;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.ClientQueryPacketListener;
import net.minecraft.network.packet.s2c.query.QueryPongS2CPacket;
import net.minecraft.network.packet.s2c.query.QueryResponseS2CPacket;
import net.minecraft.text.Text;

public class GetProtocolPacketListener implements ClientQueryPacketListener {

    private final ClientConnection connection;
    private int protocol;
    private volatile boolean completed = false;
    private boolean failed = false;

    public GetProtocolPacketListener(ClientConnection connection) {
        this.connection = connection;
    }

    @Override
    public void onResponse(QueryResponseS2CPacket packet) {
        protocol = packet.getServerMetadata().getVersion().getProtocolVersion();
        completed = true;
        connection.disconnect(Text.translatable("multiplayer.status.finished"));
    }

    @Override
    public void onPong(QueryPongS2CPacket packet) {
    }

    @Override
    public void onDisconnected(Text reason) {
        if (!completed) {
            completed = true;
            failed = true;
        }
    }

    @Override
    public ClientConnection getConnection() {
        return connection;
    }

    public int getProtocol() {
        return protocol;
    }

    public boolean hasCompleted() {
        return completed;
    }

    public boolean hasFailed() {
        return failed;
    }
}

