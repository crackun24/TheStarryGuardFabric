package com.thestarryguard.thestarryguard.Command;

import com.thestarryguard.thestarryguard.DataQuery;
import com.thestarryguard.thestarryguard.DataType.Player;
import com.thestarryguard.thestarryguard.DataType.QueryTask;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.Text;

import java.util.HashMap;

import static net.minecraft.server.command.CommandManager.literal;

public class QueryPoint {//查询点的指令
    private DataQuery mDataQuery;

    public void RegQueryPointCommand() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
                literal("TSGuard")
                        .then(literal("check")
                                .executes(context -> {
                                    // 在这里执行您的操作
                                    try {
                                        ServerPlayerEntity player = context.getSource().getPlayer();
                                        String player_uuid = player.getUuidAsString();//获取玩家的uuid
                                        if (player_uuid.isEmpty())//无法获取玩家的UUID则直接返回
                                            return 1;
                                        if (!this.mDataQuery.IsPlayerHookPointQuery(player_uuid))//玩家没有启用查询
                                        {
                                            this.mDataQuery.HookPlayerPointQuery(player_uuid);//注册玩家
                                            context.getSource().sendMessage(Text.literal("§2Enable point check."));
                                        } else {//玩家启用了查询
                                            this.mDataQuery.UnHookPlayerPointQuery(player_uuid); //取消玩家的注册
                                            context.getSource().sendMessage(Text.literal("§cDisable point check."));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        context.getSource().sendMessage(Text.literal("§cInternal error."));
                                    }

                                    return 1;
                                })
                        )
        ));
    }

    public QueryPoint(DataQuery data_query) {
        this.mDataQuery = data_query;
    }
}

