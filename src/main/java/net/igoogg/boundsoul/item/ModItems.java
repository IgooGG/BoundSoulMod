package net.igoogg.boundsoul.item;

import net.igoogg.boundsoul.BoundSoulMod;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item COLLAR =
            register("collar", new CollarItem(new Item.Settings().maxCount(1)));

    private static Item register(String name, Item item) {
        return Registry.register(
                Registries.ITEM,
                new Identifier(BoundSoulMod.MOD_ID, name),
                item
        );
    }

    public static void register() {
        // triggers class loading
    }
}