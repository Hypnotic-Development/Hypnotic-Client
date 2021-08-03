package badgamesinc.hypnotic.command.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import badgamesinc.hypnotic.command.Command;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EnchantmentArgumentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.LiteralText;
import net.minecraft.util.registry.Registry;

public class Enchant extends Command {
    private final static SimpleCommandExceptionType NOT_IN_CREATIVE = new SimpleCommandExceptionType(new LiteralText("You must be in creative mode to use this."));

    public Enchant() {
        super("enchant", "Enchants the item in your hand. REQUIRES Creative mode.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("one").then(argument("enchantment", EnchantmentArgumentType.enchantment()).then(argument("level", IntegerArgumentType.integer()).executes(context -> {
            if (!mc.player.isCreative()) throw NOT_IN_CREATIVE.create();

            ItemStack itemStack = getItemStack();
            if (itemStack != null) {
                Enchantment enchantment = context.getArgument("enchantment", Enchantment.class);
                int level = context.getArgument("level", Integer.class);

                addEnchantment(itemStack, enchantment, level);
            }

            return SINGLE_SUCCESS;
        }))));

        builder.then(literal("all_possible").then(argument("level", IntegerArgumentType.integer()).executes(context -> {
            if (!mc.player.isCreative()) throw NOT_IN_CREATIVE.create();

            ItemStack itemStack = getItemStack();
            if (itemStack != null) {
                int level = context.getArgument("level", Integer.class);

                for (Enchantment enchantment : Registry.ENCHANTMENT) {
                    if (enchantment.isAcceptableItem(itemStack)) {
                        addEnchantment(itemStack, enchantment, level);
                    }
                }
            }

            return SINGLE_SUCCESS;
        })));

        builder.then(literal("all").then(argument("level", IntegerArgumentType.integer()).executes(context -> {
            if (!mc.player.isCreative()) throw NOT_IN_CREATIVE.create();

            ItemStack itemStack = getItemStack();
            if (itemStack != null) {
                int level = context.getArgument("level", Integer.class);

                for (Enchantment enchantment : Registry.ENCHANTMENT) {
                    addEnchantment(itemStack, enchantment, level);
                }
            }

            return SINGLE_SUCCESS;
        })));
    }

    private ItemStack getItemStack() {
        ItemStack itemStack = mc.player.getMainHandStack();
        if (itemStack == null) itemStack = mc.player.getOffHandStack();
        return itemStack;
    }
    
    public static void addEnchantment(ItemStack itemStack, Enchantment enchantment, int level) {
        NbtCompound tag = itemStack.getOrCreateNbt();
        NbtList listTag;

        // Get list tag
        if (!tag.contains("Enchantments", 9)) {
            listTag = new NbtList();
            tag.put("Enchantments", listTag);
        } else {
            listTag = tag.getList("Enchantments", 10);
        }

        // Check if item already has the enchantment and modify the level
        String enchId = Registry.ENCHANTMENT.getId(enchantment).toString();

        for (NbtElement _t : listTag) {
            NbtCompound t = (NbtCompound) _t;

            if (t.getString("id").equals(enchId)) {
                t.putShort("lvl", (short) level);
                return;
            }
        }

        // Add the enchantment if it doesn't already have it
        NbtCompound enchTag = new NbtCompound();
        enchTag.putString("id", enchId);
        enchTag.putShort("lvl", (short) level);

        listTag.add(enchTag);
    }
}
