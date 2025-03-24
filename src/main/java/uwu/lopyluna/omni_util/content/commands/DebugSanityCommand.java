package uwu.lopyluna.omni_util.content.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import uwu.lopyluna.omni_util.content.managers.SanityManager;

import static uwu.lopyluna.omni_util.content.managers.SanityManager.SANITY;

public class DebugSanityCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("debugSanity")
                .executes(ctx -> sendDebugMessage(ctx.getSource().getPlayerOrException()))
        );

        dispatcher.register(Commands.literal("debugAdjustSanity").then(Commands.argument("value", FloatArgumentType.floatArg())
                .executes(ctx -> { float i = FloatArgumentType.getFloat(ctx, "value");
                    var player = ctx.getSource().getPlayerOrException();

                    SanityManager.adjustSanity(player, i);
                    return 1;
                })
        ));
        dispatcher.register(Commands.literal("debugSetSanity").then(Commands.argument("value", FloatArgumentType.floatArg())
                .executes(ctx -> { float i = FloatArgumentType.getFloat(ctx, "value");
                    var player = ctx.getSource().getPlayerOrException();

                    player.getPersistentData().putFloat(SANITY, i);
                    return 1;
                })
        ));
    }

    @SuppressWarnings("all")
    private static int sendDebugMessage(ServerPlayer player) {
        if (player == null) return 1;
        int sanity = (int) SanityManager.getSanity(player);

        player.displayClientMessage(Component.literal("⚡ Sanity: " + sanity + " / 100").withStyle(ChatFormatting.DARK_PURPLE), false);
        return 1;
    }
}