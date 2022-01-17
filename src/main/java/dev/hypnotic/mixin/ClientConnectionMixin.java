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
package dev.hypnotic.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.hypnotic.command.CommandManager;
import dev.hypnotic.event.events.EventReceivePacket;
import dev.hypnotic.event.events.EventSendPacket;
import dev.hypnotic.utils.Wrapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Inject(method = "send(Lnet/minecraft/network/Packet;Lio/netty/util/concurrent/GenericFutureListener;)V", at = @At("HEAD"), cancellable = true)
    public void send(Packet<?> packet, GenericFutureListener<? extends Future<? super Void>> callback, CallbackInfo ci) {
    	// Call commands if the prefix is sent
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
    }
}
