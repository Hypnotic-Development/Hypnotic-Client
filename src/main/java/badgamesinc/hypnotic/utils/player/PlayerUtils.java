package badgamesinc.hypnotic.utils.player;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

import badgamesinc.hypnotic.event.events.EventMove;
import badgamesinc.hypnotic.mixin.EntityTrackingSectionAccessor;
import badgamesinc.hypnotic.mixin.SectionedEntityCacheAccessor;
import badgamesinc.hypnotic.mixin.SimpleEntityLookupAccessor;
import badgamesinc.hypnotic.mixin.WorldAccessor;
import badgamesinc.hypnotic.utils.ReflectionHelper;
import badgamesinc.hypnotic.utils.world.Dimension;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongBidirectionalIterator;
import it.unimi.dsi.fastutil.longs.LongSortedSet;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HopperBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
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
	
	public static float getDistanceToGround() {
        if (mc.player.verticalCollision && mc.player.isOnGround()) {
            return 0.0F;
        }
        ClientPlayerEntity e = mc.player;
        for (float a = (float) e.getY(); a > 0.0F; a -= 1.0F) {
            int[] stairs = {53, 67, 108, 109, 114, 128, 134, 135, 136, 156, 163, 164, 180};
            int[] exemptIds = {
                    6, 27, 28, 30, 31, 32, 37, 38, 39, 40, 50, 51, 55, 59,
                    63, 65, 66, 68, 69, 70, 72, 75, 76, 77, 83, 92, 93, 94,
                    104, 105, 106, 115, 119, 131, 132, 143, 147, 148, 149, 150,
                    157, 171, 175, 176, 177};
            BlockState block = mc.world.getBlockState(new BlockPos(e.getX(), a - 1.0F, e.getZ()));
            if (!(block.getBlock() instanceof AirBlock)) {
                if ((Block.getRawIdFromState(block) == 44) || (Block.getRawIdFromState(block) == 126)) {
                    return (float) (e.getY() - a - 0.5D) < 0.0F ? 0.0F : (float) (e.getY() - a - 0.5D);
                }
                int[] arrayOfInt1;
                int j = (arrayOfInt1 = stairs).length;
                for (int i = 0; i < j; i++) {
                    int id = arrayOfInt1[i];
                    if (Block.getRawIdFromState(block) == id) {
                        return (float) (e.getY() - a - 1.0D) < 0.0F ? 0.0F : (float) (e.getY() - a - 1.0D);
                    }
                }
                j = (arrayOfInt1 = exemptIds).length;
                for (int i = 0; i < j; i++) {
                    int id = arrayOfInt1[i];
                    if (Block.getRawIdFromState(block) == id) {
                        return (float) (e.getY() - a) < 0.0F ? 0.0F : (float) (e.getY() - a);
                    }
                }
                return (float) (e.getY() - a + block.getBlock().getMaxModelOffset() - 1.0D);
            }
        }
        return 0.0F;
    }
	
	public static int getSpeedEffect() {
        if (mc.player.hasStatusEffect(StatusEffects.SPEED))
            return mc.player.getStatusEffect(StatusEffects.SPEED).getAmplifier() + 1;
        else
            return 0;
    }
	
	public static void teleport(BlockPos endPos){
    	double dist = Math.sqrt(mc.player.squaredDistanceTo(endPos.getX(), endPos.getY(), endPos.getZ()));
    	double packetDist = 5;
    	double xtp, ytp, ztp = 0;
    	
    	if(dist> packetDist){
    		double nbPackets = Math.round(dist / packetDist + 0.49999999999) - 1;
    		xtp = mc.player.getX();
    		ytp = mc.player.getY();
    		ztp = mc.player.getZ();		
    		for (int i = 1; i < nbPackets;i++){		
    			double xdi = (endPos.getX() - mc.player.getX())/( nbPackets);	
    			xtp += xdi;
    			 
    			double zdi = (endPos.getZ() - mc.player.getZ())/( nbPackets);	
    			ztp += zdi;
    			 
    			double ydi = (endPos.getY() - mc.player.getY())/( nbPackets);	
    			ytp += ydi;
    			PlayerMoveC2SPacket.PositionAndOnGround packet= new PlayerMoveC2SPacket.PositionAndOnGround(xtp, ytp, ztp, true);
    			
    			mc.player.networkHandler.sendPacket(packet);
    		}
    		
    		mc.player.setPosition(endPos.getX() + 0.5, endPos.getY(), endPos.getZ() + 0.5);
    	}else{
    		mc.player.setPosition(endPos.getX(), endPos.getY(), endPos.getZ());
    	}
    }
	
	public static void blinkToPos(Vec3d startPos, final BlockPos endPos, final double slack, final double[] pOffset) {
        double curX = startPos.x;
        double curY = startPos.y;
        double curZ = startPos.z;
        try {
            final double endX = endPos.getX() + 0.5;
            final double endY = endPos.getY() + 1.0;
            final double endZ = endPos.getZ() + 0.5;

            double distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
            int count = 0;
            while (distance > slack) {
                distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
                if (count > 120) {
                    break;
                }
                final double diffX = curX - endX;
                final double diffY = curY - endY;
                final double diffZ = curZ - endZ;
                final double offset = ((count & 0x1) == 0x0) ? pOffset[0] : pOffset[1];
                if (diffX < 0.0) {
                    if (Math.abs(diffX) > offset) {
                        curX += offset;
                    } else {
                        curX += Math.abs(diffX);
                    }
                }
                if (diffX > 0.0) {
                    if (Math.abs(diffX) > offset) {
                        curX -= offset;
                    } else {
                        curX -= Math.abs(diffX);
                    }
                }
                if (diffY < 0.0) {
                    if (Math.abs(diffY) > 0.25) {
                        curY += 0.25;
                    } else {
                        curY += Math.abs(diffY);
                    }
                }
                if (diffY > 0.0) {
                    if (Math.abs(diffY) > 0.25) {
                        curY -= 0.25;
                    } else {
                        curY -= Math.abs(diffY);
                    }
                }
                if (diffZ < 0.0) {
                    if (Math.abs(diffZ) > offset) {
                        curZ += offset;
                    } else {
                        curZ += Math.abs(diffZ);
                    }
                }
                if (diffZ > 0.0) {
                    if (Math.abs(diffZ) > offset) {
                        curZ -= offset;
                    } else {
                        curZ -= Math.abs(diffZ);
                    }
                }
                mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(curX, curY, curZ, true));
                ++count;
            }
        } catch (Exception e) {

        }
    }
	
	public static double distanceTo(Entity entity) {
        return distanceTo(entity.getX(), entity.getY(), entity.getZ());
    }

    public static double distanceTo(BlockPos blockPos) {
        return distanceTo(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public static double distanceTo(Vec3d vec3d) {
        return distanceTo(vec3d.getX(), vec3d.getY(), vec3d.getZ());
    }

    public static double distanceTo(double x, double y, double z) {
        float f = (float) (mc.player.getX() - x);
        float g = (float) (mc.player.getY() - y);
        float h = (float) (mc.player.getZ() - z);
        return MathHelper.sqrt(f * f + g * g + h * h);
    }
    
    public static void setTimerSpeed(float speed) {
    	ReflectionHelper.setPrivateValue(RenderTickCounter.class, ReflectionHelper.getPrivateValue(MinecraftClient.class, mc, "renderTickCounter", "field_1728"), 1000.0F / (float) speed / 20, "tickTime", "field_1968");
    }
    
    public static boolean isOnGround(double height) {
        if (!mc.world.getBlockCollisions(mc.player, mc.player.getBoundingBox().offset(0.0D, -height, 0.0D)).toList().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}
