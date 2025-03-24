package uwu.lopyluna.omni_util.content.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.PointedDripstoneFeature;
import net.minecraft.world.level.levelgen.feature.configurations.PointedDripstoneConfiguration;

import java.util.Optional;

public class PointedGrimrockFeature extends PointedDripstoneFeature {
    public PointedGrimrockFeature(Codec<PointedDripstoneConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<PointedDripstoneConfiguration> context) {
        LevelAccessor levelaccessor = context.level();
        BlockPos blockpos = context.origin();
        RandomSource randomsource = context.random();
        PointedDripstoneConfiguration pointeddripstoneconfiguration = context.config();
        Optional<Direction> optional = getTipDirection(levelaccessor, blockpos, randomsource);
        if (optional.isEmpty()) return false;
        else {
            BlockPos relative = blockpos.relative(optional.get().getOpposite());
            createPatchOfDripstoneBlocks(levelaccessor, randomsource, relative, pointeddripstoneconfiguration);
            int i = randomsource.nextFloat() < pointeddripstoneconfiguration.chanceOfTallerDripstone
                    && GrimrockUtils.isEmptyOrWater(levelaccessor.getBlockState(blockpos.relative(optional.get()))) ? 2 : 1;
            GrimrockUtils.growPointedGrimrock(levelaccessor, blockpos, optional.get(), i, false);
            return true;
        }
    }

    private static Optional<Direction> getTipDirection(LevelAccessor level, BlockPos pos, RandomSource random) {
        boolean flag = GrimrockUtils.isGrimrockBase(level.getBlockState(pos.above()));
        boolean flag1 = GrimrockUtils.isGrimrockBase(level.getBlockState(pos.below()));
        if (flag && flag1) return Optional.of(random.nextBoolean() ? Direction.DOWN : Direction.UP);
        else if (flag) return Optional.of(Direction.DOWN);
        else return flag1 ? Optional.of(Direction.UP) : Optional.empty();
    }

    private static void createPatchOfDripstoneBlocks(LevelAccessor level, RandomSource random, BlockPos pos, PointedDripstoneConfiguration config) {
        GrimrockUtils.placeGrimrockBlockIfPossible(level, pos);
        for (Direction direction : Direction.Plane.HORIZONTAL) if (!(random.nextFloat() > config.chanceOfDirectionalSpread)) {
            BlockPos blockpos = pos.relative(direction);
            GrimrockUtils.placeGrimrockBlockIfPossible(level, blockpos);
            if (!(random.nextFloat() > config.chanceOfSpreadRadius2)) {
                BlockPos relative = blockpos.relative(Direction.getRandom(random));
                GrimrockUtils.placeGrimrockBlockIfPossible(level, relative);
                if (!(random.nextFloat() > config.chanceOfSpreadRadius3)) {
                    BlockPos blockPos = relative.relative(Direction.getRandom(random));
                    GrimrockUtils.placeGrimrockBlockIfPossible(level, blockPos);
                }
            }
        }
    }
}
