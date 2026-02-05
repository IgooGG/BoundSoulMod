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
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity target, Hand hand) {
        if (!(target instanceof PlayerEntity targetPlayer)) return ActionResult.PASS;
        World world = user.getWorld();
        if (world.isClient) return ActionResult.SUCCESS;

        NbtCompound nbt = getOrCreate(stack);

        if (nbt.getBoolean("Locked")) {
            user.sendMessage(BoundSoulMod.literal("This collar is already locked."), true);
            return ActionResult.CONSUME;
        }

        nbt.putBoolean("Locked", true);
        nbt.putUuid("Owner", user.getUuid());
        nbt.putUuid("Target", targetPlayer.getUuid());

        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));

        targetPlayer.getInventory().insertStack(stack.copy());
        stack.decrement(1);

        user.sendMessage(BoundSoulMod.literal("Collar applied."), true);
        targetPlayer.sendMessage(BoundSoulMod.literal("You have been collared."), true);

        return ActionResult.CONSUME;
    }

    /* ---------------- HELPERS ---------------- */

    public static boolean isLocked(ItemStack stack) {
        NbtCompound nbt = get(stack);
        return nbt != null && nbt.getBoolean("Locked");
    }

    public static UUID getOwner(ItemStack stack) {
        NbtCompound nbt = get(stack);
        return nbt != null && nbt.containsUuid("Owner") ? nbt.getUuid("Owner") : null;
    }

    public static UUID getTarget(ItemStack stack) {
        NbtCompound nbt = get(stack);
        return nbt != null && nbt.containsUuid("Target") ? nbt.getUuid("Target") : null;
    }

    public static ItemStack findCollar(PlayerEntity player) {
        for (ItemStack stack : player.getInventory().main) {
            if (stack.getItem() instanceof CollarItem && isLocked(stack)) {
                return stack;
            }
        }
        return null;
    }

    private static NbtCompound get(ItemStack stack) {
        NbtComponent c = stack.get(DataComponentTypes.CUSTOM_DATA);
        return c != null ? c.copyNbt() : null;
    }

    private static NbtCompound getOrCreate(ItemStack stack) {
        NbtComponent c = stack.get(DataComponentTypes.CUSTOM_DATA);
        return c != null ? c.copyNbt() : new NbtCompound();
    }
}