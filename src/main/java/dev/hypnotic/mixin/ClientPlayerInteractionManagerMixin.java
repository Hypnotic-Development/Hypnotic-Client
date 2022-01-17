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
package dev.hypnotic.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.hypnotic.event.events.EventDestroyBlock;
import dev.hypnotic.utils.mixin.IClientPlayerInteractionManager;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;

@Mixin({ClientPlayerInteractionManager.class})
public abstract class ClientPlayerInteractionManagerMixin implements IClientPlayerInteractionManager {
   
	@Shadow private float currentBreakingProgress;
    @Shadow private int blockBreakingCooldown;
    
    @Shadow protected abstract void syncSelectedSlot();

    @Inject(method = "breakBlock", at = @At("RETURN"))
    public void breakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        new EventDestroyBlock(pos).call();
    }
    
    public void setBlockBreakProgress(float progress) {
        this.currentBreakingProgress = progress;
    }

    public void setBlockBreakingCooldown(int cooldown) {
        this.blockBreakingCooldown = cooldown;
    }

    public float getBlockBreakProgress() {
        return this.currentBreakingProgress;
    }

	@Override
	public void syncSelected() {
		syncSelectedSlot();
	}
}