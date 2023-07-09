package com.thestarryguard.thestarryguard.DataBaseStorage;

import com.thestarryguard.thestarryguard.Config;
import com.thestarryguard.thestarryguard.DataType.Action;
import com.thestarryguard.thestarryguard.DataType.Player;
import com.thestarryguard.thestarryguard.DataType.Tables;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.plaf.nimbus.State;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Mysql extends DataBase {
    private static final String DIMENSION_MAP_TABLE_NAME = "tg_dimension_map";
    private static final String ACTION_MAP_TABLE_NAME = "tg_action_map";
    private static final String ACTION_TABLE_NAME = "tg_action";
    private static final String ITEM_MAP_TABLE_NAME = "tg_item_map";
    private static final String ENTITY_MAP_TABLE_NAME = "tg_entity_map";
    private static final String PLAYER_MAP_TABLE_NAME = "tg_player_map";

    PreparedStatement insert_action;//数据库插入行为的预准备语句
    PreparedStatement insert_dimension_map;//插入维度映射的预准备语句
    PreparedStatement insert_entity_map;//插入实体名字映射的预准备语句

    PreparedStatement insert_player_map;//插入玩家映射的预准备语句
    PreparedStatement inert_action_map;//插入行为映射的预准备语句
    PreparedStatement insert_item_map;//插入物品映射的预准备语句

    private final List<String> DATABASE_TABLES_LIST = new ArrayList() {{
        add(DIMENSION_MAP_TABLE_NAME);
        add(ACTION_MAP_TABLE_NAME);
        add(ENTITY_MAP_TABLE_NAME);
        add(ACTION_TABLE_NAME);
        add(ITEM_MAP_TABLE_NAME);
        add(PLAYER_MAP_TABLE_NAME);
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
    protected synchronized void FlushPlayerMap() throws SQLException {
        String query_str = String.format("SELECT * FROM %s;", PLAYER_MAP_TABLE_NAME);//构建查询语句
        Statement stmt = this.mConn.createStatement();//创建查询
        ResultSet res = stmt.executeQuery(query_str);//执行查询

        HashMap<Player, Integer> temp = new HashMap<>();//创建一个临时的表,如果发生了意外可以保证原有的表不损坏
        while (res.next())//遍历结果集
        {
            temp.put(new Player(res.getString("name"), res.getString("uuid"))
                    , res.getInt("id"));//将对象插入临时的表中
        }

        this.idPlayerMap.clear();//清空原有的表
        this.idPlayerMap = temp;//将引用赋值给对照表
    }

    @Override
    protected synchronized void FlushActionMap() throws SQLException {
        String query_str = String.format("SELECT * FROM %s;", ACTION_MAP_TABLE_NAME);//构建查询语句
        Statement stmt = this.mConn.createStatement();//创建查询
        ResultSet res = stmt.executeQuery(query_str);//执行查询

        HashMap<String, Integer> temp = new HashMap<>();//创建一个临时的表,如果发生了意外可以保证原有的表不损坏

        while (res.next())//遍历结果集
        {
            temp.put(res.getString("action"), res.getInt("id"));//将对象插入临时的表中
        }

        this.idActionMap.clear();//清空原有的表
        this.idActionMap = temp;//将引用赋值给对照表
    }

    @Override
    protected synchronized void FlushItemMap() throws SQLException {
        String query_str = String.format("SELECT * FROM %s;", ITEM_MAP_TABLE_NAME);//构建查询语句
        Statement stmt = this.mConn.createStatement();//创建查询
        ResultSet res = stmt.executeQuery(query_str);//执行查询

        HashMap<String, Integer> temp = new HashMap<>();//创建一个临时的表,如果发生了意外可以保证原有的表不损坏

        while (res.next())//遍历结果集
        {
            temp.put(res.getString("item"), res.getInt("id"));//将对象插入临时的表中
        }

        this.idItemMap.clear();//清空原有的表
        this.idItemMap = temp;//将引用赋值给对照表
    }

    @Override
    protected synchronized void FlushDimensionMap() throws SQLException {
        String query_str = String.format("SELECT * FROM %s;", DIMENSION_MAP_TABLE_NAME);//构建查询语句
        Statement stmt = this.mConn.createStatement();//创建查询
        ResultSet res = stmt.executeQuery(query_str);//执行查询

        HashMap<String, Integer> temp = new HashMap<>();//创建一个临时的表,如果发生了意外可以保证原有的表不损坏

        while (res.next())//遍历结果集
        {
            temp.put(res.getString("dimension"), res.getInt("id"));//将对象插入临时的表中
        }

        this.idDimensionMap.clear();//清空原有的表
        this.idDimensionMap = temp;//将引用赋值给对照表

    }

    @Override
    protected void FlushEntityMap() throws SQLException {
        String query_str = String.format("SELECT * FROM %s;", ENTITY_MAP_TABLE_NAME);//构建查询语句
        Statement stmt = this.mConn.createStatement();//创建查询
        ResultSet res = stmt.executeQuery(query_str);//执行查询

        HashMap<String, Integer> temp = new HashMap<>();//创建一个临时的表,如果发生了意外可以保证原有的表不损坏

        while (res.next())//遍历结果集
        {
            temp.put(res.getString("entity"), res.getInt("id"));//将对象插入临时的表中
        }

        this.idEntityMap.clear();//清空原有的表
        this.idEntityMap = temp;//将引用赋值给对照表

    }

    @Override
    protected int GetOrCreateActionMap(String action) {
        return 0;
    }


    @Override
    protected synchronized int GetOrCreateDimensionMap(String dimension) {
        return 0;
    }

    @Override
    protected synchronized int GetOrCreatePlayerMap(Player player) {
        return 0;
    }

    @Override
    protected synchronized int GetOrCreateItemMap(String item) {
        return 0;
    }

    @Override
    protected int GetOrCreateEntityMap(String entity) {
        return 0;
    }


    @Override
    public synchronized void WriteActionToDb(Action action) throws Exception {//将玩家的行为写入数据库
        int action_id = GetOrCreateActionMap(action.actionType);//获取玩家的行为的ID
        int target_id;//目标的id(玩家放置的方块ID,玩家攻击的实体id等)
        int player_id = GetOrCreatePlayerMap(action.player);//

        switch (action.actionType) {//判断玩家的行为的类型
            case Action.BLOCK_BREAK_ACTION_NAME, Action.BLOCK_USE_ACTION_NAME://方块破坏事件或者方块使用事件则直接获取方块的id
                target_id = GetOrCreateItemMap(action.targetName);//获取方块的id
                break;
            //获取方块id
            case Action.ATTACK_ACTION_NAME://实体攻击事件
                target_id = GetOrCreateEntityMap(action.targetName);//获取实体ID
                break;
            default://如果无法找到行为的映射则直接抛出异常
                throw new Exception("Could not get the map of the type of the action.");
        }

        this.insert_action.setInt(1, player_id);          // 设置 player 参数值
        this.insert_action.setInt(2, action_id);          // 设置 action 参数值
        this.insert_action.setInt(3, target_id);          // 设置 target 参数值
        this.insert_action.setLong(4, action.time); // 设置 time 参数值
        this.insert_action.setInt(6, action.posX);         // 设置 x 参数值
        this.insert_action.setInt(7, action.posY);         // 设置 y 参数值
        this.insert_action.setInt(8, action.posZ);         // 设置 z 参数值

        if (action.actionData != null)//判断玩家的操作是否包含了额外数据
        {
            this.insert_action.setString(5, action.actionData); // 设置 data 参数
        }else{
            this.insert_action.setNull(5,Types.VARCHAR);//如果行为的额外数据为空,则设置为空
        }
        this.insert_action.execute();//执行插入数据
    }

    @Override
    public void CheckAndFixDataBaseStructure() throws Exception {//检查数据库的表的结构是否符合要求,如果不符合要求则进行数据库的表的修复
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

            switch (ele) {//检查数据库的表是否缺失
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

            if (create_table_str != null)//判断创建表的语句不为空,则证明有表缺失
            {
                stmt.execute(create_table_str);//执行创建表的命令
            }
        }

    }

    @Override
    public void ConnectToDataBase() throws Exception {//连接到数据库
        LOGGER.info("Connecting to mysql.");
        Class.forName("com.mysql.cj.jdbc.Driver");//加载mysql数据库的驱动
        this.mConn = DriverManager.getConnection(this.mURL);//连接到数据库
        this.mConn.setAutoCommit(true);//设置为自动提交
        CheckAndFixDataBaseStructure();//检查数据库的表的结构

        if (!this.mConn.isValid(5)) {//判断连接是否有效
            throw new RuntimeException("time out");//如果连接无效,则直接抛出异常
        }
        this.insert_action = this.mConn.prepareStatement(Tables.Mysql.INSERT_ACTION_STR);//设置预处理语句
        LOGGER.info("Mysql connected.");
    }
}
