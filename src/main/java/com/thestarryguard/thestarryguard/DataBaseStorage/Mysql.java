package com.thestarryguard.thestarryguard.DataBaseStorage;

import com.thestarryguard.thestarryguard.Config;
import com.thestarryguard.thestarryguard.DataType.Action;
import com.thestarryguard.thestarryguard.DataType.Tables;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.plaf.nimbus.State;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Mysql extends DataBase {
    private final List<String> DATABASE_TABLES_LIST = new ArrayList() {{
        add("tg_dimension_map");
        add("tg_action");
        add("tg_entity_map");
        add("tg_action_map");
        add("tg_item_map");
        add("tg_player_map");
    }};
    private String mURL;//mysql连接的地址
    private Connection mConn;//数据库连接对象
    private Config mConfig;//配置文件
    private Logger LOGGER;//日志记录器对象

    //构造函数
    private Mysql() {
        LOGGER = LogManager.getLogger();//获取日志记录器
    }

    static public Mysql GetMysql(String url)//mysql的工厂方法
    {
        Mysql mysql = new Mysql();
        mysql.mURL = url;
        return mysql;
    }

    @Override
    protected synchronized void FlushPlayerMap() {

    }

    @Override
    protected synchronized void FlushActionMap() {

    }

    @Override
    protected synchronized void FlushItemMap() {

    }

    @Override
    protected synchronized void FlushDimensionMap() {

    }

    @Override
    protected synchronized String GetPlayerUUIDByMap(int map_id) {
        return null;
    }

    @Override
    protected synchronized String GetPlayerNameByMap(int map_id) {
        return null;
    }

    @Override
    protected synchronized String GetActionByMap(Action.ActionType action) {
        return null;
    }

    @Override
    protected synchronized String GetItemByMap(int map_id) {
        return null;
    }

    @Override
    protected synchronized String GetDimensionByMap(int map_id) {
        return null;
    }

    @Override
    protected synchronized int GetOrCreateActionMap(Action.ActionType action) {
        return 0;
    }

    @Override
    protected synchronized int GetOrCreateDimensionMap(String dimension) {
        return 0;
    }

    @Override
    protected synchronized int GetOrCreatePlayerMap(String player_UUID, String player_name) {
        return 0;
    }

    @Override
    protected synchronized int GetOrCreateItemMap(String block) {
        return 0;
    }


    @Override
    protected synchronized void WriteBlockBreakEvent(Action action) {

    }

    @Override
    protected synchronized void WriteBlockPlaceEvent(Action action) {

    }

    @Override
    public synchronized void WriteActionToDb(Action action) {//将玩家的行为写入数据库

    }

    @Override
    public void CheckAndFixDataBaseStructure() {//检查数据库的表的结构是否符合要求,如果不符合要求则进行数据库的表的修复
        try {
            String query_str = "show tables;";//查询数据库中所有的表的mysql指令
            Statement stmt = this.mConn.createStatement();//创建查询
            ResultSet res = stmt.executeQuery(query_str);

            List<String> db_tables_list = new ArrayList<>();//放置数据库中的所有表的临时数组
            while (res.next()) {
                db_tables_list.add(res.getString(1));//将数据库返回的结果插入数组中
            }

            List<String> db_missing_tables_list = new ArrayList<>();//放置数据库中缺失的表的数组
            for (String temp : DATABASE_TABLES_LIST)//遍历表
            {
                if (!db_tables_list.contains(temp))//判断是否含有该元素
                {
                    db_missing_tables_list.add(temp);//如果数据库中返回的表不包含该元素,则直接将该元素放入缺失的表的数组中
                }
            }

            for (String ele : db_missing_tables_list) {
                String create_table_str = "";
                LOGGER.warn(ele);//FIXME
                switch (ele) {
                    case "tg_action"://判断是行为的表缺失
                        create_table_str = Tables.Mysql.CREATE_TG_ACTION;
                        break;
                    case "tg_action_map":
                        create_table_str = Tables.Mysql.CREATE_TG_ACTION_MAP;
                        break;
                    case "tg_dimension_map":
                        create_table_str = Tables.Mysql.CREATE_TG_DIMENSION_MAP;
                        break;
                    case "tg_entity_map":
                        create_table_str = Tables.Mysql.CREATE_TG_ENTITY_MAP;
                        break;
                    case "tg_item_map":
                        create_table_str = Tables.Mysql.CREATE_TG_ITEM_MAP;
                        break;
                    case "tg_player_map":
                        create_table_str = Tables.Mysql.CREATE_TG_PLAYER_MAP;
                        break;
                    default:
                        throw new Exception("Internal error: could not get the command of the missing table.");
                }

                if (create_table_str != null)//判断创建表的雨具不为空,则证明有表缺失
                {
                    stmt.execute(create_table_str);//执行创建表的命令
                }
            }

        } catch (Exception e) {
            LOGGER.info("Could not verify database structure.");
            e.printStackTrace();
        }
    }

    @Override
    public void ConnectToDataBase() {//连接到数据库
        LOGGER.info("Connecting to mysql.");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");//加载mysql数据库的驱动
            this.mConn = DriverManager.getConnection(this.mURL);//连接到数据库
            this.mConn.setAutoCommit(true);//设置为自动提交
            CheckAndFixDataBaseStructure();//检查数据库的表的结构

            if (!this.mConn.isValid(5)) {//判断连接是否有效
                throw new RuntimeException("time out");//如果连接无效,则直接抛出异常
            }

        } catch (Exception e) {
            LOGGER.info("Could not connect to mysql.");
            e.printStackTrace();
        }
        LOGGER.info("Mysql connected.");
    }
}
