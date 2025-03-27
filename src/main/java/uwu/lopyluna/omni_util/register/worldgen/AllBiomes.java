package uwu.lopyluna.omni_util.register.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.CavePlacements;
import net.minecraft.data.worldgen.placement.NetherPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.commands.FillBiomeCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.phys.AABB;
import uwu.lopyluna.omni_util.OmniUtils;
import uwu.lopyluna.omni_util.register.worldgen.placed_features.AllOrePlacements;
import uwu.lopyluna.omni_util.register.worldgen.placed_features.AllPlacements;

import java.util.function.Predicate;

@SuppressWarnings("unused")
public class AllBiomes {
    public static final ResourceKey<Biome> CURSED_BIOME = register("cursed_biome");
    public static final ResourceKey<Biome> DEAD_BIOME = register("dead_biome");
    public static final ResourceKey<Biome> GRIMSPIRE_BIOME = register("grimspire_biome");


    private static ResourceKey<Biome> register(String key) {
        return ResourceKey.create(Registries.BIOME, OmniUtils.loc(key));
    }

    public static void bootstrap(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> placed = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carver = context.lookup(Registries.CONFIGURED_CARVER);
        context.register(CURSED_BIOME, cursedBiome(placed, carver));
        context.register(DEAD_BIOME, deadBiome(placed, carver));
        context.register(GRIMSPIRE_BIOME, grimspireBiome(placed, carver));
    }

    public static void fillBiome(ServerLevel level, AABB aabb, ResourceKey<Biome> biome, Predicate<Holder<Biome>> filter) {
        BlockPos from = BlockPos.containing(aabb.minX, aabb.minY, aabb.minZ);
        BlockPos to = BlockPos.containing(aabb.maxX, aabb.maxY, aabb.maxZ);
        fillBiome(level, from, to, biome, filter);
    }
    public static void fillBiome(ServerLevel level, Vec3i from, Vec3i to, ResourceKey<Biome> biome, Predicate<Holder<Biome>> filter) {
        fillBiome(level, new BlockPos(from), new BlockPos(to), biome, filter);
    }
    public static void fillBiome(ServerLevel level, BlockPos from, BlockPos to, ResourceKey<Biome> biome, Predicate<Holder<Biome>> filter) {
        var access = level.registryAccess();
        FillBiomeCommand.fill(level, from, to, access.registryOrThrow(Registries.BIOME).getHolderOrThrow(biome), filter, c -> {});
    }

