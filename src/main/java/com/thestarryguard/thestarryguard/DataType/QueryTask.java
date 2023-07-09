package com.thestarryguard.thestarryguard.DataType;

import com.mysql.cj.QueryReturnType;

public class QueryTask {//玩家请求的查询任务

    enum QueryType {POINT, AREA}

    ;
    //查询的类型是区域还是一个点
    public final QueryType queryType;//查询的类型
    public final int x;
    public final int y;
    public final int z;
    public final int dimensionId;//维度映射的id
    public final String senderPlayerUUID;//发起查询的玩家的UUID

    public QueryTask(int x, int y, int z, int dimension_id, QueryType queryType, String senderPlayerUUID) {//构造函数
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimensionId = dimension_id;
        this.queryType = queryType;
        this.senderPlayerUUID = senderPlayerUUID;
    }

}
