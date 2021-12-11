package dev.hypnotic.hypnotic_client.utils.render.shader.shaders;

import dev.hypnotic.hypnotic_client.utils.render.shader.Shader;
import dev.hypnotic.hypnotic_client.utils.render.shader.ShaderUniform;
import dev.hypnotic.hypnotic_client.utils.render.shader.ShaderUtils;

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
