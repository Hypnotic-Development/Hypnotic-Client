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
package dev.hypnotic.utils.render.shader.shaders;

import dev.hypnotic.utils.render.shader.Shader;
import dev.hypnotic.utils.render.shader.ShaderUniform;
import dev.hypnotic.utils.render.shader.ShaderUtils;

public class PosColorShader extends Shader {

    private ShaderUniform projection, modelView;

    public PosColorShader() {
        super("posColor");
        projection = addUniform("Projection");
        modelView = addUniform("ModelView");
        this.bindAttribute("Position", 0);
        this.bindAttribute("Color", 1);
    }

    @Override
    public void updateUniforms() {
        projection.setMatrix(ShaderUtils.INSTANCE.getProjectionMatrix());
        modelView.setMatrix(ShaderUtils.INSTANCE.getModelViewMatrix());
    }
}
