#version 120

uniform sampler2D textureIn, textureToCheck;
uniform vec2 texelSize, direction;
uniform float radius;
uniform float loopRadius;

#define stepThing texelSize * direction

vec3 values = vec3(0.0);

void main() {
    if (direction.y > 0)
        if(texture2D(textureToCheck, gl_TexCoord[0].st).a != 0.0) return;

    // Incremental guassian blur, see: https://developer.nvidia.com/gpugems/gpugems3/part-vi-gpu-computing/chapter-40-incremental-computation-gaussian
    // 1 divided by (sqrt(2 * PI) * sigma)
    values.x = .4 / radius;
    values.y = exp(-0.5 / (radius * radius));
    values.z = values.y * values.y;

    float alpha = texture2D(textureIn, gl_TexCoord[0].st).a * values.x;
    for (float f = 1.0; f <= loopRadius; f++) {
        float weight = values.x;
        alpha += texture2D(textureIn, gl_TexCoord[0].xy + stepThing * f).a * weight;
        alpha += texture2D(textureIn, gl_TexCoord[0].xy - stepThing * f).a * weight;

        values.x *= values.y;
        values.y *= values.z;
    }

    gl_FragColor = vec4(0.0,0.0,0.0, alpha);
}