package com.thestarryguard.thestarryguard.Events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface PlayerLitTntEvent {//玩家点燃TNT的事件

    ActionResult interact(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit);

    Event<PlayerLitTntEvent> EVENT = EventFactory.createArrayBacked(PlayerLitTntEvent.class,
            (listeners) -> (state, world, pos, player, hand, hit) -> {
                for (PlayerLitTntEvent listener : listeners) {
                    ActionResult res = listener.interact(state, world, pos, player, hand, hit);
                    if (res != ActionResult.PASS) {
                        return res;
                    }
                }

                return ActionResult.PASS;
            }
    );
}
