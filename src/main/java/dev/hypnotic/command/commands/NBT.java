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
package dev.hypnotic.command.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import dev.hypnotic.command.Command;
import dev.hypnotic.command.CommandManager;
import dev.hypnotic.command.argtypes.CompoundNbtTagArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

public class NBT extends Command {
    public NBT() {
        super("nbt", "Modifies NBT data for an item, example: .nbt add {display:{Name:'{\"text\":\"$cRed Name\"}'}}");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("add").then(argument("nbt_data", CompoundNbtTagArgumentType.nbtTag()).executes(s -> {
            ItemStack stack = mc.player.getInventory().getMainHandStack();
            if (validBasic(stack)) {
                NbtCompound tag = CompoundNbtTagArgumentType.getTag(s, "nbt_data");
                NbtCompound source = stack.getNbt();

                if (tag != null && source != null) {
                    stack.getNbt().copyFrom(tag);
                    setStack(stack);
                } else {
                    error("Some of the NBT data could not be found, try using: " + CommandManager.INSTANCE.getPrefix() + "nbt set {nbt}");
                }
            }
            return SINGLE_SUCCESS;
        })));
        builder.then(literal("set").then(argument("nbt_data", CompoundNbtTagArgumentType.nbtTag()).executes(s -> {
            ItemStack stack = mc.player.getInventory().getMainHandStack();
            if (validBasic(stack)) {
                NbtCompound tag = s.getArgument("nbt_data", NbtCompound.class);
                stack.setNbt(tag);
                setStack(stack);
            }
            return SINGLE_SUCCESS;
        })));
        builder.then(literal("remove").then(argument("nbt_path", NbtPathArgumentType.nbtPath()).executes(s -> {
            ItemStack stack = mc.player.getInventory().getMainHandStack();
            if (validBasic(stack)) {
                NbtPathArgumentType.NbtPath path = s.getArgument("nbt_path", NbtPathArgumentType.NbtPath.class);
                path.remove(stack.getNbt());
            }
            return SINGLE_SUCCESS;
        })));
        builder.then(literal("get").executes(s -> {
            ItemStack stack = mc.player.getInventory().getMainHandStack();
            if (stack == null) {
                error("You must hold an item in your main hand.");
            } else {
                NbtCompound tag = stack.getNbt();
                String nbt = tag == null ? "none" : tag.asString();

                BaseText copyButton = new LiteralText("NBT");
                copyButton.setStyle(copyButton.getStyle()
                        .withFormatting(Formatting.UNDERLINE)
                        .withClickEvent(new ClickEvent(
                                ClickEvent.Action.RUN_COMMAND,
                                this.toString("copy")
                        ))
                        .withHoverEvent(new HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                new LiteralText("Copy the NBT data to your clipboard.")
                        )));

                BaseText text = new LiteralText("");
                text.append(copyButton);
                text.append(new LiteralText(": " + nbt));

                info(text);
            }
            return SINGLE_SUCCESS;
        }));
        builder.then(literal("copy").executes(s -> {
            ItemStack stack = mc.player.getInventory().getMainHandStack();
            if (stack == null) {
                error("You must hold an item in your main hand.");
            } else {
                NbtCompound tag = stack.getNbt();
                if (tag == null)
                    error("No NBT data on this item.");
                else {
                    mc.keyboard.setClipboard(tag.toString());
                    BaseText nbt = new LiteralText("NBT");
                    nbt.setStyle(nbt.getStyle()
                            .withFormatting(Formatting.UNDERLINE)
                            .withHoverEvent(new HoverEvent(
                                    HoverEvent.Action.SHOW_TEXT,
                                    new LiteralText(tag.toString())
                            )));

                    BaseText text = new LiteralText("");
                    text.append(nbt);
                    text.append(new LiteralText(" data copied!"));

                    info(text);
                }
            }
            return SINGLE_SUCCESS;
        }));
        builder.then(literal("count").then(argument("count", IntegerArgumentType.integer(-127, 127)).executes(context -> {
            ItemStack stack = mc.player.getInventory().getMainHandStack();

            if (validBasic(stack)) {
                int count = IntegerArgumentType.getInteger(context, "count");
                stack.setCount(count);
                setStack(stack);
                info("Set mainhand stack count to " + count);
            }

            return SINGLE_SUCCESS;
        })));
    }

    private void setStack(ItemStack stack) {
        mc.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(36 + mc.player.getInventory().selectedSlot, stack));
    }

    private boolean validBasic(ItemStack stack) {
        if (!mc.player.getAbilities().creativeMode) {
            error("Creative mode only.");
            return false;
        }

        if (stack == null) {
            error("You must hold an item in your main hand.");
            return false;
        }
        return true;
    }
}
