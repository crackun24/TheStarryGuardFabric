package com.thestarryguard.thestarryguard.DataType;

import java.util.HashMap;
import java.util.Objects;

public class Player {//玩家类
    public String UUID;
    public String name;

    @Override
    public int hashCode() {
        return this.UUID.hashCode();//使用uuid作为哈希值计算对象
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Player)) {
            return false;
        }
        Player temp = (Player) obj;
        return this.UUID.equals(temp.UUID);
    }


    public Player(String name, String uuid) {//构造函数
        this.UUID = uuid;
        this.name = name;
    }
}
