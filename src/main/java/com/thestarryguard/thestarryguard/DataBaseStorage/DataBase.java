package com.thestarryguard.thestarryguard.DataBaseStorage;

import com.thestarryguard.thestarryguard.DataType.Action;
import com.thestarryguard.thestarryguard.DataType.Player;
import com.thestarryguard.thestarryguard.DataType.QueryTask;
import oshi.hardware.HardwareAbstractionLayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import static com.thestarryguard.thestarryguard.DataType.Action.*;


public abstract class DataBase {//数据库的通用接口定义
    protected Connection mConn;//数据库连接对象
    protected static final String DIMENSION_MAP_TABLE_NAME = "tg_dimension_map";
    protected static final String ACTION_MAP_TABLE_NAME = "tg_action_map";
    protected static final String ACTION_TABLE_NAME = "tg_action";
    protected static final String ITEM_MAP_TABLE_NAME = "tg_item_map";
    protected static final String ENTITY_MAP_TABLE_NAME = "tg_entity_map";
    protected static final String PLAYER_MAP_TABLE_NAME = "tg_player_map";


    protected PreparedStatement insert_action;//数据库插入行为的预准备语句
    protected PreparedStatement insert_dimension_map;//插入维度映射的预准备语句
    protected PreparedStatement insert_entity_map;//插入实体名字映射的预准备语句
    protected PreparedStatement insert_player_map;//插入玩家映射的预准备语句
    protected PreparedStatement insert_action_map;//插入行为映射的预准备语句
    protected PreparedStatement insert_item_map;//插入物品映射的预准备语句
    protected PreparedStatement query_point_action;//查询单个点的行为
    protected PreparedStatement query_point_action_count;//查询单个点的行为的总量
    protected PreparedStatement query_area_action;//查询区域的行为
    protected PreparedStatement Query_area_action_count;//查询区域的行为的数量

    public enum DataBaseStorageType {MYSQL, SQL_LITE}

    ;//数据的储存使用的数据库类型
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


    protected synchronized void FlushPlayerMap() throws SQLException {

        String query_str = String.format("SELECT * FROM %s;", PLAYER_MAP_TABLE_NAME);//构建查询语句
        Statement stmt = this.mConn.createStatement();//创建查询
        ResultSet res = stmt.executeQuery(query_str);//执行查询

        HashMap<Player, Integer> player_id = new HashMap<>();//创建一个临时的表,如果发生了意外可以保证原有的表不损坏
        HashMap<Integer, Player> id_player = new HashMap<>();

        while (res.next())//遍历结果集
        {
            id_player.put(res.getInt("id"), new Player(res.getString("name"), res.getString("uuid")));//将对象插入临时的表中
            player_id.put(new Player(res.getString("name"), res.getString("uuid")), res.getInt("id"));//将对象插入临时的表中
        }

        this.idPlayerMap.clear();//清空原有的表
        this.idPlayerMap = id_player;//将引用赋值给对照表

        this.playerIdMap.clear();
        this.playerIdMap = player_id;

    }

    protected synchronized void FlushActionMap() throws SQLException {

        String query_str = String.format("SELECT * FROM %s;", ACTION_MAP_TABLE_NAME);//构建查询语句
        Statement stmt = this.mConn.createStatement();//创建查询
        ResultSet res = stmt.executeQuery(query_str);//执行查询

        HashMap<String, Integer> action_id = new HashMap<>();//创建一个临时的表,如果发生了意外可以保证原有的表不损坏
        HashMap<Integer, String> id_action = new HashMap<>();

        while (res.next())//遍历结果集
        {
            id_action.put(res.getInt("id"), res.getString("action"));//将对象插入临时的表中
            action_id.put(res.getString("action"), res.getInt("id"));//将对象插入临时的表中
        }

        this.idActionMap.clear();//清空原有的表
        this.idActionMap = id_action;//将引用赋值给对照表

        this.actionIdMap.clear();
        this.actionIdMap = action_id;

    }

    protected synchronized void FlushItemMap() throws SQLException {

        String query_str = String.format("SELECT * FROM %s;", ITEM_MAP_TABLE_NAME);//构建查询语句
        Statement stmt = this.mConn.createStatement();//创建查询
        ResultSet res = stmt.executeQuery(query_str);//执行查询

        HashMap<String, Integer> item_id = new HashMap<>();//创建一个临时的表,如果发生了意外可以保证原有的表不损坏
        HashMap<Integer, String> id_item = new HashMap<>();

        while (res.next())//遍历结果集
        {
            id_item.put(res.getInt("id"), res.getString("item"));//将对象插入临时的表中
            item_id.put(res.getString("item"), res.getInt("id"));//将对象插入临时的表中
        }

        this.idItemMap.clear();//清空原有的表
        this.idItemMap = id_item;//将引用赋值给对照表

        this.itemIdMap.clear();
        this.itemIdMap = item_id;

    }

