package dev.hypnotic.hypnotic_client.module.render;

import net.minecraft.item.ItemStack;

public interface IItemRenderer {

	void renderItemIntoGUI(ItemStack itemStack, float x, float y, float scale);

}
