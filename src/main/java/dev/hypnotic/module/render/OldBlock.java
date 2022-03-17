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

import dev.hypnotic.event.EventTarget;
import dev.hypnotic.event.events.EventRenderHeldItem;
import dev.hypnotic.event.events.EventRenderItem;
import dev.hypnotic.event.events.EventSwingHand;
import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.module.ModuleManager;
import dev.hypnotic.module.combat.Killaura;
import dev.hypnotic.settings.settingtypes.ModeSetting;
import dev.hypnotic.utils.Timer;
import net.minecraft.client.option.Perspective;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;

public class OldBlock extends Mod {

	public ModeSetting animation = new ModeSetting("Block Animation", "1.7(ish)", "1.7(ish)", "Slide", "Sigma", "Swing");
	public OldBlock() {
		super("OldBlock", "", Category.RENDER);
		addSettings(animation);
	}

	public float swingTicks = 0;
	public boolean swingHasElapsed = true;
	
	@Override
	public void onTick() {
		
		super.onTick();
	}
	
	
	public static Timer animationTimer = new Timer();
	@EventTarget
    private void runMethod(EventRenderItem event) {
		boolean shouldmove = animationTimer.hasTimeElapsed(1000 / 75, true);
		if (ModuleManager.INSTANCE.getModule(Killaura.class).autoBlockMode.is("Visual") && Killaura.target != null ? Killaura.target == null : !mc.player.isUsingItem()) return;
        if ((event.getItemStack().getItem() instanceof AxeItem || mc.player.getMainHandStack().getItem() instanceof SwordItem) && shouldmove) {
        	if (swingTicks < 60 && !swingHasElapsed) {
        		swingTicks+=7;
        	}
        	if (swingTicks >= 60) swingHasElapsed = true;
            if (swingHasElapsed) {
            	swingTicks-=7;
            	if (swingTicks <= 0) {
            		swingHasElapsed = false;
            	}
            }
        }
    }

    @EventTarget
    private void renderHeldItem(EventRenderHeldItem eventRenderHeldItem) {
        if (eventRenderHeldItem.getHand() == Hand.OFF_HAND && eventRenderHeldItem.getItemStack().getItem() instanceof ShieldItem && (mc.player.getMainHandStack().getItem() instanceof AxeItem || mc.player.getMainHandStack().getItem() instanceof SwordItem) && mc.options.getPerspective() == Perspective.FIRST_PERSON)
            eventRenderHeldItem.setCancelled(true);
    }
    
    @EventTarget
    public void onSwingHand(EventSwingHand event) {
    	if (mc.options.useKey.isPressed() && (mc.player.getMainHandStack().getItem() instanceof SwordItem || mc.player.getMainHandStack().getItem() instanceof AxeItem ) && !animation.is("Swing"))
    	event.setCancelled(true);
    }
}
