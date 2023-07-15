package com.thestarryguard.thestarryguard.Events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface PlayerFireBlockEvent {//玩家点燃方块的事件

    ActionResult interact(World world, PlayerEntity player, BlockState blockState, BlockPos pos);

    Event<PlayerFireBlockEvent> EVENT = EventFactory.createArrayBacked(PlayerFireBlockEvent.class,
            (listeners) -> ((world, player, blockState,pos) -> {
                for (PlayerFireBlockEvent listener : listeners) {
                    ActionResult result = listener.interact(world, player, blockState, pos);
                    if (result != ActionResult.PASS) {//如果事件返回的不是PASS则不向后传递
                        return result;
                    }
                }
                return ActionResult.PASS;
            })
    );
}
