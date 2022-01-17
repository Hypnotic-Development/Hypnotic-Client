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
package dev.hypnotic.module.render;

import java.awt.Color;
import com.mojang.blaze3d.systems.RenderSystem;

import dev.hypnotic.event.EventTarget;
import dev.hypnotic.event.events.EventRender3D;
import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.settings.settingtypes.NumberSetting;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.font.FontManager;
import dev.hypnotic.utils.player.PlayerUtils;
import dev.hypnotic.utils.render.QuadColor;
import dev.hypnotic.utils.render.RenderUtils;
import dev.hypnotic.waypoint.Waypoint;
import dev.hypnotic.waypoint.WaypointManager;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

public class Waypoints extends Mod {

	public NumberSetting distance = new NumberSetting("Render Distance", 100, 25, 1000, 1);
	
	public Waypoints() {
		super("Waypoints", "Renders waypoints at specific locations", Category.RENDER);
		addSettings(distance);
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
	}
	
	@EventTarget
	public void render(EventRender3D event) {
		MatrixStack matrices = event.getMatrices();
		for (Waypoint waypoint : WaypointManager.INSTANCE.waypoints) {
			if (waypoint.isEnabled()) {
				Camera c = mc.gameRenderer.getCamera();
	            Vec3d vv = new Vec3d(waypoint.getX() + .5, c.getPos().y, waypoint.getZ() + .5);
	            double distance = vv.distanceTo(mc.player.getPos());
	            int a = 255;
	            float scale = 3f;
	            scale /= 50f;
	            scale *= 0.55f;
	            if (distance < 100) {
	                a = (int) ((distance / 100) * 255);
	            } else scale *= distance / 100d;
	            RenderUtils.drawBoxOutline(new Box(new BlockPos(waypoint.getX(), 0, waypoint.getZ())).stretch(0, 500, 0), QuadColor.single(ColorUtils.defaultClientColor), 1);
	            RenderUtils.drawBoxFill(new Box(new BlockPos(waypoint.getX(), 0, waypoint.getZ())).stretch(0, 500, 0), QuadColor.single(new Color(ColorUtils.defaultClientColor().getRed(), ColorUtils.defaultClientColor().getGreen(), ColorUtils.defaultClientColor().getBlue(), a).getRGB()));
	            
	            Vec3d textPos = new Vec3d(waypoint.getX() + 0.5, c.getPos().y, waypoint.getZ() + 0.25);
	            matrices.push();
	            matrices.translate(textPos.x - c.getPos().x - 1, textPos.y - c.getPos().y + 2, textPos.z - c.getPos().z - 0.5);
	            matrices.translate(0, scale * 6, 0);
	            matrices.scale(-scale, -scale, scale);
	            Quaternion qu = mc.getEntityRenderDispatcher().getRotation();
	            qu = new Quaternion(-qu.getX(), -qu.getY(), qu.getZ(), qu.getW());
	            matrices.multiply(qu);
	            
	            VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
	            RenderSystem.disableDepthTest();
	            RenderSystem.polygonOffset(1, -15000000);
	            RenderSystem.enablePolygonOffset();
	            FontManager.robotoBig.drawCenteredString(matrices, waypoint.getName() + " " + Math.round(PlayerUtils.distanceTo(new BlockPos(waypoint.getX(), mc.player.getY(), waypoint.getZ()))) + "m", 0, 0, -1, true);
	            RenderSystem.polygonOffset(1, 15000000);
	            RenderSystem.disablePolygonOffset();
	            RenderSystem.enableDepthTest();
	            immediate.draw();
	            matrices.pop();
			}
		}
	}
	
	@Override
	public void onTick() {
		// TODO Auto-generated method stub
		super.onTick();
	}
	
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		super.onDisable();
	}
}