    protected synchronized void FlushDimensionMap() throws SQLException {

        String query_str = String.format("SELECT * FROM %s;", DIMENSION_MAP_TABLE_NAME);//构建查询语句
        Statement stmt = this.mConn.createStatement();//创建查询
        ResultSet res = stmt.executeQuery(query_str);//执行查询

        HashMap<String, Integer> dimension_id = new HashMap<>();//创建一个临时的表,如果发生了意外可以保证原有的表不损坏
        HashMap<Integer, String> id_dimension = new HashMap<>();

        while (res.next())//遍历结果集
        {
            id_dimension.put(res.getInt("id"), res.getString("dimension"));//将对象插入临时的表中
            dimension_id.put(res.getString("dimension"), res.getInt("id"));//将对象插入临时的表中
        }

        this.idDimensionMap.clear();//清空原有的表
        this.idDimensionMap = id_dimension;//将引用赋值给对照表

        this.dimensionIdMap.clear();
        this.dimensionIdMap = dimension_id;

    }

    protected void FlushEntityMap() throws SQLException {
        String query_str = String.format("SELECT * FROM %s;", ENTITY_MAP_TABLE_NAME);//构建查询语句
        Statement stmt = this.mConn.createStatement();//创建查询
        ResultSet res = stmt.executeQuery(query_str);//执行查询

        HashMap<String, Integer> entity_id = new HashMap<>();//创建一个临时的表,如果发生了意外可以保证原有的表不损坏
        HashMap<Integer, String> id_entity = new HashMap<>();

        while (res.next())//遍历结果集
        {
            id_entity.put(res.getInt("id"), res.getString("entity"));//将对象插入临时的表中
            entity_id.put(res.getString("entity"), res.getInt("id"));//将对象插入临时的表中
        }

        this.idEntityMap.clear();//清空原有的表
        this.idEntityMap = id_entity;//将引用赋值给对照表

        this.entityIdMap.clear();
        this.entityIdMap = entity_id;
    }

    protected int GetOrCreateActionMap(String action) throws SQLException {
        if (!this.actionIdMap.containsKey(action))//表中没有这个数据
        {
            int id = this.actionIdMap.size() + 1;//计算出新的对照的id
            this.insert_action_map.setString(1, action);  // 设置 action 参数值
            this.insert_action_map.setInt(2, id);             // 设置 id 参数值
            this.insert_action_map.execute();//执行更新

            FlushActionMap();//更新玩家行为更新的表
            return id;
        } else {
            return this.actionIdMap.get(action);
        }
    }


    protected synchronized int GetOrCreateDimensionMap(String dimension) throws SQLException {
        if (!this.dimensionIdMap.containsKey(dimension))//表中没有这个数据
        {
            int id = this.dimensionIdMap.size() + 1;//计算出新的对照的id
            this.insert_dimension_map.setString(1, dimension);  // 设置 action 参数值
            this.insert_dimension_map.setInt(2, id);             // 设置 id 参数值
            this.insert_dimension_map.execute();//执行更新

            FlushDimensionMap();//更新维度映射
            return id;
        } else {
            return this.dimensionIdMap.get(dimension);
        }
    }

    protected synchronized int GetOrCreatePlayerMap(Player player) throws SQLException {

        if (!this.playerIdMap.containsKey(player))//表中没有这个数据
        {
            int id = this.playerIdMap.size() + 1;//计算出新的对照的id

            this.insert_player_map.setString(1, player.UUID);  // 设置 uuid 参数值
            this.insert_player_map.setString(2, player.name);  // 设置 name 参数值
            this.insert_player_map.setInt(3, id);
            this.insert_player_map.execute();//执行更新指令

            FlushPlayerMap();//更新玩家映射
            return id;
        } else {
            return this.playerIdMap.get(player);
        }
    }

    protected synchronized int GetOrCreateItemMap(String item) throws SQLException {
        if (!this.itemIdMap.containsKey(item))//表中没有这个数据
        {
            int id = this.itemIdMap.size() + 1;//计算出新的对照的id
            this.insert_item_map.setString(1, item);  // 设置 action 参数值
            this.insert_item_map.setInt(2, id);             // 设置 id 参数值
            this.insert_item_map.execute();//执行更新

            FlushItemMap();//更新物品映射表
            return id;
        } else {
            return this.itemIdMap.get(item);
        }
    }

