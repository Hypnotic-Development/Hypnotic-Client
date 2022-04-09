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
*/
package dev.hypnotic.module.render;

import java.util.ArrayList;
import java.util.List;

import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import net.minecraft.entity.Entity;

/**
* @author BadGamesInc
*/
public class TrueSight extends Mod {

	private List<Entity> invisibles = new ArrayList<>();
	
	public TrueSight() {
		super("TrueSight", "Allows you to see invisible entities", Category.RENDER);
	}
	
	@Override
	public void onTick() {
		mc.world.getEntities().forEach(entity -> {
			if (entity.isInvisible() && !invisibles.contains(entity)) {
				invisibles.add(entity);
				entity.setInvisible(false);
			}
		});
		
		super.onTick();
	}
	
	@Override
	public void onDisable() {
		invisibles.forEach(entity -> {
			entity.setInvisible(true);
		});
		invisibles.clear();
		super.onDisable();
	}
}
