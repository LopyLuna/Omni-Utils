package uwu.lopyluna.omni_util.content.items.wands;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class WandItem extends Item {
    public WandItem(Properties properties) {
        super(properties.durability(4096));
    }

    public void processBlocks(ItemStack stack, ItemStack stackOff, Level level, Player player, Direction face, BlockPos origin, BlockState state, List<BlockPos> positions) {
    }

    public boolean outsideBlocks() {
        return true;
    }

    //0 FAILED
    //1 FAILED WITH EFFECTS
    //2 SUCCESS
    public int isValid(BlockState state, Direction face, Level level, BlockPos pos) {
        return 2;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        var player = context.getPlayer();
        if (player == null) return super.useOn(context);
        var level = context.getLevel();
        var origin = context.getClickedPos();
        var face = context.getClickedFace();
        var stack = context.getItemInHand();
        var stackOff = player.getItemInHand(InteractionHand.OFF_HAND);
        var state = level.getBlockState(origin);
        if (!state.isAir()) processBlocks(stack, stackOff, level, player, face, origin, state, getConnectedBlocks(level, face, player, state, origin, this));
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public int getMaxCount(Level level, Direction face, Player player, BlockState state, BlockPos pos) {
        var shifting = player.isShiftKeyDown();
        var creative = player.isCreative() && !player.isBlocking();
        var spectator = player.isSpectator();
        return creative ? shifting ? 128 : 256 : spectator ? 0 : shifting ? 64 : 128;
    }

    public int getMaxRange(Level level, Direction face, Player player, BlockState state, BlockPos pos) {
        var shifting = player.isShiftKeyDown();
        var creative = player.isCreative() && !player.isBlocking();
        var spectator = player.isSpectator();
        return creative ? shifting ? 32 : 64 : spectator ? 0 : shifting ? 16 : 32;
    }

    public static List<BlockPos> getConnectedBlocks(Level level, Direction face, Player player, BlockState state, BlockPos pos, WandItem item) {
        var origin = item.outsideBlocks() ? pos.relative(face) : pos;
        var originState = (item.outsideBlocks() ? level.getBlockState(pos.relative(face)) : state);
        var maxCount = item.getMaxCount(level, face, player, state, pos);
        int maxRange = item.getMaxRange(level, face, player, state, pos);
        if (maxCount == 0) return List.of();
        var eye = player.getEyePosition();

        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new ArrayDeque<>();
        visited.add(origin);
        queue.add(origin);

        while (!queue.isEmpty() && visited.size() < maxCount) {
            var current = queue.poll();

            for (Direction dir : Direction.values()) {
                if (dir.getAxis().equals(face.getAxis())) continue;
                var neighbor = current.relative(dir);
                if (visited.contains(neighbor)) continue;
                if (eye.distanceToSqr(neighbor.getCenter()) > maxRange) continue;
                //if (item.outsideBlocks() && !level.getBlockState(neighbor.relative(face.getOpposite())).isAir()) continue;
                if (!item.outsideBlocks() && !level.getBlockState(neighbor.relative(face)).isAir()) continue;
                if (item.outsideBlocks()) {
                    if (!state.is(level.getBlockState(neighbor.relative(face.getOpposite())).getBlock())) continue;
                }

                var neighborState = level.getBlockState(neighbor);
                if (originState.is(neighborState.getBlock()) && item.isValid(neighborState, face, level, neighbor) == 2) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        if (!item.outsideBlocks()) visited.remove(origin);
        return new ArrayList<>(visited);
    }
}
