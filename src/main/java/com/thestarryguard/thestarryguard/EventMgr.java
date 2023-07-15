package com.thestarryguard.thestarryguard;

import com.thestarryguard.thestarryguard.DataType.Action;
import com.thestarryguard.thestarryguard.DataType.Player;
import com.thestarryguard.thestarryguard.DataType.QueryTask;
import com.thestarryguard.thestarryguard.Events.BlockPlaceEvent;
import com.thestarryguard.thestarryguard.Events.PlayerKillEntityEvent;
import com.thestarryguard.thestarryguard.Events.PlayerKillPlayerEvent;
import com.thestarryguard.thestarryguard.Operation.DataQuery;
import com.thestarryguard.thestarryguard.Operation.DataStorage;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.registry.Registries;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;

public class EventMgr {
    private Config config;//配置文件对象
    private DataStorage dataStorage;//数据存储对象
    private DataQuery dataQuery;//数据查询对象

    private void HookBlockBreakEvent() {
        PlayerBlockBreakEvents.BEFORE.register(((world, player, pos, state, blockEntity) -> {
            String player_name = player.getName().getString();
            if (this.dataQuery.IsPlayerHookPointQuery(player_name)) {

                String dimension_name = world.getRegistryKey().getValue().toUnderscoreSeparatedString();//获取世界的名字
                QueryTask task = new QueryTask(pos.getX(), pos.getY(), pos.getZ(),
                        dimension_name, QueryTask.QueryType.POINT, player_name, 1);
                //创建一个新的查询任务,默认显示第一页的内容,因为是点击,所以为点查询
                this.dataQuery.AddQueryTask(task);//添加查询任务

                return false;
            } else {//玩家没有启用方块查询
                String block_id = Registries.BLOCK.getId(world.getBlockState(pos).getBlock()).toString();
                String dimension_name = world.getRegistryKey().getValue().toUnderscoreSeparatedString();//获取世界的id

                Action action = new Action(Action.BLOCK_BREAK_ACTION_NAME, new Player(player.getName().getString(),
                        player.getUuidAsString()),
                        block_id, pos.getX(), pos.getY(), pos.getZ(), dimension_name, null);

                this.dataStorage.InsertAction(action);//插入玩家破坏方块的行为对象
            }

            return true;
        }));//方块破坏事件
    }//注册方块破坏事件


    private void HookEntityAttackEvent() {
        AttackEntityCallback.EVENT.register(((player1, world1, hand1, entity, hitResult) -> {
            String world_id = world1.getRegistryKey().getValue().toUnderscoreSeparatedString();//获取世界的id
            String entity_id = Registries.ENTITY_TYPE.getId(entity.getType()).toShortTranslationKey(); //获取实体的id
            Vec3d location = entity.getPos();//获取被攻击的实体的位置

            Action action = new Action(Action.ATTACK_ACTION_NAME, new Player(player1.getName().getString(), player1.getUuidAsString()),
                    entity_id, (int) location.getX(), (int) location.getY(), (int) location.getZ(), world_id, null);//构造行为对象

            this.dataStorage.InsertAction(action);
            return ActionResult.PASS;
        }));
    }//注册玩家攻击实体事件

    private void HookBlockPlaceEvent()//方块放置的事件
    {
        BlockPlaceEvent.EVENT.register(((world, pos, state, placer, itemStack) -> {
            String dimension_name = world.getRegistryKey().getValue().toUnderscoreSeparatedString();//获取世界的名字
            String block_id = Registries.BLOCK.getId(world.getBlockState(pos).getBlock()).toString();

            Action action = new Action(Action.BLOCK_PLACE, new Player(placer.getName().getString(),
                    placer.getUuidAsString()),
                    block_id, pos.getX(), pos.getY(), pos.getZ(), dimension_name, null);
            this.dataStorage.InsertAction(action);
            return ActionResult.PASS;
        }
        ));
    }

    private void HookKillEntityEvent()//注册杀死实体的事件
    {
        PlayerKillEntityEvent.EVENT.register(((world, killer, other) -> {
            String world_id = world.getRegistryKey().getValue().toUnderscoreSeparatedString();//获取世界的id
            String entity_id = Registries.ENTITY_TYPE.getId(other.getType()).toShortTranslationKey(); //获取实体的id

            Action action = new Action(Action.KILL_ENTITY_ACTION_NAME, new Player(killer.getName().getString(), killer.getUuidAsString()),
                    entity_id, other.getBlockX(), other.getBlockY(), other.getBlockZ(), world_id, null);
            this.dataStorage.InsertAction(action);

            return ActionResult.PASS;
        }));
    }

    private void HookKillPlayerEvent()//注册玩家杀死玩家事件
    {
        PlayerKillPlayerEvent.EVENT.register(((world, killer, player) -> {

            String world_id = world.getRegistryKey().getValue().toUnderscoreSeparatedString();//获取世界的id
            String player_name = player.getName().getString(); //获取玩家的名字
            String player_uuid = player.getUuidAsString();//
            String mix_str = player_name + ":" + player_uuid;//混合型字符串,用于后面的解析

            Action action = new Action(Action.KILL_PLAYER_ACTION_NAME,
                    new Player(killer.getName().getString(), killer.getUuidAsString()),
                    mix_str, player.getBlockX(), player.getBlockY(), player.getBlockZ(), world_id, null);

            this.dataStorage.InsertAction(action);

            return ActionResult.PASS;
        }));
    }

    public void HookEvent()//注册所有在配置文件中启用的事件
    {
        if (Boolean.parseBoolean(config.GetValue("hook_block_break_event")))//判断是否注册方块破坏事件
        {
            HookBlockBreakEvent();//如果启用则注册破坏方块的事件
        }

        if (Boolean.parseBoolean(config.GetValue("hook_block_place_event")))//判断是否注册方块放置的事件
        {
            HookBlockPlaceEvent();//如果启用则注册击杀实体的事件
        }

        if (Boolean.parseBoolean(config.GetValue("hook_attack_entity_event"))) //判断是否注册攻击实体的事件
        {
            HookEntityAttackEvent();//如果启用则注册实体攻击事件
        }

        if (Boolean.parseBoolean(config.GetValue("hook_kill_entity_event")))//判断是否要注册击杀实体的事件
        {
            HookKillEntityEvent();//如果启用则注册击杀实体的事件
        }

        if(Boolean.parseBoolean(config.GetValue("hook_kill_player_event")))//判断是否要注册击杀玩家的事件
        {
            HookKillPlayerEvent();//如果启用则注册击杀玩家事件
        }
    }//注册配置文件中的事件

    public EventMgr(Config config, DataQuery data_query, DataStorage data_storage) {//构造函数
        this.config = config;
        this.dataQuery = data_query;
        this.dataStorage = data_storage;
    }
}
