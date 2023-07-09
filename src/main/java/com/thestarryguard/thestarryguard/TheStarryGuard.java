package com.thestarryguard.thestarryguard;

import com.thestarryguard.thestarryguard.DataType.Action;
import com.thestarryguard.thestarryguard.DataType.Player;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.item.AirBlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.text.html.parser.Entity;
import javax.xml.stream.Location;
import java.io.IOException;


public class TheStarryGuard implements ModInitializer {
    Logger LOGGER = LogManager.getLogger();//获取日志记录器

    Config mConfig;//配置文件对象

    DataStorage mDataStorage;//数据储存类

    private void HookBlockBreakEvent() {
        PlayerBlockBreakEvents.BEFORE.register(((world, player, pos, state, blockEntity) -> {
            String block_id = Registries.BLOCK.getId(world.getBlockState(pos).getBlock()).toString();
            String world_id = world.getRegistryKey().getValue().toUnderscoreSeparatedString();//获取世界的id

            Action action = new Action(Action.BLOCK_BREAK_ACTION_NAME, new Player(player.getName().getString(),
                    player.getUuidAsString()),
                    block_id, pos.getX(), pos.getY(), pos.getZ(), world_id, null);

            this.mDataStorage.InsertAction(action);//插入玩家破坏方块的行为对象

            return true;
        }));//方块破坏事件
    }//注册方块破坏事件

    private void HookBlockUseEvent() {
        UseBlockCallback.EVENT.register(((player, world, hand, hitResult) -> {
            if (hitResult.getType() == HitResult.Type.BLOCK &&
                    (player.getMainHandStack().getItem() != Items.AIR || player.getOffHandStack().getItem() != Items.AIR)) {//判断操作的对象是否为方块


                BlockPos location = hitResult.getBlockPos();//获取方块的坐标
                String block_id = Registries.ITEM.getId(
                                player.getMainHandStack().getItem() == Items.AIR ? player.getOffHandStack().getItem() : player.getMainHandStack().getItem())
                        .toString();//获取玩家使用的方块的ID
                String world_id = world.getRegistryKey().getValue().toUnderscoreSeparatedString();//获取世界的id

                Action action = new Action(Action.BLOCK_USE_ACTION_NAME, new Player(player.getName().getString(),
                        player.getUuidAsString()),
                        block_id, location.getX(), location.getY(), location.getZ(), world_id, null);

                this.mDataStorage.InsertAction(action);//插入玩家使用方块的行为对象
            }

            return ActionResult.PASS;
        }));
    }//注册方块放置事件

    private void HookEntityAttackEvent() {
        AttackEntityCallback.EVENT.register(((player1, world1, hand1, entity, hitResult) -> {
            String world_id = world1.getRegistryKey().getValue().toUnderscoreSeparatedString();//获取世界的id
            String entity_id = Registries.ENTITY_TYPE.getId(entity.getType()).toShortTranslationKey(); //获取实体的id
            Vec3d location = entity.getPos();//获取被攻击的实体的位置

            Action action = new Action(Action.ATTACK_ACTION_NAME,new Player(player1.getName().getString(),player1.getUuidAsString()),
                    entity_id, (int) location.getX(), (int) location.getY(), (int) location.getZ(), world_id, null);//构造行为对象

            this.mDataStorage.InsertAction(action);
            return ActionResult.PASS;
        }));
    }//注册玩家攻击实体事件
    private void HookServerClose()
    {
        ServerLifecycleEvents.SERVER_STOPPING.register((server -> {
           this.mDataStorage.CloseThread();
        }));//注册服务器关闭时向子线程发送关闭信号
    }

    private void HookEvent() {
        HookServerClose();
        if (Boolean.parseBoolean(this.mConfig.GetValue("hook_block_break_event")))//判断是否注册方块破坏事件
        {
            HookBlockBreakEvent();//如果启用则注册破坏方块的事件
        }

        if (Boolean.parseBoolean(this.mConfig.GetValue("hook_block_use_event")))//判断是否注册方块放置的事件
        {
            HookBlockUseEvent();//如果启用则注册使用方块事件
        }

        if (Boolean.parseBoolean(this.mConfig.GetValue("hook_attack_entity_event"))) //判断是否注册攻击实体的事件
        {
            HookEntityAttackEvent();//如果启用则注册实体攻击事件
        }
    }//注册配置文件中的事件

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
            HookEvent();//注册配置文件中启用的事件
        } catch (IOException e) {
            LOGGER.error("Could not init TheStarryGuard.");
            e.printStackTrace();
            throw new RuntimeException(e);//抛出异常,阻止服务器的启动
        }

    }
}
