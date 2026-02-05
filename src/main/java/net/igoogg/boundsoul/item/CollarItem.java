package net.igoogg.boundsoul.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
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

        if (user.getWorld().isClient) {
            return ActionResult.SUCCESS;
        }

        // Get or create custom data
        NbtCompound data = stack
                .getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT)
                .copyNbt();

        // Prevent rebinding
        if (data.contains("Owner")) {
            user.sendMessage(Text.literal("This collar is already bound."), false);
            return ActionResult.SUCCESS;
        }

        data.putUuid("Owner", user.getUuid());
        data.putUuid("Target", target.getUuid());

        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(data));

        user.sendMessage(
                Text.literal("You placed a collar on " + target.getName().getString()),
                false
        );
        target.sendMessage(
                Text.literal("You have been collared."),
                false
        );

        return ActionResult.SUCCESS;
    }
}
