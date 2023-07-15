package com.thestarryguard.thestarryguard;

import com.thestarryguard.thestarryguard.Command.Page;
import com.thestarryguard.thestarryguard.Command.QueryNear;
import com.thestarryguard.thestarryguard.Command.QueryPoint;
import com.thestarryguard.thestarryguard.Command.QueryVer;
import com.thestarryguard.thestarryguard.Operation.DataQuery;

public class CommandMgr {//命令管理类
    public static final String COMMAND_PREFIX = "tg";//命令的前缀
    private QueryVer mQueryVer;//查询版本信息的指令
    private Config config;//配置文件对象
    private QueryPoint mQueryPoint;//查询该点的信息的指令
    private Page mPageQuery;//查询页数的指令
    private DataQuery mDataQuery;//数据查询对象
    private QueryNear mQueryNear;//查询玩家周围的信息的指令
    private Lang mLang;//语言对象

    public void RegAllCommand() {//注册所有的指令
        int query_permission_level = 0;//判断查询的权限
        int rollback_permission_levl = 0;
        if(Boolean.parseBoolean(this.config.GetValue("query_op_only")))
        {
           query_permission_level = 4;
        }
        if(Boolean.parseBoolean(this.config.GetValue("rollback_op_only")))
        {
            rollback_permission_levl = 4;
        }

        this.mQueryVer.RegVerInfoCommand(query_permission_level);
        this.mQueryPoint.RegQueryPointCommand(query_permission_level);
        this.mPageQuery.RegQueryPointCommand(query_permission_level);//注册指令
        this.mQueryNear.RegQueryAreaCommand(query_permission_level);
    }

    public CommandMgr(DataQuery data_query,Lang lang,Config config)//构造函数
    {
        this.mQueryVer = new QueryVer();
        this.mDataQuery = data_query;
        this.mQueryPoint = new QueryPoint(data_query,lang);
        this.mPageQuery = new Page(data_query,lang);//创建对象
        this.mQueryNear = new QueryNear(data_query,lang);
        this.config = config;
    }

}
