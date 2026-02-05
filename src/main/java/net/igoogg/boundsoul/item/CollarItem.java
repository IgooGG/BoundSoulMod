package net.igoogg.boundsoul.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.Player;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.igoogg.boundsoul.BoundSoulMod;

import java.util.UUID;

public class CollarItem extends Item {

    public CollarItem(Settings settings) {
        super(settings);
    }

    // Example method to interact with an entity
    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity target, InteractionHand hand) {
        if (user.getWorld().isClient) return InteractionResult.SUCCESS; // server-side only

        CompoundTag nbt = stack.getOrCreateTag();
        if (isLocked(stack)) {
            user.sendMessage(BoundSoulMod.literal("This collar is already locked!"), true);
            return InteractionResult.CONSUME;
        }

        // Example: lock to the target entity's UUID
        nbt.putUuid("Owner", user.getUuid());
        nbt.putUuid("Target", target.getUuid());

        return InteractionResult.CONSUME;
    }

    public static boolean isLocked(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        return nbt != null && nbt.containsUuid("Target");
    }

    public static UUID getOwner(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        return nbt != null ? nbt.getUuid("Owner") : null;
    }

    public static UUID getTarget(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        return nbt != null ? nbt.getUuid("Target") : null;
    }
}