package uwu.lopyluna.omni_util.content.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.omni_util.content.AllUtils;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ClockBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<ClockBlock> CODEC = simpleCodec(ClockBlock::new);
    public static final BooleanProperty LOCKED = BlockStateProperties.LOCKED;
    public static final IntegerProperty HOUR = IntegerProperty.create("hour", 0, 11);
    public ClockBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(HOUR, 0).setValue(LOCKED, true).setValue(FACING, Direction.NORTH));
    }
    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        level.scheduleTick(pos, this, 1);
        if (!state.getValue(LOCKED)) return;
        level.setBlockAndUpdate(pos, state.setValue(LOCKED, false));
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        var hour = AllUtils.getHourOfTime(level);
        var am = AllUtils.isAmOfTime(level);
        player.displayClientMessage(Component.literal(AllUtils.TimeCycle.fromHour(hour, am).getLabel(hour, am)), true);
        if (level.isClientSide) return InteractionResult.SUCCESS;
        else return InteractionResult.CONSUME;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.getValue(LOCKED)) return;
        level.setBlockAndUpdate(pos, state.setValue(LOCKED, false));
        level.scheduleTick(pos, this, 1);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(LOCKED)) return;
        var hour = AllUtils.getHourOfTime(level);
        hour = hour == 12 ? 0 : hour;
        if (state.getValue(HOUR) != hour) level.setBlockAndUpdate(pos, state.setValue(HOUR, hour));
        level.scheduleTick(pos, this, 5);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        var level = context.getLevel();
        var hour = AllUtils.getHourOfTime(level);
        hour = hour == 12 ? 0 : hour;
        return this.defaultBlockState().setValue(HOUR, hour).setValue(FACING, context.getNearestLookingDirection().getOpposite()).setValue(LOCKED, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LOCKED, HOUR);
    }

    @Override
    protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
}
