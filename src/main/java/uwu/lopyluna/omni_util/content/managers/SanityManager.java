package uwu.lopyluna.omni_util.content.managers;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.network.PacketDistributor;
import uwu.lopyluna.omni_util.network.SyncSanityToClientPacket;

public class SanityManager {
    public static final String SANITY = "omni_utils_sanity";

    public static float getSanity(ServerPlayer player) {
        if (!player.getPersistentData().contains(SANITY)) {
            player.getPersistentData().putInt(SANITY, 100);
            PacketDistributor.sendToPlayer(player, new SyncSanityToClientPacket(100));
        }
        return player.getPersistentData().getFloat(SANITY);
    }

    public static void adjustSanity(ServerPlayer player, float amount) {
        float sanity = getSanity(player);
        sanity = Mth.clamp(sanity + amount, 0f, 100f);
        player.getPersistentData().putFloat(SANITY, sanity);

        PacketDistributor.sendToPlayer(player, new SyncSanityToClientPacket(sanity));
    }
}
