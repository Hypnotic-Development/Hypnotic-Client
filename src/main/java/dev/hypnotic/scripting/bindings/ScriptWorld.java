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
package dev.hypnotic.scripting.bindings;

import static dev.hypnotic.utils.MCUtils.mc;

import java.util.Iterator;

import org.graalvm.polyglot.Value;

import dev.hypnotic.utils.render.Vector3D;

/**
* @author BadGamesInc
*/
public class ScriptWorld {
	
	public void forEachEntity(Value callback) {
		mc.world.getEntities().forEach(e -> {
			callback.execute(e);
		});
	}
	
	public Iterator<?> getEntities() {
		return mc.world.getEntities().iterator();
	}

	public Vector3D getSpawnPos() {
		return new Vector3D(mc.world.getSpawnPos().getX(), mc.world.getSpawnPos().getY(), mc.world.getSpawnPos().getZ());
	}
	
	public Difficulty getDifficulty() {
		switch (mc.world.getDifficulty()) {
			case PEACEFUL: return Difficulty.PEACEFUL;
			case EASY: return Difficulty.EASY;
			case NORMAL: return Difficulty.NORMAL;
			case HARD: return Difficulty.HARDCORE;
			default: return Difficulty.NORMAL;
		}
	}
	
	public enum Difficulty {
		PEACEFUL,
		EASY,
		NORMAL,
		HARDCORE;
	}
}
