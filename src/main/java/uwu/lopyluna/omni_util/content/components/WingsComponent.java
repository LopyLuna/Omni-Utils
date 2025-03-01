package uwu.lopyluna.omni_util.content.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record WingsComponent(String type) {
    public static final Codec<WingsComponent> WINGS_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("type").forGetter(WingsComponent::type)
            ).apply(instance, WingsComponent::new)
    );
    public static final StreamCodec<ByteBuf, WingsComponent> WINGS_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, WingsComponent::type,
            WingsComponent::new
    );
}
