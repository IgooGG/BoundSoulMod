package net.igoogg.boundsoul.item;

import net.igoogg.boundsoul.BoundSoulMod;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.entity.LivingEntity;

import java.util.UUID;

public class CollarItem extends Item {

    public CollarItem(Item.Properties properties) {
        super(properties);
    }

    // Called when player right-clicks on an entity with this item
    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity target, InteractionHand hand) {
        if (user.level.isClientSide) return InteractionResult.SUCCESS; // only run on server

        // Lock collar permanently
        NbtCompound nbt = stack.getNbt();
        if (nbt == null) nbt = new NbtCompound();

        if (!nbt.contains("Owner")) {
            nbt.putUuid("Owner", user.getUUID());
            nbt.putUuid("Target", target.getUUID());
            stack.setNbt(nbt); // attach NBT back to stack
            user.sendMessage(BoundSoulMod.literal("Collar locked to " + target.getName().getString()), true);
        } else {
            user.sendMessage(BoundSoulMod.literal("This collar is already locked!"), true);
        }

        return InteractionResult.CONSUME;
    }

    // Check if the collar is locked
    public static boolean isLocked(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        return nbt != null && nbt.containsUuid("Owner") && nbt.containsUuid("Target");
    }

    // Get owner UUID
    public static UUID getOwner(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        if (nbt != null && nbt.containsUuid("Owner")) {
            return nbt.getUuid("Owner");
        }
        return null;
    }

    // Get target UUID
    public static UUID getTarget(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        if (nbt != null && nbt.containsUuid("Target")) {
            return nbt.getUuid("Target");
        }
        return null;
    }
}
