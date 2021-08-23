package badgamesinc.hypnotic.module.combat;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.google.common.collect.Streams;

import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventDestroyBlock;
import badgamesinc.hypnotic.event.events.EventEntity;
import badgamesinc.hypnotic.event.events.EventReceivePacket;
import badgamesinc.hypnotic.event.events.EventSendPacket;
import badgamesinc.hypnotic.mixin.PlayerMoveC2SPacketAccessor;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.BooleanSetting;
import badgamesinc.hypnotic.settings.settingtypes.ModeSetting;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;
import badgamesinc.hypnotic.utils.Comparators;
import badgamesinc.hypnotic.utils.TimeHelper;
import badgamesinc.hypnotic.utils.math.MathUtils;
import badgamesinc.hypnotic.utils.player.inventory.InventoryUtils;
import badgamesinc.hypnotic.utils.world.WorldUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnchantedGoldenAppleItem;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.Difficulty;
import net.minecraft.world.explosion.Explosion;

public class AutoCrystal extends Mod {

    private final BooleanSetting doSwitch = new BooleanSetting("Do Switch", true);
    private final BooleanSetting noGappleSwitch = new BooleanSetting("No Gapple Switch", false);
    private final BooleanSetting predictMovement = new BooleanSetting("Predict Movement", true);
    private final BooleanSetting preventSuicide = new BooleanSetting("Prevent Suicide", true);
    private final ModeSetting targetSetting = new ModeSetting("Target", "Closest", "Closest", "Most Damage");
    private final ModeSetting rotateMode = new ModeSetting("Rotations", "Packet", "Packet", "Real", "None");
    private final ModeSetting order = new ModeSetting("Order", "Place-Break", "Place-Break", "Break-Place");
    private final BooleanSetting pauseOnEat = new BooleanSetting("Pause On Eat", true);
    private final BooleanSetting pauseOnPot = new BooleanSetting("Pause On Pot", true);
    private final BooleanSetting pauseOnXP = new BooleanSetting("Pause On XP", false);
    private final BooleanSetting pauseOnMine = new BooleanSetting("Pause On Mine", false);

    private final NumberSetting placeRange = new NumberSetting("Place Range", 5, 0, 10, 0.1);
    private final NumberSetting placeDelay = new NumberSetting("Place Delay", 2, 0, 20, 1);
    private final NumberSetting placeOffhandDelay = new NumberSetting("Offh. Place Delay", 2, 0, 20, 1);
    private final NumberSetting minDamage = new NumberSetting("Minimum Damage", 7.5, 0, 15, 0.1);
    private final BooleanSetting oneDotTwelve = new BooleanSetting("1.12-", false);
    private final BooleanSetting antiSurround = new BooleanSetting("Anti-Surround", true);
    private final ModeSetting placeMode = new ModeSetting("Place Mode", "Damage", "Damage", "Distance");

    private final NumberSetting breakRange = new NumberSetting("Break Range", 5, 0, 10, 0.1);
    private final NumberSetting breakDelay = new NumberSetting("Break Delay", 2, 0, 20, 1);
    private final NumberSetting breakOffhandDelay = new NumberSetting("Offh. Break Delay", 2, 0, 20, 1);
    private final NumberSetting breakAge = new NumberSetting("Break Age", 0, 0, 20, 1);
    private final BooleanSetting breakOnSpawn = new BooleanSetting("Break On Spawn", true);
    private final ModeSetting breakMode = new ModeSetting("Break Mode", "Smart", "Smart", "All", "Own");
    private final NumberSetting maxBreakTries = new NumberSetting("Break Attempts", 3, 1, 5, 1);
    private final NumberSetting lostWindow = new NumberSetting("Fail Window", 6, 0, 20, 1);
    private final BooleanSetting retryLost = new BooleanSetting("Retry Failed Crystals", true);
    private final NumberSetting retryAfter = new NumberSetting("Retry After", 4, 0, 20, 1);
    private final BooleanSetting sync = new BooleanSetting("Sync", true);
    
