package net.igoogg.boundsoul.item;

import net.igoogg.boundsoul.BoundSoulMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Items;
import net.minecraft.core.Registry;

public class ModItems {

    public static final Item COLLAR = register("collar", new CollarItem(new Item.Properties().stacksTo(1)));

    private static Item register(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(BoundSoulMod.MOD_ID, name), item);
    }

    public static void register() {
        // Items are registered statically above, call this to ensure class is loaded
    }
}