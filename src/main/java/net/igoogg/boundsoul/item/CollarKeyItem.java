package net.igoogg.boundsoul.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

import java.util.UUID;

public class CollarKeyItem extends Item {

    public CollarKeyItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (user.getWorld().isClient) return ActionResult.PASS;

        if (!(entity instanceof ServerPlayerEntity target)) {
            return ActionResult.PASS;
        }

        ItemStack collar = CollarItem.findCollar(target);
        if (collar.isEmpty()) {
            user.sendMessage(Text.literal("No collar found."), true);
            return ActionResult.FAIL;
        }

        UUID owner = CollarItem.getOwner(collar);
        if (owner == null || !owner.equals(user.getUuid())) {
            user.sendMessage(Text.literal("You are not the owner."), true);
            return ActionResult.FAIL;
        }

        CollarItem.unlock(collar, (ServerPlayerEntity) user, target);
        return ActionResult.SUCCESS;
    }
}