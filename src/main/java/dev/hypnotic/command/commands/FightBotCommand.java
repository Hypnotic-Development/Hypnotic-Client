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
package dev.hypnotic.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import dev.hypnotic.command.Command;
import dev.hypnotic.command.argtypes.PlayerArgumentType;
import dev.hypnotic.module.ModuleManager;
import dev.hypnotic.module.combat.FightBot;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;

/**
* @author BadGamesInc
*/
public class FightBotCommand extends Command {

	public FightBotCommand() {
		super("fightbot", "To be used with the FightBot module", "fb");
	}

	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.then(literal("hunt").then(argument("player", PlayerArgumentType.player()).executes(context -> {
			PlayerEntity target = PlayerArgumentType.getPlayer(context, "player");
			FightBot fightBot = ModuleManager.INSTANCE.getModule(FightBot.class);
			
			if (target.getName().getString().equalsIgnoreCase(mc.player.getName().getString())) {
				error("You can't hunt yourself");
				return SINGLE_SUCCESS;
			}
			
			fightBot.setTarget(target);
			
			if (!fightBot.isEnabled()) fightBot.toggle();
			
			return SINGLE_SUCCESS;
		})));
	}
}
