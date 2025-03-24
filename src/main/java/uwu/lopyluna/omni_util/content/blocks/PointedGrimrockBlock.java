package uwu.lopyluna.omni_util.content.blocks;

import com.google.common.annotations.VisibleForTesting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.omni_util.mixin.AbstractCauldronBlockAccessor;
import uwu.lopyluna.omni_util.register.AllBlocks;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
public class PointedGrimrockBlock extends PointedDripstoneBlock {
    public PointedGrimrockBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull BlockState updateShape(BlockState state, Direction p_direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        if (p_direction != Direction.UP && p_direction != Direction.DOWN) return state;
        else {
            Direction direction = state.getValue(TIP_DIRECTION);
            if (direction == Direction.DOWN && level.getBlockTicks().hasScheduledTick(pos, this)) return state;
            else if (p_direction == direction.getOpposite() && !this.canSurvive(state, level, pos)) {
                if (direction == Direction.DOWN) level.scheduleTick(pos, this, 2);
                else level.scheduleTick(pos, this, 1);
                return state;
            } else {
                boolean flag = state.getValue(THICKNESS) == DripstoneThickness.TIP_MERGE;
                DripstoneThickness dripstonethickness = calculateDripstoneThickness(level, pos, direction, flag);
                return state.setValue(THICKNESS, dripstonethickness);
            }
        }
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return isValidPointedDripstonePlacement(level, pos, state.getValue(TIP_DIRECTION));
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (state.getValue(TIP_DIRECTION) == Direction.UP && state.getValue(THICKNESS) == DripstoneThickness.TIP)
            entity.causeFallDamage(fallDistance + 2.0F, 2.0F, level.damageSources().stalagmite());
        else super.fallOn(level, state, pos, entity, fallDistance);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (canDrip(state)) {
            float f = random.nextFloat();
            if (!(f > 0.12F)) getFluidAboveStalactite(level, pos, state)
                    .filter(p_221848_ -> f < 0.02F || canFillCauldron(p_221848_.fluid))
                    .ifPresent(p_221881_ -> spawnDripParticle(level, pos, state, p_221881_.fluid));
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (isStalagmite(state) && !this.canSurvive(state, level, pos)) {level.destroyBlock(pos, true);
        } else spawnFallingStalactite(state, level, pos);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        maybeTransferFluid(state, level, pos, random.nextFloat());
        if (random.nextFloat() < 0.011377778F && isStalactiteStartPos(state, level, pos))
            growStalactiteOrStalagmiteIfPossible(state, level, pos, random);
    }

    public static void maybeTransferFluid(BlockState state, ServerLevel level, BlockPos pos, float randChance) {
        if (isStalactiteStartPos(state, level, pos)) {
            Optional<FluidInfo> optional = getFluidAboveStalactite(level, pos, state);
            if (optional.isPresent()) {
                Fluid fluid = optional.get().fluid;

                net.neoforged.neoforge.fluids.FluidType.DripstoneDripInfo dripInfo = fluid.getFluidType().getDripInfo();
                if (dripInfo != null && !(randChance >= dripInfo.chance())) {
                    BlockPos blockPos = findTip(state, level, pos, 11);
                    if (blockPos != null) {
                        if (optional.get().sourceState.is(Blocks.MUD) && fluid == Fluids.WATER) {
                            BlockState blockstate1 = Blocks.CLAY.defaultBlockState();
                            level.setBlockAndUpdate(optional.get().pos, blockstate1);
                            Block.pushEntitiesUp(optional.get().sourceState, blockstate1, level, optional.get().pos);
                            level.gameEvent(GameEvent.BLOCK_CHANGE, optional.get().pos, GameEvent.Context.of(blockstate1));
                            level.levelEvent(1504, blockPos, 0);
                        } else {
                            BlockPos blockPos1 = findFillableCauldronBelowStalactiteTip(level, blockPos, fluid);
                            if (blockPos1 != null) {
                                level.levelEvent(1504, blockPos, 0);
                                int i = blockPos.getY() - blockPos1.getY();
                                int j = 50 + i;
                                BlockState blockstate = level.getBlockState(blockPos1);
                                level.scheduleTick(blockPos1, blockstate.getBlock(), j);
                            }
                        }
                    }
                }
            }
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor levelaccessor = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        Direction direction = context.getNearestLookingVerticalDirection().getOpposite();
        Direction direction1 = calculateTipDirection(levelaccessor, blockpos, direction);
        if (direction1 == null) return null;
        else {
            boolean flag = !context.isSecondaryUseActive();
            DripstoneThickness dripstonethickness = calculateDripstoneThickness(levelaccessor, blockpos, direction1, flag);
            return dripstonethickness == null
                    ? null
                    : this.defaultBlockState()
                    .setValue(TIP_DIRECTION, direction1)
                    .setValue(THICKNESS, dripstonethickness)
                    .setValue(WATERLOGGED, levelaccessor.getFluidState(blockpos).getType() == Fluids.WATER);
        }
    }

    private static void spawnFallingStalactite(BlockState state, ServerLevel level, BlockPos pos) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = pos.mutable();
        BlockState blockstate = state;

        while (isStalactite(blockstate)) {
            FallingBlockEntity fallingblockentity = FallingBlockEntity.fall(level, blockpos$mutableblockpos, blockstate);
            if (isTip(blockstate, true)) {
                int i = Math.max(1 + pos.getY() - blockpos$mutableblockpos.getY(), 6);
                float f = (float) i;
                fallingblockentity.setHurtsEntities(f, 40);
                break;
            }

            blockpos$mutableblockpos.move(Direction.DOWN);
            blockstate = level.getBlockState(blockpos$mutableblockpos);
        }
    }

    @VisibleForTesting
    public static void growStalactiteOrStalagmiteIfPossible(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockState blockstate = level.getBlockState(pos.above(1));
        BlockState blockstate1 = level.getBlockState(pos.above(2));
        if (canGrow(blockstate, blockstate1)) {
            BlockPos blockpos = findTip(state, level, pos, 7);
            if (blockpos != null) {
                BlockState blockstate2 = level.getBlockState(blockpos);
                if (canDrip(blockstate2) && canTipGrow(blockstate2, level, blockpos)) {
                    if (random.nextBoolean()) {
                        grow(level, blockpos, Direction.DOWN);
                    } else {
                        growStalagmiteBelow(level, blockpos);
                    }
                }
            }
        }
    }

    private static void growStalagmiteBelow(ServerLevel level, BlockPos pos) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = pos.mutable();

        for (int i = 0; i < 10; i++) {
            blockpos$mutableblockpos.move(Direction.DOWN);
            BlockState blockstate = level.getBlockState(blockpos$mutableblockpos);
            if (!blockstate.getFluidState().isEmpty()) {
                return;
            }

            if (isUnmergedTipWithDirection(blockstate, Direction.UP) && canTipGrow(blockstate, level, blockpos$mutableblockpos)) {
                grow(level, blockpos$mutableblockpos, Direction.UP);
                return;
            }

            if (isValidPointedDripstonePlacement(level, blockpos$mutableblockpos, Direction.UP) && !level.isWaterAt(blockpos$mutableblockpos.below())) {
                grow(level, blockpos$mutableblockpos.below(), Direction.UP);
                return;
            }

            if (!canDripThrough(level, blockpos$mutableblockpos, blockstate)) {
                return;
            }
        }
    }

