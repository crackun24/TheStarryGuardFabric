package com.thestarryguard.thestarryguard.DataBaseStorage;

import com.thestarryguard.thestarryguard.DataType.Action;
import com.thestarryguard.thestarryguard.DataType.Player;

import java.util.HashMap;

public abstract class DataBase {//数据库的通用接口定义
    protected HashMap<Player, Integer> playerIdMap = new HashMap<>();//玩家对象和ID的映射
    protected HashMap<String, Integer> actionIdMap = new HashMap<>();//玩家行为和ID的映射
    protected HashMap<String, Integer> entityIdMap = new HashMap<>();//实体的名字和ID的映射
    protected HashMap<String, Integer> dimensionIdMap = new HashMap<>();//维度的名字和ID的映射
    protected HashMap<String, Integer> itemIdMap = new HashMap<>();//物品的ID和ID的映射


    protected HashMap<Integer, Player> idPlayerMap = new HashMap<>();//玩家id和玩家对象的映射
    protected HashMap<Integer, String> idActionMap = new HashMap<>();//玩家的行为ID和玩家的行为的映射
    protected HashMap<Integer, String> idEntityMap = new HashMap<>();//实体的id和实体的名字的映射
    protected HashMap<Integer, String> idDimensionMap = new HashMap<>();//维度ID和维度的映射
    protected HashMap<Integer, String> idItemMap = new HashMap<>();//物品id和物品名称的映射


    protected abstract void FlushPlayerMap() throws Exception;//刷新玩家表的映射

    protected abstract void FlushActionMap() throws Exception;//刷新行为的表的映射

    protected abstract void FlushItemMap() throws Exception;//刷新物品的映射

    protected abstract void FlushDimensionMap() throws Exception;//刷新维度的映射

    protected abstract void FlushEntityMap() throws Exception;//刷新实体的映射表


    protected abstract int GetOrCreateActionMap(String action) throws Exception;//创建或者获取玩家行为的对照

    protected abstract int GetOrCreateDimensionMap(String dimension) throws Exception;//创建或者获取维度的对照

    protected abstract int GetOrCreatePlayerMap(Player player) throws Exception;//创建或获取玩家的对照

    protected abstract int GetOrCreateItemMap(String item) throws Exception;//创建或者获取物品的对照

    protected abstract int GetOrCreateEntityMap(String entity) throws Exception;//创建或者获取实体的id


    protected abstract void CheckAndFixDataBaseStructure() throws Exception;//检查数据库的表的结构,如果表不符合要求,则修复表

    public abstract void WriteActionToDb(Action action) throws Exception;//将玩家的行为写入数据库

    public abstract void ConnectToDataBase() throws Exception;//连接到数据库
}
