package uwu.lopyluna.omni_util.register.worldgen.placed_features;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.*;
import uwu.lopyluna.omni_util.OmniUtils;
import uwu.lopyluna.omni_util.register.worldgen.features.AllOreFeatures;

import java.util.List;

public class AllOrePlacements {
    public static final ResourceKey<PlacedFeature> ORE_GILDED = createKey("ore_gilded");
    public static final ResourceKey<PlacedFeature> ORE_QUARTZ = createKey("ore_quartz");
    public static final ResourceKey<PlacedFeature> ORE_COAL_UPPER = createKey("ore_coal_upper");
    public static final ResourceKey<PlacedFeature> ORE_COAL_LOWER = createKey("ore_coal_lower");
    public static final ResourceKey<PlacedFeature> ORE_IRON_UPPER = createKey("ore_iron_upper");
    public static final ResourceKey<PlacedFeature> ORE_IRON_MIDDLE = createKey("ore_iron_middle");
    public static final ResourceKey<PlacedFeature> ORE_IRON_SMALL = createKey("ore_iron_small");
    public static final ResourceKey<PlacedFeature> ORE_GOLD = createKey("ore_gold");
    public static final ResourceKey<PlacedFeature> ORE_GOLD_LOWER = createKey("ore_gold_lower");
    public static final ResourceKey<PlacedFeature> ORE_REDSTONE = createKey("ore_redstone");
    public static final ResourceKey<PlacedFeature> ORE_REDSTONE_LOWER = createKey("ore_redstone_lower");
    public static final ResourceKey<PlacedFeature> ORE_DIAMOND = createKey("ore_diamond");
    public static final ResourceKey<PlacedFeature> ORE_DIAMOND_MEDIUM = createKey("ore_diamond_medium");
    public static final ResourceKey<PlacedFeature> ORE_DIAMOND_LARGE = createKey("ore_diamond_large");
    public static final ResourceKey<PlacedFeature> ORE_DIAMOND_BURIED = createKey("ore_diamond_buried");
    public static final ResourceKey<PlacedFeature> ORE_AMETHYST = createKey("ore_amethyst");
    public static final ResourceKey<PlacedFeature> ORE_LAPIS = createKey("ore_lapis");
    public static final ResourceKey<PlacedFeature> ORE_LAPIS_BURIED = createKey("ore_lapis_buried");
    public static final ResourceKey<PlacedFeature> ORE_EMERALD = createKey("ore_emerald");
    public static final ResourceKey<PlacedFeature> ORE_ANCIENT_LARGE = createKey("ore_ancient_large");
    public static final ResourceKey<PlacedFeature> ORE_ANCIENT_SMALL = createKey("ore_ancient_small");
    public static final ResourceKey<PlacedFeature> ORE_COPPER = createKey("ore_copper");

