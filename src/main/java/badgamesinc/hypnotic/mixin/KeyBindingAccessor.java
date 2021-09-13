package badgamesinc.hypnotic.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

@Mixin(KeyBinding.class)
public interface KeyBindingAccessor {
    @Accessor("categoryOrderMap") @Final
    static Map<String, Integer> getCategoryOrderMap() { return null; }

    @Accessor("boundKey")
    InputUtil.Key getKey();
}
