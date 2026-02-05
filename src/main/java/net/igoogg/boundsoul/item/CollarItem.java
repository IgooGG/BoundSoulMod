package net.igoogg.boundsoul.item;

import net.igoogg.boundsoul.BoundSoulMod;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;

import net.minecraft.nbt.CompoundTag; // correct NBT class for 1.21.1

import java.util.UUID;

public class CollarItem extends Item {

    public CollarItem(Properties settings) {
        super(settings);
    }

    // Called when right-clicking a living entity
    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity target, InteractionHand hand) {
        if (user.level.isClientSide) return InteractionResult.SUCCESS; // only run on server

        // Lock the collar permanently
        CompoundTag nbt = stack.getOrCreateTag();

        if (!nbt.contains("Owner")) {
            nbt.putUUID("Owner", user.getUUID());
            nbt.putUUID("Target", target.getUUID());
            stack.setTag(nbt); // attach back to ItemStack
            user.displayClientMessage(BoundSoulMod.literal("Collar locked to " + target.getName().getString()), true);
        } else {
            user.displayClientMessage(BoundSoulMod.literal("This collar is already locked!"), true);
        }

        return InteractionResult.CONSUME;
    }

    // Check if collar is locked
    public static boolean isLocked(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        return nbt != null && nbt.hasUUID("Owner") && nbt.hasUUID("Target");
    }

    // Get owner UUID
    public static UUID getOwner(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        if (nbt != null && nbt.hasUUID("Owner")) {
            return nbt.getUUID("Owner");
        }
        return null;
    }

    // Get target UUID
    public static UUID getTarget(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        if (nbt != null && nbt.hasUUID("Target")) {
            return nbt.getUUID("Target");
        }
        return null;
    }
}
