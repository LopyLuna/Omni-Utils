package uwu.lopyluna.omni_util.register.worldgen.features;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import uwu.lopyluna.omni_util.OmniUtils;
import uwu.lopyluna.omni_util.register.worldgen.AllBlocks;

public class AllOreFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_GILDED = createKey("ore_gilded");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_QUARTZ = createKey("ore_quartz");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_COAL = createKey("ore_coal");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_COAL_BURIED = createKey("ore_coal_buried");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_IRON = createKey("ore_iron");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_IRON_SMALL = createKey("ore_iron_small");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_GOLD = createKey("ore_gold");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_GOLD_BURIED = createKey("ore_gold_buried");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_REDSTONE = createKey("ore_redstone");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_DIAMOND_SMALL = createKey("ore_diamond_small");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_DIAMOND_MEDIUM = createKey("ore_diamond_medium");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_DIAMOND_LARGE = createKey("ore_diamond_large");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_DIAMOND_BURIED = createKey("ore_diamond_buried");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_AMETHYST = createKey("ore_amethyst");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_LAPIS = createKey("ore_lapis");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_LAPIS_BURIED = createKey("ore_lapis_buried");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_EMERALD = createKey("ore_emerald");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_ANCIENT_LARGE = createKey("ore_ancient_large");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_ANCIENT_SMALL = createKey("ore_ancient_small");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_COPPER_SMALL = createKey("ore_copper_small");


    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        var ruleTest = new BlockMatchTest(AllBlocks.GRIMROCK.get());
        FeatureUtils.register(context, ORE_GILDED, Feature.ORE, new OreConfiguration(ruleTest, AllBlocks.GRIMROCK_GILDED_ORE.getDefaultState(), 12));
        FeatureUtils.register(context, ORE_QUARTZ, Feature.ORE, new OreConfiguration(ruleTest, AllBlocks.GRIMROCK_QUARTZ_ORE.getDefaultState(), 16));
        FeatureUtils.register(context, ORE_COAL, Feature.ORE, new OreConfiguration(ruleTest, AllBlocks.GRIMROCK_COAL_ORE.getDefaultState(), 16));
        FeatureUtils.register(context, ORE_COAL_BURIED, Feature.ORE, new OreConfiguration(ruleTest, AllBlocks.GRIMROCK_COAL_ORE.getDefaultState(), 16, 0.5F));
        FeatureUtils.register(context, ORE_IRON, Feature.ORE, new OreConfiguration(ruleTest, AllBlocks.GRIMROCK_IRON_ORE.getDefaultState(), 12));
        FeatureUtils.register(context, ORE_IRON_SMALL, Feature.ORE, new OreConfiguration(ruleTest, AllBlocks.GRIMROCK_IRON_ORE.getDefaultState(), 8));
        FeatureUtils.register(context, ORE_GOLD, Feature.ORE, new OreConfiguration(ruleTest, AllBlocks.GRIMROCK_GOLD_ORE.getDefaultState(), 12));
        FeatureUtils.register(context, ORE_GOLD_BURIED, Feature.ORE, new OreConfiguration(ruleTest, AllBlocks.GRIMROCK_GOLD_ORE.getDefaultState(), 12, 0.5F));
        FeatureUtils.register(context, ORE_REDSTONE, Feature.ORE, new OreConfiguration(ruleTest, AllBlocks.GRIMROCK_REDSTONE_ORE.getDefaultState(), 12));
        FeatureUtils.register(context, ORE_DIAMOND_SMALL, Feature.ORE, new OreConfiguration(ruleTest, AllBlocks.GRIMROCK_DIAMOND_ORE.getDefaultState(), 6, 0.5F));
        FeatureUtils.register(context, ORE_DIAMOND_LARGE, Feature.ORE, new OreConfiguration(ruleTest, AllBlocks.GRIMROCK_DIAMOND_ORE.getDefaultState(), 12, 0.7F));
        FeatureUtils.register(context, ORE_DIAMOND_BURIED, Feature.ORE, new OreConfiguration(ruleTest, AllBlocks.GRIMROCK_DIAMOND_ORE.getDefaultState(), 8, 1.0F));
        FeatureUtils.register(context, ORE_DIAMOND_MEDIUM, Feature.ORE, new OreConfiguration(ruleTest, AllBlocks.GRIMROCK_DIAMOND_ORE.getDefaultState(), 8, 0.5F));
        FeatureUtils.register(context, ORE_AMETHYST, Feature.ORE, new OreConfiguration(ruleTest, AllBlocks.GRIMROCK_AMETHYST_ORE.getDefaultState(), 8));
        FeatureUtils.register(context, ORE_LAPIS, Feature.ORE, new OreConfiguration(ruleTest, AllBlocks.GRIMROCK_LAPIS_ORE.getDefaultState(), 8));
        FeatureUtils.register(context, ORE_LAPIS_BURIED, Feature.ORE, new OreConfiguration(ruleTest, AllBlocks.GRIMROCK_LAPIS_ORE.getDefaultState(), 8, 1.0F));
        FeatureUtils.register(context, ORE_EMERALD, Feature.ORE, new OreConfiguration(ruleTest, AllBlocks.GRIMROCK_EMERALD_ORE.getDefaultState(), 6));
        FeatureUtils.register(context, ORE_ANCIENT_LARGE, Feature.SCATTERED_ORE, new OreConfiguration(ruleTest, AllBlocks.GRIMROCK_ANCIENT_ORE.getDefaultState(), 5, 1.0F));
        FeatureUtils.register(context, ORE_ANCIENT_SMALL, Feature.SCATTERED_ORE, new OreConfiguration(ruleTest, AllBlocks.GRIMROCK_ANCIENT_ORE.getDefaultState(), 3, 1.0F));
        FeatureUtils.register(context, ORE_COPPER_SMALL, Feature.ORE, new OreConfiguration(ruleTest, AllBlocks.GRIMROCK_COPPER_ORE.getDefaultState(), 8));
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, OmniUtils.loc(name));
    }
}
