package uwu.lopyluna.omni_util.register;

import com.mojang.serialization.Codec;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Unit;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.UUID;

import static uwu.lopyluna.omni_util.OmniUtils.REGISTER;

public class AllDataComponents {

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Component>> ENTITY_CUSTOM_NAME = REGISTER.components()
            .registerComponentType("entity_custom_name", b -> b
                    .persistent(ComponentSerialization.FLAT_CODEC)
                    .networkSynchronized(ComponentSerialization.STREAM_CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> HAS_ENTITY = REGISTER.components()
            .registerComponentType("has_entity", b -> b
                    .persistent(Unit.CODEC)
                    .networkSynchronized(StreamCodec.unit(Unit.INSTANCE)));

    @SuppressWarnings("deprecation")
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CustomData>> ENTITY = REGISTER.components()
            .registerComponentType("entity", b -> b
                    .persistent(CustomData.CODEC)
                    .networkSynchronized(CustomData.STREAM_CODEC));

    @SuppressWarnings("deprecation")
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CustomData>> ENTITY_DATA = REGISTER.components()
            .registerComponentType("entity_data", b -> b
                    .persistent(CustomData.CODEC)
                    .networkSynchronized(CustomData.STREAM_CODEC));

    @SuppressWarnings("deprecation")
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CustomData>> ADD_ENTITY_DATA = REGISTER.components()
            .registerComponentType("add_entity_data", b -> b
                    .persistent(CustomData.CODEC)
                    .networkSynchronized(CustomData.STREAM_CODEC));

    @SuppressWarnings("deprecation")
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CustomData>> PER_ENTITY_DATA = REGISTER.components()
            .registerComponentType("per_entity_data", b -> b
                    .persistent(CustomData.CODEC)
                    .networkSynchronized(CustomData.STREAM_CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> WING_TYPE = REGISTER.components()
            .registerComponentType("wing_type", b -> b
                    .persistent(Codec.STRING)
                    .networkSynchronized(ByteBufCodecs.STRING_UTF8));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UUID>> OWNER_INSTANCE = REGISTER.components()
            .registerComponentType("owner_instance", b -> b
                    .persistent(UUIDUtil.CODEC)
                    .networkSynchronized(UUIDUtil.STREAM_CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UUID>> ITEM_INSTANCE = REGISTER.components()
            .registerComponentType("item_instance", b -> b
                    .persistent(UUIDUtil.CODEC)
                    .networkSynchronized(UUIDUtil.STREAM_CODEC));

    //public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> ITEM_INSTANCES = REGISTER.components()
    //        .registerComponentType("item_instances", b -> b
    //                .persistent(Unit.CODEC)
    //                .networkSynchronized(StreamCodec.unit(Unit.INSTANCE)));

    public static void register() {}
}
