package badgamesinc.hypnotic.module.hud;

import java.awt.Color;

import badgamesinc.hypnotic.config.SaveLoad;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;
import badgamesinc.hypnotic.ui.HudEditorScreen;
import badgamesinc.hypnotic.utils.font.FontManager;
import badgamesinc.hypnotic.utils.font.NahrFont;
import badgamesinc.hypnotic.utils.render.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;

public class HudModule extends Mod {

	private int defaultX, defaultY, x, y, width, height, dragX, dragY;
	private boolean dragging;
	public NumberSetting xSet;
	public NumberSetting ySet;
	protected NahrFont font = FontManager.robotoMed; 
	
	public HudModule(String name, String description, int defaultX, int defaultY, int width, int height) {
		super(name, description, null);
		this.defaultX = defaultX;
		this.defaultY = defaultY;
		this.width = width;
		this.height = height;
		xSet = new NumberSetting("X", defaultX, 0, 1920, 1);
		ySet = new NumberSetting("Y", defaultY, 0, 1080, 1);
		this.x = (int) xSet.getValue();
		this.y = (int) ySet.getValue();
	}

	public int getDefaultX() {
		return defaultX;
	}

	public void setDefaultX(int defaultX) {
		this.defaultX = defaultX;
	}

	public int getDefaultY() {
		return defaultY;
	}

	public void setDefaultY(int defaultY) {
		this.defaultY = defaultY;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
		this.xSet.setValue(x);
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
		this.ySet.setValue(y);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public boolean hovered(int mouseX, int mouseY) {
		return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
	}
	
	public boolean isDragging() {
		return dragging;
	}
	
	public void setDragging(boolean dragging) {
		SaveLoad.INSTANCE.save();
		this.dragging = dragging;
	}
	
	public int getDragX() {
		return dragX;
	}
	
	public int getDragY() {
		return dragY;
	}
	
	public void setDragX(int dragX) {
		this.dragX = dragX;
	}
	
	public void setDragY(int dragY) {
		this.dragY = dragY;
	}
	
	public void render(MatrixStack matrices, int scaledWidth, int scaledHeight, float partialTicks) {
		font = FontManager.robotoMed;
		for (HudModule element : HudManager.INSTANCE.hudModules) {
			if (mc.currentScreen instanceof HudEditorScreen) {
				RenderUtils.fillAndBorder(matrices, element.getX(), element.getY(), element.getX() + element.getWidth(), element.getY() + element.getHeight(), element.isEnabled() ? -1 : new Color(255, 255, 255, 20).getRGB(), 0, -1);
			}
		}
	}
	
	public void updatePosition(int mouseX, int mouseY) {
		if (isDragging()) {
			setX(mouseX - dragX);
			setY(mouseY - dragY);
		}
	}
}
