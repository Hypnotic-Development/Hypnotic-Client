package badgamesinc.hypnotic.command.commands;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import badgamesinc.hypnotic.command.Command;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;

public class VClip extends Command {
	public VClip() {
        super("vclip", "Lets you clip through blocks vertically.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("blocks", DoubleArgumentType.doubleArg()).executes(context -> {
            ClientPlayerEntity player = mc.player;
            assert player != null;

            double blocks = context.getArgument("blocks", Double.class);
            if (player.hasVehicle()) {
                Entity vehicle = player.getVehicle();
                vehicle.setPosition(vehicle.getX(), vehicle.getY() + blocks, vehicle.getZ());
            }
            player.setPosition(player.getX(), player.getY() + blocks, player.getZ());

            return SINGLE_SUCCESS;
        }));
    }
}
