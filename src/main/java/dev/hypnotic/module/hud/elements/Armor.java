package dev.hypnotic.module.hud.elements;

import dev.hypnotic.module.hud.HudModule;
import dev.hypnotic.settings.settingtypes.ModeSetting;
import dev.hypnotic.settings.settingtypes.NumberSetting;
import dev.hypnotic.ui.HudEditorScreen;
import dev.hypnotic.utils.render.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class Armor extends HudModule {

	public ModeSetting direction = new ModeSetting("Allignment", "Horizontal", "Horizontal", "Vertical");
	public NumberSetting scale = new NumberSetting("Scale", 1, 0.1, 2, 0.1);
	
	public Armor() {
		super("Armor Display", "Shows your armor", 4, 50, 30, 200);
		addSettings(direction, scale);
	}
	
	@Override
	public void render(MatrixStack matrices, int scaledWidth, int scaledHeight, float partialTicks) {
		
		RenderUtils.drawItem(mc.currentScreen instanceof HudEditorScreen ? Items.GOLDEN_APPLE.getDefaultStack() : mc.player.getOffHandStack(), this.getX() + (direction.is("Horizontal") ? (0 * 20) * (float)scale.getValue() + (scale.getValue() < 1 ? - (float)scale.getValue() * 3 : + (float)scale.getValue() * 3) : 0), this.getY() + (direction.is("Vertical") ? (5 * 20) : 0), (float)scale.getValue());
		RenderUtils.drawItem(mc.currentScreen instanceof HudEditorScreen ? Items.DIAMOND_SWORD.getDefaultStack() : mc.player.getMainHandStack(), this.getX() + (direction.is("Horizontal") ? (5 * 20) * (float)scale.getValue() + (scale.getValue() < 1 ? - (float)scale.getValue() * 3 : + (float)scale.getValue() * 3) : 0), this.getY() + (direction.is("Vertical") ? (0 * 20) : 0), (float)scale.getValue());
		RenderUtils.drawItem(armorPiece(3), this.getX() + (direction.is("Horizontal") ? (1 * 20) * (float)scale.getValue() + (scale.getValue() < 1 ? - (float)scale.getValue() * 3 : + (float)scale.getValue() * 3) : 0), this.getY() + (direction.is("Vertical") ? (1 * 20) : 0), (float)scale.getValue());
		matrices.push();
		matrices.translate(this.getX() + (direction.is("Horizontal") ? (1 * 20) * (float)scale.getValue() : 0), this.getY() + (float)scale.getValue() + (direction.is("Vertical") ? (1 * 20) : 0) + 20, 0);
		matrices.scale(0.5f, 0.5f, 1);
//		FontManager.robotoMed.drawWithShadow(matrices, armorPiece(0).getMaxDamage() - armorPiece(0).getDamage() + "/" + armorPiece(0).getMaxDamage(), 0, 0, -1);
		matrices.pop(); 
		RenderUtils.drawItem(armorPiece(2), this.getX() + (direction.is("Horizontal") ? (2 * 20) * (float)scale.getValue() + (scale.getValue() < 1 ? - (float)scale.getValue() * 3 : + (float)scale.getValue() * 3) : 0), this.getY() + (direction.is("Vertical") ? (2 * 20) : 0), (float)scale.getValue());
		RenderUtils.drawItem(armorPiece(1), this.getX() + (direction.is("Horizontal") ? (3 * 20) * (float)scale.getValue() + (scale.getValue() < 1 ? - (float)scale.getValue() * 3 : + (float)scale.getValue() * 3) : 0), this.getY() + (direction.is("Vertical") ? (3 * 20) : 0), (float)scale.getValue());
		RenderUtils.drawItem(armorPiece(0), this.getX() + (direction.is("Horizontal") ? (4 * 20) * (float)scale.getValue() + (scale.getValue() < 1 ? - (float)scale.getValue() * 3 : + (float)scale.getValue() * 3) : 0), this.getY() + (direction.is("Vertical") ? (4 * 20) : 0), (float)scale.getValue());
		this.setWidth((direction.is("Horizontal") ? (5 * 23) * (float)scale.getValue() : 17));
		this.setHeight((direction.is("Vertical") ? (5 * 24) * (float)scale.getValue() : 16 * (float)scale.getValue()));
		super.render(matrices, scaledWidth, scaledHeight, partialTicks);
	}
	
	private ItemStack armorPiece(int index) {
		if (mc.currentScreen instanceof HudEditorScreen) {
			switch(index) {
				case 0: return Items.DIAMOND_BOOTS.getDefaultStack();
				case 1: return Items.DIAMOND_LEGGINGS.getDefaultStack();
				case 2: return Items.DIAMOND_CHESTPLATE.getDefaultStack();
				case 3: return Items.DIAMOND_HELMET.getDefaultStack();
			}
		}
		return mc.player.getInventory().getArmorStack(index);
	}
}
