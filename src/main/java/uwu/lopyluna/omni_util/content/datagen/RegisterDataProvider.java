package uwu.lopyluna.omni_util.content.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import uwu.lopyluna.omni_util.OmniUtils;
import uwu.lopyluna.omni_util.register.worldgen.AllBiomes;
import uwu.lopyluna.omni_util.register.worldgen.AllDimensions;
import uwu.lopyluna.omni_util.register.worldgen.features.AllFeatures;
import uwu.lopyluna.omni_util.register.worldgen.features.AllOreFeatures;
import uwu.lopyluna.omni_util.register.worldgen.placed_features.AllOrePlacements;
import uwu.lopyluna.omni_util.register.worldgen.placed_features.AllPlacements;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class RegisterDataProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, RegisterDataProvider::bootstrapConfiguredFeature)
            .add(Registries.PLACED_FEATURE, RegisterDataProvider::bootstrapPlacedFeature)
            .add(Registries.BIOME, AllBiomes::bootstrap)
            .add(Registries.DIMENSION_TYPE, AllDimensions::bootstrap);

    public RegisterDataProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(OmniUtils.MOD_ID));
    }


    public static void bootstrapConfiguredFeature(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        AllOreFeatures.bootstrap(context);
        AllFeatures.bootstrap(context);
    }
    public static void bootstrapPlacedFeature(BootstrapContext<PlacedFeature> context) {
        AllOrePlacements.bootstrap(context);
        AllPlacements.bootstrap(context);
    }
}
