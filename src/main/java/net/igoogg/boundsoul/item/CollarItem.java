package net.igoogg.boundsoul.item;

import net.igoogg.boundsoul.BoundSoulMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class CollarItem extends Item {

    public CollarItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult interactLivingEntity(
            ItemStack stack,
            Player user,
            LivingEntity target,
            InteractionHand hand
    ) {
        Level world = user.level();
        if (world.isClientSide) return InteractionResult.SUCCESS;

        CompoundTag nbt = stack.getOrCreateTag();

        if (nbt.getBoolean("Locked")) {
            user.displayClientMessage(
                    BoundSoulMod.literal("This collar is already locked!"),
                    true
            );
            return InteractionResult.CONSUME;
        }

        nbt.putBoolean("Locked", true);
        nbt.putUUID("Owner", user.getUUID());
        nbt.putUUID("Target", target.getUUID());

        user.displayClientMessage(
                BoundSoulMod.literal("You have put a collar on " + target.getName().getString()),
                true
        );

        return InteractionResult.CONSUME;
    }

    public static boolean isLocked(ItemStack stack) {
        return stack.hasTag() && stack.getTag().getBoolean("Locked");
    }

    public static UUID getOwner(ItemStack stack) {
        return stack.hasTag() ? stack.getTag().getUUID("Owner") : null;
    }

    public static UUID getTarget(ItemStack stack) {
        return stack.hasTag() ? stack.getTag().getUUID("Target") : null;
    }
}