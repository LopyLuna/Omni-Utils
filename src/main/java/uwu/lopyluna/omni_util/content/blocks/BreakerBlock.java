package uwu.lopyluna.omni_util.content.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class BreakerBlock extends DirectionalBlock {
    public static final MapCodec<BreakerBlock> CODEC = simpleCodec(BreakerBlock::new);
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

    public BreakerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(TRIGGERED, false));
    }

    public int isValid(BlockState state, Level level, BlockPos pos) {
        if (state.isAir()) return 0;
        if (!level.isAreaLoaded(pos, 1)) return 0;
        if (!level.isInWorldBounds(pos)) return 0;
        if (state.getBlock() instanceof GameMasterBlock) return 1;
        if (state.canBeReplaced()) return 2;
        var speed = state.getDestroySpeed(level, pos);
        if (speed <= -1) return 1;
        if (speed > 80) return 1;
        if (state.is(BlockTags.INCORRECT_FOR_NETHERITE_TOOL)) return 1;
        return 2;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        var facing = state.getValue(FACING).getOpposite();
        var offPos = pos.relative(facing);
        var offState = level.getBlockState(offPos);
        var i = isValid(offState, level, offPos);
        if (i == 2) level.destroyBlock(offPos, true);
        else if (i == 1) {
            var type = offState.getSoundType();
            level.playSound(null, pos, type.getHitSound(), SoundSource.BLOCKS, (type.getVolume() + 1.0F) / 2.0F, type.getPitch() * 0.8F);
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        var facing = state.getValue(FACING);
        boolean flag = level.hasNeighborSignal(pos.relative(facing));
        boolean flag1 = state.getValue(TRIGGERED);
        if (flag && !flag1) {
            level.scheduleTick(pos, this, 4);
            level.setBlock(pos, state.setValue(TRIGGERED, true), 2);
            updateNeighbours(state, level, pos);
        } else if (!flag && flag1) {
            level.setBlock(pos, state.setValue(TRIGGERED, false), 2);
            updateNeighbours(state, level, pos);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TRIGGERED);
    }

    @Override
    protected @NotNull BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    protected @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    private void updateNeighbours(BlockState state, Level level, BlockPos pos) {
        level.updateNeighborsAt(pos, this);
        level.updateNeighborsAt(pos.relative(state.getValue(FACING).getOpposite()), this);
    }

    @Override
    protected @NotNull MapCodec<? extends DirectionalBlock> codec() { return CODEC; }
}
