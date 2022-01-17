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

import net.minecraft.block.AbstractBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.hypnotic.module.ModuleManager;
import dev.hypnotic.module.render.Xray;

@Mixin(AbstractBlock.AbstractBlockState.class)
public class AbstractBlockStateMixin {
    @Inject(method = "getLuminance", at = @At("HEAD"), cancellable = true)
    public void getLuminance(CallbackInfoReturnable<Integer> cir) {
        if (ModuleManager.INSTANCE.getModule(Xray.class).isEnabled()) cir.setReturnValue(15);
    }
}
