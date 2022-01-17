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

import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import dev.hypnotic.utils.mixin.IRaycastContext;

@Mixin(RaycastContext.class)
public class RaycastContextMixin implements IRaycastContext {
    @Shadow @Final @Mutable private Vec3d start;
    @Shadow @Final @Mutable private Vec3d end;
    @Shadow @Final @Mutable private RaycastContext.ShapeType shapeType;
    @Shadow @Final @Mutable private RaycastContext.FluidHandling fluid;
    @Shadow @Final @Mutable private ShapeContext entityPosition;

    @Override
    public void set(Vec3d start, Vec3d end, RaycastContext.ShapeType shapeType, RaycastContext.FluidHandling fluidHandling, Entity entity) {
        this.start = start;
        this.end = end;
        this.shapeType = shapeType;
        this.fluid = fluidHandling;
        this.entityPosition = ShapeContext.of(entity);
    }
}
