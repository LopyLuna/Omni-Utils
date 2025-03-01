package uwu.lopyluna.omni_util.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.omni_util.OmniUtils;
import uwu.lopyluna.omni_util.client.ClientRPData;

public record SyncActivationToClientPacket(boolean bool) implements CustomPacketPayload {
    public static final Type<SyncActivationToClientPacket> TYPE = new Type<>(OmniUtils.loc("sync_activation"));

    public static final StreamCodec<FriendlyByteBuf, SyncActivationToClientPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, SyncActivationToClientPacket::bool,
            SyncActivationToClientPacket::new
    );

    public static void handle(SyncActivationToClientPacket msg, IPayloadContext context) {
        context.enqueueWork(() -> ClientRPData.updateActivation(msg.bool));
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
