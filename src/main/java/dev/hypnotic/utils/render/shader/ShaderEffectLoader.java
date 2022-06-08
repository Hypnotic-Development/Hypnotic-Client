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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.google.gson.JsonSyntaxException;

import it.unimi.dsi.fastutil.io.FastByteArrayInputStream;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.metadata.ResourceMetadata;
import net.minecraft.util.Identifier;

public class ShaderEffectLoader {

	private static final TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
	private static final ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();

	public static ShaderEffect load(Framebuffer framebuffer, String name, InputStream input) throws JsonSyntaxException, IOException {
		Identifier id = new Identifier("hypnotic", name);
		return new ShaderEffect(textureManager, new OwResourceManager(resourceManager, id, new InputStreamResource(input)), framebuffer, id);
	}

	public static ShaderEffect load(Framebuffer framebuffer, String name, String input) throws JsonSyntaxException, IOException {
		return load(framebuffer, name, new FastByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
	}
	
	public static ShaderEffect load(Framebuffer framebuffer, Identifier id, String input) throws JsonSyntaxException, IOException {
		return new ShaderEffect(textureManager, new OwResourceManager(resourceManager, id, new InputStreamResource(new FastByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)))), framebuffer, id);
	}

	private static class InputStreamResource extends Resource {

		private InputStream input;

		public InputStreamResource(InputStream input) {
			super(null, () -> input);
			this.input = input;
		}

		@Override
		public String getResourcePackName() {
			return null;
		}

		@Override
		public ResourceMetadata getMetadata() throws IOException {
			return null;
		}

		@Override
		public InputStream getInputStream() {
			return input;
		}
	}

	private static class OwResourceManager implements ResourceManager {

		private ResourceManager resourceMang;
		private Identifier id;
		private InputStreamResource resource;

		public OwResourceManager(ResourceManager resourceMang, Identifier id, InputStreamResource resource) {
			this.resourceMang = resourceMang;
			this.id = id;
			this.resource = resource;
		}

		@Override
		public Set<String> getAllNamespaces() {
			return resourceMang.getAllNamespaces();
		}

		@Override
		public List<Resource> getAllResources(Identifier id) {
			return resourceMang.getAllResources(id);
		}

		@Override
		public Stream<ResourcePack> streamResourcePacks() {
			return resourceMang.streamResourcePacks();
		}

		@Override
		public Optional<Resource> getResource(Identifier var1) {
			return id.equals(this.id) ? Optional.of(resource) : resourceMang.getResource(id);
		}

		@Override
		public Map<Identifier, Resource> findResources(String var1, Predicate<Identifier> var2) {
			return resourceMang.findResources(var1, var2);
		}

		@Override
		public Map<Identifier, List<Resource>> findAllResources(String var1, Predicate<Identifier> var2) {
			return resourceMang.findAllResources(var1, var2);
		}

	}

}
