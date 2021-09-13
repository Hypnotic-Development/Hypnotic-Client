package badgamesinc.hypnotic.utils.input;

import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class KeyUtils {
    private static final boolean[] keys = new boolean[512];
    private static final boolean[] buttons = new boolean[16];

    public static void setKeyState(int key, boolean pressed) {
        if (key >= 0 && key < keys.length) keys[key] = pressed;
    }

    public static void setButtonState(int button, boolean pressed) {
        if (button >= 0 && button < buttons.length) buttons[button] = pressed;
    }

    public static void setKeyState(KeyBinding bind, boolean pressed) {
        setKeyState(Keys.getKey(bind), pressed);
    }

    public static boolean isPressed(KeyBinding bind) {
        return isKeyPressed(Keys.getKey(bind));
    }

    public static boolean isKeyPressed(int key) {
        if (key == GLFW.GLFW_KEY_UNKNOWN) return false;
        return key < keys.length && keys[key];
    }

    public static boolean isButtonPressed(int button) {
        if (button == -1) return false;
        return button < buttons.length && buttons[button];
    }
    
    public static int getKeyCode(String key) {
    	switch (key.toLowerCase()) {
    		case ".": return GLFW.GLFW_KEY_PERIOD; 
    		case "1": return GLFW.GLFW_KEY_1; 
    		case "2": return GLFW.GLFW_KEY_2; 
    		case "3": return GLFW.GLFW_KEY_3; 
    		case "4": return GLFW.GLFW_KEY_4; 
    		case "5": return GLFW.GLFW_KEY_5; 
    		case "6": return GLFW.GLFW_KEY_6; 
    		case "7": return GLFW.GLFW_KEY_7; 
    		case "8": return GLFW.GLFW_KEY_8; 
    		case "9": return GLFW.GLFW_KEY_9; 
    		case "0": return GLFW.GLFW_KEY_0; 
    		case ",": return GLFW.GLFW_KEY_COMMA;
    		case "a": return GLFW.GLFW_KEY_A; 
    		case "b": return GLFW.GLFW_KEY_B; 
    		case "c": return GLFW.GLFW_KEY_C; 
    		case "d": return GLFW.GLFW_KEY_D; 
    		case "e": return GLFW.GLFW_KEY_E; 
    		case "f": return GLFW.GLFW_KEY_F; 
    		case "g": return GLFW.GLFW_KEY_G; 
    		case "h": return GLFW.GLFW_KEY_H; 
    		case "i": return GLFW.GLFW_KEY_I; 
    		case "j": return GLFW.GLFW_KEY_J; 
    		case "k": return GLFW.GLFW_KEY_K; 
    		case "l": return GLFW.GLFW_KEY_L; 
    		case "m": return GLFW.GLFW_KEY_M; 
    		case "n": return GLFW.GLFW_KEY_N; 
    		case "o": return GLFW.GLFW_KEY_O; 
    		case "p": return GLFW.GLFW_KEY_P; 
    		case "q": return GLFW.GLFW_KEY_Q; 
    		case "r": return GLFW.GLFW_KEY_R; 
    		case "s": return GLFW.GLFW_KEY_S; 
    		case "t": return GLFW.GLFW_KEY_T; 
    		case "u": return GLFW.GLFW_KEY_U; 
    		case "v": return GLFW.GLFW_KEY_V; 
    		case "w": return GLFW.GLFW_KEY_W; 
    		case "x": return GLFW.GLFW_KEY_X; 
    		case "y": return GLFW.GLFW_KEY_Y; 
    		case "z": return GLFW.GLFW_KEY_Z; 
    		case "`": return GLFW.GLFW_KEY_GRAVE_ACCENT; 
    		case "rshift": return GLFW.GLFW_KEY_RIGHT_SHIFT;
    		case "lshift": return GLFW.GLFW_KEY_LEFT_SHIFT; 
    		case "\\": return GLFW.GLFW_KEY_BACKSLASH;
    		case "space": return GLFW.GLFW_KEY_SPACE;
    		case ";": return GLFW.GLFW_KEY_SEMICOLON;
    		case "-": return GLFW.GLFW_KEY_MINUS;
    		case "=": return GLFW.GLFW_KEY_EQUAL;
    		case "[": return GLFW.GLFW_KEY_LEFT_BRACKET;
    		case "]": return GLFW.GLFW_KEY_RIGHT_BRACKET;
    		case "/": return GLFW.GLFW_KEY_SLASH;
    		case "'": return GLFW.GLFW_KEY_APOSTROPHE;
    	}
    	return GLFW.GLFW_KEY_UNKNOWN;
    }

	public static int getKeyCode(char key) {
		String stringKey = new StringBuilder().append(key).toString();
		return getKeyCode(stringKey);
	}
}
