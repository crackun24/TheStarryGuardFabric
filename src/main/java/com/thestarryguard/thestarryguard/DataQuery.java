package com.thestarryguard.thestarryguard;

import com.thestarryguard.thestarryguard.DataBaseStorage.DataBase;
import com.thestarryguard.thestarryguard.DataType.Action;
import com.thestarryguard.thestarryguard.DataType.QueryTask;
import net.minecraft.text.Text;

import java.util.*;

public class DataQuery extends Thread {//数据查询类
    private HashMap<String, Byte> mPointQueryPlayer;//启用了点方块查询的玩家的哈希表,第二个数值无用
    private TheStarryGuard mMain;//主线程对象
    private HashMap<String, QueryTask> mPlayerLastTask;//玩家上一次查询的任务对照,方便进行翻页的操作
    private Queue<QueryTask> mQueryTask;//查询任务的玩家
    private Boolean mCloseState;//主线程的关闭状态
    private Lang mLang;//语言文件

    private DataQuery()//构造函数
    {

    }

    private DataBase mDataBase;

    private synchronized void DoTask() throws Exception//完成任务列表中的任务
    {
        if (this.mQueryTask.isEmpty())//如果任务队列为空,则直接返回
        {
            return;
        }
        QueryTask task = this.mQueryTask.poll();//弹出位于第一个的任务
        ArrayList<Action> action_list;//玩家的行为列表
        int total_page;//总共的页数
        int total_entries;//总共的条目数

        if (task.pageId <= 0) {
            this.mMain.SendMsgToPlayer(task.senderName, Text.literal(this.mLang.ILLEGAL_PAGE));//发送错误消息给玩家
            return;
        }


        switch (task.queryType)//判断查询的类型
        {
            case POINT:
                total_entries = this.mDataBase.GetPointActionCount(task);//获取符合要求的行为的数量
                total_page = total_entries % task.Max_PAGE_AMOUNT == 0 ? total_entries / task.Max_PAGE_AMOUNT : total_entries / task.Max_PAGE_AMOUNT + 1;

                if (total_entries == 0) {
                    this.mMain.SendMsgToPlayer(task.senderName, Text.literal(this.mLang.NO_DATA));//发送错误消息给玩家
                    return;
                }
                if (task.pageId > total_page) {//判断玩家查询的页数是否大于最大的页数
                    this.mMain.SendMsgToPlayer(task.senderName, Text.literal(this.mLang.INVALID_PAGE));//发送错误消息给玩家
                    return;
                }

                action_list = this.mDataBase.GetPointAction(task);//获取点的所有行为
                break;

            case AREA:
                total_entries = this.mDataBase.GetAreaActionCount(task);//获取符合要求的行为的数量
                total_page = total_entries % task.Max_PAGE_AMOUNT == 0 ? total_entries / task.Max_PAGE_AMOUNT : total_entries / task.Max_PAGE_AMOUNT + 1;

                if (total_entries == 0) {
                    this.mMain.SendMsgToPlayer(task.senderName, Text.literal(this.mLang.NO_DATA));//发送错误消息给玩家
                    return;
                }

                if (task.pageId > total_page) {//判断玩家查询的页数是否大于最大的页数
                    this.mMain.SendMsgToPlayer(task.senderName, Text.literal(this.mLang.INVALID_PAGE));//发送错误消息给玩家
                    return;
                }

                action_list = this.mDataBase.GetAreaAction(task);//获取点的所有行为
                break;
            default://如果是未知的类型
                throw new IllegalStateException("Unexpected value: " + task.queryType);
        }

        StringBuilder msg_to_send = new StringBuilder(String.format(this.mLang.PAGE_TITLE,
                Integer.toString(total_entries), Integer.toString(total_entries == 0 ? 0 : task.pageId),
                Integer.toString(total_page)));//发送给玩家的消息

        long time = System.currentTimeMillis() / 1000;

        for (Action action : action_list)//遍历返回的结果集
        {
            long delta_time = time - action.time;//获取时间差
            String entry = String.format(this.mLang.PAGE_ENTRY, action.player.name, action.actionType, action.targetName,
                    Tool.GetDateLengthDes(delta_time));
            msg_to_send.append(entry);
        }

        this.mPlayerLastTask.put(task.senderName, task);//放入玩家与上一个任务的映射中

        this.mMain.SendMsgToPlayer(task.senderName, Text.literal(msg_to_send.toString()));//默认发送第一页的内容
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
        try {
            DoTask();//完成最后的任务
        } catch (Exception e) {
           e.printStackTrace();
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

    public synchronized void AddPageQuery(String player_name, int page)//有页数的查询
    {
        if (!this.mPlayerLastTask.containsKey(player_name)) //判断是否有这个玩家的上一次请求
        {
            this.mMain.SendMsgToPlayer(player_name, Text.literal(this.mLang.INVALID_PAGE));
            return;
        }
        QueryTask task = this.mPlayerLastTask.get(player_name);//获取玩家的上一次的人物
        task.pageId = page;//改写原来的页数

        this.mQueryTask.add(task);//将改写后的任务添加进队列中
    }

    static public DataQuery GetDataQuery(DataBase data_base, TheStarryGuard main, Lang lang) {//创建一个data_query对象
        DataQuery temp = new DataQuery();
        temp.mDataBase = data_base;//设置使用的数据库
        temp.mPointQueryPlayer = new HashMap<>();//初始化哈希表
        temp.mQueryTask = new LinkedList<>();//初始化查询任务的队列
        temp.mPlayerLastTask = new HashMap<>();//初始化玩家的上一个哈希表
        temp.mCloseState = false;
        temp.mMain = main;
        temp.mLang = lang;
        return temp;//返回构造好的对象
    }
}
