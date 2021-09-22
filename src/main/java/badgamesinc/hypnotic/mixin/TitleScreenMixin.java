package badgamesinc.hypnotic.mixin;

import static badgamesinc.hypnotic.utils.MCUtils.mc;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import badgamesinc.hypnotic.Hypnotic;
import badgamesinc.hypnotic.ui.HypnoticMainMenu;
import badgamesinc.hypnotic.ui.altmanager.altmanager2.AltManagerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

	protected TitleScreenMixin(Text title) {
		super(title);
	}
	
	@Inject(at = @At("HEAD"), method = "init") 
	public void onInit(CallbackInfo ci) {
		mc.setScreen(new HypnoticMainMenu());
	}
	
	@Inject(at = @At("RETURN"), method = "initWidgetsNormal", cancellable = true)
	private void addAltManagerButton(int y, int spacingY, CallbackInfo callbackInfo) {
		((ButtonWidget)this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, 60 + y + spacingY * 2, 200, 20, new LiteralText("Alt Manager"), (button) -> {
	         mc.setScreen(AltManagerScreen.INSTANCE);
	    }))).active = true;
	}
	
	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void onRender(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo info) 
	{
//		info.cancel();
//		
//		if (RenderSystem.isOnRenderThread()) {
//			GlStateManager._disableCull();
////			this.backgroundShader.useShader(width + 100, height, mouseX, mouseY, (System.currentTimeMillis() - initTime) / 1000f);
//			
//			RenderSystem.disableCull();
//			RenderSystem.setShader(GameRenderer::getPositionColorShader);
//			Matrix4f matrix4f = matrices.peek().getModel();
//			BufferBuilder builder = Tessellator.getInstance().getBuffer();
//			builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
////			builder.vertex(matrix4f, -1f, -1f, 0);
////			builder.vertex(matrix4f, -1f, 1f, 0);
////			builder.vertex(matrix4f, 1f, 1f, 0);
////			builder.vertex(matrix4f, 1f, -1f, 0);
//			builder.end();
//			BufferRenderer.draw(builder);
////			GL11.glBegin(GL11.GL_QUADS);
//			
////			GL11.glVertex2f(-1f, -1f);
////			GL11.glVertex2f(-1f, 1f);
////			GL11.glVertex2f(1f, 1f);
////			GL11.glVertex2f(1f, -1f);
//			
////			RenderSystem.end
////			GL11.glEnd();
//			
//			// Unbind shader
////			GL20.glUseProgram(0);
//		}
		mc.textRenderer.drawWithShadow(matrices, Hypnotic.name + " " + Hypnotic.version, 2, 2, -1);
	}
}
