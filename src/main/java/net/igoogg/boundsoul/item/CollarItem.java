package net.igoogg.boundsoul.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class CollarItem extends Item {

    public CollarItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            user.sendMessage(
                    Text.literal("This collar can be placed on another player."),
                    false
            );
        }
        return ActionResult.SUCCESS;
    }
}
