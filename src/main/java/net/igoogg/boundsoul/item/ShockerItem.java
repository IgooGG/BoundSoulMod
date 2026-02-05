package net.igoogg.boundsoul.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.UUID;

public class ShockerItem extends Item {

    private static final String TARGET_KEY = "Target";

    public ShockerItem(Settings settings) {
        super(settings);
    }

    /* ---------- NBT helpers (1.21 REQUIRED) ---------- */

    private static NbtCompound getNbt(ItemStack stack) {
        return stack.getOrDefault(
                DataComponentTypes.CUSTOM_DATA,
                NbtComponent.DEFAULT
        ).copyNbt();
    }

    private static void setNbt(ItemStack stack, NbtCompound nbt) {
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
    }

    /* ---------- Right click air = shock ---------- */

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            return TypedActionResult.pass(user.getStackInHand(hand));
        }

        ItemStack stack = user.getStackInHand(hand);
        NbtCompound nbt = getNbt(stack);

        if (!nbt.containsUuid(TARGET_KEY)) {
            user.sendMessage(Text.literal("No target bound."), true);
            return TypedActionResult.fail(stack);
        }

        UUID targetId = nbt.getUuid(TARGET_KEY);
        ServerPlayerEntity target =
                ((ServerPlayerEntity) user).getServer()
                        .getPlayerManager()
                        .getPlayer(targetId);

        if (target == null) {
            user.sendMessage(Text.literal("Target not online."), true);
            return TypedActionResult.fail(stack);
        }

        target.damage(world.getDamageSources().magic(), 2.0F);
        target.sendMessage(Text.literal("§cYou have been shocked!"), false);
        user.sendMessage(Text.literal("§aShocked " + target.getName().getString()), false);

        return TypedActionResult.success(stack);
    }

    /* ---------- Right click player = bind ---------- */

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (user.getWorld().isClient) return ActionResult.PASS;

        if (!(entity instanceof ServerPlayerEntity target)) {
            return ActionResult.PASS;
        }

        ItemStack collar = CollarItem.findCollar(target);
        if (collar.isEmpty() || !CollarItem.isLocked(collar)) {
            user.sendMessage(Text.literal("Target has no locked collar."), true);
            return ActionResult.FAIL;
        }

        NbtCompound nbt = getNbt(stack);
        nbt.putUuid(TARGET_KEY, target.getUuid());
        setNbt(stack, nbt);

        user.sendMessage(Text.literal("§aShocker bound to " + target.getName().getString()), false);
        target.sendMessage(Text.literal("§cA shocker has been bound to you."), false);

        return ActionResult.SUCCESS;
    }
}