package com.thestarryguard.thestarryguard.DataType;

public class Action {//玩家的行为类
    public static final String ATTACK_ACTION_NAME = "attack";
    public static final String BLOCK_PLACE = "block_place";
    public static final String BLOCK_BREAK_ACTION_NAME = "block_break";
    public static final String KILL_ENTITY_ACTION_NAME = "kill_entity";
    public static final String KILL_PLAYER_ACTION_NAME = "kill_player";

    public Player player;
    public int posX;
    public int posY;
    public int posZ;
    public String dimension;//玩家触发这个事件的维度
    public String targetName;
    public String actionData;//事件的数据
    public long time;//触发事件的时间
    public String actionType;

    public Action(String action_type, Player player, String target_name, int x, int y, int z, String dimension, String action_data) {
        this.player = player;
        this.posX = x;
        this.posY = y;
        this.posZ = z;//设置玩家影响的目标的坐标
        this.dimension = dimension;//设置玩家触发事件的维度
        this.actionType = action_type;
        this.targetName = target_name;//设置玩家所触发的事件影响的目标的名称(方块的ID,实体的类型等)
        this.actionData = action_data;//事件的数据,可能为空
        this.time = System.currentTimeMillis() / 1000;//设置时间触发的时间
    }
}
