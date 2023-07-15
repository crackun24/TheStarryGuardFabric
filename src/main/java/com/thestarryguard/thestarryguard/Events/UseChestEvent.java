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

import java.sql.ResultSet;

public interface UseChestEvent {
    ActionResult interact(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit);

    Event<UseChestEvent> EVENT = EventFactory.createArrayBacked(UseChestEvent.class,
            (listeners) -> (state, world, pos, player, hand, hit) -> {
                for (UseChestEvent listener : listeners) {
                    ActionResult res = listener.interact(state, world, pos, player, hand, hit);
                    if (res != ActionResult.PASS) {
                        return res;
                    }
                }
                return ActionResult.PASS;
            });
}