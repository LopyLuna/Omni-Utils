package uwu.lopyluna.omni_util.content.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import uwu.lopyluna.omni_util.content.power.PowerManager;

public class DebugPowerCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("debugRadiantPower")
                .executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                    sendRPDebugMessage(player);
                    return 1;
                })
        );
        dispatcher.register(Commands.literal("debugResetRadiantPower")
                .executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                    PowerManager.resetRP(player);
                    player.displayClientMessage(Component.literal("🔄 RP values reset!").withStyle(ChatFormatting.YELLOW), false);
                    return 1;
                })
        );
    }

    private static void sendRPDebugMessage(ServerPlayer player) {
        int generated = PowerManager.getGeneratedRP(player);
        int consumed = PowerManager.getConsumedRP(player);
        int netRP = PowerManager.getNetRP(player);

        player.displayClientMessage(Component.literal("🔹 Radiant Power (RP) Debug Info 🔹").withStyle(ChatFormatting.AQUA), false);
        player.displayClientMessage(Component.literal("⚡ Generated RP: " + generated).withStyle(ChatFormatting.GREEN), false);
        player.displayClientMessage(Component.literal("🔥 Consumed RP: " + consumed).withStyle(ChatFormatting.RED), false);
        player.displayClientMessage(Component.literal("🔋 Net RP: " + netRP).withStyle(ChatFormatting.YELLOW), false);

        if (netRP < 0) player.displayClientMessage(Component.literal("⚠ Warning: You are in RP debt! ⚠").withStyle(ChatFormatting.DARK_RED), false);
    }
}