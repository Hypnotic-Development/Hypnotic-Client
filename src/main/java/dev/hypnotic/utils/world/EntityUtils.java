/*
* Copyright (C) 2022 Hypnotic Development
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package dev.hypnotic.utils.world;

import static dev.hypnotic.utils.MCUtils.mc;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import dev.hypnotic.config.friends.FriendManager;
import dev.hypnotic.utils.RotationUtils;
import dev.hypnotic.utils.player.FakePlayerEntity;
import dev.hypnotic.utils.player.PlayerUtils;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;

public class EntityUtils {

	public static boolean isAttackable(Entity e, boolean ignoreFriends) {
		return e instanceof LivingEntity
				&& e.isAlive()
				&& e != MinecraftClient.getInstance().player
				&& !e.isConnectedThroughVehicle(MinecraftClient.getInstance().player)
				&& (!ignoreFriends || !FriendManager.INSTANCE.isFriend((LivingEntity)e));
	}
	
	public static List<BlockPos> getSurroundBlocks(PlayerEntity player) {
        if (player == null) return null;

        List<BlockPos> positions = new ArrayList<>();

        for (Direction direction : Direction.values()) {
            if (direction == Direction.UP || direction == Direction.DOWN) continue;

            BlockPos pos = player.getBlockPos().offset(direction);

            if (mc.world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN) {
                positions.add(pos);
            }
        }

        return positions;
    }

    public static BlockPos getCityBlock(PlayerEntity player) {
        List<BlockPos> posList = getSurroundBlocks(player);
        posList.sort(Comparator.comparingDouble(PlayerUtils::distanceTo));
        return posList.isEmpty() ? null : posList.get(0);
    }
    
    public static GameMode getGameMode(PlayerEntity player) {
        if (player == null) return null;
        PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(player.getUuid());
        if (playerListEntry == null) return null;
        return playerListEntry.getGameMode();
    }
    
    public static class Target {
	    
	    private static final List<Entity> ENTITIES = new ArrayList<>();
	
	    public static Entity get(Predicate<Entity> isGood, SortPriority sortPriority) {
	        ENTITIES.clear();
	        getList(ENTITIES, isGood, sortPriority, 1);
	        if (!ENTITIES.isEmpty()) {
	            return ENTITIES.get(0);
	        }
	
	        return null;
	    }
	    
	    public static void getList(List<Entity> targetList, Predicate<Entity> isGood, SortPriority sortPriority, int maxCount) {
	        targetList.clear();
	
	        for (Entity entity : mc.world.getEntities()) {
	            if (isGood.test(entity)) targetList.add(entity);
	        }
	
	        for (Entity entity : mc.world.getEntities()) {
	            if (entity instanceof FakePlayerEntity && isGood.test(entity)) targetList.add(entity);
	        }
	
	        targetList.sort((e1, e2) -> sort(e1, e2, sortPriority));
	        targetList.removeIf(entity -> targetList.indexOf(entity) > maxCount -1);
	    }
	    
	    public static boolean isBadTarget(PlayerEntity target, double range) {
	        if (target == null) return true;
	        return mc.player.distanceTo(target) > range || !target.isAlive() || target.isDead() || target.getHealth() <= 0;
	    }
	    
	    public static PlayerEntity getPlayerTarget(double range, SortPriority priority) {
	        if (mc.player == null || mc.world == null) return null;
	        return (PlayerEntity) get(entity -> {
	            if (!(entity instanceof PlayerEntity) || entity == mc.player) return false;
	            if (((PlayerEntity) entity).isDead() || ((PlayerEntity) entity).getHealth() <= 0) return false;
	            if (mc.player.distanceTo(entity) > range) return false;
	            if (FriendManager.INSTANCE.isFriend((LivingEntity)entity)) return false;
	            return EntityUtils.getGameMode((PlayerEntity) entity) == GameMode.SURVIVAL || entity instanceof FakePlayerEntity;
	        }, priority);
	    }
	    
	    private static int sort(Entity e1, Entity e2, SortPriority priority) {
	        return switch (priority) {
	            case LowestDistance -> Double.compare(e1.distanceTo(mc.player), e2.distanceTo(mc.player));
	            case HighestDistance -> invertSort(Double.compare(e1.distanceTo(mc.player), e2.distanceTo(mc.player)));
	            case LowestHealth -> sortHealth(e1, e2);
	            case HighestHealth -> invertSort(sortHealth(e1, e2));
	            case ClosestAngle -> sortAngle(e1, e2);
	        };
	    }
	
	    private static int sortHealth(Entity e1, Entity e2) {
	        boolean e1l = e1 instanceof LivingEntity;
	        boolean e2l = e2 instanceof LivingEntity;
	
	        if (!e1l && !e2l) return 0;
	        else if (e1l && !e2l) return 1;
	        else if (!e1l) return -1;
	
	        return Float.compare(((LivingEntity) e1).getHealth(), ((LivingEntity) e2).getHealth());
	    }
	
	    private static int sortAngle(Entity e1, Entity e2) {
	        boolean e1l = e1 instanceof LivingEntity;
	        boolean e2l = e2 instanceof LivingEntity;
	
	        if (!e1l && !e2l) return 0;
	        else if (e1l && !e2l) return 1;
	        else if (!e1l) return -1;
	
	        double e1yaw = Math.abs(RotationUtils.getYaw(e1) - mc.player.getYaw());
	        double e2yaw = Math.abs(RotationUtils.getYaw(e2) - mc.player.getYaw());
	
	        double e1pitch = Math.abs(RotationUtils.getPitch(e1) - mc.player.getPitch());
	        double e2pitch = Math.abs(RotationUtils.getPitch(e2) - mc.player.getPitch());
	
	        return Double.compare(Math.sqrt(e1yaw * e1yaw + e1pitch * e1pitch), Math.sqrt(e2yaw * e2yaw + e2pitch * e2pitch));
	    }
	
	    private static int invertSort(int sort) {
	        if (sort == 0) return 0;
	        return sort > 0 ? -1 : 1;
	    }
    }
}
