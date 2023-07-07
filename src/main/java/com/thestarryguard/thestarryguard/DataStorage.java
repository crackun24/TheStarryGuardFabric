package com.thestarryguard.thestarryguard;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

enum DataBaseStorageType{MYSQL,SQL_LITE};//数据的储存使用的数据库类型
public class DataStorage {//数据储存类
    Logger LOGGER = LogManager.getLogger();//获取日志记录器
    private DataBaseStorageType mDbStorageType;//使用存储数据库的类型

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

    private DataStorage()//构造函数
    {
    }
}
