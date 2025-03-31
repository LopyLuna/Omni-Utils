package uwu.lopyluna.omni_util.content.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uwu.lopyluna.omni_util.register.AllBlocks;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class InteractorBlock extends DirectionalBlock {
    public static final MapCodec<InteractorBlock> CODEC = simpleCodec(InteractorBlock::new);
    public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;

    public InteractorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.SOUTH).setValue(ENABLED, false));
    }

    public void interact(BlockState state, Level level, BlockPos pos, @Nullable Player player, boolean activate) {
        if (state.getValue(ENABLED) == activate) return;
        level.setBlock(pos, state.setValue(ENABLED, activate), 3);
        this.updateNeighbours(state, level, pos);
        if (activate) level.scheduleTick(pos, this, 40);
        level.playSound(player, pos, activate ? SoundEvents.COPPER_BULB_TURN_OFF : SoundEvents.COPPER_BULB_TURN_ON, SoundSource.BLOCKS, 1f, activate ? 0.7f : 0.9f);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        interact(state, level, pos, null, false);
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                                       Player player, InteractionHand hand, BlockHitResult hitResult) {
        var instance = super.useItemOn(stack, state, level, pos, player, hand, hitResult);
        if (player.getItemInHand(hand).is(AllBlocks.INTERACTOR.asItem())) return instance;
        if (!level.isAreaLoaded(pos, 1)) return instance;
        var facing = state.getValue(FACING);
        if (!hitResult.getDirection().equals(facing)) return instance;
        var newPos = pos.relative(facing);
        if (!player.canInteractWithBlock(newPos, 1.0)) return instance;
        if (!level.isAreaLoaded(newPos, 1)) return instance;
        interact(state, level, pos, player, true);
        var newState = level.getBlockState(newPos);
        var newLoc = hitResult.getLocation();
        newLoc = newLoc.subtract(Vec3.atLowerCornerOf(pos));
        newLoc = newLoc.add(Vec3.atLowerCornerOf(newPos));
        var newHitResult = hitResult.getType().equals(HitResult.Type.MISS) ?
                BlockHitResult.miss(newLoc, hitResult.getDirection(), newPos) :
                new BlockHitResult(newLoc, hitResult.getDirection(), newPos, hitResult.isInside());
        return newState.useItemOn(stack, level, player, hand, newHitResult);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                                        Player player, BlockHitResult hitResult) {
        var instance = super.useWithoutItem(state, level, pos, player, hitResult);
        if (player.getMainHandItem().is(AllBlocks.INTERACTOR.asItem()) || player.getOffhandItem().is(AllBlocks.INTERACTOR.asItem())) return instance;
        var facing = state.getValue(FACING);
        if (!level.isAreaLoaded(pos, 1)) return instance;
        var newPos = pos.relative(facing);
        if (!player.canInteractWithBlock(newPos, 1.0)) return instance;
        if (!level.isAreaLoaded(newPos, 1)) return instance;
        interact(state, level, pos, player, true);
        var newState = level.getBlockState(newPos);
        var newLoc = hitResult.getLocation();
        newLoc = newLoc.subtract(Vec3.atLowerCornerOf(pos));
        newLoc = newLoc.add(Vec3.atLowerCornerOf(newPos));
        var newHitResult = hitResult.getType().equals(HitResult.Type.MISS) ?
                BlockHitResult.miss(newLoc, hitResult.getDirection(), newPos) :
                new BlockHitResult(newLoc, hitResult.getDirection(), newPos, hitResult.isInside());
        return newState.useWithoutItem(level, player, newHitResult);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ENABLED);
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
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite().getOpposite());
    }

    private void updateNeighbours(BlockState state, Level level, BlockPos pos) {
        level.updateNeighborsAt(pos, this);
        level.updateNeighborsAt(pos.relative(state.getValue(FACING)), this);
    }

    @Override
    protected @NotNull MapCodec<? extends DirectionalBlock> codec() { return CODEC; }
}
