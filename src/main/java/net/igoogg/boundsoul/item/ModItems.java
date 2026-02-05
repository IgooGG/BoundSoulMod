package net.igoogg.boundsoul.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.igoogg.boundsoul.BoundSoulMod;

public class ModItems {

    // Example item
    public static final Item COLLAR = registerItem(
            "collar",
            new CollarItem(new Item.Settings().maxCount(1))
    );

    private static Item registerItem(String name, Item item) {
        return Registry.register(
                Registries.ITEM,
                new Identifier(BoundSoulMod.MOD_ID, name),
                item
        );
    }

    public static void registerModItems() {
        System.out.println("Registering Mod Items for " + BoundSoulMod.MOD_ID);

        // Add items to creative tabs
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(COLLAR);
        });
    }
}