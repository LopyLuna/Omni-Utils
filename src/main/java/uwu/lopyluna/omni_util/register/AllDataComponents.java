package uwu.lopyluna.omni_util.register;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;

import static uwu.lopyluna.omni_util.OmniUtils.REG;

public class AllDataComponents {

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> WINGS_COMPONENTS = REG.components()
            .registerComponentType("wing_type", b -> b
                    .persistent(Codec.STRING)
                    .networkSynchronized(ByteBufCodecs.STRING_UTF8));

    public static void register() {}
}
