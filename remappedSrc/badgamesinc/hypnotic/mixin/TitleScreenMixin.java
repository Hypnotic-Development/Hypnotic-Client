package badgamesinc.hypnotic.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import badgamesinc.hypnotic.Hypnotic;
import badgamesinc.hypnotic.ui.altmanager.altmanager2.AltManagerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import static badgamesinc.hypnotic.utils.MCUtils.mc;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

	protected TitleScreenMixin(Text title) {
		super(title);
	}

	@Inject(at = @At("RETURN"), method = "initWidgetsNormal")
	private void addAltManagerButton(int y, int spacingY, CallbackInfo callbackInfo) {
		((ButtonWidget)this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, 60 + y + spacingY * 2, 200, 20, new LiteralText("Alt Manager"), (button) -> {
	         mc.setScreen(AltManagerScreen.INSTANCE);
	    }))).active = true;
	}
	
	@Inject(method = "render", at = @At("TAIL"))
    private void onRender(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo info) 
	{
		mc.textRenderer.drawWithShadow(matrices, Hypnotic.name + " " + Hypnotic.version, 2, 2, -1);
	}
}
