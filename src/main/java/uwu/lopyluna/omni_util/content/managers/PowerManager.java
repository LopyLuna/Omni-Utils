package uwu.lopyluna.omni_util.content.managers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import uwu.lopyluna.omni_util.network.SyncRPToClientPacket;

public class PowerManager {
    private static final String RP_GENERATED = "rp_generated";
    private static final String ACTIVATED_DELAY = "activated_delay_";
    private static final String RP_CONSUMED = "rp_consumed";

    public static int getGeneratedRP(ServerPlayer player) {
        return player.getPersistentData().getInt(RP_GENERATED);
    }

    public static int getDelay(ServerPlayer player, String type) {
        var key = ACTIVATED_DELAY + type;
        var has = player.getPersistentData().contains(key);
        var delay = has ? player.getPersistentData().getInt(key) : 0;
        if (delay == 0) {
            if (has) player.getPersistentData().remove(key);
            return 0;
        }
        return delay;
    }
    public static int getDelay(ServerPlayer player, ResourceLocation type) {
        return getDelay(player, type.getPath());
    }

    public static int getConsumedRP(ServerPlayer player) {
        return player.getPersistentData().getInt(RP_CONSUMED);
    }

    public static void adjustGeneratedRP(ServerPlayer player, int amount) {
        int current = getGeneratedRP(player);
        player.getPersistentData().putInt(RP_GENERATED, current + amount);
        sendRPToClient(player);
    }

    public static void adjustConsumedRP(ServerPlayer player, int amount) {
        int current = getConsumedRP(player);
        player.getPersistentData().putInt(RP_CONSUMED, current + amount);
        sendRPToClient(player);
    }

    public static void adjustDelay(ServerPlayer player, String type, int amount) {
        int current = getDelay(player, type);
        var key = ACTIVATED_DELAY + type;
        int value = Mth.clamp(current + amount, 0, 2);
        var has = player.getPersistentData().contains(key);
        if (value != 0) player.getPersistentData().putInt(key, value);
        else if (has) player.getPersistentData().remove(key);
    }

    public static void adjustDelay(ServerPlayer player, ResourceLocation type, int amount) {
        adjustDelay(player, type.getPath(), amount);
    }

    public static int getNetRP(ServerPlayer player) {
        return getGeneratedRP(player) - getConsumedRP(player);
    }
    public static int getNetRPOmni(Player player) {
        int gen = player.getPersistentData().getInt(RP_GENERATED);
        int con = player.getPersistentData().getInt(RP_CONSUMED);
        return gen - con;
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