package dev.hypnotic.hypnotic_client.utils.input;

import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.google.common.collect.Lists;

import net.minecraft.client.option.KeyBinding;

public class KeyUtils {
    
	public static KeyUtils INSTANCE = new KeyUtils();
	
	private static final boolean[] keys = new boolean[512];
    private static final boolean[] buttons = new boolean[16];
    
    public List<String> keyList = Lists.newArrayList();
    public List<Integer> keyList2 = Lists.newArrayList();
    
    public KeyUtils() {
    	addKeys();
    }

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
    
    public static int getKey(String key) {
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
    		case "rctrl": return GLFW.GLFW_KEY_RIGHT_CONTROL;
    		case "lctrl": return GLFW.GLFW_KEY_LEFT_CONTROL; 
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

	public static int getKey(char key) {
		String stringKey = new StringBuilder().append(key).toString();
		return getKey(stringKey);
	}
	
	public static int getKeyScanCode(String key) {
		return GLFW.glfwGetKeyScancode(getKey(key));
	}
	
	
	public void addKeys() {
		keyList.add(".");
		keyList.add("1");
		keyList.add("2");
		keyList.add("3");
		keyList.add("4");
		keyList.add("5");
		keyList.add("6");
		keyList.add("7");
		keyList.add("8");
		keyList.add("9");
		keyList.add("0");
		keyList.add(",");
		keyList.add("a");
		keyList.add("b");
		keyList.add("c");
		keyList.add("d");
		keyList.add("e");
		keyList.add("f");
		keyList.add("g");
		keyList.add("h");
		keyList.add("i");
		keyList.add("j");
		keyList.add("k");
		keyList.add("l");
		keyList.add("m");
		keyList.add("n");
		keyList.add("o");
		keyList.add("p");
		keyList.add("q");
		keyList.add("r");
		keyList.add("s");
		keyList.add("t");
		keyList.add("u");
		keyList.add("v");
		keyList.add("w");
		keyList.add("x");
		keyList.add("y");
		keyList.add("z");
		keyList.add("`");
		keyList.add("rshift");
		keyList.add("lshift");
		keyList.add("rctrl");
		keyList.add("lctrl");
		keyList.add("space");
		keyList.add(";");
		keyList.add("-");
		keyList.add("=");
		keyList.add("[");
		keyList.add("]");
		keyList.add("/");
		
		keyList2.add(getKey("."));
		keyList2.add(getKey("1"));
		keyList2.add(getKey("2"));
		keyList2.add(getKey("3"));
		keyList2.add(getKey("4"));
		keyList2.add(getKey("5"));
		keyList2.add(getKey("6"));
		keyList2.add(getKey("7"));
		keyList2.add(getKey("8"));
		keyList2.add(getKey("9"));
		keyList2.add(getKey("0"));
		keyList2.add(getKey(","));
		keyList2.add(getKey("a"));
		keyList2.add(getKey("b"));
		keyList2.add(getKey("c"));
		keyList2.add(getKey("d"));
		keyList2.add(getKey("e"));
		keyList2.add(getKey("f"));
		keyList2.add(getKey("g"));
		keyList2.add(getKey("h"));
		keyList2.add(getKey("i"));
		keyList2.add(getKey("j"));
		keyList2.add(getKey("k"));
		keyList2.add(getKey("l"));
		keyList2.add(getKey("m"));
		keyList2.add(getKey("n"));
		keyList2.add(getKey("o"));
		keyList2.add(getKey("p"));
		keyList2.add(getKey("q"));
		keyList2.add(getKey("r"));
		keyList2.add(getKey("s"));
		keyList2.add(getKey("t"));
		keyList2.add(getKey("u"));
		keyList2.add(getKey("v"));
		keyList2.add(getKey("w"));
		keyList2.add(getKey("x"));
		keyList2.add(getKey("y"));
		keyList2.add(getKey("z"));
		keyList2.add(getKey("`"));
		keyList2.add(getKey("rshift"));
		keyList2.add(getKey("lshift"));
		keyList2.add(getKey("rctrl"));
		keyList2.add(getKey("lctrl"));
		keyList2.add(getKey("space"));
		keyList2.add(getKey(";"));
		keyList2.add(getKey("-"));
		keyList2.add(getKey("="));
		keyList2.add(getKey("["));
		keyList2.add(getKey("]"));
		keyList2.add(getKey("/"));
	}
}
