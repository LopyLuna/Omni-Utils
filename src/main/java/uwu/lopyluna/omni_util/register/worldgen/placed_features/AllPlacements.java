package uwu.lopyluna.omni_util.register.worldgen.placed_features;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ClampedNormalInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.*;
import uwu.lopyluna.omni_util.OmniUtils;
import uwu.lopyluna.omni_util.register.worldgen.features.AllFeatures;

public class AllPlacements {
    public static final ResourceKey<PlacedFeature> GRIMROCK_CLUSTER = createKey("grimrock_cluster");
    public static final ResourceKey<PlacedFeature> LARGE_GRIMROCK = createKey("large_grimrock");
    public static final ResourceKey<PlacedFeature> POINTED_GRIMROCK = createKey("pointed_grimrock");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var holdergetter = context.lookup(Registries.CONFIGURED_FEATURE);
        var holder3 = holdergetter.getOrThrow(AllFeatures.GRIMROCK_CLUSTER);
        var holder4 = holdergetter.getOrThrow(AllFeatures.LARGE_GRIMROCK);
        var holder5 = holdergetter.getOrThrow(AllFeatures.POINTED_GRIMROCK);

        PlacementUtils.register(
                context,
                GRIMROCK_CLUSTER,
                holder3,
                CountPlacement.of(UniformInt.of(48, 96)),
                InSquarePlacement.spread(),
                PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                BiomeFilter.biome()
        );
        PlacementUtils.register(
                context,
                LARGE_GRIMROCK,
                holder4,
                CountPlacement.of(UniformInt.of(10, 48)),
                InSquarePlacement.spread(),
                PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                BiomeFilter.biome()
        );
        PlacementUtils.register(
                context,
                POINTED_GRIMROCK,
                holder5,
                CountPlacement.of(UniformInt.of(192, 256)),
                InSquarePlacement.spread(),
                PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                CountPlacement.of(UniformInt.of(1, 5)),
                RandomOffsetPlacement.of(ClampedNormalInt.of(0.0F, 3.0F, -10, 10), ClampedNormalInt.of(0.0F, 0.6F, -2, 2)),
                BiomeFilter.biome()
        );
    }

    public static void addPointedGrimrock(BiomeGenerationSettings.Builder builder) {
        builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, GRIMROCK_CLUSTER);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, LARGE_GRIMROCK);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, POINTED_GRIMROCK);
    }

    public static ResourceKey<PlacedFeature> createKey(String key) {
        return ResourceKey.create(Registries.PLACED_FEATURE, OmniUtils.loc(key));
    }
}
