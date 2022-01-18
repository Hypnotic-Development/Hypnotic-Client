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
*/
package dev.hypnotic.scripting;

import java.awt.Color;

import dev.hypnotic.utils.font.FontManager;
import dev.hypnotic.utils.render.QuadColor;
import dev.hypnotic.utils.render.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

/**
* @author BadGamesInc
*/
public class ScriptRenderer {

	// Render methods for scripts
	

	public void drawString(MatrixStack matrices, String string, float x, float y, Color color, boolean shadow) {
		if (shadow) FontManager.roboto.drawWithShadow(matrices, string, x, y, color.getRGB());
		else FontManager.roboto.draw(matrices, string, x, y, color.getRGB());
	}
	
	public Box boundingBox(Vec3d v1, Vec3d v2) {
		return new Box(v1, v2);
	}
	
	public Box boundingBox(BlockPos pos) {
		return new Box(pos);
	}
	
	public BlockPos blockPos(double x, double y, double z) {
		return new BlockPos(x, y, z);
	}
	
	public void drawRect(MatrixStack matrices, double x, double y, double x2, double y2, Color color) {
		RenderUtils.fill(matrices, x, y, x2, y2, color.getRGB());
	}
	
	public void drawRoundedRect(MatrixStack matrices, float x, float y, float x2, float y2, float smooth, Color color) {
		RenderUtils.drawRoundedRect(matrices, x, y, x2, y2, smooth, color);
	}
	
	public void drawFilledCircle(MatrixStack matrices, double x, double y, float radius, Color color) {
		RenderUtils.drawFilledCircle(matrices, x, y, radius, color);
	}
	
	public void drawOutlineBox(MatrixStack matrices, Box bb, float lineWidth, Color color) {
		RenderUtils.drawBoxOutline(bb, QuadColor.single(color.getRGB()), lineWidth);
	}
	
	public void drawFilledBox(MatrixStack matrices, Box bb, Color color) {
		RenderUtils.drawBoxFill(bb, QuadColor.single(color.getRGB()));
	}
	
	public void drawOutlineBox(MatrixStack matrices, BlockPos pos, float lineWidth, Color color) {
		RenderUtils.drawBoxOutline(pos, QuadColor.single(color.getRGB()), lineWidth);
	}
	
	public void drawFilledBox(MatrixStack matrices, BlockPos pos, Color color) {
		RenderUtils.drawBoxFill(pos, QuadColor.single(color.getRGB()));
	}
}
