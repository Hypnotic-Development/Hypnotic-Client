package badgamesinc.hypnotic.ui.altmanager;

import java.awt.Color;
import java.io.File;

import badgamesinc.hypnotic.Hypnotic;
import badgamesinc.hypnotic.ui.altmanager.account.Account;
import badgamesinc.hypnotic.ui.altmanager.account.Accounts;
import badgamesinc.hypnotic.utils.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class AltManagerScreen extends Screen {
	
	private String status;
	@SuppressWarnings("unused")
	private boolean loggingIn = false;
	
	protected AltManagerScreen() {
		super(new LiteralText("AltManager"));
	}
	
	public static AltManagerScreen INSTANCE = new AltManagerScreen();
	public File altsFile = new File(Hypnotic.hypnoticDir, "alts.txt");
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		fill(matrices, 100, 70, width - 100, height - 70, new Color(0, 0, 0, 100).getRGB());
		drawStringWithShadow(matrices, textRenderer, MinecraftClient.getInstance().getSession().getProfile().getName(), 20, 20, -1);
		RenderUtils.drawCenteredStringWithShadow(matrices, textRenderer, status, width / 2, 20, -1);
		((ButtonWidget)this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, height - 50, 200, 20, new LiteralText("Add alt"), (button) -> {
	         MinecraftClient.getInstance().setScreen(new AddAltScreen(this));
	    }))).active = true;
		
		int offset = 0;
		for (Account<?> alt : Accounts.get()) {
			((ButtonWidget)this.addDrawableChild(new ButtonWidget(100, height - 50 + offset, 200, 20, new LiteralText("Login"), (button) -> {
		         MinecraftClient.getInstance().setScreen(new AddAltScreen(this));
		    }))).active = true;
			drawStringWithShadow(matrices, textRenderer, alt.getUsername(), 100 + 5, offset, -1);
			drawStringWithShadow(matrices, textRenderer, alt.getType().name(), 100 + 5, offset + 20, -1);
			offset+=50;
		}
		super.render(matrices, mouseX, mouseY, delta);
	}
	
	@Override
	protected void init() {
		status = "Idle...";
		AltsFile.INSTANCE.loadAlts();
		super.init();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		return super.mouseClicked(mouseX, mouseY, button);
	}
}