    public static void addGrimOres(BiomeGenerationSettings.Builder builder) {
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, AllOrePlacements.ORE_COAL_UPPER);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, AllOrePlacements.ORE_COAL_LOWER);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, AllOrePlacements.ORE_IRON_UPPER);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, AllOrePlacements.ORE_IRON_MIDDLE);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, AllOrePlacements.ORE_IRON_SMALL);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, AllOrePlacements.ORE_GOLD);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, AllOrePlacements.ORE_GOLD_LOWER);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, AllOrePlacements.ORE_REDSTONE);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, AllOrePlacements.ORE_REDSTONE_LOWER);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, AllOrePlacements.ORE_DIAMOND);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, AllOrePlacements.ORE_DIAMOND_MEDIUM);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, AllOrePlacements.ORE_DIAMOND_LARGE);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, AllOrePlacements.ORE_DIAMOND_BURIED);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, AllOrePlacements.ORE_AMETHYST);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, AllOrePlacements.ORE_LAPIS);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, AllOrePlacements.ORE_LAPIS_BURIED);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, AllOrePlacements.ORE_COPPER);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, AllOrePlacements.ORE_EMERALD);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, AllOrePlacements.ORE_GILDED);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, AllOrePlacements.ORE_QUARTZ);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, AllOrePlacements.ORE_ANCIENT_LARGE);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, AllOrePlacements.ORE_ANCIENT_SMALL);
    }

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var holdergetter = context.lookup(Registries.CONFIGURED_FEATURE);
        var oreGilded = holdergetter.getOrThrow(AllOreFeatures.ORE_GILDED);
        var oreQuartz = holdergetter.getOrThrow(AllOreFeatures.ORE_QUARTZ);
        var oreCoalUpper = holdergetter.getOrThrow(AllOreFeatures.ORE_COAL);
        var oreCoalLower = holdergetter.getOrThrow(AllOreFeatures.ORE_COAL_BURIED);
        var oreIronUpper = holdergetter.getOrThrow(AllOreFeatures.ORE_IRON);
        var oreIronMiddle = holdergetter.getOrThrow(AllOreFeatures.ORE_IRON);
        var oreIronSmall = holdergetter.getOrThrow(AllOreFeatures.ORE_IRON_SMALL);
        var oreGold = holdergetter.getOrThrow(AllOreFeatures.ORE_GOLD_BURIED);
        var oreRedstone = holdergetter.getOrThrow(AllOreFeatures.ORE_REDSTONE);
        var oreDiamond = holdergetter.getOrThrow(AllOreFeatures.ORE_DIAMOND_SMALL);
        var oreDiamondMedium = holdergetter.getOrThrow(AllOreFeatures.ORE_DIAMOND_MEDIUM);
        var oreDiamondLarge = holdergetter.getOrThrow(AllOreFeatures.ORE_DIAMOND_LARGE);
        var oreDiamondBuried = holdergetter.getOrThrow(AllOreFeatures.ORE_DIAMOND_BURIED);
        var oreAmethyst = holdergetter.getOrThrow(AllOreFeatures.ORE_AMETHYST);
        var oreLapis = holdergetter.getOrThrow(AllOreFeatures.ORE_LAPIS);
        var oreLapisBuried = holdergetter.getOrThrow(AllOreFeatures.ORE_LAPIS_BURIED);
        var oreEmerald = holdergetter.getOrThrow(AllOreFeatures.ORE_EMERALD);
        var oreAncientLarge = holdergetter.getOrThrow(AllOreFeatures.ORE_ANCIENT_LARGE);
        var oreAncientSmall = holdergetter.getOrThrow(AllOreFeatures.ORE_ANCIENT_SMALL);
        var oreCopper = holdergetter.getOrThrow(AllOreFeatures.ORE_COPPER_SMALL);
        PlacementUtils.register(context, ORE_GILDED, oreGilded, commonOrePlacement(8, PlacementUtils.RANGE_10_10));
        PlacementUtils.register(context, ORE_QUARTZ, oreQuartz, commonOrePlacement(12, PlacementUtils.RANGE_10_10));
        PlacementUtils.register(context, ORE_COAL_UPPER, oreCoalUpper, commonOrePlacement(20, HeightRangePlacement.uniform(VerticalAnchor.absolute(136), VerticalAnchor.top())));
        PlacementUtils.register(context, ORE_COAL_LOWER, oreCoalLower, commonOrePlacement(15, HeightRangePlacement.triangle(VerticalAnchor.absolute(0), VerticalAnchor.absolute(192))));
        PlacementUtils.register(context, ORE_IRON_UPPER, oreIronUpper, commonOrePlacement(60, HeightRangePlacement.triangle(VerticalAnchor.absolute(80), VerticalAnchor.absolute(384))));
        PlacementUtils.register(context, ORE_IRON_MIDDLE, oreIronMiddle, commonOrePlacement(10, HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(56))));
        PlacementUtils.register(context, ORE_IRON_SMALL, oreIronSmall, commonOrePlacement(10, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(72))));
        PlacementUtils.register(context, ORE_GOLD, oreGold, commonOrePlacement(4, HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(32))));
        PlacementUtils.register(context, ORE_GOLD_LOWER, oreGold, orePlacement(CountPlacement.of(UniformInt.of(0, 2)), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-48))));
        PlacementUtils.register(context, ORE_REDSTONE, oreRedstone, commonOrePlacement(10, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(384))));
        PlacementUtils.register(context, ORE_REDSTONE_LOWER, oreRedstone, commonOrePlacement(10, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-32), VerticalAnchor.aboveBottom(64))));
        PlacementUtils.register(context, ORE_DIAMOND, oreDiamond, commonOrePlacement(7, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80))));
        PlacementUtils.register(context, ORE_DIAMOND_MEDIUM, oreDiamondMedium, commonOrePlacement(4, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(128))));
        PlacementUtils.register(context, ORE_DIAMOND_LARGE, oreDiamondLarge, rareOrePlacement(9, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80))));
        PlacementUtils.register(context, ORE_DIAMOND_BURIED, oreDiamondBuried, commonOrePlacement(4, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80))));
        PlacementUtils.register(context, ORE_AMETHYST, oreAmethyst, commonOrePlacement(75, HeightRangePlacement.triangle(VerticalAnchor.absolute(-32), VerticalAnchor.absolute(384))));
        PlacementUtils.register(context, ORE_LAPIS, oreLapis, commonOrePlacement(2, HeightRangePlacement.triangle(VerticalAnchor.absolute(-32), VerticalAnchor.absolute(256))));
        PlacementUtils.register(context, ORE_LAPIS_BURIED, oreLapisBuried, commonOrePlacement(4, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(128))));
        PlacementUtils.register(context, ORE_EMERALD, oreEmerald, commonOrePlacement(100, HeightRangePlacement.triangle(VerticalAnchor.absolute(-32), VerticalAnchor.absolute(384))));
        PlacementUtils.register(context, ORE_ANCIENT_LARGE, oreAncientLarge, InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.absolute(8), VerticalAnchor.absolute(24)), BiomeFilter.biome());
        PlacementUtils.register(context, ORE_ANCIENT_SMALL, oreAncientSmall, InSquarePlacement.spread(), PlacementUtils.RANGE_8_8, BiomeFilter.biome());
        PlacementUtils.register(context, ORE_COPPER, oreCopper, commonOrePlacement(14, HeightRangePlacement.triangle(VerticalAnchor.absolute(-32), VerticalAnchor.absolute(256))));
    }

    private static List<PlacementModifier> orePlacement(PlacementModifier countPlacement, PlacementModifier heightRange) {
        return List.of(countPlacement, InSquarePlacement.spread(), heightRange, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier heightRange) {
        return orePlacement(CountPlacement.of(count), heightRange);
    }

    @SuppressWarnings("SameParameterValue")
    private static List<PlacementModifier> rareOrePlacement(int chance, PlacementModifier heightRange) {
        return orePlacement(RarityFilter.onAverageOnceEvery(chance), heightRange);
    }

    public static ResourceKey<PlacedFeature> createKey(String key) {
        return ResourceKey.create(Registries.PLACED_FEATURE, OmniUtils.loc(key));
    }
}
