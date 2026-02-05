package net.igoogg.boundsoul.item;

import net.igoogg.boundsoul.BoundSoulMod;
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
            LivingEntity entity,
            Hand hand
    ) {
        World world = user.getWorld();
        if (world.isClient) return ActionResult.SUCCESS;

        NbtCompound nbt = stack.getOrCreateNbt();

        if (nbt.getBoolean("Locked")) {
            user.sendMessage(
                    BoundSoulMod.literal("This collar is already locked!"),
                    true
            );
            return ActionResult.CONSUME;
        }

        nbt.putBoolean("Locked", true);
        nbt.putUuid("Owner", user.getUuid());
        nbt.putUuid("Target", entity.getUuid());

        user.sendMessage(
                BoundSoulMod.literal("You have put a collar on " + entity.getName().getString()),
                true
        );

        return ActionResult.CONSUME;
    }

    public static boolean isLocked(ItemStack stack) {
        return stack.hasNbt() && stack.getNbt().getBoolean("Locked");
    }

    public static UUID getOwner(ItemStack stack) {
        return stack.hasNbt() ? stack.getNbt().getUuid("Owner") : null;
    }

    public static UUID getTarget(ItemStack stack) {
        return stack.hasNbt() ? stack.getNbt().getUuid("Target") : null;
    }
}