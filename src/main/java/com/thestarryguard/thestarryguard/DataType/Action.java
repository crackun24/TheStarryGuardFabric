package com.thestarryguard.thestarryguard.DataType;

public class Action {//玩家的行为类

    public enum ActionType {BLOCK_BREAK, BLOCK_USE, ATTACK_ENTITY}//玩家行为的枚举

    public enum TargetType {ENTITY, PLAYER, BLOCK}//目标的类型

    public final ActionType actionType;
    public final String playerUUID;//发出行为的玩家的UUID
    public final String targetName;//玩家行为影响的目标名字
    public final int posX;
    public final int posY;
    public final int posZ;
    public final String dimension;//玩家触发这个事件的维度
    public final TargetType targetType;//事件的类型
    public final String eventData;//事件的数据
    public final long time;//触发事件的时间

    public Action(ActionType type, TargetType targetType, String playerUUID, String targetName, int x, int y, int z, String dimension, String eventData) {
        this.actionType = type;//设置玩家行为的类型
        this.playerUUID = playerUUID;//设置发出行为的玩家的UUID
        this.targetName = targetName;//设置玩家的行为影响的目标的名字
        this.posX = x;
        this.posY = y;
        this.posZ = z;//设置玩家影响的目标的坐标
        this.dimension = dimension;//设置玩家触发事件的维度
        this.targetType = targetType;//设置玩家所触发事件影响到的目标的类型
        this.eventData = eventData;//事件的数据,可能为空
        this.time = System.currentTimeMillis()/1000;//设置时间触发的时间
    }
}
