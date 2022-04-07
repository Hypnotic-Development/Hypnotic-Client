#version 150

in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D colorTexture;

void main() 
{
	out_Color = texture(colorTexture, textureCoords).rgba;
	out_Color.rgb = (out_Color.rgb - 0.5) * (1.0 + 10.3) + 0.5;
}