    private static void grow(ServerLevel server, BlockPos pos, Direction direction) {
        BlockPos blockpos = pos.relative(direction);
        BlockState blockstate = server.getBlockState(blockpos);
        if (isUnmergedTipWithDirection(blockstate, direction.getOpposite())) createMergedTips(blockstate, server, blockpos);
        else if (blockstate.isAir() || blockstate.is(Blocks.WATER)) createDripstone(server, blockpos, direction, DripstoneThickness.TIP);
    }

    private static void createDripstone(LevelAccessor level, BlockPos pos, Direction direction, DripstoneThickness thickness) {
        BlockState blockstate = AllBlocks.POINTED_GRIMROCK.get()
                .defaultBlockState().setValue(TIP_DIRECTION, direction)
                .setValue(THICKNESS, thickness).setValue(WATERLOGGED, level.getFluidState(pos).getType() == Fluids.WATER);
        level.setBlock(pos, blockstate, 3);
    }

    private static void createMergedTips(BlockState state, LevelAccessor level, BlockPos pos) {
        BlockPos blockPos;
        BlockPos blockPos1;
        if (state.getValue(TIP_DIRECTION) == Direction.UP) {
            blockPos1 = pos;
            blockPos = pos.above();
        } else {
            blockPos = pos;
            blockPos1 = pos.below();
        }
        createDripstone(level, blockPos, Direction.DOWN, DripstoneThickness.TIP_MERGE);
        createDripstone(level, blockPos1, Direction.UP, DripstoneThickness.TIP_MERGE);
    }

    private static void spawnDripParticle(Level level, BlockPos pos, BlockState state, Fluid p_fluid) {
        Vec3 vec3 = state.getOffset(level, pos);
        double d1 = (double)pos.getX() + 0.5 + vec3.x;
        double d2 = (double)((float)(pos.getY() + 1) - 0.6875F) - 0.0625;
        double d3 = (double)pos.getZ() + 0.5 + vec3.z;
        Fluid fluid = getDripFluid(level, p_fluid);
        ParticleOptions particleoptions = fluid.getFluidType().getDripInfo() != null ? fluid.getFluidType().getDripInfo().dripParticle() : ParticleTypes.DRIPPING_DRIPSTONE_WATER;
        if (particleoptions != null) level.addParticle(particleoptions, d1, d2, d3, 0.0, 0.0, 0.0);
    }

