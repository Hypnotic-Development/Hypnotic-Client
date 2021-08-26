package badgamesinc.hypnotic.module.render;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventKeyPress;
import badgamesinc.hypnotic.event.events.EventRenderGUI;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.font.FontManager;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

public class TabGUI extends Mod {

	private int x, y, width, height, currentTab;
	private boolean expanded;
	
	public TabGUI() {
		super("TabGUI", "Ingame tabgui", Category.RENDER);
	}
	
	int animTicks;
	int animTicks2;
	int expandTicks;
	boolean shouldMoveUp;
	boolean shouldMoveDown;
	boolean shouldMoveUp2;
	boolean shouldMoveDown2;
	
	@EventTarget
	public void renderGUI(EventRenderGUI event) {
		MatrixStack matrices = event.getMatrices();
		x = 4;
		y = 30;
		width = 80;
		height = 15;
		if (animTicks != currentTab * 15) {
			if (shouldMoveDown)
			animTicks++;
			if (shouldMoveUp)
				animTicks--;
		} else {
			shouldMoveUp = false;
			shouldMoveDown = false;
		}
		if (animTicks > (Category.values().length - 1) * 15) {
			animTicks = 0;
		}
		if (animTicks < 0) {
			animTicks = 0;
		}
		
		if (animTicks2 != Category.values()[currentTab].moduleIndex * 15) {
			if (shouldMoveDown2)
			animTicks2++;
			if (shouldMoveUp2)
				animTicks2--;
		} else {
			shouldMoveUp2 = false;
			shouldMoveDown2 = false;
		}
		if (animTicks2 > (ModuleManager.INSTANCE.getModulesInCategory(Category.values()[currentTab]).size() - 1) * 15) {
			animTicks2 = 0;
		}
		if (animTicks2 < 0) {
			animTicks2 = 0;
		}
		DrawableHelper.fill(matrices, x, y, x + width, y + Category.values().length * 15, new Color(0, 0, 0, 100).getRGB());
		DrawableHelper.fill(matrices, x, y + animTicks, x + width, y + height + animTicks, ColorUtils.getClientColorInt());
		DrawableHelper.fill(matrices, x, y, x + width, y - 1, ColorUtils.getClientColorInt());
		DrawableHelper.fill(matrices, x, y, x + 1, y + Category.values().length * 15, ColorUtils.getClientColorInt());
		DrawableHelper.fill(matrices, x, y + Category.values().length * 15, x + width, y - 1 + Category.values().length * 15, ColorUtils.getClientColorInt());
		DrawableHelper.fill(matrices, x + width, y - 1, x + width + 1, y + Category.values().length * 15, ColorUtils.getClientColorInt());
		int offset = 0;
		for (Category category : Category.values()) {
			FontManager.robotoMed.drawWithShadow(matrices, category.name, x + 4, y + offset + 2, -1);
			offset+= 15;
		}
		if (expanded) {
			if (expandTicks < 165) {
				expandTicks += Math.max(event.getPartialTicks() * 10, 10);
			}
		} else {
			if (expandTicks > 0) {
				expandTicks -= Math.max(event.getPartialTicks() * 10, 10);
			}
		}
		int x2 = x + 4;
		if (expanded || expandTicks > 0) {
			ArrayList<Mod> modules = ModuleManager.INSTANCE.getModulesInCategory(Category.values()[currentTab]);
			
			RenderSystem.enableScissor(x, y, x + width * 100, mc.getWindow().getScaledHeight());
			GlStateManager._scissorBox(168, 100, expandTicks, 10000);
			DrawableHelper.fill(matrices, x2 + width, y + (currentTab * 15), x2 + width * 2, y + (currentTab * 15) + ((ModuleManager.INSTANCE.getModulesInCategory(Category.values()[currentTab]).size() - 1) * 15) + height, new Color(0, 0, 0, 100).getRGB());
			DrawableHelper.fill(matrices, x + width + 3, y + currentTab * 15, x2 + width, y + modules.size() * 15 + currentTab * 15, ColorUtils.getClientColorInt());
			DrawableHelper.fill(matrices, x2 + 1 + width * 2, y - 1 + currentTab * 15, x2 - 1 + width, y + currentTab * 15, ColorUtils.getClientColorInt());
			DrawableHelper.fill(matrices, x + 3 + width, y + (currentTab * 15) + 1 + modules.size() * 15, x2 + width * 2, y + (currentTab * 15) + modules.size() * 15, ColorUtils.getClientColorInt());
			DrawableHelper.fill(matrices, x2 + width * 2, y + (currentTab * 15), x2 + width * 2 + 1, y + (currentTab * 15) + 1 + modules.size() * 15, ColorUtils.getClientColorInt());
			
			DrawableHelper.fill(matrices, x2 + width, y + (currentTab * 15) + animTicks2, x2 + width * 2, y + (currentTab * 15) + animTicks2 + height, ColorUtils.getClientColor().getRGB());
			int modCount2 = 0;
			for (Mod mod : modules) {
				FontManager.robotoMed.drawWithShadow(matrices, mod.getName(), x2 + width + 4, y + 2 + (currentTab * 15) + modCount2, mod.isEnabled() ? ColorUtils.getClientColorInt() : -1);
				modCount2+=15;
			}
			RenderSystem.disableScissor();
		} else if (!expanded) {
			
		}
		
		
	}
	
	@EventTarget
	public void eventKeyPress(EventKeyPress event) {
		if (event.getAction() == GLFW.GLFW_PRESS && mc.currentScreen == null) {
			Category category = Category.values()[currentTab];
			ArrayList<Mod> modules = ModuleManager.INSTANCE.getModulesInCategory(Category.values()[currentTab]);
			if (event.getKey() == GLFW.GLFW_KEY_UP) {
				if (!expanded) {
					if (currentTab <= 0) {currentTab = Category.values().length - 1; shouldMoveDown = true; shouldMoveUp = false;}
					else {currentTab--; shouldMoveUp = true; shouldMoveDown = false;}
				} else {
					if (category.moduleIndex <= 0) {category.moduleIndex = modules.size() - 1; shouldMoveDown2 = true; shouldMoveUp2 = false;}
					else {category.moduleIndex--; shouldMoveUp2 = true; shouldMoveDown2 = false;}
				}
			} else if (event.getKey() == GLFW.GLFW_KEY_DOWN) {
				if (!expanded) {
					if (currentTab >= Category.values().length - 1) {currentTab = 0; shouldMoveUp = true; shouldMoveDown = false;}
					else {currentTab++; shouldMoveDown = true; shouldMoveUp = false;}
				} else {
					if (category.moduleIndex >= modules.size() - 1) {category.moduleIndex = 0; shouldMoveUp2 = true; shouldMoveDown2 = false;}
					else {category.moduleIndex++; shouldMoveDown2 = true; shouldMoveUp2 = false;}
				}
			} else if (event.getKey() == GLFW.GLFW_KEY_RIGHT) {
				if (!expanded) {expanded = true; animTicks2 = category.moduleIndex * 15;}
				else {if (modules.get(category.moduleIndex).getName() == this.getName()) {} else modules.get(category.moduleIndex).toggle();}
			} else if (event.getKey() == GLFW.GLFW_KEY_LEFT && expanded) {
				expanded = false;
			}
		}
	}

	@Override
	public void onDisable() {
		for (Category category : Category.values()) {
			category.moduleIndex = 0;
		}
		super.onDisable();
	}
}
