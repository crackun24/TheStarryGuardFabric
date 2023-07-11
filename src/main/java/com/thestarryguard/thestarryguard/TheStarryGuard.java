package com.thestarryguard.thestarryguard;

import com.thestarryguard.thestarryguard.DataBaseStorage.DataBase;
import com.thestarryguard.thestarryguard.DataBaseStorage.Mysql;
import com.thestarryguard.thestarryguard.DataBaseStorage.Sqlite;
import com.thestarryguard.thestarryguard.DataType.Action;
import com.thestarryguard.thestarryguard.DataType.Player;
import com.thestarryguard.thestarryguard.DataType.QueryTask;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;


public class TheStarryGuard implements ModInitializer {
    Logger LOGGER = LogManager.getLogger();//获取日志记录器
    Config mConfig;//配置文件对象

    DataStorage mDataStorage;//数据储存类
    DataQuery mDataQuery;//数据查询对象
    CommandMgr mCommandMgr;//命令管理对象
    MinecraftServer mServer;
    Lang mLang;//语言对象
    private void LoadLang() throws IOException//加载语言文件
    {
        String config_path = FabricLoader.getInstance().getConfigDir().toFile().getPath();//获取配置文件的路径
        File lang_file_path = new File(config_path + "/TheStarryGuard/lang.properties");//语言文件
        this.mLang = Lang.LoadLang(lang_file_path);
    }
    private void HookBlockBreakEvent() {
        PlayerBlockBreakEvents.BEFORE.register(((world, player, pos, state, blockEntity) -> {
            String player_name = player.getName().getString();
            if (this.mDataQuery.IsPlayerHookPointQuery(player_name)) {

                String dimension_name = world.getRegistryKey().getValue().toUnderscoreSeparatedString();//获取世界的名字
                QueryTask task = new QueryTask(pos.getX(), pos.getY(), pos.getZ(),
                        dimension_name, QueryTask.QueryType.POINT, player_name, 1);
                //创建一个新的查询任务,默认显示第一页的内容,因为是点击,所以为点查询
                this.mDataQuery.AddQueryTask(task);//添加查询任务

                return false;
            } else {//玩家没有启用方块查询
                String block_id = Registries.BLOCK.getId(world.getBlockState(pos).getBlock()).toString();
                String dimension_name = world.getRegistryKey().getValue().toUnderscoreSeparatedString();//获取世界的id

                Action action = new Action(Action.BLOCK_BREAK_ACTION_NAME, new Player(player.getName().getString(),
                        player.getUuidAsString()),
                        block_id, pos.getX(), pos.getY(), pos.getZ(), dimension_name, null);

                this.mDataStorage.InsertAction(action);//插入玩家破坏方块的行为对象
            }

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
                String dimension_name = world.getRegistryKey().getValue().toUnderscoreSeparatedString();//获取世界的名字

                Action action = new Action(Action.BLOCK_USE_ACTION_NAME, new Player(player.getName().getString(),
                        player.getUuidAsString()),
                        block_id, location.getX(), location.getY(), location.getZ(), dimension_name, null);

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

            Action action = new Action(Action.ATTACK_ACTION_NAME, new Player(player1.getName().getString(), player1.getUuidAsString()),
                    entity_id, (int) location.getX(), (int) location.getY(), (int) location.getZ(), world_id, null);//构造行为对象

            this.mDataStorage.InsertAction(action);
            return ActionResult.PASS;
        }));
    }//注册玩家攻击实体事件

    private void HookServerStart() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            this.mServer = server;//获取服务器对象
        });
    }

    private void HookServerClose() {
        ServerLifecycleEvents.SERVER_STOPPING.register((server -> {
            this.mDataStorage.CloseThread();
            this.mDataQuery.CloseDataQuery();
        }));//注册服务器关闭时向子线程发送关闭信号
    }

    private void CreateDataStorageAndQuery()//创建一个数据储存对象和查询对象
    {
        DataBase data_base;
        switch (this.mConfig.GetValue("data_storage_type"))//确认数据存储的类型
        {
            case "mysql" -> {
                String db_name = this.mConfig.GetValue("mysql_name");
                String db_host = this.mConfig.GetValue("mysql_host");
                String db_port = this.mConfig.GetValue("mysql_port");
                String db_user = this.mConfig.GetValue("mysql_user");
                String db_pass = this.mConfig.GetValue("mysql_pass");
                String url = String.format("jdbc:mysql://%s:%s/%s?autoReconnect=true&serverTimezone=UTC&useSSL=false&user=%s&password=%s", db_host, db_port, db_name, db_user, db_pass);
                data_base = Mysql.GetMysql(url);//构建一个mysql数据库连接对象
            }
            case "sqlite" -> {
                File config_dir = FabricLoader.getInstance().getConfigDir().toFile();//获取配置文件存放的根目录
                File database_path = new File(config_dir.getPath() + "/TheStarryGuard" + Sqlite.FILE_NAME);
                data_base = Sqlite.GetSqlite(database_path);//构建一个sqlite数据库的连接对象
            }
            default -> throw new RuntimeException("Unable to confirm the database type used.");//如果无法确认使用的数据库类型,直接抛出异常
        }

        this.mDataStorage = DataStorage.GetDataStorage(data_base);//构造数据储存对象
        this.mDataQuery = DataQuery.GetDataQuery(data_base, this,mLang);//构造数据查询对象
        this.mDataStorage.start();//启动数据同步线程
        this.mDataQuery.start();//启动数据查询线程
    }

    private void HookEvent() {
        HookServerClose();
        HookServerStart();
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

    private void RegCommand() {
        this.mCommandMgr = new CommandMgr(this.mDataQuery,this.mLang);//初始化命令管理对象
        this.mCommandMgr.RegAllCommand();//注册所有的指令
    }

    private void LoadConfig() throws IOException {//加载配置

        LOGGER.info("Loading TheStarryGuard config file.");
        String config_file_path = FabricLoader.getInstance().getConfigDir().toString();//获取配置文件存放的路径
        this.mConfig = Config.LoadConfig(config_file_path);//加载配置文件
    }

    public synchronized void SendMsgToPlayer(String name, Text text)//给玩家发送消息
    {
        ServerPlayerEntity player = this.mServer.getPlayerManager().getPlayer(name);//获取玩家对象
        if (player != null) {
            player.sendMessage(text);//发送消息
        } else {
            LOGGER.info("Could not find player");
        }
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Loading TheStarryGuard");
        try {
            LoadLang();//加载语言文件
            LoadConfig();//加载配置文件
            HookEvent();//注册配置文件中启用的事件
            CreateDataStorageAndQuery();//创建数据储存对象
            RegCommand();//注册命令
        } catch (IOException e) {
            LOGGER.error("Could not init TheStarryGuard.");
            e.printStackTrace();
            throw new RuntimeException(e);//抛出异常,阻止服务器的启动
        }
    }
}
