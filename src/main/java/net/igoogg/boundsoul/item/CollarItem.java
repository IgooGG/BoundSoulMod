package net.igoogg.boundsoul.item;

import net.igoogg.boundsoul.BoundSoulMod;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.UUID;

public class CollarItem extends Item {

    public CollarItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(
            ItemStack stack,
            PlayerEntity user,
            LivingEntity target,
            Hand hand
    ) {
        World world = user.getWorld();
        if (world.isClient) return ActionResult.SUCCESS;

        NbtCompound nbt = getOrCreateData(stack);

        if (nbt.getBoolean("Locked")) {
            user.sendMessage(
                    BoundSoulMod.literal("This collar is already locked!"),
                    true
            );
            return ActionResult.CONSUME;
        }

        nbt.putBoolean("Locked", true);
        nbt.putUuid("Owner", user.getUuid());
        nbt.putUuid("Target", target.getUuid());

        stack.set(
                DataComponentTypes.CUSTOM_DATA,
                NbtComponent.of(nbt)
        );

        user.sendMessage(
                BoundSoulMod.literal("You have put a collar on " + target.getName().getString()),
                true
        );

        return ActionResult.CONSUME;
    }

    /* ---------- helpers ---------- */

    private static NbtCompound getOrCreateData(ItemStack stack) {
        NbtComponent comp = stack.get(DataComponentTypes.CUSTOM_DATA);
        return comp != null ? comp.copyNbt() : new NbtCompound();
    }

    public static boolean isLocked(ItemStack stack) {
        NbtComponent comp = stack.get(DataComponentTypes.CUSTOM_DATA);
        return comp != null && comp.copyNbt().getBoolean("Locked");
    }

    public static UUID getOwner(ItemStack stack) {
        NbtComponent comp = stack.get(DataComponentTypes.CUSTOM_DATA);
        return comp != null ? comp.copyNbt().getUuid("Owner") : null;
    }

    public static UUID getTarget(ItemStack stack) {
        NbtComponent comp = stack.get(DataComponentTypes.CUSTOM_DATA);
        return comp != null ? comp.copyNbt().getUuid("Target") : null;
    }
}