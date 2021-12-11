package dev.hypnotic.hypnotic_client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.hypnotic.hypnotic_client.command.CommandManager;
import dev.hypnotic.hypnotic_client.event.events.EventReceivePacket;
import dev.hypnotic.hypnotic_client.event.events.EventSendPacket;
import dev.hypnotic.hypnotic_client.utils.Wrapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.s2c.play.DisconnectS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Inject(method = "send(Lnet/minecraft/network/Packet;Lio/netty/util/concurrent/GenericFutureListener;)V", at = @At("HEAD"), cancellable = true)
    public void send(Packet<?> packet, GenericFutureListener<? extends Future<? super Void>> callback, CallbackInfo ci) {
    	//Call commands if the prefix is sent
    	if(packet instanceof ChatMessageC2SPacket && ((ChatMessageC2SPacket) packet).getChatMessage().startsWith(CommandManager.get().getPrefix())) {
    		try {
				CommandManager.get().dispatch(((ChatMessageC2SPacket) packet).getChatMessage().substring(CommandManager.get().getPrefix().length()));
            } catch (CommandSyntaxException e) {
            	e.printStackTrace();
                Wrapper.tellPlayer(e.getMessage());
            }
			ci.cancel();
        }
    	EventSendPacket event = new EventSendPacket(packet);
    	event.call();
    	if (event.isCancelled()) ci.cancel();
    }

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    public void receive(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci) {
    	EventReceivePacket event = new EventReceivePacket(packet);
    	event.call();
        if(event.isCancelled()) ci.cancel();
        if (packet instanceof GameJoinS2CPacket) {
//    		try {
//				Hypnotic.INSTANCE.api.setOnline(mc.getSession().getUsername());
//			} catch (IOException | InterruptedException e) {
//				e.printStackTrace();
//			}
    	}
    	if (packet instanceof DisconnectS2CPacket) {
//    		try {
//				Hypnotic.INSTANCE.api.remOnline(mc.getSession().getUsername());
//			} catch (IOException | InterruptedException e) {
//				e.printStackTrace();
//			}
    	}
    }
}
