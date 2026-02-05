package net.igoogg.boundsoul.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.igoogg.boundsoul.BoundSoulMod;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item COLLAR =
            register("collar", new CollarItem(new Item.Settings().maxCount(1)));

    public static final Item COLLAR_KEY =
            register("collar_key", new CollarKeyItem(new Item.Settings().maxCount(1)));

    public static final Item SHOCKER =
            register("shocker", new ShockerItem(new Item.Settings().maxCount(1)));

    private static Item register(String name, Item item) {
        return Registry.register(
                Registries.ITEM,
                Identifier.of(BoundSoulMod.MOD_ID, name),
                item
        );
    }

    public static void register() {
        BoundSoulMod.LOGGER.info("Registering items for " + BoundSoulMod.MOD_ID);
    }
}