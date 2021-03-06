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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import static org.lwjgl.opengl.GL20.*;

public class Shader {// unused as of right now. doesn't play well with Minecraft's VBO system

	private String shaderName;
	private int shaderProgram;
	private int vertexID;
	private int fragID;
	private ArrayList<ShaderUniform> uniforms = new ArrayList<>();

	public Shader(String shaderName) {
		this.shaderName = shaderName;
		this.shaderProgram = glCreateProgram();
		String vCode = readShader("/assets/hypnotic/shaders/" + shaderName + ".vsh");
		String fCode = readShader("/assets/hypnotic/shaders/" + shaderName + ".fsh");
		createProgram(vCode, fCode);
	}

	public void bind() {
		glUseProgram(shaderProgram);
	}

	public void detach() {
		glUseProgram(0);
	}

	public ShaderUniform addUniform(String name) {
		int i = glGetUniformLocation(shaderProgram, name);
		if (i < 0) {
			throw new NullPointerException("Could not find uniform " + name + " in shader " + shaderName);
		}
		ShaderUniform uniform = new ShaderUniform(name, i);
		uniforms.add(uniform);
		return uniform;
	}

	public void bindAttribute(String name, int index) {
		glBindAttribLocation(shaderProgram, index, name);
	}

	public void createProgram(String vertex, String frag) {
		vertexID = loadShader(vertex, GL_VERTEX_SHADER);
		fragID = loadShader(frag, GL_FRAGMENT_SHADER);
		glAttachShader(shaderProgram, vertexID);
		glAttachShader(shaderProgram, fragID);
		glLinkProgram(shaderProgram);
		glValidateProgram(shaderProgram);
		int errorCheckValue = glGetError();
		if (glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE) {
			System.out.println(this.shaderName + " Shader failed to compile!");
			System.out.println(glGetProgramInfoLog(shaderProgram, 2048));
			System.exit(-1);
		}
		if (errorCheckValue != GL_NO_ERROR) {
			System.out.println(this.shaderName + " Could not create shader " + this.shaderName);
			System.out.println(glGetProgramInfoLog(shaderProgram, glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH)));
			System.exit(-1);
		}
	}

	private int loadShader(String source, int type) {
		int shaderID = glCreateShader(type);
		glShaderSource(shaderID, source);
		glCompileShader(shaderID);

		if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.out.println(this.shaderName + " Shader failed to compile!");
			System.out.println(glGetShaderInfoLog(shaderID, 2048));
			System.exit(-1);
		}
		return shaderID;
	}

	private String readShader(String fileLoc) {
		try {
			InputStream in = getClass().getResourceAsStream(fileLoc);
			assert in != null;
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder sb = new StringBuilder();
			String inString;
			while ((inString = reader.readLine()) != null) {
				sb.append(inString);
				sb.append("\n");
			}
			in.close();
			reader.close();
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "Error";
		}
	}
}
