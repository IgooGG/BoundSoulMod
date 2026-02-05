package net.igoogg.boundsoul;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.igoogg.boundsoul.item.ModItems;


public class BoundSoulMod implements ModInitializer {

    public static final String MOD_ID = "boundsoul";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModItems.register();
        LOGGER.info("Bound Soul mod initialized");
    }

}
