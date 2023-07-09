package com.thestarryguard.thestarryguard.DataType;

public class Player {//玩家类
    public String UUID;
    public String name;

    public Player(String name,String uuid) {//构造函数
        this.UUID = uuid;
        this.name = name;
    }
}
