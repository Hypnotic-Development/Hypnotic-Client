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
package dev.hypnotic.scripting;

import dev.hypnotic.utils.font.FontManager;
import dev.hypnotic.utils.player.PlayerUtils;
import dev.hypnotic.utils.player.inventory.InventoryUtils;
import dev.hypnotic.utils.world.EntityUtils;
import dev.hypnotic.utils.world.WorldUtils;

/**
* @author BadGamesInc
*/
public class ScriptUtils {

	// Here so scripts can access various utils
	public static ScriptUtils INSTANCE = new ScriptUtils();
	public WorldUtils world = new WorldUtils();
	public PlayerUtils player = new PlayerUtils();
	public InventoryUtils inventory = new InventoryUtils();
	public FontManager font = FontManager.INSTANCE;
	public EntityUtils entity = new EntityUtils();
}
