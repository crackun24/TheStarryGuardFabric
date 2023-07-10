package com.thestarryguard.thestarryguard;

import com.thestarryguard.thestarryguard.DataBaseStorage.DataBase;
import com.thestarryguard.thestarryguard.DataType.QueryTask;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.*;

public class DataQuery extends Thread {//数据查询类
    private HashMap<String, Byte> mPointQueryPlayer;//启用了点方块查询的玩家的哈希表,第二个数值无用
    private TheStarryGuard mMain;//主线程对象
    private HashMap<String, QueryTask> mPlayerLastTask;//玩家上一次查询的任务对照,方便进行翻页的操作
    private Queue<QueryTask> mQueryTask;//查询任务的玩家
    private Boolean mCloseState;//主线程的关闭状态
    private DataQuery()//构造函数
    {

    }

    private DataBase mDataBase;

    private synchronized void DoPointTask(QueryTask task) throws Exception//完成点的人物
    {
        int amount = this.mDataBase.GetPointActionCount(task);
        System.out.println(amount);
        this.mMain.SendMsgToPlayer(task.senderName,Text.literal(Integer.toString(amount)));
    }

    private synchronized void DoTask() throws Exception//完成任务列表中的人物
    {
        while (!this.mQueryTask.isEmpty())//如果不是则一直循环
        {
            QueryTask task = this.mQueryTask.poll();//弹出位于第一个的任务
            switch (task.queryType)//判断查询的类型
            {
                case POINT:
                    DoPointTask(task);
                    break;
                case AREA:
                    break;
                default://如果是未知的类型
                    throw new IllegalStateException("Unexpected value: " + task.queryType);
            }
        }
    }

    private Boolean GetCloseState()//获取关闭状态
    {
        return this.mCloseState;
    }

    @Override
    public void run()//启动线程
    {
        while (!this.mCloseState)//主线程未关闭则一直运行查询线程
        {
            try {
                sleep(1000);
                DoTask();//完成任务
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized Boolean IsPlayerHookPointQuery(String player_uuid)//判断玩家是否启用了点方块查询的指令
    {
        return this.mPointQueryPlayer.containsKey(player_uuid);//判断是否含有这个键,如果有则直接返回true
    }

    public synchronized void HookPlayerPointQuery(String player_uuid)//启用玩家的点查询
    {
        this.mPointQueryPlayer.put(player_uuid, null);//插入查询
    }

    public synchronized void UnHookPlayerPointQuery(String player_uuid)//关闭玩家的点查询
    {
        this.mPointQueryPlayer.remove(player_uuid);//删除查询
    }

    public synchronized void AddQueryTask(QueryTask query_task)//添加查询任务到队列中
    {
        this.mQueryTask.add(query_task);//将查询任务添加进队列中
    }

    public synchronized void CloseDataQuery()//关闭查询线程
    {
        this.mCloseState = true;//设置关闭状态成立
    }

    static public DataQuery GetDataQuery(DataBase data_base,TheStarryGuard main) {//创建一个data_query对象
        DataQuery temp = new DataQuery();
        temp.mDataBase = data_base;//设置使用的数据库
        temp.mPointQueryPlayer = new HashMap<>();//初始化哈希表
        temp.mQueryTask = new LinkedList<>();//初始化查询任务的队列
        temp.mCloseState = false;
        temp.mMain = main;
        return temp;//返回构造好的对象
    }
}
