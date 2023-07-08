package com.thestarryguard.thestarryguard.DataBaseStorage;

import com.thestarryguard.thestarryguard.DataType.Action;

import java.util.HashMap;

public abstract class DataBase {//数据库的通用接口定义
    protected HashMap<Integer, String> idPlayerMap;//玩家UUID和ID的映射
    protected HashMap<Integer, String> idActionMap;//玩家行为和ID的映射
    protected HashMap<Integer, String> idEntityMap;//实体的名字和ID的映射
    protected HashMap<Integer, String> idDimensionMap;//维度的名字和ID的映射
    protected HashMap<Integer, String> idItemMap;//物品的ID和ID的映射


    protected abstract void FlushPlayerMap();//刷新玩家表的映射

    protected abstract void FlushActionMap();//刷新行为的表的映射

    protected abstract void FlushItemMap();//刷新物品的映射

    protected abstract void FlushDimensionMap();//刷新维度的映射
    protected abstract void FlushEntityMap();//刷新实体的映射表


    protected abstract String GetPlayerUUIDByMap(int map_id);//通过玩家UUID对照获取玩的UUID

    protected abstract String GetPlayerNameByMap(int map_id);//根据玩家的名字对照获取玩家的名字

    protected abstract String GetActionByMap(Action.ActionType action);//通过玩家的行为对照获取玩家的行为的名字

    protected abstract String GetItemByMap(int map_id);//通过方块的对照获取方块的名字
    protected abstract String GetEntityByMap(int entity_id);//通过实体的对照获取实体的名字

    protected abstract String GetDimensionByMap(int map_id);//通过维度的对照获取维度的名字


    protected abstract int GetOrCreateActionMap(Action.ActionType action);//创建或者获取玩家行为的对照

    protected abstract int GetOrCreateDimensionMap(String dimension);//创建或者获取维度的对照

    protected abstract int GetOrCreatePlayerMap(String player_UUID, String player_name);//创建或获取玩家的对照

    protected abstract int GetOrCreateItemMap(String item);//创建或者获取物品的对照
    protected abstract int GetOrCreateEntityMap(String entity);//创建或者获取实体的id



    protected abstract void CheckAndFixDataBaseStructure();//检查数据库的表的结构,如果表不符合要求,则修复表

    public abstract void WriteActionToDb(Action action);//将玩家的行为写入数据库

    public abstract void ConnectToDataBase();//连接到数据库
}