    protected int GetOrCreateEntityMap(String entity) throws SQLException {
        if (!this.entityIdMap.containsKey(entity))//表中没有这个数据
        {
            int id = this.entityIdMap.size() + 1;//计算出新的对照的id
            this.insert_entity_map.setString(1, entity);  // 设置 action 参数值
            this.insert_entity_map.setInt(2, id);             // 设置 id 参数值
            this.insert_entity_map.execute();//执行更新

            FlushEntityMap();//更新实体映射
            return id;
        } else {
            return this.entityIdMap.get(entity);
        }
    }

    protected synchronized Player GetPlayerById(int player_id) throws Exception {
        return this.idPlayerMap.get(player_id);
    }

    protected synchronized String GetEntityById(int entity_id) throws Exception {
        return this.idEntityMap.get(entity_id);
    }

    protected synchronized String GetItemById(int item_id) throws Exception {
        return this.idItemMap.get(item_id);
    }

    protected synchronized String GetDimensionById(int dimension_id) throws Exception {
        return this.idDimensionMap.get(dimension_id);
    }

    protected synchronized String GetActionById(int action_id) throws Exception {
        return this.idActionMap.get(action_id);
    }

    protected abstract void CheckAndFixDataBaseStructure() throws Exception;//检查数据库的表的结构,如果表不符合要求,则修复表

    protected String GetObjByActionAndId(String action, int obj_id) throws Exception {
        switch (action)//判断是哪一种类型
        {
            case BLOCK_PLACE, BLOCK_BREAK_ACTION_NAME:
                return GetItemById(obj_id);
            case ATTACK_ACTION_NAME:
                return GetEntityById(obj_id);
            default:
                throw new RuntimeException("Could not find the action.");
        }
    }

    public synchronized void WriteActionToDb(Action action) throws Exception {//将玩家的行为写入数据库
        int action_id = GetOrCreateActionMap(action.actionType);//获取玩家的行为的ID
        int target_id;//目标的id(玩家放置的方块ID,玩家攻击的实体id等)
        int player_id = GetOrCreatePlayerMap(action.player);//
        int dimension_id = GetOrCreateDimensionMap(action.dimension);

        target_id = switch (action.actionType) {//判断玩家的行为的类型
            case Action.BLOCK_BREAK_ACTION_NAME, BLOCK_PLACE ->//方块破坏事件或者方块使用事件则直接获取方块的id
                    GetOrCreateItemMap(action.targetName);//获取方块的id
            //获取方块id
            case Action.ATTACK_ACTION_NAME ->//实体攻击事件
                    GetOrCreateEntityMap(action.targetName);//获取实体ID
            default ->//如果无法找到行为的映射则直接抛出异常
                    throw new Exception("Could not get the map of the type of the action.");
        };

        this.insert_action.setInt(1, player_id);          // 设置 player 参数值
        this.insert_action.setInt(2, action_id);          // 设置 action 参数值
        this.insert_action.setInt(3, target_id);          // 设置 target 参数值
        this.insert_action.setLong(4, action.time);         // 设置 time 参数值
        this.insert_action.setInt(6, action.posX);         // 设置 x 参数值
        this.insert_action.setInt(7, action.posY);         // 设置 y 参数值
        this.insert_action.setInt(8, action.posZ);         // 设置 z 参数值
        this.insert_action.setInt(9, dimension_id);         //设置dimension参数值

        if (action.actionData != null)//判断玩家的操作是否包含了额外数据
        {
            this.insert_action.setString(5, action.actionData); // 设置 data 参数
        } else {
            this.insert_action.setNull(5, Types.VARCHAR);//如果行为的额外数据为空,则设置为空
        }
        this.insert_action.execute();//执行插入数据
    }

    public int GetPointActionCount(QueryTask query_task) throws SQLException {//获取点的玩家行为的数量
        int dimension_id = GetOrCreateDimensionMap(query_task.dimensionName);//获取维度的映射id
        this.query_point_action_count.setInt(1, query_task.x);      // 替换为指定的x值
        this.query_point_action_count.setInt(2, query_task.y);      // 替换为指定的y值
        this.query_point_action_count.setInt(3, query_task.z);      // 替换为指定的z值
        this.query_point_action_count.setInt(4, dimension_id);       // 替换为指定的dimension值

        ResultSet res = this.query_point_action_count.executeQuery();//执行查询
        if (res.next()) {
            return res.getInt("count");//返回一共有几个结果
        }
        return 0;
    }

