package badgamesinc.hypnotic.utils.text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Matrix4f;

public class Utils {

	public static boolean rendering3D = true;
	
	public static void unscaledProjection() {
        RenderSystem.setProjectionMatrix(Matrix4f.projectionMatrix(0, MinecraftClient.getInstance().getWindow().getFramebufferWidth(), 0, MinecraftClient.getInstance().getWindow().getFramebufferHeight(), 1000, 3000));
        rendering3D = false;
    }

    public static void scaledProjection() {
        RenderSystem.setProjectionMatrix(Matrix4f.projectionMatrix(0, (float) (MinecraftClient.getInstance().getWindow().getFramebufferWidth() / MinecraftClient.getInstance().getWindow().getScaleFactor()), 0, (float) (MinecraftClient.getInstance().getWindow().getFramebufferHeight() / MinecraftClient.getInstance().getWindow().getScaleFactor()), 1000, 3000));
        rendering3D = true;
    }
    
    public static byte[] readBytes(File file) {
        try {
            InputStream in = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            byte[] buffer = new byte[256];
            int read;
            while ((read = in.read(buffer)) > 0) out.write(buffer, 0, read);

            in.close();
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new byte[0];
    }
}
