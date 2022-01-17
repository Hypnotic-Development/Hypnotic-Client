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
package dev.hypnotic.utils.render.shader;

import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vector4f;
import org.lwjgl.system.MemoryStack;

import dev.hypnotic.utils.render.Matrix4x4;
import dev.hypnotic.utils.render.Vector2D;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public class ShaderUniform {

    private final String name;
    private final int location;

    public ShaderUniform(String name, int location) {
        this.name = name;
        this.location = location;
    }

    public final void setInt(int value) {
        glUniform1i(location, value);
    }

    public final void setFloat(float value) {
        glUniform1f(location, value);
    }

    public final void setBoolean(boolean value) {
        glUniform1i(location, value ? 1 : 0);
    }

    public final void setVec(Vector2D value) {
        glUniform2f(location, (float)value.getX(), (float)value.getY());
    }

    public final void setVec(Vec2f value) {
        glUniform2f(location, value.x, value.y);
    }

    public final void setVec(Vector4f value) {
        glUniform4f(location, value.getX(), value.getY(), value.getZ(), value.getW());
    }

    public void setMatrix(FloatBuffer matrix) {
        glUniformMatrix4fv(location, false, matrix);
    }

    public void setMatrix(Matrix4x4 matrix4x4) {
        glUniformMatrix4fv(location, false, matrix4x4.toFloatBuffer());
    }

    public void setMatrix(Matrix4f matrix) {
        try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            FloatBuffer floatBuffer = memoryStack.mallocFloat(16);
            matrix.write(floatBuffer, true);
            glUniformMatrix4fv(location, false, floatBuffer);
        }
    }

    public final void setVec(Vec3d value) {
        glUniform3f(location, (float) value.getX(), (float) value.getY(), (float) value.getZ());
    }

    public final String getName() {
        return this.name;
    }

    public final int getLocation() {
        return this.location;
    }

    public static ShaderUniform get(int shaderID, String uniformName) {
        return new ShaderUniform(uniformName, glGetUniformLocation(shaderID, uniformName));
    }
}
