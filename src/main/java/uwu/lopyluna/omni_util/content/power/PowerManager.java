package uwu.lopyluna.omni_util.content.power;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import uwu.lopyluna.omni_util.network.SyncRPToClientPacket;

public class PowerManager {
    private static final String RP_GENERATED = "rp_generated";
    private static final String RP_CONSUMED = "rp_consumed";

    public static void resetGeneratedRP(ServerPlayer player) {
        player.getPersistentData().putInt(RP_GENERATED, 0);
    }

    public static int getGeneratedRP(ServerPlayer player) {
        return player.getPersistentData().getInt(RP_GENERATED);
    }

    public static void resetConsumedRP(ServerPlayer player) {
        player.getPersistentData().putInt(RP_CONSUMED, 0);
    }

    public static int getConsumedRP(ServerPlayer player) {
        return player.getPersistentData().getInt(RP_CONSUMED);
    }

    public static void addGeneratedRP(ServerPlayer player, int amount) {
        int current = getGeneratedRP(player);
        player.getPersistentData().putInt(RP_GENERATED, current + amount);
        sendRPToClient(player);
    }

    public static void addConsumedRP(ServerPlayer player, int amount) {
        int current = getConsumedRP(player);
        player.getPersistentData().putInt(RP_CONSUMED, current + amount);
        sendRPToClient(player);
    }

    public static int getNetRP(ServerPlayer player) {
        return getGeneratedRP(player) - getConsumedRP(player);
    }

    public static void resetRP(ServerPlayer player) {
        player.getPersistentData().putInt(RP_GENERATED, 0);
        player.getPersistentData().putInt(RP_CONSUMED, 0);
    }

    public static void sendRPToClient(ServerPlayer player) {
        int currentRP = getNetRP(player);
        PacketDistributor.sendToPlayer(player, new SyncRPToClientPacket(currentRP));
    }
}