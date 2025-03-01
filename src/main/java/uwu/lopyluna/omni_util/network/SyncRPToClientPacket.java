package uwu.lopyluna.omni_util.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.omni_util.OmniUtils;
import uwu.lopyluna.omni_util.client.ClientRPData;

public record SyncRPToClientPacket(int rpValue) implements CustomPacketPayload {
    public static final Type<SyncRPToClientPacket> TYPE = new Type<>(OmniUtils.loc("sync_rp"));

    public static final StreamCodec<FriendlyByteBuf, SyncRPToClientPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SyncRPToClientPacket::rpValue,
            SyncRPToClientPacket::new
    );

    public static void handle(SyncRPToClientPacket msg, IPayloadContext context) {
        context.enqueueWork(() -> ClientRPData.updateRP(msg.rpValue));
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
