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

import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import dev.hypnotic.utils.mixin.IBox;

@Mixin(Box.class)
public class BoxMixin implements IBox {
    @Shadow @Final @Mutable public double minX;
    @Shadow @Final @Mutable public double minY;
    @Shadow @Final @Mutable public double minZ;

    @Shadow @Final @Mutable public double maxX;
    @Shadow @Final @Mutable public double maxY;
    @Shadow @Final @Mutable public double maxZ;

    @Override
    public void expand(double v) {
        this.minX -= v;
        this.minY -= v;
        this.minZ -= v;
        this.maxX += v;
        this.maxY += v;
        this.maxZ += v;
    }

    @Override
    public void set(double x1, double y1, double z1, double x2, double y2, double z2) {
        this.minX = Math.min(x1, x2);
        this.minY = Math.min(y1, y2);
        this.minZ = Math.min(z1, z2);
        this.maxX = Math.max(x1, x2);
        this.maxY = Math.max(y1, y2);
        this.maxZ = Math.max(z1, z2);
    }
}
