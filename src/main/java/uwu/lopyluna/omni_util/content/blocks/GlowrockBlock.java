package uwu.lopyluna.omni_util.content.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class GlowrockBlock extends Block {
    public static final IntegerProperty GLOWING = IntegerProperty.create("glowing", 0, 2);

    public GlowrockBlock(Properties properties) {
        super(properties.randomTicks());
        this.registerDefaultState(this.getStateDefinition().any().setValue(GLOWING, 0));
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        int value = state.getValue(GLOWING);
        int glowing = 0;
        for (var direction : Direction.values()) if (level.getBlockState(pos.offset(direction.getNormal())).hasProperty(GLOWING)) glowing++;
        if (glowing > 4) state.setValue(GLOWING, 2);
        else if (glowing > 2) state.setValue(GLOWING, 1);
        else state.setValue(GLOWING, 0);
        if (value != state.getValue(GLOWING)) level.setBlock(pos, state, 3);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(GLOWING);
    }
}
