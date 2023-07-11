package com.thestarryguard.thestarryguard.Command;

import com.thestarryguard.thestarryguard.DataQuery;
import com.thestarryguard.thestarryguard.Lang;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import static net.minecraft.server.command.CommandManager.literal;

public class QueryPoint {//查询点的指令
    private DataQuery mDataQuery;
    private Lang mLang;//语言对象

    public void RegQueryPointCommand() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
                literal("TSGuard")
                        .then(literal("check")
                                .executes(context -> {
                                    // 在这里执行您的操作
                                    try {
                                        ServerPlayerEntity player = context.getSource().getPlayer();
                                        if (player == null)//判断是否可以正常获取玩家对象
                                        {
                                            return -1;

                                        }
                                        String player_name = player.getName().getString();//获取玩家的名字
                                        if (player_name.isEmpty())//无法获取玩家的UUID则直接返回
                                        {
                                            return 1;
                                        }
                                        if (!this.mDataQuery.IsPlayerHookPointQuery(player_name))//玩家没有启用查询
                                        {
                                            this.mDataQuery.HookPlayerPointQuery(player_name);//注册玩家
                                            context.getSource().sendMessage(Text.literal(this.mLang.ENABLE_QUERY));
                                        } else {//玩家启用了查询
                                            this.mDataQuery.UnHookPlayerPointQuery(player_name); //取消玩家的注册
                                            context.getSource().sendMessage(Text.literal(this.mLang.DISABLE_QUERY));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        context.getSource().sendMessage(Text.literal(this.mLang.ERROR_MSG));
                                    }

                                    return 1;
                                })
                        )
        ));
    }

    public QueryPoint(DataQuery data_query,Lang lang) {
        this.mDataQuery = data_query;
        this.mLang = lang;
    }
}

