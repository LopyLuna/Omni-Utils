package uwu.lopyluna.omni_util.content.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AngelBlock extends Block {
    public static final MapCodec<AngelBlock> CODEC = simpleCodec(AngelBlock::new);
    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
    public AngelBlock(Properties properties) {
        super(properties.randomTicks());
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        level.scheduleTick(pos, this, 4);
        super.onPlace(state, level, pos, oldState, movedByPiston);
    }

    @Override
    protected @NotNull MapCodec<? extends Block> codec() {
        return CODEC;
    }
    protected IntegerProperty getAgeProperty() {
        return AGE;
    }
    public int getMaxAge() {
        return 7;
    }
    public int getAge(BlockState state) {
        return state.getValue(this.getAgeProperty());
    }
    public final boolean isMaxAge(BlockState state) {
        return this.getAge(state) >= this.getMaxAge();
    }
    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return !this.isMaxAge(state);
    }

    public BlockState getStateForAge(int age) {
        return this.defaultBlockState().setValue(this.getAgeProperty(), age);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        level.scheduleTick(pos, this, 4);
        if (random.nextBoolean() || random.nextBoolean()) return;

        var id = random.nextInt(999999);
        if (!level.isAreaLoaded(pos, 1)) return;
        int i = this.getAge(state);
        int max = this.getMaxAge();
        if (i >= (max-1)) {
            level.destroyBlockProgress(id, pos, -1);
            level.destroyBlock(pos, true);
        } else {
            double progress = (double) i / (double) max;
            level.setBlock(pos, this.getStateForAge(i + 1), 2);
            level.destroyBlockProgress(id, pos, (int) (progress * 10.0));
            level.playSeededSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    state.getSoundType().getHitSound(), SoundSource.BLOCKS, .25f, 1, level.random.nextLong());
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }
}
