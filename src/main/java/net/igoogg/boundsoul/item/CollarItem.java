package net.igoogg.boundsoul.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;

public class CollarItem extends Item {

    public static final String OWNER_KEY = "Owner";
    public static final String LOCKED_KEY = "Locked";

    public CollarItem(Settings settings) {
        super(settings);
    }

    /* ================= NBT ================= */

    public static boolean isLocked(ItemStack stack) {
        return stack.hasNbt() && stack.getNbt().getBoolean(LOCKED_KEY);
    }

    public static UUID getOwner(ItemStack stack) {
        if (!stack.hasNbt()) return null;
        return stack.getNbt().containsUuid(OWNER_KEY)
                ? stack.getNbt().getUuid(OWNER_KEY)
                : null;
    }

    public static void bind(ItemStack stack, ServerPlayerEntity owner, ServerPlayerEntity target) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putUuid(OWNER_KEY, owner.getUuid());
        nbt.putBoolean(LOCKED_KEY, true);

        /* ===== MESSAGES ===== */
        owner.sendMessage(
                Text.literal("§aYou have locked a collar onto §f" + target.getName().getString()),
                false
        );
        target.sendMessage(
                Text.literal("§cA collar has been locked onto you by §f" + owner.getName().getString()),
                false
        );
    }

    public static void unlock(ItemStack stack, ServerPlayerEntity owner, ServerPlayerEntity target) {
        stack.removeSubNbt(OWNER_KEY);
        stack.removeSubNbt(LOCKED_KEY);

        owner.sendMessage(
                Text.literal("§aYou removed the collar from §f" + target.getName().getString()),
                false
        );
        target.sendMessage(
                Text.literal("§aYour collar has been removed"),
                false
        );
    }

    /* ================= LEASH ================= */

    public static void tickLeash(ServerPlayerEntity target) {
        ItemStack stack = findCollar(target);
        if (stack.isEmpty() || !isLocked(stack)) return;

        UUID ownerId = getOwner(stack);
        if (ownerId == null) return;

        ServerPlayerEntity owner =
                target.getServer().getPlayerManager().getPlayer(ownerId);
        if (owner == null) return;

        double maxDistance = 6.0;
        Vec3d ownerPos = owner.getPos();
        Vec3d targetPos = target.getPos();

        double distance = ownerPos.distanceTo(targetPos);
        if (distance <= maxDistance) return;

        Vec3d pull = ownerPos.subtract(targetPos)
                .normalize()
                .multiply(0.15);

        target.addVelocity(pull.x, pull.y, pull.z);
        target.velocityModified = true;
    }

    private static ItemStack findCollar(PlayerEntity player) {
        for (ItemStack stack : player.getInventory().main) {
            if (stack.getItem() instanceof CollarItem) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }
}