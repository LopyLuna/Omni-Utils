package uwu.lopyluna.omni_util.content.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uwu.lopyluna.omni_util.register.AllBlocks;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class GlowrockBlock extends Block {
    public static final IntegerProperty GLOWING = IntegerProperty.create("glowing", 0, 2);

    public GlowrockBlock(Properties properties) {
        super(properties.randomTicks());
        this.registerDefaultState(this.getStateDefinition().any().setValue(GLOWING, 0));
    }

    @Override
    protected @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return convertState(level, pos);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return convertState(context.getLevel(), context.getClickedPos());
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        if (!level.isAreaLoaded(pos, 1)) return;
        var newState = convertState(level, pos);
        int newValue = newState.getValue(GLOWING);
        int value = state.getValue(GLOWING);
        if (value != newValue) level.setBlockAndUpdate(pos, newState);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    public BlockState convertState(LevelAccessor level, BlockPos pos) {
        var newState = AllBlocks.GLOWROCK.getDefaultState();
        int glowing = 0;
        var area = BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1));
        for (var off : area) if (level.getBlockState(off).is(AllBlocks.GLOWROCK)) glowing++;
        float mul = (float) glowing / 24f;
        int value = (int) (3f * mul);
        value = Mth.clamp(value, 0, 2);
        return newState.setValue(GLOWING, value);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(GLOWING);
    }
}
