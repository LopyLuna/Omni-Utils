package uwu.lopyluna.omni_util.content.blocks.power_crank;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.omni_util.content.blocks.base.OmniBlockEntity;
import uwu.lopyluna.omni_util.content.blocks.base.PowerBlockBlock;
import uwu.lopyluna.omni_util.register.AllBlockEntities;

public class PowerCrankBlock extends PowerBlockBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public PowerCrankBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getClickedFace());
    }

    @NotNull
    @Override
    public BlockEntityType<? extends OmniBlockEntity> getBlockEntityType() {
        return AllBlockEntities.POWER_CRANK.get();
    }

    protected @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    protected @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
    }
}
