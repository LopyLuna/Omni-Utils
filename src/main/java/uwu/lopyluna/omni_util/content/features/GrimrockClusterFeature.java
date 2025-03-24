package uwu.lopyluna.omni_util.content.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ClampedNormalFloat;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Column;
import net.minecraft.world.level.levelgen.feature.DripstoneClusterFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.DripstoneClusterConfiguration;
import uwu.lopyluna.omni_util.register.AllBlocks;

import java.util.Optional;
import java.util.OptionalInt;

public class GrimrockClusterFeature extends DripstoneClusterFeature {
    public GrimrockClusterFeature(Codec<DripstoneClusterConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<DripstoneClusterConfiguration> context) {
        WorldGenLevel worldgenlevel = context.level();
        BlockPos blockpos = context.origin();
        DripstoneClusterConfiguration dripstoneclusterconfiguration = context.config();
        RandomSource randomsource = context.random();
        if (GrimrockUtils.isNotEmptyOrWater(worldgenlevel, blockpos)) {
            return false;
        } else {
            int i = dripstoneclusterconfiguration.height.sample(randomsource);
            float f = dripstoneclusterconfiguration.wetness.sample(randomsource);
            float f1 = dripstoneclusterconfiguration.density.sample(randomsource);
            int j = dripstoneclusterconfiguration.radius.sample(randomsource);
            int k = dripstoneclusterconfiguration.radius.sample(randomsource);

            for (int l = -j; l <= j; l++)
                for (int i1 = -k; i1 <= k; i1++) {
                    double d0 = this.getChanceOfStalagmiteOrStalactite(j, k, l, i1, dripstoneclusterconfiguration);
                    BlockPos offset = blockpos.offset(l, 0, i1);
                    this.placeColumn(worldgenlevel, randomsource, offset, l, i1, f, d0, i, f1, dripstoneclusterconfiguration);
                }
            return true;
        }
    }

    private void placeColumn(WorldGenLevel level, RandomSource random, BlockPos pos,
                             int x, int z, float wetness, double chance, int height, float density,
                             DripstoneClusterConfiguration config) {
        Optional<Column> optional = Column.scan(level, pos, config.floorToCeilingSearchRange, GrimrockUtils::isEmptyOrWater, GrimrockUtils::isNeitherEmptyNorWater);
        if (optional.isPresent()) {
            OptionalInt ceiling = optional.get().getCeiling();
            OptionalInt floor = optional.get().getFloor();
            if (ceiling.isPresent() || floor.isPresent()) {
                boolean flag = random.nextFloat() < wetness;
                Column column;
                if (flag && floor.isPresent() && this.canPlacePool(level, pos.atY(floor.getAsInt()))) {
                    int i = floor.getAsInt();
                    column = optional.get().withFloor(OptionalInt.of(i - 1));
                    level.setBlock(pos.atY(i), Blocks.WATER.defaultBlockState(), 2);
                } else column = optional.get();
                OptionalInt columnFloor = column.getFloor();
                boolean flag1 = random.nextDouble() < chance;
                int j;
                if (ceiling.isPresent() && flag1 && this.isNotLava(level, pos.atY(ceiling.getAsInt()))) {
                    int k = config.dripstoneBlockLayerThickness.sample(random);
                    this.replaceBlocksWithDripstoneBlocks(level, pos.atY(ceiling.getAsInt()), k, Direction.UP);
                    int l;
                    if (columnFloor.isPresent()) l = Math.min(height, ceiling.getAsInt() - columnFloor.getAsInt());
                    else l = height;
                    j = this.getDripstoneHeight(random, x, z, density, l, config);
                } else j = 0;
                boolean flag2 = random.nextDouble() < chance;
                int i3;
                if (columnFloor.isPresent() && flag2 && this.isNotLava(level, pos.atY(columnFloor.getAsInt()))) {
                    int i1 = config.dripstoneBlockLayerThickness.sample(random);
                    this.replaceBlocksWithDripstoneBlocks(level, pos.atY(columnFloor.getAsInt()), i1, Direction.DOWN);
                    if (ceiling.isPresent())
                        i3 = Math.max(0, j + Mth.randomBetweenInclusive(random, -config.maxStalagmiteStalactiteHeightDiff, config.maxStalagmiteStalactiteHeightDiff));
                    else i3 = this.getDripstoneHeight(random, x, z, density, height, config);
                } else i3 = 0;

                int j1;
                int j3;
                if (ceiling.isPresent() && columnFloor.isPresent() && ceiling.getAsInt() - j <= columnFloor.getAsInt() + i3) {
                    int k1 = columnFloor.getAsInt();
                    int l1 = ceiling.getAsInt();
                    int i2 = Math.max(l1 - j, k1 + 1);
                    int j2 = Math.min(k1 + i3, l1 - 1);
                    int k2 = Mth.randomBetweenInclusive(random, i2, j2 + 1);
                    int l2 = k2 - 1;
                    j3 = l1 - k2;
                    j1 = l2 - k1;
                } else {
                    j3 = j;
                    j1 = i3;
                }
                boolean flag3 = random.nextBoolean() && j3 > 0 && j1 > 0 && column.getHeight().isPresent() && j3 + j1 == column.getHeight().getAsInt();
                if (ceiling.isPresent())
                    GrimrockUtils.growPointedGrimrock(level, pos.atY(ceiling.getAsInt() - 1), Direction.DOWN, j3, flag3);
                if (columnFloor.isPresent())
                    GrimrockUtils.growPointedGrimrock(level, pos.atY(columnFloor.getAsInt() + 1), Direction.UP, j1, flag3);
            }
        }
    }

