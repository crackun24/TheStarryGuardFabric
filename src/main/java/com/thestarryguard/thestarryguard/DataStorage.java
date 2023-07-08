package com.thestarryguard.thestarryguard;


import com.thestarryguard.thestarryguard.DataBaseStorage.DataBase;
import com.thestarryguard.thestarryguard.DataBaseStorage.Mysql;
import com.thestarryguard.thestarryguard.DataType.Action;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.util.*;

enum DataBaseStorageType {MYSQL, SQL_LITE};//数据的储存使用的数据库类型

public class DataStorage extends Thread {//数据储存类,同时启动线程,不定时向数据库同步数据

    Logger LOGGER = LogManager.getLogger();//获取日志记录器
    private DataBase mDataBase;//数据库对象
    private DataBaseStorageType mDbStorageType;//使用存储数据库的类型

    private Queue<Action> mActionList;

    private synchronized void PutActionToDb()//将玩家的行为存入数据库
    {
        if (!mActionList.isEmpty()) {//判断队列是否为空
            Action action = this.mActionList.poll();//弹出数据
            LOGGER.info(String.format("player: %s,dimension: %s,block: %s,action: %s",
                    action.playerUUID, action.dimension, action.targetName, action.actionType.name()));
        }
    }


    private DataStorage() {//构造函数
        this.mActionList = new LinkedList<>();//实例化队列对象
    }

    public synchronized void PrintList() {//FIXME 测试方法
        Iterator<Action> iterator = this.mActionList.iterator();
        while (iterator.hasNext()) {
            Action temp = iterator.next();
            LOGGER.info(temp.posX);
        }
    }

    public synchronized void InsertAction(Action action) {//插入数据
        this.mActionList.add(action);
    }

    public void run() {//数据库同步数据的线程
        try{
            this.mDataBase.ConnectToDataBase();//连接到数据库

        }catch (Exception e)
        {
            LOGGER.error("Could not connect to dataBase.");
            e.printStackTrace();
        }

        while (true) {
            try {
                sleep(1000);
                //PrintList();//FIXME 调试
                PutActionToDb();//弹出玩家的行为
            } catch (Exception e) {
                LOGGER.error(String.format("An error occurred when running the data: %s",e.toString()));
                e.printStackTrace();
            }
        }
    }

    static public DataStorage GetDataStorage(final Config config) {//构造一个数据储存对象的工厂方法
        DataStorage temp_obj = new DataStorage();//示例化一个对象

        switch (config.GetValue("data_storage_type"))//确认数据存储的类型
        {
            case "mysql":
                temp_obj.mDbStorageType = DataBaseStorageType.MYSQL;//设置使用mysql存储数据
                String db_name = config.GetValue("mysql_name");
                String db_host = config.GetValue("mysql_host");
                String db_port = config.GetValue("mysql_port");
                String db_user = config.GetValue("mysql_user");
                String db_pass = config.GetValue("mysql_pass");

                String url = String.format("jdbc:mysql://%s:%s/%s?autoReconnect=true&serverTimezone=UTC&useSSL=false&user=%s&password=%s",db_host,db_port,db_name,db_user,db_pass);
                temp_obj.mDataBase = Mysql.GetMysql(url);//构建一个mysql数据库连接对象

                break;
            case "sql_lite":
                temp_obj.mDbStorageType = DataBaseStorageType.SQL_LITE;
                break;
            default:
                throw new RuntimeException("Unable to confirm the database type used.");//如果无法确认使用的数据库类型,直接抛出异常
        }

        return temp_obj;//返回构造好的对象
    }
}
