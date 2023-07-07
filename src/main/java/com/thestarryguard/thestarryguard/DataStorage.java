package com.thestarryguard.thestarryguard;


import com.thestarryguard.thestarryguard.DataType.Action;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.crypto.Data;
import java.util.*;

enum DataBaseStorageType {MYSQL, SQL_LITE};//数据的储存使用的数据库类型

public class DataStorage extends Thread{//数据储存类,同时启动线程,不定时向数据库同步数据

    Logger LOGGER = LogManager.getLogger();//获取日志记录器
    private DataBaseStorageType mDbStorageType;//使用存储数据库的类型

    private Queue<Action> mActionList;

    private synchronized void PutActionToDb()//将玩家的行为存入数据库
    {
       this.mActionList.poll();//弹出数据
    }

    private DataStorage() {//构造函数
        this.mActionList = new LinkedList<>();//实例化队列对象
    }

    public synchronized void PrintList(){//FIXME 测试方法
        LOGGER.info("test");

        Iterator<Action> iterator = this.mActionList.iterator();
        while (iterator.hasNext())
        {
            Action temp = iterator.next();
            LOGGER.info(temp.posX);
        }
    }

    public synchronized void InsertAction(Action action) {//插入数据
        this.mActionList.add(action);
    }

    public void run(){//数据库同步数据的线程
        while (true)
        {
            try {
                sleep(1000);
                PrintList();//FIXME 调试
                PutActionToDb();//弹出玩家的行为
            } catch (InterruptedException e) {
            }
        }
    }
    static public DataStorage GetDataStorage(final Config config) {//构造一个数据储存对象的工厂方法
        DataStorage temp_obj = new DataStorage();//示例化一个对象

        switch (config.GetValue("data_storage_type"))//确认数据存储的类型
        {
            case "mysql":
                temp_obj.mDbStorageType = DataBaseStorageType.MYSQL;//设置使用mysql存储数据

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
