package com.thestarryguard.thestarryguard.Events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface PlayerKillPlayerEvent {
    Event<PlayerKillPlayerEvent> EVENT = EventFactory.createArrayBacked(PlayerKillPlayerEvent.class,
            (listeners) -> (world, killer, player) -> {
                for (PlayerKillPlayerEvent listener : listeners) {
                    ActionResult result = listener.interact(world, killer, player);
                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            }
    );

    ActionResult interact(World world, PlayerEntity killer, PlayerEntity player);
}
