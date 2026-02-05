package net.igoogg.boundsoul.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class CollarItem extends Item {

    public CollarItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (!(entity instanceof PlayerEntity target)) {
            return ActionResult.PASS;
        }

        if (user.getWorld().isClient) return ActionResult.SUCCESS;

        NbtCompound nbt = stack.getOrCreateNbt(); // ✅ This works in 1.21.1

        if (nbt.contains("Locked") && nbt.getBoolean("Locked")) {
            user.sendMessage(Text.literal("This collar is locked and cannot be removed."), false);
            return ActionResult.SUCCESS;
        }

        nbt.putUuid("Owner", user.getUuid());
        nbt.putUuid("Target", target.getUuid());
        nbt.putBoolean("Locked", true); // ⚡ lock forever

        stack.setNbt(nbt);

        user.sendMessage(
                Text.literal("You placed a collar on " + target.getName().getString() + " (locked forever)"),
                false
        );
        target.sendMessage(Text.literal("You have been collared."), false);

        return ActionResult.SUCCESS;
    }
}
