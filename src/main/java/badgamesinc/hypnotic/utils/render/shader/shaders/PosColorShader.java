package badgamesinc.hypnotic.utils.render.shader.shaders;

import badgamesinc.hypnotic.utils.render.shader.Shader;
import badgamesinc.hypnotic.utils.render.shader.ShaderUniform;
import badgamesinc.hypnotic.utils.render.shader.ShaderUtils;

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
