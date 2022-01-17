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
package dev.hypnotic.ui.altmanager.account;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;

import java.io.File;
import java.io.IOException;

import dev.hypnotic.Hypnotic;
import dev.hypnotic.utils.ISerializable;
import dev.hypnotic.utils.StreamUtils;

//Credits to meteor client (https://github.com/MeteorDevelopment/meteor-client)
public abstract class System<T> implements ISerializable<T> {
    private File file;

    public System(String name) {
        if (name != null) {
            this.file = new File(Hypnotic.hypnoticDir, name + ".nbt");
        }
    }

    public void init() {}

    public void save(File folder) {
        File file = getFile();
        if (file == null) return;

        NbtCompound tag = toTag();
        if (tag == null) return;

        try {
            File tempFile = File.createTempFile("hypnotic", file.getName());
            NbtIo.write(tag, tempFile);

            if (folder != null) file = new File(folder, file.getName());

            file.getParentFile().mkdirs();
            StreamUtils.copy(tempFile, file);
            tempFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        save(null);
    }

    public void load(File folder) {
        File file = getFile();
        if (file == null) return;

        try {
            if (folder != null) file = new File(folder, file.getName());

            if (file.exists()) {
                fromTag(NbtIo.read(file));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        load(null);
    }

    public File getFile() {
        return file;
    }

    @Override
    public NbtCompound toTag() {
        return null;
    }

    @Override
    public T fromTag(NbtCompound tag) {
        return null;
    }
}
