package net.igoogg.boundsoul;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.igoogg.boundsoul.item.CollarItem;
import net.minecraft.server.network.ServerPlayerEntity;

public class BoundSoulEvents {

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                CollarItem.tickLeash(player);
            }
        });
    }
}