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
package dev.hypnotic.command.argtypes;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;

import java.util.Arrays;
import java.util.Collection;

import static net.minecraft.nbt.StringNbtReader.EXPECTED_VALUE;

public class CompoundNbtTagArgumentType implements ArgumentType<NbtCompound> {
    private static final Collection<String> EXAMPLES = Arrays.asList("{foo:bar}", "{foo:[aa, bb],bar:15}");

    public static CompoundNbtTagArgumentType nbtTag() {
        return new CompoundNbtTagArgumentType();
    }

    public NbtCompound parse(StringReader reader) throws CommandSyntaxException {
        reader.skipWhitespace();
        if (!reader.canRead()) {
            throw EXPECTED_VALUE.createWithContext(reader);
        }
        StringBuilder b = new StringBuilder();
        int open = 0;
        while (reader.canRead()) {
            if (reader.peek() == '{') {
                open++;
            }
            else if (reader.peek() == '}') {
                open--;
            }
            if (open == 0)
                break;
            b.append(reader.read());
        }
        reader.expect('}');
        b.append('}');
        return StringNbtReader.parse(b.toString()
                .replace("$", "\u00a7")
                .replace("\u00a7\u00a7", "$")
        );
    }

    public static NbtCompound getTag(final CommandContext<?> context, String name) {
        return context.getArgument(name, NbtCompound.class);
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
