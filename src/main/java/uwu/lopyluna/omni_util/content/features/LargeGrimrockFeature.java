package uwu.lopyluna.omni_util.content.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Column;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.LargeDripstoneFeature;
import net.minecraft.world.level.levelgen.feature.configurations.LargeDripstoneConfiguration;
import net.minecraft.world.phys.Vec3;
import uwu.lopyluna.omni_util.register.AllBlocks;

import javax.annotation.Nullable;
import java.util.Optional;

public class LargeGrimrockFeature extends LargeDripstoneFeature {
    public LargeGrimrockFeature(Codec<LargeDripstoneConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<LargeDripstoneConfiguration> context) {
        WorldGenLevel worldgenlevel = context.level();
        BlockPos blockpos = context.origin();
        LargeDripstoneConfiguration largedripstoneconfiguration = context.config();
        RandomSource randomsource = context.random();
        if (GrimrockUtils.isNotEmptyOrWater(worldgenlevel, blockpos)) {
            return false;
        } else {
            Optional<Column> optional = Column.scan(worldgenlevel, blockpos, largedripstoneconfiguration.floorToCeilingSearchRange,
                    GrimrockUtils::isEmptyOrWater, GrimrockUtils::isGrimrockBaseOrLava);
            if (optional.isPresent() && optional.get() instanceof Column.Range column$range) {
                if (column$range.height() < 4) {
                    return false;
                } else {
                    int i = (int)((float)column$range.height() * largedripstoneconfiguration.maxColumnRadiusToCaveHeightRatio);
                    int j = Mth.clamp(i, largedripstoneconfiguration.columnRadius.getMinValue(), largedripstoneconfiguration.columnRadius.getMaxValue());
                    int k = Mth.randomBetweenInclusive(randomsource, largedripstoneconfiguration.columnRadius.getMinValue(), j);
                    LargeGrimrock largeGrimrock = makeDripstone(
                            blockpos.atY(column$range.ceiling() - 1), false, randomsource, k,
                            largedripstoneconfiguration.stalactiteBluntness, largedripstoneconfiguration.heightScale);
                    LargeGrimrock largeGrimrock1 = makeDripstone(
                            blockpos.atY(column$range.floor() + 1), true, randomsource, k,
                            largedripstoneconfiguration.stalagmiteBluntness, largedripstoneconfiguration.heightScale);
                    WindOffset windOffset;
                    if (largeGrimrock.isSuitableForWind(largedripstoneconfiguration) && largeGrimrock1.isSuitableForWind(largedripstoneconfiguration)) {
                        windOffset = new WindOffset(blockpos.getY(), randomsource, largedripstoneconfiguration.windSpeed);
                    } else windOffset = WindOffset.noWind();
                    
                    boolean flag = largeGrimrock.moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(worldgenlevel, windOffset);
                    boolean flag1 = largeGrimrock1.moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(worldgenlevel, windOffset);
                    if (flag) largeGrimrock.placeBlocks(worldgenlevel, randomsource, windOffset);
                    if (flag1) largeGrimrock1.placeBlocks(worldgenlevel, randomsource, windOffset);
                    return true;
                }
            } else return false;
        }
    }

    private static LargeGrimrock makeDripstone(BlockPos root, boolean pointingUp, RandomSource random, int radius, FloatProvider bluntnessBase, FloatProvider scaleBase) {
        return new LargeGrimrockFeature.LargeGrimrock(root, pointingUp, radius, bluntnessBase.sample(random), scaleBase.sample(random));
    }

    static final class LargeGrimrock {
        private BlockPos root;
        private final boolean pointingUp;
        private int radius;
        private final double bluntness;
        private final double scale;

        LargeGrimrock(BlockPos root, boolean pointingUp, int radius, double bluntness, double scale) {
            this.root = root;
            this.pointingUp = pointingUp;
            this.radius = radius;
            this.bluntness = bluntness;
            this.scale = scale;
        }

        private int getHeight() {
            return this.getHeightAtRadius(0.0F);
        }

        boolean moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(WorldGenLevel level, WindOffset windOffset) {
            while (this.radius > 1) {
                BlockPos.MutableBlockPos blockpos$mutableblockpos = this.root.mutable();
                int i = Math.min(10, this.getHeight());
                for (int j = 0; j < i; j++) {
                    if (level.getBlockState(blockpos$mutableblockpos).is(Blocks.LAVA)) return false;
                    if (GrimrockUtils.isCircleMostlyEmbeddedInStone(level, windOffset.offset(blockpos$mutableblockpos), this.radius)) {
                        this.root = blockpos$mutableblockpos;
                        return true;
                    }
                    blockpos$mutableblockpos.move(this.pointingUp ? Direction.DOWN : Direction.UP);
                }
                this.radius /= 2;
            }
            return false;
        }

        private int getHeightAtRadius(float radius) {
            return (int)GrimrockUtils.getGrimrockHeight(radius, this.radius, this.scale, this.bluntness);
        }

        void placeBlocks(WorldGenLevel level, RandomSource random, WindOffset windOffset) {
            for (int i = -this.radius; i <= this.radius; i++) for (int j = -this.radius; j <= this.radius; j++) {
                float f = Mth.sqrt((float)(i * i + j * j));
                if (!(f > (float)this.radius)) {
                    int k = this.getHeightAtRadius(f);
                    if (k > 0) {
                        if ((double)random.nextFloat() < 0.2) k = (int)((float)k * Mth.randomBetween(random, 0.8F, 1.0F));

                        BlockPos.MutableBlockPos blockpos$mutableblockpos = this.root.offset(i, 0, j).mutable();
                        boolean flag = false;
                        int l = this.pointingUp ? level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, blockpos$mutableblockpos.getX(), blockpos$mutableblockpos.getZ()) : Integer.MAX_VALUE;

                        for (int i1 = 0; i1 < k && blockpos$mutableblockpos.getY() < l; i1++) {
                            BlockPos blockpos = windOffset.offset(blockpos$mutableblockpos);
                            if (GrimrockUtils.isEmptyOrWaterOrLava(level, blockpos)) {
                                flag = true;
                                Block block = AllBlocks.GRIMROCK.get();
                                level.setBlock(blockpos, block.defaultBlockState(), 2);
                            } else if (flag && level.getBlockState(blockpos).is(BlockTags.BASE_STONE_OVERWORLD)) break;
                            blockpos$mutableblockpos.move(this.pointingUp ? Direction.UP : Direction.DOWN);
                        }
                    }
                }
            }
        }

        boolean isSuitableForWind(LargeDripstoneConfiguration config) {
            return this.radius >= config.minRadiusForWind && this.bluntness >= (double)config.minBluntnessForWind;
        }
    }

    static final class WindOffset {
        private final int originY;
        @Nullable private final Vec3 windSpeed;

        WindOffset(int originY, RandomSource random, FloatProvider magnitude) {
            this.originY = originY;
            float f = magnitude.sample(random);
            float f1 = Mth.randomBetween(random, 0.0F, (float) Math.PI);
            this.windSpeed = new Vec3(Mth.cos(f1) * f, 0.0, Mth.sin(f1) * f);
        }
        private WindOffset() {
            this.originY = 0;
            this.windSpeed = null;
        }
        static WindOffset noWind() {
            return new WindOffset();
        }
        BlockPos offset(BlockPos pos) {
            if (this.windSpeed == null) return pos;
            else {
                int i = this.originY - pos.getY();
                Vec3 vec3 = this.windSpeed.scale(i);
                return pos.offset(Mth.floor(vec3.x), 0, Mth.floor(vec3.z));
            }
        }
    }
}