    public ArrayList<Action> GetPointAction(QueryTask query_task) throws Exception {//获取点玩家的行为
        int start_pos = query_task.Max_PAGE_AMOUNT * (query_task.pageId - 1);
        int dimension_id = this.GetOrCreateDimensionMap(query_task.dimensionName);//获取维度的名字

        this.query_point_action.setInt(1, query_task.x);      // 替换为指定的x值
        this.query_point_action.setInt(2, query_task.y);      // 替换为指定的y值
        this.query_point_action.setInt(3, query_task.z);      // 替换为指定的z值
        this.query_point_action.setInt(4, dimension_id);       // 替换为指定的dimension值
        this.query_point_action.setInt(5, start_pos);        // 结果起始索引（第一行的索引为0），替换为你需要的范围
        this.query_point_action.setInt(6, query_task.Max_PAGE_AMOUNT);       // 结果数量，替换为你需要的范围大小

        ResultSet res = query_point_action.executeQuery();
        ArrayList<Action> temp = new ArrayList<>();
        while (res.next())//遍历结果集
        {
            Player player = GetPlayerById(res.getInt("player"));
            String action = GetActionById(res.getInt("action"));
            String dimension_name = GetDimensionById(res.getInt("dimension"));
            String obj_name = GetObjByActionAndId(action, res.getInt("target"));

            Action action_temp = new Action(action, player, obj_name,
                    res.getInt("x"), res.getInt("y"), res.getInt("z"), dimension_name, res.getString("data"));
            action_temp.time = res.getLong("time");
            temp.add(action_temp);
        }
        return temp;//返回结果
    }

    public int GetAreaActionCount(QueryTask query_task) throws Exception {//获取区域内所有行为的数量

        this.Query_area_action_count.setInt(1, query_task.x - query_task.MAX_AREA_QUERY_AREA_RADIUS);
        this.Query_area_action_count.setInt(2, query_task.x + query_task.MAX_AREA_QUERY_AREA_RADIUS);
        this.Query_area_action_count.setInt(3, query_task.y - query_task.MAX_AREA_QUERY_AREA_RADIUS);
        this.Query_area_action_count.setInt(4, query_task.y + query_task.MAX_AREA_QUERY_AREA_RADIUS);
        this.Query_area_action_count.setInt(5, query_task.z - query_task.MAX_AREA_QUERY_AREA_RADIUS);
        this.Query_area_action_count.setInt(6, query_task.z + query_task.MAX_AREA_QUERY_AREA_RADIUS);

        int dimension_id = GetOrCreateDimensionMap(query_task.dimensionName);//获取维度的映射id
        this.Query_area_action_count.setInt(7, dimension_id);

        ResultSet res = this.Query_area_action_count.executeQuery();//执行查询

        if (res.next()) {
            return res.getInt("count");//返回结果
        }
        return 0;
    }

    public ArrayList<Action> GetAreaAction(QueryTask query_task) throws Exception {//获取区域内所有行为的数量
        int start_pos = query_task.Max_PAGE_AMOUNT * (query_task.pageId - 1);

        this.query_area_action.setInt(1, query_task.x - query_task.MAX_AREA_QUERY_AREA_RADIUS);
        this.query_area_action.setInt(2, query_task.x + query_task.MAX_AREA_QUERY_AREA_RADIUS);
        this.query_area_action.setInt(3, query_task.y - query_task.MAX_AREA_QUERY_AREA_RADIUS);
        this.query_area_action.setInt(4, query_task.y + query_task.MAX_AREA_QUERY_AREA_RADIUS);
        this.query_area_action.setInt(5, query_task.z - query_task.MAX_AREA_QUERY_AREA_RADIUS);
        this.query_area_action.setInt(6, query_task.z + query_task.MAX_AREA_QUERY_AREA_RADIUS);
        this.query_area_action.setInt(8, start_pos);
        this.query_area_action.setInt(9, query_task.Max_PAGE_AMOUNT);

        int dimension_id = GetOrCreateDimensionMap(query_task.dimensionName);//获取维度的映射id
        this.query_area_action.setInt(7, dimension_id);

        ResultSet res = this.query_area_action.executeQuery();//执行查询
        ArrayList<Action> temp = new ArrayList<>();

        while (res.next())//遍历结果集
        {
            Player player = GetPlayerById(res.getInt("player"));
            String action = GetActionById(res.getInt("action"));
            String dimension_name = GetDimensionById(res.getInt("dimension"));
            String obj_name = GetObjByActionAndId(action, res.getInt("target"));

            Action action_temp = new Action(action, player, obj_name,
                    res.getInt("x"), res.getInt("y"), res.getInt("z"), dimension_name, res.getString("data"));
            action_temp.time = res.getLong("time");
            temp.add(action_temp);
        }
        return temp;//返回结果
    }

    public abstract void ConnectToDataBase() throws Exception;//连接到数据库
}
