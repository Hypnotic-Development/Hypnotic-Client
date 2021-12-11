package dev.hypnotic.hypnotic_client.module.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.collect.Maps;

import dev.hypnotic.hypnotic_client.Hypnotic;
import dev.hypnotic.hypnotic_client.event.EventTarget;
import dev.hypnotic.hypnotic_client.event.events.EventRender3D;
import dev.hypnotic.hypnotic_client.event.events.EventRenderGUI;
import dev.hypnotic.hypnotic_client.event.events.EventRenderNametags;
import dev.hypnotic.hypnotic_client.module.Category;
import dev.hypnotic.hypnotic_client.module.Mod;
import dev.hypnotic.hypnotic_client.module.ModuleManager;
import dev.hypnotic.hypnotic_client.settings.settingtypes.BooleanSetting;
import dev.hypnotic.hypnotic_client.settings.settingtypes.NumberSetting;
import dev.hypnotic.hypnotic_client.utils.ColorUtils;
import dev.hypnotic.hypnotic_client.utils.Utils;
import dev.hypnotic.hypnotic_client.utils.font.FontManager;
import dev.hypnotic.hypnotic_client.utils.font.NahrFont;
import dev.hypnotic.hypnotic_client.utils.math.MathUtils;
import dev.hypnotic.hypnotic_client.utils.render.RenderUtils;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AirBlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.Vec3d;

public class Nametags extends Mod {

	public BooleanSetting players = new BooleanSetting("Players", true);
	public BooleanSetting monsters = new BooleanSetting("Monsters", true);
	public BooleanSetting animals = new BooleanSetting("Animals", true);
	public BooleanSetting passives = new BooleanSetting("Passives", true);
	public BooleanSetting invisibles = new BooleanSetting("Invisibles", true);
	public NumberSetting scale = new NumberSetting("Scale", 1.3, 0.5, 5, 0.01);
	public BooleanSetting bg = new BooleanSetting("Background", true);
	public BooleanSetting ping = new BooleanSetting("Ping", true);
	public BooleanSetting healthbar = new BooleanSetting("Healthbar", true);
	public BooleanSetting items = new BooleanSetting("Items", true);
	public BooleanSetting head = new BooleanSetting("Head", true);
	public BooleanSetting distance = new BooleanSetting("Distance", true);
	public BooleanSetting gamemode = new BooleanSetting("Gamemode", true);
	public BooleanSetting forceMcFont = new BooleanSetting("Force MC Font", false);
	
	private HashMap<Entity, Vec3d> positions = Maps.newHashMap();
	int count = 0;
	
	private static NahrFont font = FontManager.roboto;
	
	public Nametags() {
		super("Nametags", "Renders a custom nametag above players", Category.RENDER);
		addSettings(scale, bg, ping, healthbar, items, head, distance, gamemode, players, monsters, animals, passives, invisibles, forceMcFont);
	}
	
	@EventTarget
	public void eventRenderNametags(EventRenderNametags event) {
		if (shouldRenderEntity(event.getEntity())) event.setCancelled(true);
	}

	@EventTarget
	public void eventRender3D(EventRender3D event) {
		if (font.mcFont != forceMcFont.isEnabled()) font = new NahrFont(Utils.getFileFromJar(ModuleManager.INSTANCE.getModule(this.getClass()).getClass().getClassLoader(), "assets/hypnotic/fonts/Roboto-Regular.ttf"), 18, 1, forceMcFont.isEnabled());
		this.positions.clear();
		mc.world.getEntities().forEach(entity -> {
			float offset = entity.getHeight() + 0.2f;
			Vec3d vec = RenderUtils.getPos(entity, offset, event.getTickDelta(), event.getMatrices());
            this.positions.put(entity, vec);
		});
	}
	
	@EventTarget
	public void eventRenderGUI(EventRenderGUI event) {
		mc.world.getEntities().forEach(entity -> {
            if (shouldRenderEntity(entity)) {
                drawNametags(entity, event);
            }
        });
		mc.world.getEntities().forEach(entity -> {
            if (entity instanceof LivingEntity && shouldRenderEntity(entity) && items.isEnabled()) {
                drawNametagInv((LivingEntity) entity, event);
            }
        });
	}
	
