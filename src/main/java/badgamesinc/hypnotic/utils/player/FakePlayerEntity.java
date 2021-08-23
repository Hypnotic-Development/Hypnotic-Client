package badgamesinc.hypnotic.utils.player;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;

public class FakePlayerEntity extends AbstractClientPlayerEntity {

    public FakePlayerEntity(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public Identifier getSkinTexture() {
        if (hasSkinTexture())
            return super.getSkinTexture();
        else
            return MCAPIHelper.INSTANCE.getPlayerSkin(MinecraftClient.getInstance().getSession().getProfile() == null ? this.uuid : MinecraftClient.getInstance().getSession().getProfile().getId());
    }
}
