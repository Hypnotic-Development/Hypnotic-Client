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
package dev.hypnotic.event.events;

import dev.hypnotic.event.Event;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class EventRenderItem extends Event {

    private MatrixStack matrixStack;
    private ItemStack itemStack;
    private ModelTransformation.Mode type;
    private RenderTime renderTime;
    private boolean leftHanded;

    public EventRenderItem(MatrixStack matrixStack, ItemStack itemStack, ModelTransformation.Mode type, RenderTime renderTime, boolean leftHanded)
    {
        this.matrixStack = matrixStack;
        this.itemStack = itemStack;
        this.type = type;
        this.renderTime = renderTime;
        this.leftHanded = leftHanded;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public ModelTransformation.Mode getType() {
        return type;
    }

    public RenderTime getRenderTime() {
        return renderTime;
    }

    public MatrixStack getMatrixStack() {
        return matrixStack;
    }

    public boolean isLeftHanded() {
        return leftHanded;
    }

    public enum RenderTime {
        PRE, POST
    }
}