	private void drawInv(LivingEntity player, float posX, float posY, EventRenderGUI eventRender2D) {
        int itemWidth = 16;
        int totalCount = getItems(player).size();
        float startX = (posX - ((totalCount * itemWidth) / 2.f));
        posY = (posY - 28);
        count = 0;
        for (ItemStack itemStack : getItems(player)) {
            if (!(itemStack.getItem() instanceof AirBlockItem)) {
                float newX = startX + (count * 16);
                RenderUtils.drawItem(itemStack, newX, posY);
                if (itemStack.hasEnchantments()) {
                    float scale = 0.5f;
                    MatrixStack matrixStack = eventRender2D.getMatrices();
                    matrixStack.push();
                    matrixStack.scale(scale, scale, 1);
                    int enchCount = 1;
                    for (NbtElement tag : itemStack.getEnchantments()) {
                        try {
                            NbtCompound compoundTag = (NbtCompound) tag;
                            float newY = ((posY - ((10 * scale) * enchCount) + 0.5f) / scale);
                            float newerX = (newX / scale);
                            String name = getEnchantName(compoundTag);
                            float nameWidth = font.getStringWidth(name);
                            RenderUtils.fill(eventRender2D.getMatrices(), newerX, newY - 1, newerX + nameWidth, newY + 9, 0x35000000);
                            font.draw(eventRender2D.getMatrices(), name, newerX, newY - 3, -1);
                            enchCount++;
                        } catch (Exception ignored) {}
                    }
                    matrixStack.pop();
                }
                count++;
            }
        }
    }

    private String getEnchantName(NbtCompound compoundTag) {
        int level = compoundTag.getShort("lvl");
        String name = compoundTag.getString("id").split(":")[1];
        if (name.contains("_")) {
            String[] s = name.split("_");
            name = s[0].substring(0, 1).toUpperCase() + s[0].substring(1, 3) + s[1].substring(0, 1).toUpperCase();
        } else {
            name = name.substring(0, 1).toUpperCase() + name.substring(1, 3);
        }
        name += level;
        return name;
    }

    public void drawNametags(Entity playerEntity, EventRenderGUI eventRender2D) {
        Vec3d vec = positions.get(playerEntity);
        if (RenderUtils.isOnScreen2d(vec)) {
            float x = (float) vec.x;
            float y = (float) vec.y - (playerEntity instanceof PlayerEntity ? 18 : 0);
            String nameString = getNameString(playerEntity);
            float length = font.getStringWidth(nameString) + 0;
            eventRender2D.getMatrices().push();
            eventRender2D.getMatrices().translate(x, y, 0);
            float scaleDist = mc.player.distanceTo(playerEntity) < 60 ? 60 : (mc.player.distanceTo(playerEntity) > 100 ? 100 : mc.player.distanceTo(playerEntity));
            float scaleFactor = ((scaleDist) * 0.05f) * (float)scale.getValue() * 0.2f;
            eventRender2D.getMatrices().scale(scaleFactor, scaleFactor, 0);
            if (playerEntity instanceof LivingEntity && healthbar.isEnabled()) {
                float percent = ((LivingEntity) playerEntity).getHealth() / ((LivingEntity) playerEntity).getMaxHealth();
                float barLength = (int) (((length) + 6) * percent);
                RenderUtils.fill(eventRender2D.getMatrices(), - (length / 2) - (playerEntity instanceof PlayerEntity ? (head.isEnabled() ? 18 : 2) : 2), - 1, 0 + barLength - length / 2, 0, getHealthColor(((LivingEntity) playerEntity)));
            }
            if (bg.isEnabled()) RenderUtils.fill(eventRender2D.getMatrices(), - (length / 2) - 2 - (head.isEnabled() ? (playerEntity instanceof PlayerEntity ? 16 : 0) : 0), - 17, (length / 2) + 6, - 1, new Color(0, 0, 0, 175).getRGB());
            if (!font.mcFont)
            font.drawCenteredString(eventRender2D.getMatrices(), nameString, 2, - 18, -1, true);
            else font.drawCenteredString(eventRender2D.getMatrices(), nameString, 2, - 15, -1, true);
            if (playerEntity instanceof PlayerEntity) {
                PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(playerEntity.getUuid());
                if (playerListEntry != null && head.isEnabled()) {
                	RenderUtils.drawFace(eventRender2D.getMatrices(), - length / 2 - 18, - 17, (int)2, playerListEntry.getSkinTexture());
                }
            }
            eventRender2D.getMatrices().pop();
        }
    }

