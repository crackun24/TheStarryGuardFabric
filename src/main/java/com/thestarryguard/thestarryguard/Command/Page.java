package com.thestarryguard.thestarryguard.Command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.thestarryguard.thestarryguard.CommandMgr;
import com.thestarryguard.thestarryguard.Operation.DataQuery;
import com.thestarryguard.thestarryguard.Lang;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class Page {
    private DataQuery mDataQuery;
    private Lang mLang;

    public void RegQueryPointCommand(int permission_level) {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
                literal(CommandMgr.COMMAND_PREFIX).requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(permission_level))
                        .then(literal("page")
                                .then(argument("page", IntegerArgumentType.integer())
                                        .executes(context -> {
                                            // 在这里执行您的操作
                                            try {
                                                ServerPlayerEntity player = context.getSource().getPlayer();
                                                if (player == null) {
                                                    return 1;//如果无法获取到玩家对象,则直接返回函数
                                                }
                                                String player_name = player.getName().getString();//获取玩家的名字
                                                if (player_name.isEmpty())//无法获取玩家的UUID则直接返回
                                                    return 1;
                                                int page = IntegerArgumentType.getInteger(context, "page");//获取玩家输入的页数
                                                this.mDataQuery.AddPageQuery(player_name, page);//将玩家的请求添加

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                context.getSource().sendMessage(Text.literal(this.mLang.ERROR_MSG));
                                            }
                                            return 1;
                                        })
                                )
                        )));
    }

    public Page(DataQuery data_query, Lang lang) {
        this.mDataQuery = data_query;
        this.mLang = lang;
    }
}