    enum Mode { DAMAGE, DISTANCE }
    enum BreakMode { OWN, SMART, ALL }
    enum Order { PLACE_BREAK, BREAK_PLACE }
    enum Target { CLOSEST, MOST_DAMAGE }
    enum Rotations { PACKET, REAL, NONE }

    private final TimeHelper renderTimer = new TimeHelper();
    private final TimeHelper placeTimer = new TimeHelper();
    private final TimeHelper breakTimer = new TimeHelper();
    private final TimeHelper cleanupTimer = new TimeHelper();
    private final TimeHelper rotationTimer = new TimeHelper();
    private BlockPos rotatePos = null;
    private double[] rotations = null;
    public BlockPos target = null;
    private final LinkedHashMap<Vec3d, Long> placedCrystals = new LinkedHashMap<>();
    private final LinkedHashMap<EndCrystalEntity, AtomicInteger> spawnedCrystals = new LinkedHashMap<>();
    private final LinkedHashMap<EndCrystalEntity, Integer> lostCrystals = new LinkedHashMap<>();
    private Entity targetPlayer;

    public AutoCrystal() {
        super("AutoCrystal", "cpvp god gamer", Category.COMBAT);
        addSettings(placeMode, targetSetting, rotateMode, order, breakMode, placeRange, placeDelay, placeOffhandDelay, minDamage, breakRange, 
        		breakDelay, breakOffhandDelay, breakAge, maxBreakTries, lostWindow, retryAfter, sync, doSwitch, noGappleSwitch, predictMovement, preventSuicide, pauseOnEat, pauseOnPot,
        		pauseOnXP, pauseOnMine, oneDotTwelve, antiSurround, breakOnSpawn, retryLost);
    }

    /*@Override
    public String getInfo() {
        if (targetPlayer != null
                && !targetPlayer.isRemoved()
                && !WorldUtils.hasZeroHealth(targetPlayer)
                && !(mc.player.distanceTo(targetPlayer) > Math.max(placeRange.isEnabled(), breakRange.isEnabled()) + 8)) {
            if(targetPlayer instanceof PlayerEntity) return ((PlayerEntity)targetPlayer).getGameProfile().getName();
            else if(targetPlayer instanceof OtherClientPlayerEntity) return targetPlayer.getDisplayName().asString();
            else return "null";
        }
        else return "null";
    }*/

    @Override
    public void onTick() {
        run();
    }

