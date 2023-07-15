package com.thestarryguard.thestarryguard;

import com.thestarryguard.thestarryguard.DataBaseStorage.DataBase;
import com.thestarryguard.thestarryguard.DataBaseStorage.Mysql;
import com.thestarryguard.thestarryguard.DataBaseStorage.Sqlite;
import com.thestarryguard.thestarryguard.Operation.DataQuery;
import com.thestarryguard.thestarryguard.Operation.DataStorage;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;


public class TheStarryGuard implements ModInitializer {
    Logger LOGGER = LogManager.getLogger();//获取日志记录器
    Config config;//配置文件对象

    DataStorage dataStorage;//数据储存类
    DataQuery dataQuery;//数据查询对象
    CommandMgr commandMgr;//命令管理对象
    MinecraftServer server;
    Lang lang;//语言对象
    EventMgr eventMgr;//事件管理对象

    private void LoadLang() throws IOException//加载语言文件
    {
        String config_path = FabricLoader.getInstance().getConfigDir().toFile().getPath();//获取配置文件的路径
        File lang_file_path = new File(config_path + "/TheStarryGuard/lang.properties");//语言文件
        this.lang = Lang.LoadLang(lang_file_path);
    }


    private void HookServerStart() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            this.server = server;//获取服务器对象
        });
    }

    private void HookServerClose() {
        ServerLifecycleEvents.SERVER_STOPPING.register((server -> {
            this.dataStorage.CloseThread();
            this.dataQuery.CloseDataQuery();
        }));//注册服务器关闭时向子线程发送关闭信号
    }


    private void CreateDataStorageAndQuery()//创建一个数据储存对象和查询对象
    {
        DataBase data_base;
        switch (this.config.GetValue("data_storage_type"))//确认数据存储的类型
        {
            case "mysql" -> {
                String db_name = this.config.GetValue("mysql_name");
                String db_host = this.config.GetValue("mysql_host");
                String db_port = this.config.GetValue("mysql_port");
                String db_user = this.config.GetValue("mysql_user");
                String db_pass = this.config.GetValue("mysql_pass");
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

        this.dataStorage = DataStorage.GetDataStorage(data_base);//构造数据储存对象
        this.dataQuery = DataQuery.GetDataQuery(data_base, this, lang);//构造数据查询对象
        this.dataStorage.start();//启动数据同步线程
        this.dataQuery.start();//启动数据查询线程
    }


    private void RegCommand() {
        this.commandMgr = new CommandMgr(this.dataQuery, this.lang,this.config);//初始化命令管理对象
        this.commandMgr.RegAllCommand();//注册所有的指令
    }

    private void LoadConfig() throws IOException {//加载配置

        LOGGER.info("Loading TheStarryGuard config file.");
        String config_file_path = FabricLoader.getInstance().getConfigDir().toString();//获取配置文件存放的路径
        this.config = Config.LoadConfig(config_file_path);//加载配置文件
    }

    public synchronized void SendMsgToPlayer(String name, Text text)//给玩家发送消息
    {
        ServerPlayerEntity player = this.server.getPlayerManager().getPlayer(name);//获取玩家对象
        if (player != null) {
            player.sendMessage(text);//发送消息
        } else {
            LOGGER.info("Could not find player");
        }
    }

    public void InitEvent()//初始化事件
    {
        HookServerStart();
        HookServerClose();
        this.eventMgr = new EventMgr(config, dataQuery, dataStorage);//事件管理对象
        this.eventMgr.HookEvent();//注册配置文件中启用的对象
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Loading TheStarryGuard");
        try {
            LoadLang();//加载语言文件
            LoadConfig();//加载配置文件
            CreateDataStorageAndQuery();//创建数据储存对象
            InitEvent();//初始化事件
            RegCommand();//注册命令
        } catch (IOException e) {
            LOGGER.error("Could not init TheStarryGuard.");
            e.printStackTrace();
            throw new RuntimeException(e);//抛出异常,阻止服务器的启动
        }
    }
}