    private boolean canPlacePool(WorldGenLevel level, BlockPos pos) {
        BlockState blockstate = level.getBlockState(pos);
        if (!blockstate.is(Blocks.WATER) && !blockstate.is(AllBlocks.GRIMROCK) && !blockstate.is(AllBlocks.POINTED_GRIMROCK)) {
            if (level.getBlockState(pos.above()).getFluidState().is(FluidTags.WATER)) return false;
            else {
                for (Direction direction : Direction.Plane.HORIZONTAL)
                    if (!this.canBeAdjacentToWater(level, pos.relative(direction))) return false;
                return this.canBeAdjacentToWater(level, pos.below());
            }
        } else return false;
    }

    private void replaceBlocksWithDripstoneBlocks(WorldGenLevel level, BlockPos pos, int thickness, Direction direction) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = pos.mutable();
        for (int i = 0; i < thickness; i++) {
            if (!GrimrockUtils.placeGrimrockBlockIfPossible(level, blockpos$mutableblockpos)) return;
            blockpos$mutableblockpos.move(direction);
        }
    }

    private boolean isNotLava(LevelReader level, BlockPos pos) {
        return !level.getBlockState(pos).is(Blocks.LAVA);
    }

    private int getDripstoneHeight(RandomSource random, int x, int z, float chance, int height, DripstoneClusterConfiguration config) {
        if (random.nextFloat() > chance) {
            return 0;
        } else {
            int i = Math.abs(x) + Math.abs(z);
            float f = (float)Mth.clampedMap(i, 0.0, config.maxDistanceFromCenterAffectingHeightBias, (double)height / 2.0, 0.0);
            return (int)randomBetweenBiased(random, (float)height, f, (float)config.heightDeviation);
        }
    }

    private double getChanceOfStalagmiteOrStalactite(int xRadius, int zRadius, int x, int z, DripstoneClusterConfiguration config) {
        int i = xRadius - Math.abs(x);
        int j = zRadius - Math.abs(z);
        int k = Math.min(i, j);
        return Mth.clampedMap((float)k, 0.0F, (float)config.maxDistanceFromEdgeAffectingChanceOfDripstoneColumn, config.chanceOfDripstoneColumnAtMaxDistanceFromCenter, 1.0F);
    }

    private boolean canBeAdjacentToWater(LevelAccessor level, BlockPos pos) {
        BlockState blockstate = level.getBlockState(pos);
        return blockstate.is(BlockTags.BASE_STONE_OVERWORLD) || blockstate.getFluidState().is(FluidTags.WATER);
    }
    private static float randomBetweenBiased(RandomSource random, float max, float mean, float deviation) {
        return ClampedNormalFloat.sample(random, mean, deviation, 0.0f, max);
    }
}
