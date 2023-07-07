package com.thestarryguard.thestarryguard;

import net.minecraft.block.Block;
import com.thestarryguard.thestarryguard.DataType.Action;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import org.apache.commons.compress.compressors.lz77support.LZ77Compressor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.rmi.registry.Registry;


public class TheStarryGuard implements ModInitializer {
    Logger LOGGER = LogManager.getLogger();//获取日志记录器

    Config mConfig;//配置文件对象

    DataStorage mDataStorage;//数据储存类

    private void HookBreakEvent() {
        PlayerBlockBreakEvents.AFTER.register(((world, player, pos, state, blockEntity) -> {

            String block_id = world.getBlockState(pos).getBlock().getName().getString();

            Action action = new Action(Action.ActionType.BLOCK_BREAK, Action.TargetType.BLOCK, player.getUuidAsString(),
                    block_id, pos.getX(), pos.getY(), pos.getZ(), Action.Dimension.OVER_WORLD, null);
            this.mDataStorage.InsertAction(action);

        }));//方块破坏事件
    }//注册方块破坏事件

    private void HookPlaceEvent() {
        UseBlockCallback.EVENT.register(((player, world, hand, hitResult) -> {

            return ActionResult.PASS;
        }));
    }//注册方块放置事件

    private void HookAttackEvent() {
        AttackBlockCallback.EVENT.register(((player, world, hand, pos, direction) -> {

            return ActionResult.PASS;
        }));
    }//注册玩家攻击事件

    private void InitPlugin() throws IOException {//初始化插件
        LOGGER.info("Loading TheStarryGuard config file.");
        String config_file_path = FabricLoader.getInstance().getConfigDir().toString();//获取配置文件存放的路径
        this.mConfig = Config.LoadConfig(config_file_path);//加载配置文件

        LOGGER.info("Loading TheStarryGuard data storage.");
        this.mDataStorage = DataStorage.GetDataStorage(this.mConfig);//开始构建数据储存对象
        this.mDataStorage.start();//启动数据同步线程
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Loading TheStarryGuard");
        try {
            InitPlugin();//初始化模组
        } catch (IOException e) {
            LOGGER.error("Could not init TheStarryGuard.");
            e.printStackTrace();
            throw new RuntimeException(e);//抛出异常,阻止服务器的启动
        }

        HookBreakEvent();//FIXME
    }
}
