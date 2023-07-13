package com.thestarryguard.thestarryguard.Command;

import com.thestarryguard.thestarryguard.CommandMgr;
import com.thestarryguard.thestarryguard.ModInfo;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.text.Text;
import static net.minecraft.server.command.CommandManager.literal;

public class QueryVer {//查询数据的指令

    public void RegVerInfoCommand()//注册显示模组信息的命令
    {
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) ->
                        dispatcher.register(literal(CommandMgr.COMMAND_PREFIX)
                                .executes(context -> {
                                        context.getSource().sendMessage(Text.literal(
                                                String.format("TheStarryGuard v%s,by:%s.\nBuild date %s",
                                                        ModInfo.MOD_VERSION,ModInfo.AUTHOR,ModInfo.BUILD_DATE)));
                                            return 1;
                                        }
                                )
                        )
                )
        );

    }
}
