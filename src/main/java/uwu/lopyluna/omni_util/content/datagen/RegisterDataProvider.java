package uwu.lopyluna.omni_util.content.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import uwu.lopyluna.omni_util.OmniUtils;
import uwu.lopyluna.omni_util.register.worldgen.AllBiomes;
import uwu.lopyluna.omni_util.register.worldgen.AllDimensions;
import uwu.lopyluna.omni_util.register.worldgen.features.AllOreFeatures;
import uwu.lopyluna.omni_util.register.worldgen.placed_features.AllOrePlacements;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class RegisterDataProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.BIOME, AllBiomes::bootstrap)
            .add(Registries.CONFIGURED_FEATURE, AllOreFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, AllOrePlacements::bootstrap)
            .add(Registries.DIMENSION_TYPE, AllDimensions::bootstrap);

    public RegisterDataProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(OmniUtils.MOD_ID));
    }
}
