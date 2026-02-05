package net.igoogg.boundsoul.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.nbt.NbtCompound;

public class CollarItem extends Item {

    public CollarItem(Settings settings) {
        super(settings);
    }

    /**
     * Called when the player uses the item on another entity.
     */
    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        // Only allow placing on players
        if (!(entity instanceof PlayerEntity target)) return ActionResult.PASS;

        // Client-side early exit
        if (user.getWorld().isClient) return ActionResult.SUCCESS;

        NbtCompound nbt = stack.getOrCreateNbt();

        // Check if collar is already locked
        if (nbt.contains("Locked") && nbt.getBoolean("Locked")) {
            user.sendMessage(Text.literal("This collar is locked and cannot be removed."), false);
            return ActionResult.SUCCESS;
        }

        // Set ownership and lock
        nbt.putUuid("Owner", user.getUuid());
        nbt.putUuid("Target", target.getUuid());
        nbt.putBoolean("Locked", true); // lock forever

        // Notify players
        user.sendMessage(Text.literal("You placed a collar on " + target.getName().getString() + " (locked forever)"), false);
        target.sendMessage(Text.literal("You have been collared."), false);

        return ActionResult.SUCCESS;
    }

    /**
     * Helper: check if an ItemStack is collared and locked.
     */
    public static boolean isLocked(ItemStack stack) {
        if (!stack.hasNbt()) return false;
        NbtCompound nbt = stack.getOrCreateNbt();
        return nbt.contains("Locked") && nbt.getBoolean("Locked");
    }

    /**
     * Helper: get owner UUID
     */
    public static java.util.UUID getTarget(ItemStack stack) {
        if (!stack.hasNbt()) return null;
        NbtCompound nbt = stack.getOrCreateNbt();
        if (!nbt.containsUuid("Target")) return null;
        return nbt.getUuid("Target");
    }
}