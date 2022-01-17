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
package dev.hypnotic.module.player;

import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.settings.Setting;
import dev.hypnotic.settings.settingtypes.NumberSetting;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.mixin.IClientPlayerInteractionManager;
import net.minecraft.entity.effect.StatusEffects;

public class SpeedMine extends Mod {
    public NumberSetting speed = new NumberSetting("Speed", 1.0D, 0.0D, 10.0D, 0.1D);

    public SpeedMine() {
        super("SpeedMine", "Mine blocks faster", Category.PLAYER);
        this.addSettings(new Setting[]{this.speed});
    }

    public void onTick() {
        String var10001 = ColorUtils.gray;
        this.setDisplayName("SpeedMine " + var10001 + this.speed.getValue());
        IClientPlayerInteractionManager minething = (IClientPlayerInteractionManager)mc.interactionManager;
        if (mc.player.hasStatusEffect(StatusEffects.HASTE)) {
            mc.player.hasStatusEffect(StatusEffects.HASTE);
        }

        if (minething != null && (double)minething.getBlockBreakProgress() > 1.0D - this.speed.getValue() * 0.1D) {
            minething.setBlockBreakProgress(1.0F);
        }

        if (minething != null)
        minething.setBlockBreakingCooldown(0);
        super.onTick();
    }
}
