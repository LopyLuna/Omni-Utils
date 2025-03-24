package uwu.lopyluna.omni_util.content.features;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.level.levelgen.feature.DripstoneUtils;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.omni_util.content.blocks.PointedGrimrockBlock;
import uwu.lopyluna.omni_util.register.AllBlocks;

import java.util.function.Consumer;

public class GrimrockUtils extends DripstoneUtils {

    protected static double getGrimrockHeight(double radius, double maxRadius, double scale, double minRadius) {
        if (radius < minRadius) radius = minRadius;
        double d1 = radius / maxRadius * 0.384;
        double d2 = 0.75 * Math.pow(d1, 1.3333333333333333);
        double d3 = Math.pow(d1, 0.6666666666666666);
        double d4 = 0.3333333333333333 * Math.log(d1);
        double d5 = scale * (d2 - d3 - d4);
        d5 = Math.max(d5, 0.0);
        return d5 / 0.384 * maxRadius;
    }

    protected static boolean isCircleMostlyEmbeddedInStone(WorldGenLevel level, @NotNull BlockPos pos, int radius) {
        if (isEmptyOrWaterOrLava(level, pos)) return false;
        else {
            float f1 = 6.0F / (float)radius;
            for (float f2 = 0.0F; f2 < (float) (Math.PI * 2); f2 += f1) {
                int i = (int)(Mth.cos(f2) * (float)radius);
                int j = (int)(Mth.sin(f2) * (float)radius);
                if (isEmptyOrWaterOrLava(level, pos.offset(i, 0, j))) return false;
            }
            return true;
        }
    }

    protected static void buildBaseToTipColumn(@NotNull Direction direction, int height, boolean mergeTip, @NotNull Consumer<BlockState> blockSetter) {
        if (height >= 3) {
            blockSetter.accept(createPointedGrimrock(direction, DripstoneThickness.BASE));
            for (int i = 0; i < height - 3; i++) blockSetter.accept(createPointedGrimrock(direction, DripstoneThickness.MIDDLE));
        }
        if (height >= 2) blockSetter.accept(createPointedGrimrock(direction, DripstoneThickness.FRUSTUM));
        if (height >= 1) blockSetter.accept(createPointedGrimrock(direction, mergeTip ? DripstoneThickness.TIP_MERGE : DripstoneThickness.TIP));
    }

    protected static void growPointedGrimrock(LevelAccessor level, BlockPos pos, Direction direction, int height, boolean mergeTip) {
        if (isGrimrockBase(level.getBlockState(pos.relative(direction.getOpposite())))) {
            BlockPos.MutableBlockPos blockpos$mutableblockpos = pos.mutable();
            buildBaseToTipColumn(direction, height, mergeTip, p_313662_ -> {
                if (p_313662_.is(AllBlocks.POINTED_GRIMROCK)) p_313662_ = p_313662_.setValue(PointedGrimrockBlock.WATERLOGGED, level.isWaterAt(blockpos$mutableblockpos));

                level.setBlock(blockpos$mutableblockpos, p_313662_, 2);
                blockpos$mutableblockpos.move(direction);
            });
        }
    }

    protected static boolean placeGrimrockBlockIfPossible(LevelAccessor level, BlockPos pos) {
        BlockState blockstate = level.getBlockState(pos);
        if (blockstate.is(AllBlocks.GRIMROCK)) {
            level.setBlock(pos, AllBlocks.GRIMROCK.getDefaultState(), 2);
            return true;
        } else return false;
    }

    private static BlockState createPointedGrimrock(Direction direction, DripstoneThickness dripstoneThickness) {
        return AllBlocks.POINTED_GRIMROCK
                .getDefaultState()
                .setValue(PointedGrimrockBlock.TIP_DIRECTION, direction)
                .setValue(PointedGrimrockBlock.THICKNESS, dripstoneThickness);
    }

    protected static boolean isEmptyOrWaterOrLava(LevelAccessor level, @NotNull BlockPos pos) {
        return level.isStateAtPosition(pos, GrimrockUtils::isEmptyOrWaterOrLava);
    }

    protected static boolean isNotEmptyOrWater(LevelAccessor level, @NotNull BlockPos pos) {
        return !level.isStateAtPosition(pos, GrimrockUtils::isEmptyOrWater);
    }

    @SuppressWarnings("unused")
    public static boolean isGrimrockBaseOrLava(BlockState state) {
        return isGrimrockBase(state) || state.is(Blocks.LAVA);
    }

    public static boolean isGrimrockBase(BlockState state) {
        return state.is(AllBlocks.GRIMROCK);
    }
}
