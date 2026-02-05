package net.igoogg.boundsoul.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.UUID;

public class ShockerItem extends Item {

    public static final String TARGET_KEY = "Target";

    public ShockerItem(Settings settings) {
        super(settings);
    }

    /* ==== RIGHT CLICK AIR: SHOCK ==== */
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient || !(user instanceof ServerPlayerEntity player)) {
            return TypedActionResult.success(user.getStackInHand(hand));
        }

        ItemStack stack = user.getStackInHand(hand);
        if (!stack.hasNbt() || !stack.getNbt().containsUuid(TARGET_KEY)) {
            player.sendMessage(Text.literal("§cNo player bound to this shocker"), false);
            return TypedActionResult.fail(stack);
        }

        UUID targetId = stack.getNbt().getUuid(TARGET_KEY);
        ServerPlayerEntity target =
                player.getServer().getPlayerManager().getPlayer(targetId);

        if (target == null) {
            player.sendMessage(Text.literal("§cBound player is offline"), false);
            return TypedActionResult.fail(stack);
        }

        target.damage(target.getDamageSources().magic(), 2.0f);
        target.sendMessage(Text.literal("§4You are shocked!"), false);
        player.sendMessage(
                Text.literal("§eYou shocked §f" + target.getName().getString()),
                false
        );

        return TypedActionResult.success(stack);
    }

    /* ==== RIGHT CLICK PLAYER: BIND ==== */
    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (user.getWorld().isClient) return ActionResult.SUCCESS;
        if (!(user instanceof ServerPlayerEntity owner)) return ActionResult.PASS;
        if (!(entity instanceof ServerPlayerEntity target)) return ActionResult.PASS;

        ItemStack collar = target.getInventory().main.stream()
                .filter(s -> s.getItem() instanceof CollarItem)
                .findFirst()
                .orElse(ItemStack.EMPTY);

        if (collar.isEmpty() || !CollarItem.isLocked(collar)) {
            owner.sendMessage(Text.literal("§cTarget is not collared"), false);
            return ActionResult.FAIL;
        }

        stack.getOrCreateNbt().putUuid(TARGET_KEY, target.getUuid());

        owner.sendMessage(
                Text.literal("§aShocker bound to §f" + target.getName().getString()),
                false
        );
        target.sendMessage(
                Text.literal("§cA shocker has been bound to you"),
                false
        );

        return ActionResult.SUCCESS;
    }
}