    public void drawNametagInv(LivingEntity playerEntity, EventRenderGUI eventRender2D) {
        Vec3d vec = positions.get(playerEntity);
        if (RenderUtils.isOnScreen2d(vec)) {
            float x = (float) vec.x;
            float y = (float) vec.y - (playerEntity instanceof PlayerEntity ? 18 : 0);
//            if (showInv)
                drawInv(playerEntity, x - 8, y - 10, eventRender2D);
        }
    }

    private ArrayList<ItemStack> getItems(LivingEntity player) {
        ArrayList<ItemStack> stackList = new ArrayList<>();
        player.getArmorItems().forEach(itemStack -> {
            if (itemStack != null) {
                if (!(itemStack.getItem() instanceof AirBlockItem)) {
                    stackList.add(itemStack);
                }
            }
        });
        ItemStack itemStack = player.getEquippedStack(EquipmentSlot.MAINHAND);
        if (!(itemStack.getItem() instanceof AirBlockItem)) {
            stackList.add(itemStack);
        }
        itemStack = player.getEquippedStack(EquipmentSlot.OFFHAND);
        if (!(itemStack.getItem() instanceof AirBlockItem)) {
            stackList.add(itemStack);
        }
        return stackList;
    }

    public String getNameString(Entity entity) {
        String name = entity.getDisplayName().asString();
        PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(entity.getUuid());
        String gameModeText = gamemode.isEnabled() ? (playerListEntry != null ? ColorUtils.aqua + playerListEntry.getGameMode().getName().substring(0, 1).toUpperCase() + " " + ColorUtils.reset : "") : "";
        String pingText = ping.isEnabled() ? (playerListEntry != null ? playerListEntry.getLatency() : "0") + "ms " : "";
        String distanceText = distance.isEnabled() ? Math.round(mc.player.distanceTo(entity)) + "m " : " ";   	
        if (name.trim().isEmpty())
            name = entity.getName().getString();
        if (entity instanceof ItemEntity) {
        	ItemEntity itemEntity = (ItemEntity)entity;
            if (itemEntity.getStack().getCount() > 1)
                name += " \247fx" + itemEntity.getStack().getCount();
        }
        String displayName = "";
        if (entity instanceof LivingEntity)
        	displayName = gameModeText + ColorUtils.white + name.replaceAll(ColorUtils.colorChar, "&") + " " + pingText + distanceText + getHealthString((LivingEntity) entity) + (Hypnotic.isHypnoticUser(entity.getName().asString()) ? ColorUtils.purple + " H" : "");
        return displayName;
    }

    private String getHealthString(LivingEntity player) {
        String health = ColorUtils.green;
        int healthPercent = (int) ((player.getHealth() / player.getMaxHealth()) * 100);
        if (healthPercent <= 75)
            health = ColorUtils.colorChar + "e";
        if (healthPercent <= 50)
            health = ColorUtils.colorChar + "6";
        if (healthPercent <= 25)
            health = ColorUtils.colorChar + "4";
        if (!Float.isNaN(getHealth(player)) && !Float.isInfinite(getHealth(player)))
            health += "[" + MathUtils.round(getHealth(player), 1) + "]";
        else
            health += "NaN";
        return health;
    }

    private float getHealth(LivingEntity player) {
    	return Math.round(player.getHealth());
    }

    private int getHealthColor(LivingEntity player) {
        float percent = (player.getHealth() / player.getMaxHealth()) * 100;
        return RenderUtils.getPercentColor(percent);
    }
	
	public boolean shouldRenderEntity(Entity entity) {
		if (players.isEnabled() && entity instanceof PlayerEntity && entity != mc.player) return true;
		if (monsters.isEnabled() && entity instanceof Monster) return true;
		if (animals.isEnabled() && entity instanceof AnimalEntity) return true;
		if (passives.isEnabled() && entity instanceof PassiveEntity && !(entity instanceof AnimalEntity)) return true;
		if (invisibles.isEnabled() && entity.isInvisible()) return true;
		return false;
	}
	
	public Color getEntityColor(Entity entity, int alpha) {
		if (entity instanceof PlayerEntity) return new Color(255, 255, 255, alpha);
		if (entity instanceof Monster) return new Color(255, 255, 255, alpha);
		if (entity instanceof AnimalEntity) return new Color(30, 255, 30, alpha);
		if (entity instanceof PassiveEntity) return new Color(255, 255, 255, alpha);
		if (entity.isInvisible()) return new Color(255, 255, 255, alpha);
		return new Color(255, 255, 255);
	}
}
