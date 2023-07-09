package com.thestarryguard.thestarryguard;

import com.thestarryguard.thestarryguard.DataBaseStorage.DataBase;

public class DataQuery extends Thread{//数据查询类

    private DataQuery()//构造函数
    {

    }

    private DataBase mDataBase;
    @Override
    public void run()//启动线程
    {

    }

    static public DataQuery GetDataQuery(DataBase data_base) {//创建一个data_query对象
        DataQuery temp = new DataQuery();
        temp.mDataBase = data_base;//设置使用的数据库
        return temp;//返回构造好的对象
    }
}
