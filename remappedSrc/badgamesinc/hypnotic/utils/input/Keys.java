package badgamesinc.hypnotic.utils.input;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import badgamesinc.hypnotic.mixin.KeyBindingAccessor;

import java.util.Map;

public class Keys {
    private static final String CATEGORY = "Hypnotic Client";

    public static KeyBinding OPEN_CLICK_GUI = new KeyBinding("key.hypnotic-client.open-click-gui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, CATEGORY);

    public static KeyBinding[] apply(KeyBinding[] binds) {
        // Add category
        Map<String, Integer> categories = KeyBindingAccessor.getCategoryOrderMap();

        int highest = 0;
        for (int i : categories.values()) {
            if (i > highest) highest = i;
        }

        categories.put(CATEGORY, highest + 1);

        // Add key binding
        KeyBinding[] newBinds = new KeyBinding[binds.length + 1];

        System.arraycopy(binds, 0, newBinds, 0, binds.length);
        newBinds[binds.length] = OPEN_CLICK_GUI;

        return newBinds;
    }

    public static int getKey(KeyBinding bind) {
        return ((KeyBindingAccessor) bind).getKey().getCode();
    }
}
