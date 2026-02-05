package net.igoogg.boundsoul.item;

import net.igoogg.boundsoul.BoundSoulMod;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.UUID;

public class ShockerItem extends Item {

    public ShockerItem(Settings settings) {
        super(settings);
    }

    /* ================= SHOCK AIR ================= */

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (world.isClient) {
            return TypedActionResult.success(stack);
        }

        NbtCompound nbt = get(stack);
        if (nbt == null || !nbt.containsUuid("Target")) {
            user.sendMessage(BoundSoulMod.literal("No target bound."), true);
            return TypedActionResult.fail(stack);
        }

        ServerPlayerEntity target =
                world.getServer().getPlayerManager().getPlayer(nbt.getUuid("Target"));

        if (target == null) {
            user.sendMessage(BoundSoulMod.literal("Target offline."), true);
            return TypedActionResult.fail(stack);
        }

        shock(target);
        user.sendMessage(BoundSoulMod.literal("⚡ Shock delivered."), true);

        return TypedActionResult.success(stack);
    }

    /* ================= BIND TO COLLARED PLAYER ================= */

    @Override
    public boolean useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (!(entity instanceof PlayerEntity target)) return false;
        World world = user.getWorld();
        if (world.isClient) return true;

        ItemStack collar = CollarItem.findCollar(target);
        if (collar == null) {
            user.sendMessage(BoundSoulMod.literal("Target is not collared."), true);
            return true;
        }

        UUID owner = CollarItem.getOwner(collar);
        if (!user.getUuid().equals(owner)) {
            user.sendMessage(BoundSoulMod.literal("You are not the owner."), true);
            return true;
        }

        NbtCompound nbt = new NbtCompound();
        nbt.putUuid("Target", target.getUuid());
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));

        user.sendMessage(
                BoundSoulMod.literal("Shocker bound to " + target.getName().getString()),
                true
        );

        return true;
    }

    /* ================= EFFECT ================= */

    private static void shock(PlayerEntity target) {
        target.damage(target.getWorld().getDamageSources().magic(), 4.0F);
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 2));
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 60, 1));
    }

    /* ================= NBT ================= */

    private static NbtCompound get(ItemStack stack) {
        NbtComponent c = stack.get(DataComponentTypes.CUSTOM_DATA);
        return c != null ? c.copyNbt() : null;
    }
}