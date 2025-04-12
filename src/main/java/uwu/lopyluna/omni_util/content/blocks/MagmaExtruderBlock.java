package uwu.lopyluna.omni_util.content.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class MagmaExtruderBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<MagmaExtruderBlock> CODEC = simpleCodec(MagmaExtruderBlock::new);
    public static final BooleanProperty LOCKED = BlockStateProperties.LOCKED;
    public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;
    public static final BooleanProperty HAS_COBBLE = BooleanProperty.create("has_cobble");
    public MagmaExtruderBlock(Properties properties) {
        super(properties.randomTicks());
        this.registerDefaultState(this.stateDefinition.any().setValue(HAS_COBBLE, false).setValue(LOCKED, true).setValue(ENABLED, false).setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        level.scheduleTick(pos, this, 20);
        if (!oldState.is(state.getBlock())) this.checkPoweredState(level, pos, state);
        if (!state.getValue(LOCKED)) return;
        level.setBlockAndUpdate(pos, state.setValue(LOCKED, false));
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.getValue(LOCKED)) return;
        level.setBlockAndUpdate(pos, state.setValue(LOCKED, false));
        level.scheduleTick(pos, this, 20);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(LOCKED)) return;
        var above = pos.above();
        if (state.getValue(ENABLED)) {
            if (state.getValue(HAS_COBBLE)) {
                var aboveState = level.getBlockState(above);
                if (aboveState.isAir() || aboveState.canBeReplaced()) {
                    level.setBlockAndUpdate(above, Blocks.COBBLESTONE.defaultBlockState());
                    level.setBlockAndUpdate(pos, state.setValue(HAS_COBBLE, false));
                    level.playSound(null, above, SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 0.9F, 0.8F);
                    level.playSound(null, above, SoundEvents.LAVA_POP, SoundSource.BLOCKS, 0.3F, 0.8F);
                }
            } else {
                level.setBlockAndUpdate(pos, state.setValue(HAS_COBBLE, true));
                level.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (random.nextFloat() - random.nextFloat()) * 0.8F);
            }
        }
        level.scheduleTick(pos, this, 40);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        var player = context.getPlayer();
        var dir = player != null ? player.getDirection().getOpposite() : Direction.NORTH;
        return this.defaultBlockState().setValue(ENABLED, false).setValue(FACING, dir).setValue(HAS_COBBLE, false).setValue(LOCKED, false);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        this.checkPoweredState(level, pos, state);
    }

    private void checkPoweredState(Level level, BlockPos pos, BlockState state) {
        boolean flag = level.hasNeighborSignal(pos);
        if (flag != state.getValue(ENABLED)) level.setBlock(pos, state.setValue(ENABLED, flag), 3);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ENABLED, HAS_COBBLE, LOCKED);
    }

    @Override
    protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
}
