package badgamesinc.hypnotic.module.render;

import java.awt.Color;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;

import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.systems.RenderSystem;

import badgamesinc.hypnotic.config.friends.FriendManager;
import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventEntityRender;
import badgamesinc.hypnotic.event.events.EventRender3D;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.BooleanSetting;
import badgamesinc.hypnotic.settings.settingtypes.ColorSetting;
import badgamesinc.hypnotic.settings.settingtypes.ModeSetting;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.render.RenderUtils;
import badgamesinc.hypnotic.utils.render.shader.OutlineShaderManager;
import badgamesinc.hypnotic.utils.render.shader.ShaderEffectLoader;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.ChestMinecartEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;

public class ESP extends Mod {

	public ModeSetting mode = new ModeSetting("Mode", "Shader", "Shader", "Box", "Box-Fill");
	
	public BooleanSetting players = new BooleanSetting("Players", true);
	public ColorSetting playerColor = new ColorSetting("Player Color", Integer.toHexString(Color.PINK.getRGB()));
	public ColorSetting friendColor = new ColorSetting("Friend Color", ColorUtils.pingle);
	
	public BooleanSetting monsters = new BooleanSetting("Monsters", false);
	public ColorSetting monsterColor = new ColorSetting("Monsters Color", Integer.toHexString(Color.RED.getRGB()));
	
	public BooleanSetting animals = new BooleanSetting("Animals", false);
	public ColorSetting animalColor = new ColorSetting("Animals Color", Integer.toHexString(Color.GREEN.getRGB()));
	
	public BooleanSetting passives = new BooleanSetting("Passives", false);
	public ColorSetting passiveColor = new ColorSetting("Passives Color", Integer.toHexString(Color.WHITE.getRGB()));
	
	public BooleanSetting invisibles = new BooleanSetting("Invisibles", true);
	public ColorSetting invisibleColor = new ColorSetting("Invisibles Color", Integer.toHexString(Color.ORANGE.getRGB()));
	
	public BooleanSetting items = new BooleanSetting("Items", false);
	public ColorSetting itemColor = new ColorSetting("Items Color", Integer.toHexString(Color.WHITE.getRGB()));
	
	public BooleanSetting crystals = new BooleanSetting("Crystals", true);
	public ColorSetting crystalColor = new ColorSetting("Crystals Color", Integer.toHexString(Color.MAGENTA.getRGB()));
	
	public NumberSetting shaderWidth = new NumberSetting("Shader Width", 1, 0, 2, 0.1);

	private int lastWidth = -1;
	private int lastHeight = -1;
	private double lastShaderWidth;
	private boolean shaderUnloaded = true;
	
	public ESP() {
		super("ESP", "Renders a box on players", Category.RENDER);
		addSettings(mode, players, playerColor, friendColor, monsters, monsterColor, animals, animalColor, passives, passiveColor, invisibles, invisibleColor, items, itemColor, crystals, crystalColor, shaderWidth);
	}

	@EventTarget
	public void eventRender3D(EventRender3D event) {
		for (Entity entity : mc.world.getEntities()) {
			if (shouldRenderEntity(entity) && entity != mc.player) {
				Vec3d renderPos = RenderUtils.getEntityRenderPosition(entity, event.getTickDelta());
				Box bb = new Box(renderPos.x - entity.getWidth() + 0.25, renderPos.y, renderPos.z - entity.getWidth() + 0.25, renderPos.x + entity.getWidth() - 0.25, renderPos.y + entity.getHeight() + 0.1, renderPos.z + entity.getWidth() - 0.25);
				if (mode.is("Box-Fill")) RenderUtils.drawEntityBox(event.getMatrices(), entity, renderPos.x, renderPos.y, renderPos.z, getEntityColor(entity, 80));
				if (mode.is("Box")) RenderUtils.drawOutlineBox(event.getMatrices(), bb, getEntityColor(entity, 80), true);
			}
		}
		
		
			if (mc.getWindow().getFramebufferWidth() != lastWidth || mc.getWindow().getFramebufferHeight() != lastHeight
					|| lastShaderWidth != shaderWidth.getValue() || shaderUnloaded) {
				try {
					ShaderEffect shader = ShaderEffectLoader.load(mc.getFramebuffer(), "esp-shader",
							String.format(
									Locale.ENGLISH,
									IOUtils.toString(getClass().getResource("/assets/hypnotic/shaders/mc_outline.ujson"), StandardCharsets.UTF_8), 
									shaderWidth.getValue() / 2,
									shaderWidth.getValue() / 4));

					shader.setupDimensions(mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight());
					lastWidth = mc.getWindow().getFramebufferWidth();
					lastHeight = mc.getWindow().getFramebufferHeight();
					lastShaderWidth = shaderWidth.getValue();
					shaderUnloaded = false;

					OutlineShaderManager.loadShader(shader);
				} catch (JsonSyntaxException | IOException e) {
					e.printStackTrace();
				}
			}
	}
	
