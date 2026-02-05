package net.igoogg.boundsoul.item;

import net.igoogg.boundsoul.BoundSoulMod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.UUID;

public class CollarKeyItem extends Item {

    public CollarKeyItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity target, Hand hand) {
        if (!(target instanceof PlayerEntity targetPlayer)) return ActionResult.PASS;
        World world = user.getWorld();
        if (world.isClient) return ActionResult.SUCCESS;

        ItemStack collar = CollarItem.findCollar(targetPlayer);
        if (collar == null) {
            user.sendMessage(Text.literal("No collar found."), true);
            return ActionResult.CONSUME;
        }

        UUID owner = CollarItem.getOwner(collar);
        if (!user.getUuid().equals(owner)) {
            user.sendMessage(Text.literal("You are not the owner."), true);
            return ActionResult.CONSUME;
        }

        targetPlayer.getInventory().removeOne(collar);

        user.sendMessage(Text.literal("Collar unlocked."), true);
        targetPlayer.sendMessage(Text.literal("Your collar has been removed."), true);

        return ActionResult.CONSUME;
    }
}