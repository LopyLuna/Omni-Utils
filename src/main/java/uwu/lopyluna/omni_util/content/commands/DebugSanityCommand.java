package uwu.lopyluna.omni_util.content.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import uwu.lopyluna.omni_util.content.managers.SanityManager;

public class DebugSanityCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("debugSanity")
                .executes(ctx -> sendDebugMessage(ctx.getSource().getPlayerOrException()))
        );
    }

    @SuppressWarnings("all")
    private static int sendDebugMessage(ServerPlayer player) {
        if (player == null) return 1;
        int sanity = (int) SanityManager.getSanity(player);

        player.displayClientMessage(Component.literal("⚡ Sanity: " + sanity + " / 100").withStyle(ChatFormatting.DARK_PURPLE), false);
        return 1;
    }
}