    public static Biome grimspireBiome(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> worldCarvers) {
        MobSpawnSettings.Builder mobSpawnBuilder = new MobSpawnSettings.Builder();
        mobSpawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE_VILLAGER, 2, 1, 1));
        mobSpawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SPIDER,         25, 1, 2));
        mobSpawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.CAVE_SPIDER,         25, 1, 2));
        mobSpawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE,         24, 1, 1));
        mobSpawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.HUSK,         24, 1, 1));
        mobSpawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SKELETON,       25, 1, 2));
        mobSpawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.BOGGED,       25, 1, 2));
        mobSpawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.CREEPER,        25, 1, 2));
        mobSpawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.PHANTOM,        25, 1, 2));
        mobSpawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN,        5, 1, 2));
        mobSpawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.WITCH,           2, 1, 1));
        BiomeGenerationSettings.Builder generationSettings = new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers);
        generationSettings
                .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.GLOW_LICHEN)
                .addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, NetherPlacements.BASALT_PILLAR);
        AllPlacements.addPointedGrimrock(generationSettings);
        AllOrePlacements.addGrimOres(generationSettings);
        //BiomeDefaultFeatures.addDefaultCarversAndLakes(generationSettings);

        BiomeDefaultFeatures.addDefaultMonsterRoom(generationSettings);
        BiomeDefaultFeatures.addDefaultMushrooms(generationSettings);
        BiomeDefaultFeatures.addFossilDecoration(generationSettings);

        return (new Biome.BiomeBuilder())
                .hasPrecipitation(false)
                .temperature(2.0F)
                .downfall(0.0F)
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(4405620)
                        .waterFogColor(4405620)
                        .fogColor(0)
                        .skyColor(0)
                        .grassColorOverride(4473924)
                        .foliageColorOverride(4473924)
                        .ambientLoopSound(SoundEvents.AMBIENT_WARPED_FOREST_LOOP)
                        .ambientMoodSound(new AmbientMoodSettings(SoundEvents.AMBIENT_WARPED_FOREST_MOOD, 6000, 8, 2.0))
                        .ambientAdditionsSound(new AmbientAdditionsSettings(SoundEvents.AMBIENT_WARPED_FOREST_ADDITIONS, 0.0111))
                        .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_DEEP_DARK))
                        .ambientParticle(new AmbientParticleSettings(ParticleTypes.SMOKE, 0.06F))
                        .build())
                .mobSpawnSettings(mobSpawnBuilder.build())
                .generationSettings(generationSettings.build())
                .build();
    }

    public static Biome deadBiome(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> worldCarvers) {
        MobSpawnSettings.Builder mobSpawnBuilder = new MobSpawnSettings.Builder();
        mobSpawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE_VILLAGER, 5, 1, 2));
        mobSpawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SPIDER,         100, 4, 8));
        mobSpawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE,         95, 1, 2));
        mobSpawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SKELETON,       100, 4, 8));
        mobSpawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.CREEPER,        100, 4, 8));
        mobSpawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN,        10, 1, 8));
        mobSpawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.WITCH,           5, 1, 2));
        return (new Biome.BiomeBuilder())
                .hasPrecipitation(true)
                .temperature(2.0F)
                .downfall(0.1F)
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(4405620)
                        .waterFogColor(4405620)
                        .fogColor(5127764)
                        .skyColor(5127764)
                        .grassColorOverride(5523524)
                        .foliageColorOverride(5523524)
                        .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_BASALT_DELTAS))
                        .ambientParticle(new AmbientParticleSettings(ParticleTypes.MYCELIUM, 0.03F))
                        .build())
                .mobSpawnSettings(mobSpawnBuilder.build())
                .generationSettings(new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers).build())
                .build();
    }

    public static Biome cursedBiome(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> worldCarvers) {
        MobSpawnSettings.Builder mobSpawnBuilder = new MobSpawnSettings.Builder();
        mobSpawnBuilder.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE_VILLAGER, 25, 2, 4));
        mobSpawnBuilder.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.CAVE_SPIDER,         25, 4, 8));
        mobSpawnBuilder.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.SPIDER,         100, 8, 16));
        mobSpawnBuilder.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.HUSK,         25, 2, 4));
        mobSpawnBuilder.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE,         100, 2, 4));
        mobSpawnBuilder.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.WITHER_SKELETON,       10, 4, 8));
        mobSpawnBuilder.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.STRAY,       25, 6, 12));
        mobSpawnBuilder.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.BOGGED,       25, 6, 12));
        mobSpawnBuilder.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.SKELETON,       100, 8, 16));
        mobSpawnBuilder.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.CREEPER,        100, 8, 16));
        mobSpawnBuilder.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN,        25, 2, 16));
        mobSpawnBuilder.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.PHANTOM,       10, 4, 8));
        mobSpawnBuilder.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.WITCH,           25, 2, 4));

        //mobSpawnBuilder.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.CAVE_SPIDER,         125, 4, 8));
        //mobSpawnBuilder.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.SPIDER,         200, 8, 16));
        //mobSpawnBuilder.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.HUSK,         25, 2, 4));
        //mobSpawnBuilder.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.CREEPER,        200, 8, 16));
        //mobSpawnBuilder.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN,        25, 2, 16));
        //mobSpawnBuilder.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.WITCH,           25, 2, 4));

        return (new Biome.BiomeBuilder())
                .hasPrecipitation(false)
                .temperature(0.5F)
                .downfall(0.0F)
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(4473924)
                        .waterFogColor(4473924)
                        .fogColor(2236969)
                        .skyColor(2236969)
                        .grassColorOverride(4473924)
                        .foliageColorOverride(4473924)
                        .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_DEEP_DARK))
                        .ambientParticle(new AmbientParticleSettings(ParticleTypes.SMOKE, 0.12F))
                        .build())
                .mobSpawnSettings(mobSpawnBuilder.build())
                .generationSettings(new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers).build())
                .build();
    }
}
