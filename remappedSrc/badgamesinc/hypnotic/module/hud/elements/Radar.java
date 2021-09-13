package badgamesinc.hypnotic.module.hud.elements;

import java.awt.Color;

import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.module.hud.HudModule;
import badgamesinc.hypnotic.module.render.CustomFont;
import badgamesinc.hypnotic.settings.settingtypes.BooleanSetting;
import badgamesinc.hypnotic.settings.settingtypes.ColorSetting;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.font.FontManager;
import badgamesinc.hypnotic.utils.font.NahrFont;
import badgamesinc.hypnotic.utils.render.RenderUtils;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;

public class Radar extends HudModule {

	public BooleanSetting players = new BooleanSetting("Players", true);
	public BooleanSetting monsters = new BooleanSetting("Monsters", true);
	public BooleanSetting animals = new BooleanSetting("Animals", true);
	public BooleanSetting passives = new BooleanSetting("Passives", true);
	public BooleanSetting invisibles = new BooleanSetting("Invisibles", true);
	public BooleanSetting items = new BooleanSetting("Items", true);
	public NumberSetting size = new NumberSetting("Size", 100, 0, 200, 10);
	public ColorSetting color = new ColorSetting("Color", ColorUtils.pingle);
	NahrFont font = FontManager.robotoMed;
	
	
	public Radar() {
		super("Radar", "render the mans", 4, 20, 100, 100);
		addSettings(size, players, monsters, animals, passives, invisibles, items, color);
		this.setEnabled(true);
	}