    private void run() {
    	if (breakMode.is("All")) System.out.println("he");
//    	if (targetPlayer != null)
//    	System.out.println(targetPlayer.getName());
//    	else System.out.println(":(");
        // Break modes on a separate thread, otherwise the smart break damage calculations hold up the onTick function for all modules.
            for(Entity entity : mc.world.getEntities()) {
//            	System.out.println("h");
                if(entity.distanceTo(mc.player) > Math.max(placeRange.getValue(), breakRange.getValue()) +2) continue;
                if(entity instanceof EndCrystalEntity) {
                    EndCrystalEntity c = (EndCrystalEntity) entity;
                    if(breakMode.is("Smart")) {
                        // Check if the player wants to retry breaking lost crystals
                        if(!retryLost.isEnabled() && lostCrystals.containsKey(c)) continue;
                        if(lostCrystals.containsKey(c)) {
                            if(c.age < lostCrystals.get(c) + lostWindow.getValue() + retryAfter.getValue()) continue;
                        }
                        if(getDamage(entity.getPos(), targetPlayer) >= (minDamage.getValue() / 2) && !spawnedCrystals.containsKey(c)) {
                            //add crystal to spawned list so that it can be broken.
                            spawnedCrystals.put(c, new AtomicInteger(0));
                            lostCrystals.remove(c);
                            continue;
                        }
                    }
                    if(breakMode.is("All")) {
                        if(lostCrystals.containsKey(c)) {
                            if(c.age < lostCrystals.get(c) + lostWindow.getValue()) continue;
                        }
                        if(!spawnedCrystals.containsKey(c)) {
                            spawnedCrystals.put(c, new AtomicInteger(0));
                            lostCrystals.remove(c);
                            continue;
                        }
                    }
                    if(breakMode.is("Own") && retryLost.isEnabled()) {
                        if(!lostCrystals.containsKey(c)) continue;
                        if(lostCrystals.containsKey(c)) {
                            if(c.age < lostCrystals.get(c) + lostWindow.getValue() + retryAfter.getValue()) continue;
                        }
                        spawnedCrystals.put(c, new AtomicInteger(0));
                        lostCrystals.remove(c);
                        continue;
                    }
                }
            }

        // pause with options
        if((pauseOnEat.isEnabled() && mc.player.isUsingItem() && (mc.player.getMainHandStack().getItem().isFood() || mc.player.getOffHandStack().getItem().isFood())) ||
                (pauseOnPot.isEnabled() && mc.player.isUsingItem() && (mc.player.getMainHandStack().getItem() instanceof PotionItem || mc.player.getOffHandStack().getItem() instanceof PotionItem)) ||
                (pauseOnXP.isEnabled() && mc.player.isUsingItem() && (mc.player.getMainHandStack().getItem() == Items.EXPERIENCE_BOTTLE || mc.player.getOffHandStack().getItem() == Items.EXPERIENCE_BOTTLE)) ||
                (pauseOnMine.isEnabled() && mc.interactionManager.isBreakingBlock()))
            return;

        // rotations
        /*
        There is no point in performing this more than once per tick - movement packets are only sent on tick anyways.
        So instead of performing this on place / on break, rotate onTick towards a position. This will also help to
        smoothen the rotation out somewhat while moving (which, in absence of yawsteps, will help somewhat) and by
        using a reset delay of 10 ticks (may need adjustment) we can avoid micro resets of the rotation of the player
        in between place/breaks which should help alleviate rubberbanding problems with NCP's fly check. -Makrennel
         */
        if(rotations != null && rotationTimer.passedTicks(10)) {
            rotatePos = null;
            rotations = null;
            rotationTimer.reset();
        } else if(rotatePos != null) {
            rotations = WorldUtils.calculateLookAt(rotatePos.getX() + 0.5, rotatePos.getY() + 0.5, rotatePos.getZ() + 0.5, mc.player);
        }

        // cleanup render
        if(cleanupTimer.passedSec(3)) {
            target = null;
            renderTimer.reset();
        }

        // do logic
        boolean offhand = mc.player.getOffHandStack().getItem() == Items.END_CRYSTAL;
        if(order.is("Place-Break")) {
            place(offhand);
            explode(offhand);
        } else {
            explode(offhand);
            place(offhand);
        }

        // cleanup place map and lost crystals every ten seconds
        if(cleanupTimer.passedSec(10)) {
            for(Map.Entry<EndCrystalEntity, Integer> entry: lostCrystals.entrySet()) {
                if(mc.world.getEntityById(entry.getKey().getId()) == null)
                    lostCrystals.remove(entry.getKey());
            }

            // cleanup crystals that never spawned
            Optional<Map.Entry<Vec3d, Long>> first = placedCrystals.entrySet().stream().findFirst();
            if(first.isPresent()) {
                Map.Entry<Vec3d, Long> entry = first.get();
                if((System.nanoTime() / 1000000) - entry.getValue() >= 10000) placedCrystals.remove(entry.getKey());
            }
            cleanupTimer.reset();
        }

        // rotate for actual mode
        if(rotations != null && rotateMode.is("Real")) {
            mc.player.setPitch((float) rotations[1]);
            mc.player.setYaw((float) rotations[0]);
        }
    }

    private void place(boolean offhand) {
        if(placeTimer.passedTicks((long) (offhand ? placeOffhandDelay.getValue() : placeDelay.getValue()))) {
            // if no gapple switch and player is holding apple
            if(!offhand && noGappleSwitch.isEnabled() && mc.player.getInventory().getMainHandStack().getItem() instanceof EnchantedGoldenAppleItem) {
                if(target != null) target = null;
                return;
            }
            // find best crystal spot
            BlockPos target = getBestPlacement();
            if(target == null) return;
            placeCrystal(offhand, target);
            placeTimer.reset();
        }
    }

