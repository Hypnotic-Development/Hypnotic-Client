package dev.hypnotic.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.hypnotic.ui.serverfinder.ServerFinder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

@Mixin(MultiplayerScreen.class)
public abstract class MultiplayerScreenMixin extends Screen {

	protected MultiplayerScreenMixin(Text title) {
		super(title);
	}

	@Inject(method = "init", at = @At("TAIL"), cancellable = true)
	private void addServerFinderButton(CallbackInfo ci) {
		this.addDrawableChild(new ButtonWidget(this.width - 120, this.height - 30, 100, 20, new LiteralText("Server Finder"), (button) -> {
			MinecraftClient.getInstance().setScreen(new ServerFinder((MultiplayerScreen)(Object)this));
	    }));
	}
	
}
