package uwu.lopyluna.omni_util.content.blocks.base;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class OmniBlock extends BaseOmniBlock {
    protected OmniBlock(Properties properties) {
        super(properties);
    }

    public abstract BlockEntityType<? extends OmniBlockEntity> getBlockEntityType();

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return getBlockEntityType().create(pos, state);
    }
}