	@Override
    public void render(MatrixStack matrices, int scaledWidth, int scaledHeight, float partialTicks) {
		this.setWidth((float) this.size.getValue());
		this.setHeight((float) this.size.getValue());
		MatrixStack matrixStack = matrices;
        if (mc.player == null) return;
        DrawableHelper.fill(matrixStack, index, index, index, keyCode, index);
        RenderUtils.drawBorderRect(matrices, this.getX() + 1, this.getY(), this.getX() + this.getWidth() - 1, this.getY() + this.getHeight(), color.getColor().getRGB(), 1);
        RenderUtils.fillAndBorder(matrixStack, this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), color.getRGB(), new Color(50, 50, 50, 250).getRGB(), 1);
        float midPos = this.getWidth() / 2.0f - 1;
        RenderUtils.fill(matrixStack, this.getX() + midPos, this.getY() + 1, this.getX() + midPos + 1, this.getY() + this.getHeight() - 1, new Color(60, 60, 60, 250).getRGB());
        RenderUtils.fill(matrixStack, this.getX() + 1, this.getY() + midPos, this.getX() + this.getWidth() - 1, this.getY() + midPos + 1, new Color(60, 60, 60, 250).getRGB());
        if (mc.world != null)
            for (Entity entity : mc.world.getEntities()) {
                if (!shouldRenderEntity(entity)) continue;
                float xPos = (float) (entity.getX() - mc.player.getX()) + midPos + this.getX();
                float yPos = (float) (entity.getZ() - mc.player.getZ()) + midPos + this.getY() + this.getHeight();
                if (xPos < this.getX() + this.getWidth() - 2 && yPos < this.getY() + this.getHeight() - 2 && yPos > this.getY() + 2 && xPos > this.getX() + 2) {
                    RenderUtils.fill(matrixStack, xPos, yPos, xPos + 1, yPos + 1, getEntityColor(entity).getRGB());
                }
            }
        /*if (Radar.INSTANCE.waypoints) {
            matrixStack.push();
            float scale = 0.75f;
            matrixStack.scale(scale, scale, 1);
            Waypoints.waypoints.forEach(waypoint -> {
                float xPos = (float) (waypoint.getX() - mc.player.getX()) + midPos + this.getX();
                float yPos = (float) (waypoint.getZ() - mc.player.getZ()) + midPos + this.getY() + this.getHeight();
                String letter = waypoint.getName().substring(0, 1);
                    if (xPos < this.getX() + this.getWidth() - 2 && yPos < this.getY() + this.getHeight() + this.getWidth() - 2 && yPos > this.getY() + this.getHeight() + 2 && xPos > this.getX() + 2) {
                        font.drawCenteredString(matrixStack, letter, xPos / scale, yPos / scale, waypoint.getColor());
                    }
            });
            matrixStack.pop();
        }*/
        matrixStack.push();
        matrixStack.translate(this.getX() + midPos + 0.5, this.getY() + midPos + 0.5, 0);
        RenderUtils.fill(matrixStack, -0.5f, -0.5f, 0.5f, 0.5f, color.getRGB());
        matrixStack.multiply(new Quaternion(new Vec3f(0.0F, 0.0F, 1.0F), mc.player.getYaw() + 180, true));
        drawPointer(matrixStack);
        matrixStack.pop();
        if (ModuleManager.INSTANCE.getModule(CustomFont.class).isEnabled()) {
	        font.drawCenteredString(matrixStack, "N", this.getX() + midPos + 1, this.getY() - 2, -1);
	        font.drawCenteredString(matrixStack, "S", this.getX() + midPos + 1, this.getY() + this.getWidth() - 15, -1);
	        font.drawWithShadow(matrixStack, "W", this.getX() + 3, this.getY() + (this.getWidth() / 2) - 9, -1);
	        font.drawWithShadow(matrixStack, "E", this.getX() + this.getWidth() - 3 - font.getStringWidth("E"), this.getY() + (this.getWidth() / 2) - 9, -1);
        } else {
        	mc.textRenderer.drawWithShadow(matrixStack, "N", this.getX() + midPos - 2, this.getY() + 4 - 2, -1);
        	mc.textRenderer.drawWithShadow(matrixStack, "S", this.getX() + midPos - 2, this.getY() + 4 + this.getHeight() - 15, -1);
        	mc.textRenderer.drawWithShadow(matrixStack, "W", this.getX() + 3, this.getY() + 4 + this.getHeight() / 2 - 9, -1);
        	mc.textRenderer.drawWithShadow(matrixStack, "E", this.getX() + this.getWidth() - 3 - font.getStringWidth("E"), this.getY() + 4 + this.getHeight() / 2 - 9, -1);
        }
        super.render(matrices, scaledWidth, scaledHeight, partialTicks);
	}
	
	public boolean shouldRenderEntity(Entity entity) {
		if (players.isEnabled() && entity instanceof PlayerEntity) return true;
		if (monsters.isEnabled() && entity instanceof Monster) return true;
		if (animals.isEnabled() && entity instanceof AnimalEntity) return true;
		if (passives.isEnabled() && entity instanceof PassiveEntity && !(entity instanceof AnimalEntity)) return true;
		if (invisibles.isEnabled() && entity.isInvisible()) return true;
		if (items.isEnabled() && entity instanceof ItemEntity) return true;
		return false;
	}
	
	public Color getEntityColor(Entity entity) {
		if (entity instanceof PlayerEntity) return new Color(255, 10, 100, 255);
		if (entity instanceof Monster) return new Color(255, 255, 255, 255);
		if (entity instanceof AnimalEntity) return new Color(30, 255, 30, 255);
		if (entity instanceof PassiveEntity) return new Color(255, 255, 255, 255);
		if (entity.isInvisible()) return new Color(255, 255, 255, 255);
		if (entity instanceof ItemEntity) return Color.green;
		return new Color(255, 255, 255);
	}

    private void drawPointer(MatrixStack matrixStack) {
        Matrix4f matrix4f = matrixStack.peek().getModel();
        Color color1 = color.getColor();

        RenderUtils.setup2DRender(false);

        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        matrixStack.scale(2.5f, 2.5f, 2.5f);
        matrixStack.translate(0, 1.5, 0);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS/*QUADS*/, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(matrix4f,0, -4, 0).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
        bufferBuilder.vertex(matrix4f,-1, 0, 0).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
        bufferBuilder.vertex(matrix4f,1, 0, 0).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
        bufferBuilder.vertex(matrix4f,0, -4, 0).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);

        RenderUtils.end2DRender();
    }
}
