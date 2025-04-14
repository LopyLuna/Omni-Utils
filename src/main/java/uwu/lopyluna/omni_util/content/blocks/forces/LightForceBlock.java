package uwu.lopyluna.omni_util.content.blocks.forces;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import uwu.lopyluna.omni_util.content.blocks.CollisionBlock;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class LightForceBlock extends CollisionBlock {
    public LightForceBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasCollision(BlockGetter pLevel, BlockState pState, BlockPos pPos, Entity pEntity, CollisionContext pContext) {
        return !pEntity.getType().getCategory().isFriendly();
    }
}
