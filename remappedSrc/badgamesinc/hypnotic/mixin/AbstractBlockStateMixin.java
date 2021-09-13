package badgamesinc.hypnotic.mixin;

import net.minecraft.block.AbstractBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.module.render.Xray;

@Mixin(AbstractBlock.AbstractBlockState.class)
public class AbstractBlockStateMixin {
    @Inject(method = "getLuminance", at = @At("HEAD"), cancellable = true)
    public void getLuminance(CallbackInfoReturnable<Integer> cir) {
        if (ModuleManager.INSTANCE.getModule(Xray.class).isEnabled()) cir.setReturnValue(15);
    }
}
