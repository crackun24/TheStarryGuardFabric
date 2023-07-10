package com.thestarryguard.thestarryguard;

import com.thestarryguard.thestarryguard.Command.QueryPoint;
import com.thestarryguard.thestarryguard.Command.QueryVer;

public class CommandMgr {//命令管理类
    private QueryVer mQueryVer;//查询版本信息的指令
    private QueryPoint mQueryPoint;//查询该点的信息的指令
    private DataQuery mDataQuery;//数据查询对象

    public void RegAllCommand() {//注册所有的指令
        this.mQueryVer.RegVerInfoCommand();
        this.mQueryPoint.RegQueryPointCommand();
    }

    public CommandMgr(DataQuery data_query)//构造函数
    {
        this.mQueryVer = new QueryVer();
        this.mDataQuery = data_query;
        this.mQueryPoint = new QueryPoint(data_query);
    }

}
