package uwu.lopyluna.omni_util.content.blocks.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class PanelBlock extends PowerBlockBlock {
    public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;
    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 7.0, 16.0);
    public PanelBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ENABLED,false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ENABLED);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        if (state.getValue(ENABLED)) {
            if (random.nextInt(20) == 0 && random.nextBoolean() && random.nextBoolean()) level.playLocalSound((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5,
                    SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 0.1F + random.nextFloat(), random.nextFloat() * 0.7F + 0.6F, false);

            if (random.nextInt(2) == 0) for (int i = 0; i < random.nextInt(1) + 1; i++) level.addParticle(ParticleTypes.END_ROD,
                    (double)pos.getX() + 0.5 + random.nextDouble() / 2.0 * (double)(random.nextBoolean() ? 1 : -1),
                    (double)pos.getY() + 0.5,
                    (double)pos.getZ() + 0.5 + random.nextDouble() / 2.0 * (double)(random.nextBoolean() ? 1 : -1),
                    0.0, 0.01, 0.0
            );
        }
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }
}
