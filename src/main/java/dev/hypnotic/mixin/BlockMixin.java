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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.hypnotic.event.events.EventCollide;
import dev.hypnotic.module.ModuleManager;
import dev.hypnotic.module.render.Xray;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(method = "shouldDrawSide", at = @At("HEAD"), cancellable = true)
    private static void shouldDrawSide(BlockState state, BlockView world, BlockPos pos, Direction side, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        if (ModuleManager.INSTANCE.getModule(Xray.class).isEnabled()) {
            cir.setReturnValue(Xray.blocks.contains(state.getBlock()));
        }
    }

    @Inject(method = "isTranslucent", at = @At("HEAD"), cancellable = true)
    public void isTranslucent(BlockState state, BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (ModuleManager.INSTANCE.getModule(Xray.class).isEnabled()) {
            cir.setReturnValue(!Xray.blocks.contains(state.getBlock()));
        }
    }
    
    @Inject(method = "pushEntitiesUpBeforeBlockChange", at = @At("HEAD"), cancellable = true)
    private static void pushEntitiesUpBeforeBlockChange(BlockState from, BlockState to, World world, BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
    	VoxelShape voxelShape = VoxelShapes.combine(from.getCollisionShape(world, pos), to.getCollisionShape(world, pos), BooleanBiFunction.ONLY_SECOND).offset((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
    	EventCollide.Block event = new EventCollide.Block(voxelShape.getBoundingBox(), pos);
    	event.call();
    	if (event.isCancelled()) cir.cancel();
    }
}