    private void placeCrystal(boolean offhand, BlockPos pos) {
        // switch to crystals if not holding
        if(!offhand && mc.player.getInventory().getMainHandStack().getItem() != Items.END_CRYSTAL) {
            if(doSwitch.isEnabled()) {
                int slot = InventoryUtils.findInHotbar(Items.END_CRYSTAL);
                if (slot != -1) {
                    mc.player.getInventory().selectedSlot = slot;
                    mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(mc.player.getInventory().selectedSlot));
                }
            } else return;
        }

        // place
        mc.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(offhand ? Hand.OFF_HAND : Hand.MAIN_HAND, new BlockHitResult(new Vec3d(0.5f, 0.5f, 0.5f), Direction.UP, pos, false)));
        rotationTimer.reset();
        rotatePos = pos;

        // add to place map
        placedCrystals.put(new Vec3d(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5), System.nanoTime() / 1000000);

        // set render pos
        target = pos;
    }

    private void explode(boolean offhand) {
        if(!shouldBreakCrystal(offhand)) return;

        for(Map.Entry<EndCrystalEntity, AtomicInteger> entry: spawnedCrystals.entrySet()) {
            // check if crystal can be broken
            if(!canBreakCrystal(entry.getKey())) continue;

            breakCrystal(entry.getKey(), offhand);

            // remove if it hits limit of tries
            if(entry.getValue().get() + 1 == maxBreakTries.getValue()) {
                lostCrystals.put(entry.getKey(), entry.getKey().age);
                spawnedCrystals.remove(entry.getKey());
            }
            else entry.getValue().set(entry.getValue().get() + 1);
        }
    }

    @EventTarget
    public void spawnEntityEvent(EventEntity.Spawn event) {
        if(event.getEntity() instanceof EndCrystalEntity) {
            EndCrystalEntity crystal = (EndCrystalEntity) event.getEntity();

            // loop through all placed crystals to see if it matches
            for(Map.Entry<Vec3d, Long> entry: new ArrayList<>(placedCrystals.entrySet())) {
                if(entry.getKey().equals(crystal.getPos())) {
                    // break crystal if possible and add to spawned crystals map
                    boolean offhand = shouldOffhand();
                    if(shouldBreakCrystal(offhand) && canBreakCrystal(crystal) && breakOnSpawn.isEnabled()) {
                        breakCrystal(crystal, offhand);
                        spawnedCrystals.put(crystal, new AtomicInteger(1));
                    } else spawnedCrystals.put(crystal, new AtomicInteger(0));

                    // remove from placed list
                    placedCrystals.remove(entry.getKey());
                }
            }
        }
    }

    @EventTarget
    public void removeEntityEvent(EventEntity.Remove event) {
        // remove spawned crystals from map when they are removed
        if(event.getEntity() instanceof EndCrystalEntity) {
            EndCrystalEntity crystal = (EndCrystalEntity) event.getEntity();
            BlockPos pos = event.getEntity().getBlockPos().down();
            if(canCrystalBePlacedHere(pos) && pos.equals(getBestPlacement()) && spawnedCrystals.containsKey(crystal)) placeCrystal(shouldOffhand(), pos);

            spawnedCrystals.remove(crystal);
        }
    }

    @EventTarget
    public void destroyBlockEvent (EventDestroyBlock event) {
        // place crystal at broken block place
        if(antiSurround.isEnabled()) {
            BlockPos pos = event.getPos().down();
            if(isPartOfHole(pos) && canCrystalBePlacedHere(pos)) placeCrystal(shouldOffhand(), pos);
        }
    }

    @EventTarget
    private void recievePacket(EventSendPacket event) {
        // rotation spoofing
        if(event.getPacket() instanceof PlayerMoveC2SPacket && rotations != null && rotateMode.is("Packet")) {
            ((PlayerMoveC2SPacketAccessor) event.getPacket()).setPitch((float) rotations[1]);
            ((PlayerMoveC2SPacketAccessor) event.getPacket()).setYaw((float) rotations[0]);
            mc.player.headYaw = (float) rotations[0];
            mc.player.bodyYaw = (float) rotations[0];
        }
    }

    //Remove Crystals from lists on Explosion packet received
    @EventTarget
    private void receivePacket(EventReceivePacket event) {
        if(event.getPacket() instanceof ExplosionS2CPacket) {
            final ExplosionS2CPacket packet = (ExplosionS2CPacket) event.getPacket();
            for(Entity e : mc.world.getEntities()) {
                if(e instanceof EndCrystalEntity) {
                    if(e.squaredDistanceTo(packet.getX(), packet.getY(), packet.getZ()) <= 36) {
                        //Remove from all these lists because we can be sure it has broken if the packet was received
                        spawnedCrystals.remove(e);
                        lostCrystals.remove(e);
                    }
                }
            }
        }
    }

    // draw target
