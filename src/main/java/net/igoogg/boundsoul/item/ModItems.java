package net.igoogg.boundsoul.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.igoogg.boundsoul.BoundSoulMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item COLLAR = registerItem(
            "collar",
            new CollarItem(new Item.Settings().maxCount(1))
    );

    private static Item registerItem(String name, Item item) {
        Item registered = Registry.register(
                Registries.ITEM,
                Identifier.of(BoundSoulMod.MOD_ID, name),
                item
        );

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries ->
                entries.add(registered)
        );

        return registered;
    }

    public static void register() {
        // called from main mod class
    }
}
