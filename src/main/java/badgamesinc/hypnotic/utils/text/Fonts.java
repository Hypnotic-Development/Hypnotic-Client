package badgamesinc.hypnotic.utils.text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import badgamesinc.hypnotic.Hypnotic;
import net.minecraft.client.MinecraftClient;

public class Fonts {
	private static MinecraftClient mc = MinecraftClient.getInstance();
    private static final String[] BUILTIN_FONTS = { "JetBrains Mono.ttf", "Comfortaa.ttf", "Tw Cen MT.ttf", "Pixelation.ttf", "Roboto-Regular.ttf"};
    public static final String DEFAULT_FONT = "Roboto-Regular.tff";
    private static final File FOLDER = new File("/assets/hypnotic/fonts");

    public static CustomTextRenderer CUSTOM_FONT;

    private static String lastFont = "";

    public static void init() {
        FOLDER.mkdirs();
        CUSTOM_FONT = new CustomTextRenderer(new File(Hypnotic.hypnoticDir, "fonts/" + DEFAULT_FONT));
        lastFont = DEFAULT_FONT;
    }
    
    public static String[] getAvailableFonts() {
        List<String> fonts = new ArrayList<>(4);

        File[] files = FOLDER.listFiles(File::isFile);
        if (files != null) {
            for (File file : files) {
                int i = file.getName().lastIndexOf('.');
                if (file.getName().substring(i).equals(".ttf")) {
                    fonts.add(file.getName().substring(0, i));
                }
            }
        }

        return fonts.toArray(new String[0]);
    }
}
