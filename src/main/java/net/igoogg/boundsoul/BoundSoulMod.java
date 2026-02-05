package net.igoogg.boundsoul;

import net.fabricmc.api.ModInitializer;
import net.igoogg.boundsoul.item.ModItems;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoundSoulMod implements ModInitializer {

    public static final String MOD_ID = "boundsoul";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModItems.register();
        LOGGER.info("Bound Soul mod initialized");
    }

    // ✅ REQUIRED by CollarItem
    public static Text literal(String text) {
        return Text.literal(text);
    }
}