//    @Override
//    public void onRender3d() {
//        if(target != null) {
//            Color fillColor = new Color(
//                    colorRed.isEnabled(),
//                    colorGreen.isEnabled(),
//                    colorBlue.isEnabled(),
//                    fillAlpha.isEnabled()
//            );
//            Color outlineColor = new Color(
//                    colorRed.isEnabled(),
//                    colorGreen.isEnabled(),
//                    colorBlue.isEnabled(),
//                    boxAlpha.isEnabled()
//            );
//            RenderUtils.renderBlock(
//                    target,
//                    fillColor,
//                    outlineColor,
//                    lineThickness.isEnabled(),
//                    expandRender.isEnabled()
//            );
//        }
//    }

    private boolean isPartOfHole(BlockPos pos) {
        List<Entity> entities = new ArrayList<>();
        entities.addAll(mc.world.getOtherEntities(mc.player, new Box(pos.add(1, 0, 0))));
        entities.addAll(mc.world.getOtherEntities(mc.player, new Box(pos.add(-1, 0, 0))));
        entities.addAll(mc.world.getOtherEntities(mc.player, new Box(pos.add(0, 0, 1))));
        entities.addAll(mc.world.getOtherEntities(mc.player, new Box(pos.add(0, 0, -1))));
        return entities.stream().anyMatch(entity -> entity instanceof PlayerEntity)
                || entities.stream().anyMatch(entity -> entity instanceof OtherClientPlayerEntity);
    }

    private boolean shouldOffhand() {
        return mc.player.getMainHandStack().getItem() == Items.END_CRYSTAL;
    }

    private boolean shouldBreakCrystal(boolean offhand) {
        return breakTimer.passedTicks((long) (offhand ? breakOffhandDelay.getValue() : breakDelay.getValue()));
    }

    private boolean canBreakCrystal(EndCrystalEntity crystal) {
        return mc.player.distanceTo(crystal) <= breakRange.getValue() // check range
                && !(mc.player.getHealth() - getDamage(crystal.getPos(), mc.player) <= 1 && preventSuicide.isEnabled()) // check suicide
                && crystal.age >= breakAge.getValue(); // check that the crystal has been in the world for the minimum age specified
    }

    private void breakCrystal(EndCrystalEntity crystal, boolean offhand) {
        // find hand
        Hand hand = offhand ? Hand.OFF_HAND : Hand.MAIN_HAND;

        // break
        if(sync.isEnabled()) mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.interact(crystal, false, hand));
        mc.interactionManager.attackEntity(mc.player, crystal);
        mc.player.swingHand(hand);

        //spoof rotations
        rotationTimer.reset();
        rotatePos = crystal.getBlockPos();

        // reset timer
        breakTimer.reset();
    }

    private BlockPos getBestPlacement() {
        double bestScore = 69420;
        BlockPos target = null;
        for(Entity targetedPlayer: getTargets()) {
            // find best location to place
            List<BlockPos> targetsBlocks = getPlaceableBlocks(targetedPlayer);
            List<BlockPos> blocks = getPlaceableBlocks(mc.player);

            for(BlockPos pos: blocks) {
                if(!targetsBlocks.contains(pos) || (double) getDamage(new Vec3d(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5), targetedPlayer) < minDamage.getValue())
                    continue;

                double score = getScore(pos, targetedPlayer);
                if (target != null) {
                    targetPlayer = targetedPlayer;
                } else targetPlayer = null;

                if(target == null || (score < bestScore && score != -1)) {
                    target = pos;
                    bestScore = score;
                }
            }
        }
        return target;
    }

    // utils
    private double getScore(BlockPos pos, Entity player) {
        double score;
        if(placeMode.is("Distance")) {
            score = Math.abs(player.getY() - pos.up().getY())
                    + Math.abs(player.getX() - pos.getX())
                    + Math.abs(player.getZ() - pos.getZ());

            if(rayTrace(
                    new Vec3d(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5),
                    new Vec3d(player.getPos().x,
                            player.getPos().y,
                            player.getPos().z))

                    == HitResult.Type.BLOCK) score = -1;
        } else {
            score = 200 - getDamage(new Vec3d(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5), player);
        }

        return score;
    }

    private List<Entity> getTargets() {
        List<Entity> targets = new ArrayList<>();
        if(targetSetting.is("Closest")) {
            targets.addAll(Streams.stream(mc.world.getEntities()).filter(this::isValidTarget).collect(Collectors.toList()));
            targets.sort(Comparators.entityDistance);
        } else if(targetSetting.is("Most Damage")) {
            for(Entity entity: mc.world.getEntities()) {
                if(!isValidTarget(entity))
                    continue;
                targets.add(entity);
            }
        }

        return targets;
    }

    private boolean isValidTarget(Entity entity) {
        return WorldUtils.isValidTarget(entity, Math.max(placeRange.getValue(), breakRange.getValue()) + 8);
    }

    private List<BlockPos> getPlaceableBlocks(Entity player) {
        List<BlockPos> square = new ArrayList<>();

        int range = (int) MathUtils.round(placeRange.getValue(), 0);

        BlockPos pos = player.getBlockPos();
        if(predictMovement.isEnabled()) pos.add(new Vec3i(player.getVelocity().x, player.getVelocity().y, player.getVelocity().z));

        for(int x = -range; x <= range; x++)
            for(int y = -range; y <= range; y++)
                for(int z = -range; z <= range; z++)
                    square.add(pos.add(x, y, z));

        return square.stream().filter(blockPos -> canCrystalBePlacedHere(blockPos) && mc.player.squaredDistanceTo(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5) <= (range * range)).collect(Collectors.toList());
    }

    private boolean canCrystalBePlacedHere(BlockPos pos) {
        BlockPos boost = pos.add(0, 1, 0);
        if(!oneDotTwelve.isEnabled()) {
            return (mc.world.getBlockState(pos).getBlock() == Blocks.BEDROCK
                    || mc.world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN)
                    && mc.world.getBlockState(boost).getBlock() == Blocks.AIR
                    && mc.world.getNonSpectatingEntities(Entity.class, new Box(boost)).stream().allMatch(entity -> entity instanceof EndCrystalEntity && !isCrystalLost((EndCrystalEntity) entity));
        } else {
            BlockPos boost2 = pos.add(0, 2, 0);
            return (mc.world.getBlockState(pos).getBlock() == Blocks.BEDROCK
                    || mc.world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN)
                    && mc.world.getBlockState(boost).getBlock() == Blocks.AIR
                    && mc.world.getBlockState(boost2).getBlock() == Blocks.AIR
                    && mc.world.getNonSpectatingEntities(Entity.class, new Box(boost)).stream().allMatch(entity -> entity instanceof EndCrystalEntity && !isCrystalLost((EndCrystalEntity) entity))
                    && mc.world.getNonSpectatingEntities(Entity.class, new Box(boost2)).stream().allMatch(entity -> entity instanceof EndCrystalEntity && !isCrystalLost((EndCrystalEntity) entity));
        }
    }

    private boolean isCrystalLost(EndCrystalEntity entity) {
        if(lostCrystals.containsKey(entity)) {
            return entity.age >= lostCrystals.get(entity) + lostWindow.getValue();
        }
        return false;
    }

    // damage calculations
    public static float getDamage(Vec3d vec3d, Entity entity) {
    	if (entity != null) {
	        float f2 = 12.0f;
	        double d7 = Math.sqrt(entity.squaredDistanceTo(vec3d)) / f2;
	        if(d7 <= 1.0D) {
	            double d8 = entity.getX() - vec3d.x;
	            double d9 = entity.getEyeY() - vec3d.y;
	            double d10 = entity.getZ() - vec3d.z;
	            double d11 = Math.sqrt(d8 * d8 + d9 * d9 + d10 * d10);
	            if(d11 != 0.0D) {
	                double d12 = Explosion.getExposure(vec3d, entity);
	                double d13 = (1.0D - d7) * d12;
	                float damage = transformForDifficulty((float)((int)((d13 * d13 + d13) / 2.0D * 7.0D * (double)f2 + 1.0D)));
	                if(entity instanceof PlayerEntity) {
	                    damage = DamageUtil.getDamageLeft(damage, (float)((PlayerEntity)entity).getArmor(), (float)((PlayerEntity)entity).getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS));
	                    damage = getReduction(((PlayerEntity)entity), damage, DamageSource.GENERIC);
	                }
	                return damage;
	            }
	        }
    	}
        return 0.0f;
    }

    private static float transformForDifficulty(float f) {
        if(mc.world.getDifficulty() == Difficulty.PEACEFUL) f = 0.0F;
        if(mc.world.getDifficulty() == Difficulty.EASY) f = Math.min(f / 2.0F + 1.0F, f);
        if(mc.world.getDifficulty() == Difficulty.HARD) f = f * 3.0F / 2.0F;
        return f;
    }

    // get blast reduction off armor and potions
    private static float getReduction(PlayerEntity player, float f, DamageSource damageSource) {
        if (player.hasStatusEffect(StatusEffects.RESISTANCE) && damageSource != DamageSource.OUT_OF_WORLD) {
            int i = (player.getStatusEffect(StatusEffects.RESISTANCE).getAmplifier() + 1) * 5;
            int j = 25 - i;
            float f1 = f * (float)j;
            float f2 = f;
            f = Math.max(f1 / 25.0F, 0.0F);
            float f3 = f2 - f;
            if (f3 > 0.0F && f3 < 3.4028235E37F) {
                if (player instanceof ServerPlayerEntity) {
                    player.increaseStat(Stats.DAMAGE_RESISTED, Math.round(f3 * 10.0F));
                } else if (damageSource.getAttacker() instanceof ServerPlayerEntity) {
                    ((ServerPlayerEntity)damageSource.getAttacker()).increaseStat(Stats.DAMAGE_DEALT_RESISTED, Math.round(f3 * 10.0F));
                }
            }
        }

        if (f <= 0.0F) {
            return 0.0F;
        } else {
            int k = EnchantmentHelper.getProtectionAmount(player.getArmorItems(), damageSource);
            if (k > 0) {
                f = DamageUtil.getInflictedDamage(f, (float)k);
            }

            return f;
        }
    }

    // raytracing
    public static HitResult.Type rayTrace(Vec3d start, Vec3d end) {
        double minX = Math.min(start.x, end.x);
        double minY = Math.min(start.y, end.y);
        double minZ = Math.min(start.z, end.z);
        double maxX = Math.max(start.x, end.x);
        double maxY = Math.max(start.y, end.y);
        double maxZ = Math.max(start.z, end.z);

        for(double x = minX; x > maxX; x += 1) {
            for(double y = minY; y > maxY; y += 1) {
                for(double z = minZ; z > maxZ; z += 1) {
                    BlockState blockState = mc.world.getBlockState(new BlockPos(x, y, z));

                    if(blockState.getBlock() == Blocks.OBSIDIAN
                            || blockState.getBlock() == Blocks.BEDROCK
                            || blockState.getBlock() == Blocks.BARRIER)
                        return HitResult.Type.BLOCK;
                }
            }
        }

        return HitResult.Type.MISS;
    }
}
