package uwu.lopyluna.omni_util.register.worldgen.features;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ClampedNormalFloat;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.DripstoneClusterConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.LargeDripstoneConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.PointedDripstoneConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement;
import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
import uwu.lopyluna.omni_util.OmniUtils;
import uwu.lopyluna.omni_util.register.worldgen.AllFeature;

public class AllFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> GRIMROCK_CLUSTER = createKey("grimrock_cluster");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_GRIMROCK = createKey("large_grimrock");
    public static final ResourceKey<ConfiguredFeature<?, ?>> POINTED_GRIMROCK = createKey("pointed_grimrock");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        FeatureUtils.register(
                context,
                GRIMROCK_CLUSTER,
                AllFeature.GRIMROCK_CLUSTER.get(),
                new DripstoneClusterConfiguration(
                        12,UniformInt.of(3, 6), UniformInt.of(2, 8), 1, 3,
                        UniformInt.of(2, 4), UniformFloat.of(0.3F, 0.7F), ClampedNormalFloat.of(0.1F, 0.3F, 0.1F, 0.9F), 0.1F, 3, 8
                )
        );
        FeatureUtils.register(
                context,
                LARGE_GRIMROCK,
                AllFeature.LARGE_GRIMROCK.get(),
                new LargeDripstoneConfiguration(
                        30, UniformInt.of(3, 19), UniformFloat.of(0.4F, 2.0F), 0.33F,
                        UniformFloat.of(0.3F, 0.9F), UniformFloat.of(0.4F, 1.0F), UniformFloat.of(0.0F, 0.3F), 4, 0.6F
                )
        );
        FeatureUtils.register(
                context,
                POINTED_GRIMROCK,
                Feature.SIMPLE_RANDOM_SELECTOR,
                new SimpleRandomFeatureConfiguration(
                        HolderSet.direct(
                                PlacementUtils.inlinePlaced(
                                        AllFeature.POINTED_GRIMROCK.get(),
                                        new PointedDripstoneConfiguration(0.2F, 0.7F, 0.5F, 0.5F),
                                        EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE, 12),
                                        RandomOffsetPlacement.vertical(ConstantInt.of(1))
                                ),
                                PlacementUtils.inlinePlaced(
                                        AllFeature.POINTED_GRIMROCK.get(),
                                        new PointedDripstoneConfiguration(0.2F, 0.7F, 0.5F, 0.5F),
                                        EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE, 12),
                                        RandomOffsetPlacement.vertical(ConstantInt.of(-1))
                                )
                        )
                )
        );
    }


    public static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, OmniUtils.loc(name));
    }
}
