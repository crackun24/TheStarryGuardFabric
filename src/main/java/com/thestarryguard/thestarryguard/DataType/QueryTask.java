package com.thestarryguard.thestarryguard.DataType;

import com.mysql.cj.QueryReturnType;

public class QueryTask {//玩家请求的查询任务

    public enum QueryType {POINT, AREA}

    //查询的类型是区域还是一个点
    public final QueryType queryType;//查询的类型
    public final int Max_PAGE_AMOUNT = 5;//每页显示的最大结果数量
    public final int pageId;//结果的页数
    public final int x;
    public final int y;
    public final int z;
    public final String dimensionName;//维度的名字
    public final String senderName;//发起查询的玩家的UUID

    public QueryTask(int x, int y, int z, String dimension_name, QueryType queryType, String sender_name,int pageId) {//构造函数
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimensionName = dimension_name;
        this.queryType = queryType;
        this.senderName = sender_name;
        this.pageId = pageId;
    }

}
