package uwu.lopyluna.omni_util.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.omni_util.OmniUtils;
import uwu.lopyluna.omni_util.client.ClientSanityData;

public record SyncSanityToClientPacket(float sanityValue) implements CustomPacketPayload {
    public static final Type<SyncSanityToClientPacket> TYPE = new Type<>(OmniUtils.loc("sync_sanity"));

    public static final StreamCodec<FriendlyByteBuf, SyncSanityToClientPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, SyncSanityToClientPacket::sanityValue,
            SyncSanityToClientPacket::new
    );

    public static void handle(SyncSanityToClientPacket msg, IPayloadContext context) {
        context.enqueueWork(() -> ClientSanityData.update(msg.sanityValue));
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}