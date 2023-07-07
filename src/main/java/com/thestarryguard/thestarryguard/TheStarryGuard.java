package com.thestarryguard.thestarryguard;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.util.ActionResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class TheStarryGuard implements ModInitializer {
    Logger LOGGER = LogManager.getLogger();//获取日志记录器

    private void HookBreakEvent() {
        PlayerBlockBreakEvents.AFTER.register(((world, player, pos, state, blockEntity) -> {

        }));//方块破坏事件
    }//注册方块破坏事件

    private void HookPlaceEvent() {
        UseBlockCallback.EVENT.register(((player, world, hand, hitResult) -> {

            return ActionResult.PASS;
        }));
    }//注册方块放置事件

    private void HookAttackEvent() {
        AttackBlockCallback.EVENT.register(((player, world, hand, pos, direction) ->{

            return ActionResult.PASS;
        } ));
    }//注册玩家攻击事件

    @Override
    public void onInitialize() {
        LOGGER.info("Loading TheStarryGuard");

    }
}
