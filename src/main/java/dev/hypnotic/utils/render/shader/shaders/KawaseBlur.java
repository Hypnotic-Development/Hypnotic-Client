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
*/
package dev.hypnotic.utils.render.shader.shaders;

import static dev.hypnotic.utils.MCUtils.mc;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import dev.hypnotic.utils.render.RenderUtils;
import dev.hypnotic.utils.render.shader.ShaderUtil;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.util.math.MatrixStack;

/**
* @author BadGamesInc
*/
public class KawaseBlur {
	
	public static final KawaseBlur INSTANCE = new KawaseBlur();
    
	public ShaderUtil kawaseDown = new ShaderUtil("shaders/kawasedown.fsh");
    public ShaderUtil kawaseUp = new ShaderUtil("shaders/kawaseup.fsh");

    public Framebuffer framebuffer = new SimpleFramebuffer(1, 1, false, false);


    public void setupUniforms(float offset) {
        kawaseDown.setUniformf("offset", offset, offset);
        kawaseUp.setUniformf("offset", offset, offset);
    }

    private int currentIterations;

    private List<Framebuffer> framebufferList = new ArrayList<>();

    private void initFramebuffers(float iterations) {
        for(Framebuffer framebuffer : framebufferList) {
            framebuffer.delete();
        }
        framebufferList.clear();

        framebufferList.add(RenderUtils.createFrameBuffer(framebuffer));


        for(int i = 1; i <= iterations; i++) {
            Framebuffer framebuffer = new SimpleFramebuffer(mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight(), false, false);
          //  framebuffer.setFramebufferFilter(GL11.GL_LINEAR);
            framebufferList.add(RenderUtils.createFrameBuffer(framebuffer));
        }
    }



    public void renderBlur(MatrixStack matrices, int iterations, int offset) {
        if(currentIterations != iterations) {
            initFramebuffers(iterations);
            currentIterations = iterations;
        }

        renderFBO(matrices, framebufferList.get(1), mc.getFramebuffer().getColorAttachment(), kawaseDown, offset);
        
        //Downsample
        for (int i = 1; i < iterations; i++) {
            renderFBO(matrices, framebufferList.get(i + 1), framebufferList.get(i).getColorAttachment(), kawaseDown, offset);
        }

        //Upsample
        for (int i = iterations; i > 1; i--) {
            renderFBO(matrices, framebufferList.get(i - 1), framebufferList.get(i).getColorAttachment(), kawaseUp, offset);
        }


        mc.getFramebuffer().beginWrite(true);

        RenderSystem.setShaderTexture(0, framebufferList.get(1).getColorAttachment());
        kawaseUp.init();
        kawaseUp.setUniformf("offset", offset, offset);
        kawaseUp.setUniformf("halfpixel", 0.5f / mc.getWindow().getFramebufferWidth(), 0.5f / mc.getWindow().getFramebufferHeight());
        kawaseUp.setUniformi("inTexture", 0);
        ShaderUtil.drawQuads(matrices);
        kawaseUp.unload();

    }

    private void renderFBO(MatrixStack matrices, Framebuffer framebuffer, int framebufferTexture, ShaderUtil shader, float offset) {
        framebuffer.clear(true);
        framebuffer.beginWrite(true);
        shader.init();
        RenderSystem.setShaderTexture(0, framebufferTexture);
        shader.setUniformf("offset", offset, offset);
        shader.setUniformi("inTexture", 0);
        shader.setUniformf("halfpixel", 0.5f / mc.getWindow().getFramebufferWidth(), 0.5f / mc.getWindow().getFramebufferHeight());
        ShaderUtil.drawQuads(matrices);
        shader.unload();
        framebuffer.endWrite();
    }
}