	@EventTarget
	public void onEntityRender(EventEntityRender.Single.Pre event) {
		float[] color = getEntityColor(event.getEntity());

		if (shouldRenderEntity(event.getEntity())) {
			if (mode.is("Shader")) {
				event.setVertex(getOutline(mc.getBufferBuilders(), color[0], color[1], color[2]));
			}
		}
	}
	
	public boolean shouldRenderEntity(Entity entity) {
		if (players.isEnabled() && entity instanceof PlayerEntity) return true;
		if (monsters.isEnabled() && entity instanceof Monster) return true;
		if (animals.isEnabled() && entity instanceof AnimalEntity) return true;
		if (passives.isEnabled() && entity instanceof PassiveEntity && !(entity instanceof AnimalEntity)) return true;
		if (invisibles.isEnabled() && entity.isInvisible()) return true;
		if (items.isEnabled() && entity instanceof ItemEntity) return true;
		if (crystals.isEnabled() && entity instanceof EndCrystalEntity) return true;
		if (entity instanceof ChestMinecartEntity) return true;
		return false;
	}
	
	public Color getEntityColor(Entity entity, int alpha) {
		if (players.isEnabled() && entity instanceof PlayerEntity) {
			if (FriendManager.INSTANCE.isFriend((LivingEntity)entity)) return friendColor.getColor();
			else return playerColor.getColor();
		}
		if (entity instanceof Monster) return monsterColor.getColor();
		if (entity instanceof AnimalEntity) return animalColor.getColor();
		if (entity instanceof PassiveEntity) return passiveColor.getColor();
		if (entity.isInvisible()) return invisibleColor.getColor();
		if (entity instanceof ItemEntity) return itemColor.getColor();
		if (entity instanceof EndCrystalEntity) return crystalColor.getColor();
		return new Color(255, 255, 255);
	}
	
	private float[] getEntityColor(Entity entity) {
		if (players.isEnabled() && entity instanceof PlayerEntity) {
			if (FriendManager.INSTANCE.isFriend((LivingEntity)entity)) return friendColor.getRGBFloat();
			else return playerColor.getRGBFloat();
		} else if (monsters.isEnabled() && entity instanceof Monster) {
			return monsterColor.getRGBFloat();
		} else if (animals.isEnabled() && entity instanceof AnimalEntity) {
			return animalColor.getRGBFloat();
		} else if (items.isEnabled() && entity instanceof ItemEntity) {
			return itemColor.getRGBFloat();
		} else if (entity instanceof EndCrystalEntity && crystals.isEnabled()) {
			return crystalColor.getRGBFloat();
		} else if (invisibles.isEnabled() && entity.isInvisible()) {
			return invisibleColor.getRGBFloat();
		}

		return new float[] {1, 1, 1};
	}
	

	void renderOutline(Entity e, Color color, MatrixStack stack) {
        float red = color.getRed() / 255f;
        float green = color.getGreen() / 255f;
        float blue = color.getBlue() / 255f;
        float alpha = color.getAlpha() / 255f;
        Camera c = mc.gameRenderer.getCamera();
        Vec3d camPos = c.getPos();
        Vec3d start = e.getPos().subtract(camPos);
        float x = (float) start.x;
        float y = (float) start.y;
        float z = (float) start.z;

        double r = Math.toRadians(-c.getYaw() + 90);
        float sin = (float) (Math.sin(r) * (e.getWidth() / 1.7));
        float cos = (float) (Math.cos(r) * (e.getWidth() / 1.7));
        stack.push();

        Matrix4f matrix = stack.peek().getPositionMatrix();
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        GL11.glDepthFunc(GL11.GL_ALWAYS);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableBlend();
        buffer.begin(VertexFormat.DrawMode.DEBUG_LINES,
                VertexFormats.POSITION_COLOR);

        buffer.vertex(matrix, x + sin, y, z + cos).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x - sin, y, z - cos).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x - sin, y, z - cos).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x - sin, y + e.getHeight(), z - cos).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x - sin, y + e.getHeight(), z - cos).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x + sin, y + e.getHeight(), z + cos).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x + sin, y + e.getHeight(), z + cos).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x + sin, y, z + cos).color(red, green, blue, alpha).next();

        buffer.end();

        BufferRenderer.draw(buffer);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        RenderSystem.disableBlend();
        stack.pop();
    }
	
	private VertexConsumerProvider getOutline(BufferBuilderStorage buffers, float r, float g, float b) {
		OutlineVertexConsumerProvider ovsp = buffers.getOutlineVertexConsumers();
		ovsp.setColor((int) (r * 255), (int) (g * 255), (int) (b * 255), 255);
		return ovsp;
	}
	
	@Override
	public void onTickDisabled() {
		playerColor.setVisible(players.isEnabled());
		monsterColor.setVisible(monsters.isEnabled());
		animalColor.setVisible(animals.isEnabled());
		passiveColor.setVisible(passives.isEnabled());
		invisibleColor.setVisible(invisibles.isEnabled());
		itemColor.setVisible(items.isEnabled());
		crystalColor.setVisible(crystals.isEnabled());
		shaderWidth.setVisible(mode.is("Shader"));
		super.onTickDisabled();
	}
}
