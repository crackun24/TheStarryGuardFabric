package com.thestarryguard.thestarryguard.Events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public interface BucketUseEvent {//玩家使用水桶的事件

    ActionResult interact(World world, PlayerEntity user, Hand hand, Item item);

    Event<BucketUseEvent> EVENT = EventFactory.createArrayBacked(BucketUseEvent.class,
            (listeners) -> (world, user, hand,item) -> {
                for (BucketUseEvent listener : listeners) {
                    ActionResult result = listener.interact(world, user, hand,item);
                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            }
    );
}
