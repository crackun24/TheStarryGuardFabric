package com.thestarryguard.thestarryguard.DataBaseStorage;

import com.thestarryguard.thestarryguard.DataType.Action;

public abstract class DataBase {//数据库的通用接口定义

    protected abstract void VerifyDbConnection();//校验数据库是否连接成功

    protected abstract String GetPlayerUUIDByMap(int map_id);//通过玩家UUID对照获取玩的UUID

    protected abstract String GetPlayerNameByMap(int map_id);//根据玩家的名字对照获取玩家的名字

    protected abstract String GetActionByMap(Action.ActionType action);//通过玩家的行为对照获取玩家的行为的名字

    protected abstract String GetBlockByMap(int map_id);//通过方块的对照获取方块的名字


    protected abstract int GetOrCreateActionMap(Action.ActionType action);//创建或者获取玩家行为的对照

    protected abstract int GetOrCreatePlayerMap(String player_UUID, String player_name);//创建或获取玩家的对照

    protected abstract int GetOrCreateBlockMap(String block_id);//创建或者获取方块的对照

    protected abstract void WriteBlockBreakEvent(Action action);//写入玩家破坏方块的事件

    protected abstract void WriteBlockPlaceEvent(Action action);//写入玩家放置方块的事件

    public abstract void CheckDbConnection();//检查数据库的连接

    public abstract void WriteActionToDb(Action action);//将玩家的行为写入数据库
}
