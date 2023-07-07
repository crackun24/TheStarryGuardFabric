package com.thestarryguard.thestarryguard.DataType;

import net.minecraft.entity.player.PlayerEntity;

enum ActionType {BLOCK_BREAK, BLOCK_PLACE, ATTACK};

abstract class Action {//玩家的行为抽象类
    public final ActionType actionType;
    public final String playerUUID;//发出行为的玩家的UUID
    public final String targetName;//玩家行为影响的目标名字
    public final int posX;
    public final int posY;
    public final int posZ;
    public Action(ActionType type, String playerUUID,String targetName,int x,int y,int z) {
        this.actionType = type;//设置玩家行为的类型
        this.playerUUID = playerUUID;//设置发出行为的玩家的UUID
        this.targetName = targetName;//设置玩家的行为影响的目标的名字
        this.posX = x;
        this.posY = y;
        this.posZ = z;//设置玩家影响的目标的坐标
    }
}
