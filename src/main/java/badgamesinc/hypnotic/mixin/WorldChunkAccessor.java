package badgamesinc.hypnotic.mixin;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(WorldChunk.class)
public interface WorldChunkAccessor {
    @Accessor("blockEntities")
    Map<BlockPos, BlockEntity> getBlockEntities();
}
