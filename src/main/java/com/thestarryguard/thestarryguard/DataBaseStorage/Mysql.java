package com.thestarryguard.thestarryguard.DataBaseStorage;

import com.thestarryguard.thestarryguard.Config;
import com.thestarryguard.thestarryguard.DataType.Tables;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class Mysql extends DataBase {
    Config config;
    private final Logger LOGGER;//日志记录器对象

    private void ConnectToDb() throws SQLException//连接到数据库
    {
        String url = String.format("jdbc:mysql://%s:%s/%s",
                this.config.GetValue("mysql_host"), this.config.GetValue("mysql_port"),
                this.config.GetValue("mysql_name"));

        this.mConn = DriverManager.getConnection(url,
                this.config.GetValue("mysql_user"),this.config.GetValue("mysql_pass"));//创建一个新的连接
        this.mConn.setAutoCommit(true);//设置为自动提交
    }
    //构造函数
    private Mysql() {
        LOGGER = LogManager.getLogger();//获取日志记录器
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");//加载数据库的驱动
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Could not load database driver.");
        }
    }

    static public Mysql GetMysql(Config config)//mysql的工厂方法
    {
        Mysql mysql = new Mysql();
        mysql.config = config;
        return mysql;
    }

    @Override
    public void CheckAndFixDataBaseStructure() throws Exception {//检查数据库的表的结构是否符合要求,如果不符合要求则进行数据库的表的修复
        Statement stmt = this.mConn.createStatement();//创建查询
        stmt.execute(Tables.Mysql.CREATE_TG_ACTION);
        stmt.execute(Tables.Mysql.CREATE_TG_ACTION_MAP);
        stmt.execute(Tables.Mysql.CREATE_TG_DIMENSION_MAP);
        stmt.execute(Tables.Mysql.CREATE_TG_ENTITY_MAP);
        stmt.execute(Tables.Mysql.CREATE_TG_ITEM_MAP);
        stmt.execute(Tables.Mysql.CREATE_TG_PLAYER_MAP);
    }

    @Override
    protected void VerifyConnection() throws Exception {
        if (!this.mConn.isValid(3)) {
            this.mConn.close();
            ConnectToDb();//重新连接到数据库
        }
    }


    @Override
    public void ConnectToDataBase() throws Exception {//连接到数据库
        LOGGER.info("Connecting to mysql.");

        ConnectToDb();//连接到数据库
        CheckAndFixDataBaseStructure();//检查数据库的表的结构

        if (!this.mConn.isValid(5)) {//判断连接是否有效
            throw new RuntimeException("time out");//如果连接无效,则直接抛出异常
        }
        FlushActionMap();//刷新对照表
        FlushPlayerMap();
        FlushDimensionMap();
        FlushItemMap();
        FlushEntityMap();

        this.insert_action = this.mConn.prepareStatement(Tables.INSERT_ACTION_STR);//设置预处理语句
        this.insert_action_map = this.mConn.prepareStatement(Tables.INSERT_ACTION_MAP_STR);
        this.insert_dimension_map = this.mConn.prepareStatement(Tables.INSERT_DIMENSION_MAP_STR);
        this.insert_entity_map = this.mConn.prepareStatement(Tables.INSERT_ENTITY_MAP_STR);
        this.insert_item_map = this.mConn.prepareStatement(Tables.INSERT_ITEM_MAP_STR);
        this.insert_player_map = this.mConn.prepareStatement(Tables.INSERT_PLAYER_MAP_STR);
        this.query_point_action_count = this.mConn.prepareStatement(Tables.QUERY_POINT_ACTION_COUNT);
        this.query_point_action = this.mConn.prepareStatement(Tables.QUERY_POINT_ACTION);
        this.Query_area_action_count = this.mConn.prepareStatement(Tables.QUERY_AREA_ACTION_COUNT);
        this.query_area_action = this.mConn.prepareStatement(Tables.QUERY_AREA_ACTION);

        LOGGER.info("Mysql connected.");
    }
}


