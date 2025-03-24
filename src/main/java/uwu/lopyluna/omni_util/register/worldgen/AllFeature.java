package uwu.lopyluna.omni_util.register.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.DripstoneClusterConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.LargeDripstoneConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.PointedDripstoneConfiguration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import uwu.lopyluna.omni_util.OmniUtils;
import uwu.lopyluna.omni_util.content.features.GrimrockClusterFeature;
import uwu.lopyluna.omni_util.content.features.LargeGrimrockFeature;
import uwu.lopyluna.omni_util.content.features.PointedGrimrockFeature;

public class AllFeature {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, OmniUtils.MOD_ID);

    public static final DeferredHolder<Feature<?>, Feature<DripstoneClusterConfiguration>> GRIMROCK_CLUSTER =
            FEATURES.register("grimrock_cluster", () -> new GrimrockClusterFeature(DripstoneClusterConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, Feature<LargeDripstoneConfiguration>> LARGE_GRIMROCK =
            FEATURES.register("large_grimrock", () -> new LargeGrimrockFeature(LargeDripstoneConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, Feature<PointedDripstoneConfiguration>> POINTED_GRIMROCK =
            FEATURES.register("pointed_grimrock", () -> new PointedGrimrockFeature(PointedDripstoneConfiguration.CODEC));

    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
    }
}
