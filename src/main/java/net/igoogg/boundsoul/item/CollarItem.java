package net.igoogg.boundsoul.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.igoogg.boundsoul.BoundSoulMod;

import java.util.UUID;

public class CollarItem extends Item {

    public CollarItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity target, InteractionHand hand) {
        Level world = user.level();
        if (world.isClientSide) return InteractionResult.SUCCESS;

        CompoundTag nbt = stack.getOrCreateTag();

        if (isLocked(stack)) {
            user.displayClientMessage(BoundSoulMod.literal("This collar is already locked!"), true);
            return InteractionResult.CONSUME;
        }

        nbt.putUUID("Owner", user.getUUID());
        nbt.putUUID("Target", target.getUUID());

        user.displayClientMessage(BoundSoulMod.literal("You have put a collar on " + target.getName().getString()), true);
        if (target instanceof Player targetPlayer) {
            targetPlayer.displayClientMessage(BoundSoulMod.literal("You have been collared by " + user.getName().getString()), true);
        }

        return InteractionResult.CONSUME;
    }

    public static boolean isLocked(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        return nbt != null && nbt.hasUUID("Target");
    }

    public static UUID getOwner(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        return nbt != null ? nbt.getUUID("Owner") : null;
    }

    public static UUID getTarget(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        return nbt != null ? nbt.getUUID("Target") : null;
    }
}