    @Nullable
    private static BlockPos findTip(BlockState state, LevelAccessor level, BlockPos pos, int maxIterations) {
        if (isTip(state, false)) return pos;
        else {
            Direction direction = state.getValue(TIP_DIRECTION);
            BiPredicate<BlockPos, BlockState> bipredicate = (blockPos, blockState) -> blockState.is(AllBlocks.POINTED_GRIMROCK.get()) && blockState.getValue(TIP_DIRECTION) == direction;
            return findBlockVertical(level, pos, direction.getAxisDirection(), bipredicate, blockState -> isTip(blockState, false), maxIterations).orElse(null);
        }
    }

    @Nullable
    private static Direction calculateTipDirection(LevelReader level, BlockPos pos, Direction dir) {
        Direction direction;
        if (isValidPointedDripstonePlacement(level, pos, dir)) direction = dir;
        else {
            if (!isValidPointedDripstonePlacement(level, pos, dir.getOpposite())) return null;
            direction = dir.getOpposite();
        }
        return direction;
    }

    private static DripstoneThickness calculateDripstoneThickness(LevelReader level, BlockPos pos, Direction dir, boolean isTipMerge) {
        Direction direction = dir.getOpposite();
        BlockState blockstate = level.getBlockState(pos.relative(dir));
        if (isPointedDripstoneWithDirection(blockstate, direction)) return !isTipMerge && blockstate.getValue(THICKNESS) != DripstoneThickness.TIP_MERGE ? DripstoneThickness.TIP : DripstoneThickness.TIP_MERGE;
        else if (!isPointedDripstoneWithDirection(blockstate, dir)) return DripstoneThickness.TIP;
        else {
            DripstoneThickness dripstonethickness = blockstate.getValue(THICKNESS);
            if (dripstonethickness != DripstoneThickness.TIP && dripstonethickness != DripstoneThickness.TIP_MERGE) {
                BlockState blockstate1 = level.getBlockState(pos.relative(direction));
                return !isPointedDripstoneWithDirection(blockstate1, dir) ? DripstoneThickness.BASE : DripstoneThickness.MIDDLE;
            } else return DripstoneThickness.FRUSTUM;
        }
    }

    public static boolean canDrip(BlockState state) {
        return isStalactite(state) && state.getValue(THICKNESS) == DripstoneThickness.TIP && !state.getValue(WATERLOGGED);
    }

    private static boolean canTipGrow(BlockState state, ServerLevel level, BlockPos pos) {
        Direction direction = state.getValue(TIP_DIRECTION);
        BlockPos blockpos = pos.relative(direction);
        BlockState blockstate = level.getBlockState(blockpos);
        if (!blockstate.getFluidState().isEmpty()) return false;
        else return blockstate.isAir() || isUnmergedTipWithDirection(blockstate, direction.getOpposite());
    }

    private static Optional<BlockPos> findRootBlock(Level level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(TIP_DIRECTION);
        BiPredicate<BlockPos, BlockState> bipredicate = (p_202015_, p_202016_) -> p_202016_.is(AllBlocks.POINTED_GRIMROCK.get()) && p_202016_.getValue(TIP_DIRECTION) == direction;
        return findBlockVertical(level, pos, direction.getOpposite().getAxisDirection(), bipredicate, p_154245_ -> !p_154245_.is(AllBlocks.POINTED_GRIMROCK.get()), 11);
    }

    private static boolean isValidPointedDripstonePlacement(LevelReader level, BlockPos pos, Direction dir) {
        BlockPos blockpos = pos.relative(dir.getOpposite());
        BlockState blockstate = level.getBlockState(blockpos);
        return blockstate.isFaceSturdy(level, blockpos, dir) || isPointedDripstoneWithDirection(blockstate, dir);
    }

    private static boolean isTip(BlockState state, boolean isTipMerge) {
        if (!state.is(AllBlocks.POINTED_GRIMROCK.get())) {
            return false;
        } else {
            DripstoneThickness dripstonethickness = state.getValue(THICKNESS);
            return dripstonethickness == DripstoneThickness.TIP || isTipMerge && dripstonethickness == DripstoneThickness.TIP_MERGE;
        }
    }

    private static boolean isUnmergedTipWithDirection(BlockState state, Direction dir) {
        return isTip(state, false) && state.getValue(TIP_DIRECTION) == dir;
    }

    private static boolean isStalactite(BlockState state) {
        return isPointedDripstoneWithDirection(state, Direction.DOWN);
    }

    private static boolean isStalagmite(BlockState state) {
        return isPointedDripstoneWithDirection(state, Direction.UP);
    }

