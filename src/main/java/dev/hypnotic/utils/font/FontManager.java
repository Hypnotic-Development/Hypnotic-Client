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
package dev.hypnotic.utils.font;

import dev.hypnotic.utils.Utils;

public class FontManager {

	public static FontManager INSTANCE = new FontManager();
	public static String font = "Roboto-Regular.ttf";
	public static final String assets = "assets/hypnotic/fonts/";
	public static int size = 20;
	public static boolean mcFont = false;
	public static NahrFont robotoSmaller = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + font), 14, 1, mcFont);
	public static NahrFont robotoSmall = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + font), 16, 1, mcFont);
	public static NahrFont roboto = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + font), 18, 1, mcFont);
	public static NahrFont robotoMed = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + font), 20, 1, mcFont);
	public static NahrFont robotoMed2 = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + font), 22, 1, mcFont);
	public static NahrFont robotoBig = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + font), 25, 1, mcFont);
	public static NahrFont robotoCustomSize = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + font), size, 1, mcFont);
	public static NahrFont greycliff = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + "greycliff.ttf"), 20, 1, mcFont);
	public static NahrFont icons = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + "icons.ttf"), 21, 1, false);
	public static NahrFont iconsSmall = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + "icons.ttf"), 18, 1, false);
	
	public static void setMcFont(boolean mcFont) {
		FontManager.mcFont = mcFont;
		robotoSmaller = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + font), 14, 1, mcFont);
		robotoSmall = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + font), 16, 1, mcFont);
		roboto = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + font), 18, 1, mcFont);
		robotoMed = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + font), 20, 1, mcFont);
		robotoMed2 = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + font), 22, 1, mcFont);
		robotoBig = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + font), 25, 1, mcFont);
		robotoCustomSize = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + font), size, 1, mcFont);
		icons = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + "icons.ttf"), 21, 1, false);
		iconsSmall = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + "icons.ttf"), 18, 1, false);
	}
	
	public static void setSize(int size) {
		FontManager.size = size;
		robotoCustomSize = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + font), size, 1, mcFont);
	}
}
