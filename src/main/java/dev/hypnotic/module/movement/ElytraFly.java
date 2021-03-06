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
package dev.hypnotic.module.movement;

import dev.hypnotic.event.EventTarget;
import dev.hypnotic.event.events.EventMove;
import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.settings.settingtypes.BooleanSetting;
import dev.hypnotic.settings.settingtypes.ModeSetting;
import dev.hypnotic.settings.settingtypes.NumberSetting;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.player.PlayerUtils;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.math.Vec3d;

public class ElytraFly extends Mod {

	public ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "Firework", "NCP");
	public NumberSetting flySpeed = new NumberSetting("Speed", 0.5, 0.1, 2, 0.1);
	public NumberSetting downSpeed = new NumberSetting("Down Speed", 0.5, 0.1, 2, 0.1);
	public NumberSetting fallDistance = new NumberSetting("Fall Distance", 3, 0, 10, 0.5);
	public BooleanSetting autoElytra = new BooleanSetting("Auto Elytra", true);
	public BooleanSetting slowGlide = new BooleanSetting("Slow Glide", false);
	
	public ElytraFly() {
		super("ElytraFly", "zoomer", Category.MOVEMENT);
		addSettings(mode, flySpeed, downSpeed, fallDistance, autoElytra, slowGlide);
	}

	@EventTarget
    public void onMotion(EventMove event) {
        this.setDisplayName("ElytraFly " + ColorUtils.gray + mode.getSelected());
        if (wearingElytra() && (autoElytra.isEnabled() && mc.player.fallDistance >= fallDistance.getValue() && !mc.player.isOnGround() && !mc.player.isFallFlying())) {
            if (mc.player.age % 5 == 0)
                mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
        }

        if (mc.player.isFallFlying()) {
            if (mode.is("Firework")) {

                Vec3d vec3d_1 = mc.player.getRotationVector();
//                double double_1 = 1.5D;
                Vec3d vec3d_2 = mc.player.getVelocity();
                mc.player.setVelocity(vec3d_2.add(vec3d_1.x * 0.1D + (vec3d_1.x * 1.5D - vec3d_2.x) * 0.5D, vec3d_1.y * 0.1D + (vec3d_1.y * 1.5D - vec3d_2.y) * 0.5D, vec3d_1.z * 0.1D + (vec3d_1.z * 1.5D - vec3d_2.z) * 0.5D));
            } else {
                if (mode.is("NCP") || mode.is("Vanilla")) {
                    PlayerUtils.setMoveSpeed(event, flySpeed.getValue());
                    if (event.getY() <= 0)
                        event.setY(mc.player.isSneaking() ? (float)-downSpeed.getValue() : (slowGlide.isEnabled() ? -0.01 : 0));
                    if (mode.is("Vanilla")) {
                    	if (mc.options.jumpKey.isPressed()) event.setY(flySpeed.getValue());
                    	if (mc.options.sneakKey.isPressed()) event.setY(-flySpeed.getValue());
                    }
                }

            }
        }
    }

    private boolean wearingElytra() {
        ItemStack equippedStack = mc.player.getEquippedStack(EquipmentSlot.CHEST);
        return equippedStack != null && equippedStack.getItem() == Items.ELYTRA;
    }

}

