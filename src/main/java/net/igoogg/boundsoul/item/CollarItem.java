package net.igoogg.boundsoul.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class CollarItem extends Item {

    public CollarItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean useOnEntity(ItemStack stack, PlayerEntity user, net.minecraft.entity.LivingEntity entity, Hand hand) {
        if (!(entity instanceof PlayerEntity target)) {
            return false;
        }

        if (user.getWorld().isClient) {
            return true;
        }

        // Prevent re-binding
        if (stack.hasNbt() && stack.getNbt().contains("Owner")) {
            user.sendMessage(Text.literal("This collar is already bound."), false);
            return true;
        }

        stack.getOrCreateNbt().putUuid("Owner", user.getUuid());
        stack.getOrCreateNbt().putUuid("Target", target.getUuid());

        user.sendMessage(Text.literal("You placed a collar on " + target.getName().getString()), false);
        target.sendMessage(Text.literal("You have been collared."), false);

        return true;
    }
}