    private static boolean isStalactiteStartPos(BlockState state, LevelReader level, BlockPos pos) {
        return isStalactite(state) && !level.getBlockState(pos.above()).is(AllBlocks.POINTED_GRIMROCK.get());
    }

    private static boolean isPointedDripstoneWithDirection(BlockState state, Direction dir) {
        return state.is(AllBlocks.POINTED_GRIMROCK.get()) && state.getValue(TIP_DIRECTION) == dir;
    }

    @Nullable
    private static BlockPos findFillableCauldronBelowStalactiteTip(Level level, BlockPos pos, Fluid fluid) {
        Predicate<BlockState> predicate = state -> state.getBlock() instanceof AbstractCauldronBlock
                && ((AbstractCauldronBlockAccessor) state.getBlock()).canReceiveStalactiteDrip$OmniUtils(fluid);
        BiPredicate<BlockPos, BlockState> bipredicate = (p_202034_, p_202035_) -> canDripThrough(level, p_202034_, p_202035_);
        return findBlockVertical(level, pos, Direction.DOWN.getAxisDirection(), bipredicate, predicate, 11).orElse(null);
    }

    @Nullable
    public static BlockPos findStalactiteTipAboveCauldron(Level level, BlockPos pos) {
        BiPredicate<BlockPos, BlockState> bipredicate = (p_202030_, p_202031_) -> canDripThrough(level, p_202030_, p_202031_);
        return findBlockVertical(level, pos, Direction.UP.getAxisDirection(), bipredicate, PointedGrimrockBlock::canDrip, 11).orElse(null);
    }

    public static @NotNull Fluid getCauldronFillFluidType(ServerLevel level, BlockPos pos) {
        return getFluidAboveStalactite(level, pos, level.getBlockState(pos))
                .map(info -> info.fluid)
                .filter(PointedGrimrockBlock::canFillCauldron)
                .orElse(Fluids.EMPTY);
    }

    private static Optional<FluidInfo> getFluidAboveStalactite(Level level, BlockPos pos, BlockState state) {
        return !isStalactite(state) ? Optional.empty() : findRootBlock(level, pos, state).map(above -> {
            BlockPos blockpos = above.above();
            BlockState blockstate = level.getBlockState(blockpos);
            Fluid fluid;
            if (blockstate.is(Blocks.MUD) && !level.dimensionType().ultraWarm()) fluid = Fluids.WATER;
            else fluid = level.getFluidState(blockpos).getType();

            return new FluidInfo(blockpos, fluid, blockstate);
        });
    }

    private static boolean canFillCauldron(Fluid fluid) {
        return fluid.getFluidType().getDripInfo() != null;
    }

    private static boolean canGrow(BlockState dripstoneState, BlockState state) {
        return dripstoneState.is(AllBlocks.GRIMROCK) && state.is(Blocks.WATER) && state.getFluidState().isSource();
    }

    private static Fluid getDripFluid(Level level, Fluid fluid) {
        if (fluid.isSame(Fluids.EMPTY)) {
            return level.dimensionType().ultraWarm() ? Fluids.LAVA : Fluids.WATER;
        } else {
            return fluid;
        }
    }

    private static Optional<BlockPos> findBlockVertical(
            LevelAccessor level,
            BlockPos pos,
            Direction.AxisDirection axis,
            BiPredicate<BlockPos, BlockState> positionalStatePredicate,
            Predicate<BlockState> statePredicate,
            int maxIterations
    ) {
        Direction direction = Direction.get(axis, Direction.Axis.Y);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = pos.mutable();

        for (int i = 1; i < maxIterations; i++) {
            blockpos$mutableblockpos.move(direction);
            BlockState blockstate = level.getBlockState(blockpos$mutableblockpos);
            if (statePredicate.test(blockstate)) {
                return Optional.of(blockpos$mutableblockpos.immutable());
            }

            if (level.isOutsideBuildHeight(blockpos$mutableblockpos.getY()) || !positionalStatePredicate.test(blockpos$mutableblockpos, blockstate)) {
                return Optional.empty();
            }
        }

        return Optional.empty();
    }

    private static final VoxelShape REQUIRED_SPACE_TO_DRIP_THROUGH_NON_SOLID_BLOCK = Block.box(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);
    private static boolean canDripThrough(BlockGetter level, BlockPos pos, BlockState state) {
        if (state.isAir()) return true;
        else if (state.isSolidRender(level, pos)) return false;
        else if (!state.getFluidState().isEmpty()) return false;
        else {
            VoxelShape voxelshape = state.getCollisionShape(level, pos);
            return !Shapes.joinIsNotEmpty(REQUIRED_SPACE_TO_DRIP_THROUGH_NON_SOLID_BLOCK, voxelshape, BooleanOp.AND);
        }
    }
    
    record FluidInfo(BlockPos pos, Fluid fluid, BlockState sourceState) {
    }
}
