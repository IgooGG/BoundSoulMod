package net.igoogg.boundsoul.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.Player;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.World;
import net.igoogg.boundsoul.BoundSoulMod;

import java.util.UUID;

public class CollarItem extends Item {

    public CollarItem(Settings settings) {
        super(settings);
    }

    @Override
    public InteractionResult useOnEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        World world = user.getWorld();
        if (world.isClient) return InteractionResult.SUCCESS;

        NbtCompound nbt = stack.getOrCreateNbt();

        if (isLocked(stack)) {
            user.sendMessage(BoundSoulMod.literal("This collar is already locked!"), true);
            return InteractionResult.CONSUME;
        }

        // Set the owner and target
        nbt.putUuid("Owner", user.getUuid());
        nbt.putUuid("Target", entity.getUuid());

        // Feedback messages
        user.sendMessage(BoundSoulMod.literal("You have put a collar on " + entity.getName().getString()), true);
        if (entity instanceof Player targetPlayer) {
            targetPlayer.sendMessage(BoundSoulMod.literal("You have been collared by " + user.getName().getString()), true);
        }

        return InteractionResult.CONSUME;
    }

    public static boolean isLocked(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        return nbt != null && nbt.containsUuid("Target");
    }

    public static UUID getOwner(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        return nbt != null ? nbt.getUuid("Owner") : null;
    }

    public static UUID getTarget(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        return nbt != null ? nbt.getUuid("Target") : null;
    }
}