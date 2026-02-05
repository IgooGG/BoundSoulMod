package net.igoogg.boundsoul.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;

public class CollarItem extends Item {

    public static final String OWNER = "Owner";
    public static final String LOCKED = "Locked";

    public CollarItem(Settings settings) {
        super(settings);
    }

    /* ---------- NBT ---------- */

    private static NbtCompound getNbt(ItemStack stack) {
        return stack.getOrDefault(
                DataComponentTypes.CUSTOM_DATA,
                NbtComponent.DEFAULT
        ).copyNbt();
    }

    private static void setNbt(ItemStack stack, NbtCompound nbt) {
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
    }

    public static boolean isLocked(ItemStack stack) {
        return getNbt(stack).getBoolean(LOCKED);
    }

    public static UUID getOwner(ItemStack stack) {
        NbtCompound nbt = getNbt(stack);
        return nbt.containsUuid(OWNER) ? nbt.getUuid(OWNER) : null;
    }

    public static void bind(ItemStack stack, ServerPlayerEntity owner, ServerPlayerEntity target) {
        NbtCompound nbt = getNbt(stack);
        nbt.putUuid(OWNER, owner.getUuid());
        nbt.putBoolean(LOCKED, true);
        setNbt(stack, nbt);

        owner.sendMessage(Text.literal("§aYou collared " + target.getName().getString()), false);
        target.sendMessage(Text.literal("§cYou were collared by " + owner.getName().getString()), false);
    }

    public static void unlock(ItemStack stack, ServerPlayerEntity owner, ServerPlayerEntity target) {
        NbtCompound nbt = new NbtCompound();
        setNbt(stack, nbt);

        owner.sendMessage(Text.literal("§aCollar removed"), false);
        target.sendMessage(Text.literal("§aYour collar was removed"), false);
    }

    /* ---------- LEASH ---------- */

    public static void tickLeash(ServerPlayerEntity target) {
        ItemStack collar = findCollar(target);
        if (collar.isEmpty() || !isLocked(collar)) return;

        UUID ownerId = getOwner(collar);
        if (ownerId == null) return;

        ServerPlayerEntity owner =
                target.getServer().getPlayerManager().getPlayer(ownerId);
        if (owner == null) return;

        double maxDist = 6.0;
        Vec3d delta = owner.getPos().subtract(target.getPos());
        if (delta.lengthSquared() <= maxDist * maxDist) return;

        Vec3d pull = delta.normalize().multiply(0.15);
        target.addVelocity(pull);
        target.velocityModified = true;
    }

    public static ItemStack findCollar(PlayerEntity player) {
        for (ItemStack stack : player.getInventory().main) {
            if (stack.getItem() instanceof CollarItem) return stack;
        }
        return ItemStack.EMPTY;
    }
}