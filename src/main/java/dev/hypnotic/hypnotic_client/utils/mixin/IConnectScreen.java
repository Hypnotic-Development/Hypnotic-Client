package dev.hypnotic.hypnotic_client.utils.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.ClientConnection;

public interface IConnectScreen {
    boolean isConnectingCancelled();
    Screen getParent();
    void _setVersionRequestConnection(ClientConnection connection);

}
