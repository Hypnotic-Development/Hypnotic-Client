package badgamesinc.hypnotic.ui.clickgui;

import java.awt.Color;

import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.ui.clickgui.settings.SettingsWindow;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.font.FontManager;
import badgamesinc.hypnotic.utils.render.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class ModuleButton {

	public Mod mod;
	public int x, y, width, height, animation2, startY;
	double animation = 0;
	public SettingsWindow settingsWindow = null;
	public static boolean open = false;
	
	public ModuleButton(Mod mod, Category cateogry, int x, int y) {
		this.mod = mod;
		this.x = x;
		this.y = y;
		this.startY = y;
		this.width = 100;
		this.height = 10;
	}
	
	public void render(MatrixStack matrices, int mouseX, int mouseY) {
		width = 400;
		height = 10;
		if (settingsWindow == null)
			open = false;
		else
			open = true;
		if (!mod.isEnabled()) {
			if (animation < 8) animation+=1;
			if (animation2 > 0) animation2-=5;
		} else if (mod.isEnabled()) {
			if (animation > 0) animation-=1;
			if (animation2 < 255) animation2+=5;
		}
		RenderUtils.setup2DRender(false);
		RenderUtils.bindTexture(new Identifier("hypnotic", "textures/modulebackground.png"));
		RenderUtils.drawTexture(matrices, x - 15, y - 11, 430, 32, 0, 0, 430, 32, 430, 32);
		RenderUtils.drawRoundedRect(matrices, x, y, x + width, y + height, 6, new Color(55, 55, 55));
		FontManager.roboto.drawWithShadow(matrices, mod.getName(), x, y, new Color(255, 255, 255).getRGB());
		Color color = ColorUtils.defaultClientColor();
		RenderUtils.drawFilledCircle(matrices, x + width - 30, y - 5 + height / 2, 10, new Color(45, 45, 45));
		RenderUtils.drawFilledCircle(matrices, x + width - 20, y - 5 + height / 2, 10, new Color(45, 45, 45));
		RenderUtils.fill(matrices, x + width - 25, y + 5 + height / 2, x + width - 15, y - 5 + height / 2, new Color(45, 45, 45).getRGB());
		if (mod.isEnabled()) {
			RenderUtils.drawFilledCircle(matrices, x + width - 30, y - 5 + height / 2, 10, color);
			RenderUtils.fill(matrices, x + width - 24 - animation / 4, y + 5 + height / 2, x + width - 15 - animation, y - 5 + height / 2, color.getRGB());
			RenderUtils.drawFilledCircle(matrices, x + width - 20 - animation, y - 5 + height / 2, 10, color);
		}
		RenderUtils.drawFilledCircle(matrices, x + width - (20 + animation), y - 4 + height / 2, 8, new Color(255, 255, 255, 255));
		RenderUtils.end2DRender();
	}
	
	public boolean hovered(int mouseX, int mouseY) {
		return mouseX >= x - 3 && mouseX <= x + width && mouseY >= y - 3 && mouseY <= y + height;
	}
	
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if (hovered(mouseX, mouseY)) {
			if (button == 0) {
				mod.toggle();
			} else if (button == 1) {
				settingsWindow = new SettingsWindow(this);
			}
		}
	}
	
	public void init() {
		animation = 0;
		animation2 = 0;
	}
}
