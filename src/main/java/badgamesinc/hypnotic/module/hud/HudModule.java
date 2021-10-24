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

	private int defaultX, defaultY, x, y, dragX, dragY;
	private float width, height;
	private double scaleX, scaleY, scaleStartX, scaleStartY, startWidth, startHeight, prevScaleX, prevScaleY;
	private boolean dragging, scaling, draggable;
	public NumberSetting xSet;
	public NumberSetting ySet;
	protected NahrFont font = FontManager.robotoMed; 
	
	public HudModule(String name, String description, int defaultX, int defaultY, int width, int height) {
		super(name, description, null);
		this.defaultX = defaultX;
		this.defaultY = defaultY;
		this.width = width;
		this.height = height;
		this.scaleX = 1;
		this.scaleY = 1;
		this.draggable = true;
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

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
	
	public double getPrevScaleX() {
		return prevScaleX;
	}
	
	public double getPrevScaleY() {
		return prevScaleY;
	}
	
	public void setPrevScaleX(double d) {
		this.prevScaleX = d;
	}
	
	public void setPrevScaleY(double d) {
		this.prevScaleY = d;
	}
	
	public boolean hovered(int mouseX, int mouseY) {
		return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
	}
	
	public boolean hovered(int mouseX, int mouseY, float f, float g, float h, float i) {
		return mouseX >= f && mouseX <= h && mouseY >= g && mouseY <= i;
	}
	
	public boolean isDragging() {
		return dragging;
	}
	
	public void setDragging(boolean dragging) {
		SaveLoad.INSTANCE.save();
		if (this.isDraggable()) this.dragging = dragging;
		else this.dragging = false;
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
	
	public double getScaleX() {
		return scaleX;
	}
	
	public double getScaleY() {
		return scaleY;
	}
	
	public void setScaleX(double scaleX) {
		this.scaleX = scaleX;
	}
	
	public void setScaleY(double scaleY) {
		this.scaleY = scaleY;
	}
	
	public double getScaleStartX() {
		return scaleStartX;
	}
	
	public double getScaleStartY() {
		return scaleStartY;
	}
	
	public void setScaleStartX(double d) {
		this.scaleStartX = d;
	}
	
	public void setScaleStartY(double d) {
		this.scaleStartY = d;
	}
	
	public double getStartWidth() {
		return startWidth;
	}
	
	public double getStartHeight() {
		return startHeight;
	}
	
	public void setStartWidth(float startWidth) {
		this.startWidth = startWidth;
	}
	
	public void setStartHeight(float startHeight) {
		this.startHeight = startHeight;
	}
	
	public boolean isScaling() {
		return scaling;
	}
	
	public boolean isDraggable() {
		return draggable;
	}
	
	public void setDraggable(boolean draggable) {
		this.draggable = draggable;
	}
	
	public void setScaling(boolean scaling, int mouseX, int mouseY) {
		this.setScaleStartX(mouseX - this.getPrevScaleX());
		this.setScaleStartY(mouseY + this.getPrevScaleY());
		this.setStartWidth(this.getWidth());
		this.setStartHeight(this.getHeight());
		this.scaling = scaling;
	}
	
	public void setScaling(boolean scaling) {
		this.setPrevScaleX(this.getScaleX());
		this.setPrevScaleY(this.getScaleY());
		this.scaling = scaling;
	}
	
	public void render(MatrixStack matrices, int scaledWidth, int scaledHeight, float partialTicks) {
		font = FontManager.robotoMed2;
		for (HudModule element : HudManager.INSTANCE.hudModules) {
			if (mc.currentScreen instanceof HudEditorScreen) {
				if (element.isDraggable()) RenderUtils.fillAndBorder(matrices, element.getX(), element.getY(), element.getX() + element.getWidth(), element.getY() + element.getHeight(), element.isEnabled() ? -1 : new Color(255, 255, 255, 20).getRGB(), 0, -1);
			}
		}
	}
	
	public void updatePosition(int mouseX, int mouseY) {
		if (isDragging()) {
			setX(mouseX - dragX);
			setY(mouseY - dragY);
		}
		if (isScaling()) {
			if (1 - (getScaleStartX() - mouseX) * 0.1 > -1000000) {
				double scaleFactorX = 1 - (this.scaleStartX - mouseX) * 0.02;
				double scaleFactorY = 1 - (this.scaleStartY - mouseY) * 0.02;
				setScaleX(scaleFactorX);
				setScaleY(scaleFactorY);
				this.setWidth((int) (this.startWidth * scaleFactorX));
				this.setHeight((int) (this.startHeight * scaleFactorY));
			}
		}
	}
}
