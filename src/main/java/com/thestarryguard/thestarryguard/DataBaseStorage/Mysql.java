package com.thestarryguard.thestarryguard.DataBaseStorage;

import com.thestarryguard.thestarryguard.DataType.Action;

public class Mysql extends DataBase {

    private String mURL;//mysql连接的地址

    public Mysql(String url) {
        this.mURL = url;
    }

    @Override
    protected void VerifyDbConnection() {

    }

    @Override
    protected String GetPlayerUUIDByMap(int map_id) {
        return null;
    }

    @Override
    protected String GetPlayerNameByMap(int map_id) {
        return null;
    }

    @Override
    protected String GetActionByMap(Action.ActionType action) {
        return null;
    }

    @Override
    protected String GetBlockByMap(int map_id) {
        return null;
    }

    @Override
    protected int GetOrCreateActionMap(Action.ActionType action) {
        return 0;
    }

    @Override
    protected int GetOrCreatePlayerMap(String player_UUID, String player_name) {
        return 0;
    }

    @Override
    protected int GetOrCreateBlockMap(String block_id) {
        return 0;
    }

    @Override
    protected void WriteBlockBreakEvent(Action action) {

    }

    @Override
    protected void WriteBlockPlaceEvent(Action action) {

    }

    @Override
    public void CheckDbConnection() {

    }

    @Override
    public void WriteActionToDb(Action action) {

    }
}
