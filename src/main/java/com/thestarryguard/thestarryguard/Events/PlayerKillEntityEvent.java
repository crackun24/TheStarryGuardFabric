package com.thestarryguard.thestarryguard.Events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;

public interface PlayerKillEntityEvent {
    Event<PlayerKillEntityEvent> EVENT = EventFactory.createArrayBacked(PlayerKillEntityEvent.class,
            (listeners) -> (world, killer, other) -> {
                for (PlayerKillEntityEvent listener : listeners) {
                    ActionResult result = listener.interact(world, killer, other);
                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            }
    );

    ActionResult interact(ServerWorld world, PlayerEntity killer, LivingEntity entity);
}
