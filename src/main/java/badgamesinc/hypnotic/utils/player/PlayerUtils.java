package badgamesinc.hypnotic.utils.player;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

import badgamesinc.hypnotic.event.events.EventMove;
import badgamesinc.hypnotic.mixin.EntityTrackingSectionAccessor;
import badgamesinc.hypnotic.mixin.SectionedEntityCacheAccessor;
import badgamesinc.hypnotic.mixin.SimpleEntityLookupAccessor;
import badgamesinc.hypnotic.mixin.WorldAccessor;
import badgamesinc.hypnotic.utils.world.Dimension;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongBidirectionalIterator;
import it.unimi.dsi.fastutil.longs.LongSortedSet;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.HopperBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.entity.EntityLookup;
import net.minecraft.world.entity.EntityTrackingSection;
import net.minecraft.world.entity.SectionedEntityCache;
import net.minecraft.world.entity.SimpleEntityLookup;

public class PlayerUtils {

	private static MinecraftClient mc = MinecraftClient.getInstance();
	
	public static boolean isMoving() {
		return mc.player.input.movementForward != 0 || mc.player.input.movementSideways != 0;
	}
	
	public static void setMoveSpeed(EventMove event, final double speed) {
        double forward = mc.player.input.movementForward;
        double strafe = mc.player.input.movementSideways;
        float yaw = mc.player.getYaw();
        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            event.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
            event.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
        }
    }
	
	public static float getTotalHealth(LivingEntity entity) {
        return entity.getHealth() + entity.getAbsorptionAmount();
    }
	
	public static boolean intersectsWithEntity(Box box, Predicate<Entity> predicate) {
        EntityLookup<Entity> entityLookup = ((WorldAccessor) mc.world).getEntityLookup();

        // Fast implementation using SimpleEntityLookup that returns on the first intersecting entity
        if (entityLookup instanceof Entity) {
            SimpleEntityLookup<Entity> simpleEntityLookup = (SimpleEntityLookup<Entity>)entityLookup;
        	SectionedEntityCache<Entity> cache = ((SimpleEntityLookupAccessor) simpleEntityLookup).getCache();
            LongSortedSet trackedPositions = ((SectionedEntityCacheAccessor) cache).getTrackedPositions();
            Long2ObjectMap<EntityTrackingSection<Entity>> trackingSections = ((SectionedEntityCacheAccessor) cache).getTrackingSections();

            int i = ChunkSectionPos.getSectionCoord(box.minX - 2);
            int j = ChunkSectionPos.getSectionCoord(box.minY - 2);
            int k = ChunkSectionPos.getSectionCoord(box.minZ - 2);
            int l = ChunkSectionPos.getSectionCoord(box.maxX + 2);
            int m = ChunkSectionPos.getSectionCoord(box.maxY + 2);
            int n = ChunkSectionPos.getSectionCoord(box.maxZ + 2);

            for (int o = i; o <= l; o++) {
                long p = ChunkSectionPos.asLong(o, 0, 0);
                long q = ChunkSectionPos.asLong(o, -1, -1);
                LongBidirectionalIterator longIterator = trackedPositions.subSet(p, q + 1).iterator();

                while (longIterator.hasNext()) {
                    long r = longIterator.nextLong();
                    int s = ChunkSectionPos.unpackY(r);
                    int t = ChunkSectionPos.unpackZ(r);

                    if (s >= j && s <= m && t >= k && t <= n) {
                        EntityTrackingSection<Entity> entityTrackingSection = trackingSections.get(r);

                        if (entityTrackingSection != null && entityTrackingSection.getStatus().shouldTrack()) {
                            for (Entity entity : ((EntityTrackingSectionAccessor) entityTrackingSection).<Entity>getCollection()) {
                                if (entity.getBoundingBox().intersects(box) && predicate.test(entity)) return true;
                            }
                        }
                    }
                }
            }

            return false;
        }
        // Slow implementation that loops every entity if for some reason the EntityLookup implementation is changed
        AtomicBoolean found = new AtomicBoolean(false);

        entityLookup.forEachIntersects(box, entity -> {
            if (!found.get() && predicate.test(entity)) found.set(true);
        });

        return found.get();
    }
	
	public static Dimension getDimension() {
        switch (mc.world.getRegistryKey().getValue().getPath()) {
            case "the_nether":
                return Dimension.NETHER;
            case "the_end":
                return Dimension.END;
            default:
                return Dimension.OVERWORLD;
        }
    }
	
	public static boolean isInsideBlock() {
        for(int x = MathHelper.floor(mc.player.getBoundingBox().minX); x < MathHelper.floor(mc.player.getBoundingBox().maxX) + 1; x++) {
            for(int y = MathHelper.floor(mc.player.getBoundingBox().minY); y < MathHelper.floor(mc.player.getBoundingBox().maxY) + 1; y++) {
                for(int z = MathHelper.floor(mc.player.getBoundingBox().minZ); z < MathHelper.floor(mc.player.getBoundingBox().maxZ) + 1; z++) {
                    Block block = mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if(block != null && !(block instanceof AirBlock)) {
                        Box boundingBox = new Box(new BlockPos(x, y, z));
                        if(block instanceof HopperBlock)
                            boundingBox = new Box(x, y, z, x + 1, y + 1, z + 1);
                        if(boundingBox != null && mc.player.getBoundingBox().intersects(boundingBox))
                            return true;
                    }
                }
            }
        }
        return false;
    }
	
	public static void setMotion(double speed) {
        double forward = mc.player.input.movementForward;
        double strafe = mc.player.input.movementSideways;
        float yaw = mc.player.getYaw();
        if ((forward == 0.0D) && (strafe == 0.0D)) {
            mc.player.setVelocity(0, mc.player.getVelocity().getY(), 0);
        } else {
            if (forward != 0.0D) {
                if (strafe > 0.0D) {
                    yaw += (forward > 0.0D ? -45 : 45);
                } else if (strafe < 0.0D) {
                    yaw += (forward > 0.0D ? 45 : -45);
                }
                strafe = 0.0D;
                if (forward > 0.0D) {
                    forward = 1;
                } else if (forward < 0.0D) {
                    forward = -1;
                }
            }
            double x = forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F));
            double z = forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F));
            mc.player.setVelocity(x, mc.player.getVelocity().getY(), z);
        }
    }
	
	public static void setSpeed(final double moveSpeed, double yVelocity, final float pseudoYaw, final double pseudoStrafe, final double pseudoForward) {
        double forward = pseudoForward;
        double strafe = pseudoStrafe;
        float yaw = pseudoYaw;
        if (pseudoForward != 0.0D) {
            if (pseudoStrafe > 0.0D) {
                yaw = pseudoYaw + (float)(pseudoForward > 0.0D ? -45 : 45);
            } else if (pseudoStrafe < 0.0D) {
                yaw = pseudoYaw + (float)(pseudoForward > 0.0D ? 45 : -45);
            }

            strafe = 0.0D;
            if (pseudoForward > 0.0D) {
                forward = 1.0D;
            } else if (pseudoForward < 0.0D) {
                forward = -1.0D;
            }
        }

        if (strafe > 0.0D) {
            strafe = 1.0D;
        } else if (strafe < 0.0D) {
            strafe = -1.0D;
        }

        double mx = Math.cos(Math.toRadians((double)(yaw + 90.0F)));
        double mz = Math.sin(Math.toRadians((double)(yaw + 90.0F)));
        double x = forward * moveSpeed * mx + strafe * moveSpeed * mz;
        double z = forward * moveSpeed * mz - strafe * moveSpeed * mx;
        mc.player.setVelocity(x, yVelocity, z);
    }
}
