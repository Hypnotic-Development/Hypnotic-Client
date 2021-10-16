package badgamesinc.hypnotic.ui.clickgui2;

import java.awt.Color;

import com.mojang.blaze3d.systems.RenderSystem;

import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.module.render.ClickGUIModule;
import badgamesinc.hypnotic.ui.HudEditorScreen;
import badgamesinc.hypnotic.ui.OptionsScreen;
import badgamesinc.hypnotic.ui.WaypointManagerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import static badgamesinc.hypnotic.utils.MCUtils.mc;

public class MenuBar {

	public static MenuBar INSTANCE = new MenuBar(Tab.CLICKGUI);
	private Tab currentTab;
	private int x, y, width, height;
	
	
	public MenuBar(Tab currentTab) {
		this.currentTab = currentTab;
		int count = 10;
		for (Tab tab : Tab.values()) {
			tab.offset = count;
			tab.width = 100;
			tab.height = 100;
			count+=100;
		}
		this.width = Tab.values().length * 50;
		this.height = 35;
	}
	
	int animTicks = 0;

	public void renderMenuBar(MatrixStack matrices, int mouseX, int mouseY, int scaledWidth, int scaledHeight) {
		this.x = (scaledWidth / 2) - width / 2;
		this.y = scaledHeight;
		if (hovered(mouseX, mouseY)) {
			animTicks = 35;
//			if (animTicks < height) animTicks+=3;
		} else {
			animTicks = 35;
//			if (animTicks > 0) animTicks-=3;
		}
		
		Screen.fill(matrices, x, y, x + width, y - animTicks, new Color(0, 0, 0, 200).getRGB());
		int count = 0;
		for (Tab tab : Tab.values()) {
			tab.offset = count;
			tab.x = x + 7 + tab.offset;
			tab.y = (y - animTicks) + 5;
			tab.width = 30;
			tab.height = 30;
			RenderSystem.setShaderTexture(0, new Identifier("hypnotic", "textures/" + tab.name.toLowerCase() + ".png"));
			if (hoveredTab(tab, mouseX, mouseY) || tab.equals(currentTab)) RenderSystem.setShaderColor(0.6f, 0.6f, 0.6f, 0.6f);
			else RenderSystem.setShaderColor(1, 1, 1, 1);
			Screen.drawTexture(matrices, tab.x, tab.y, 0, 0, tab.width, tab.height, tab.width, tab.height);
			
			count+=50;
		}
	}
	
	public boolean hovered(int mouseX, int mouseY) {
		return mouseX >= x && mouseX <= x + width && mouseY <= y && mouseY >= y - height;
	}
	
	public boolean hoveredTab(Tab tab, int mouseX, int mouseY) {
		return mouseX >= tab.x && mouseX <= tab.x + tab.width && mouseY >= tab.y && mouseY <= tab.y + tab.width;
	}
	
	public Tab getCurrentTab() {
		return currentTab;
	}
	
	public void setCurrentTab(Tab currentTab) {
		this.currentTab = currentTab;
	}
	
	public void mouseClicked(int mouseX, int mouseY, int button) {
		for (Tab tab : Tab.values()) {
			if (hoveredTab(tab, mouseX, mouseY) && button == 0) {
				switch(tab) {
					case CLICKGUI:
						this.setCurrentTab(Tab.CLICKGUI);
						mc.setScreen(ModuleManager.INSTANCE.getModule(ClickGUIModule.class).mode.is("New") ? badgamesinc.hypnotic.ui.clickgui.ClickGUI.INSTANCE : ClickGUI.INSTANCE);
						break;
					case HUDEDITOR:
						this.setCurrentTab(Tab.HUDEDITOR);
						mc.setScreen(HudEditorScreen.INSTANCE);
						break;
					case OPTIONS:
						this.setCurrentTab(Tab.OPTIONS);
						mc.setScreen(OptionsScreen.INSTANCE);
						break;
					case WAYPOINT:
						this.setCurrentTab(Tab.WAYPOINT);
						mc.setScreen(WaypointManagerScreen.INSTANCE);
						break;
					default:
						this.setCurrentTab(Tab.CLICKGUI);
						mc.setScreen(ClickGUI.INSTANCE);
						break;
				}
			}
		}
	}
	
	public static enum Tab {
		CLICKGUI("ClickGui"),
		HUDEDITOR("HudEditor"),
		WAYPOINT("Waypoint"),
		OPTIONS("Options");
		
		String name;
		boolean hovered;
		int x, y, width, height, offset;
		private Tab(String name) {
			this.name = name;
		}
	}
}
