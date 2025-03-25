package uwu.lopyluna.omni_util.content.items;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.omni_util.content.items.base.BlockBreakingDiggerItem;
import uwu.lopyluna.omni_util.register.AllTags;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static net.minecraft.world.item.HoeItem.changeIntoState;
import static uwu.lopyluna.omni_util.content.items.HammerItem.getGameType;

@ParametersAreNonnullByDefault
public class ScytheItem extends BlockBreakingDiggerItem {
    public int size;
    public int amount;

    public ScytheItem(int size, int amount, Tier tier, Properties properties) {
        super(tier, AllTags.MINEABLE_WITH_SCYTHE, properties);
        this.size = size;
        this.amount = amount;
    }

    @Override
    public void onBlockBreak(ItemStack pStack, Level pLevel, Player pBreaker, BlockPos pPos, BlockState pState, BlockHitResult pRayTrace, BlockEvent.BreakEvent pEvent) {
        if (pBreaker.isShiftKeyDown()) return;
        //if (pState.getBlock() instanceof CropBlock) harvestConnectedCrops(pLevel, pBreaker, pPos, amount * 2, pStack, true);
        if (pState.is(AllTags.BULK_MINEABLE_WITH_SCYTHE)) breakConnectedBlocks(pLevel, pBreaker, pPos, amount, pStack);

    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        var level = context.getLevel();
        var clickedPos = context.getClickedPos();
        var player = context.getPlayer();
        if (player != null && level.getBlockState(clickedPos).getBlock() instanceof CropBlock)
            return harvestConnectedCrops(level, player, clickedPos, amount * 2, context.getItemInHand(), false) ? InteractionResult.sidedSuccess(level.isClientSide) : InteractionResult.PASS;

        var success = false;
        if (!level.getBlockState(clickedPos).is(Blocks.FARMLAND)) for (BlockPos blockpos : BlockPos.betweenClosed(clickedPos.offset(-size, -1, -size), clickedPos.offset(size, 0, size))) {
            var newContext = new UseOnContext(context.getLevel(), context.getPlayer(), context.getHand(), context.getItemInHand(),
                    new BlockHitResult(context.getClickLocation(), context.getClickedFace(), blockpos, context.isInside()));
            BlockState toolModifiedState = level.getBlockState(blockpos).getToolModifiedState(newContext, net.neoforged.neoforge.common.ItemAbilities.HOE_TILL, false);
            Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> pair = toolModifiedState == null ? null : Pair.of(ctx -> true, changeIntoState(toolModifiedState));
            if (pair == null) continue;
            Predicate<UseOnContext> predicate = pair.getFirst();
            Consumer<UseOnContext> consumer = pair.getSecond();
            if (predicate.test(newContext)) {
                if (!success) level.playSound(player, clickedPos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (!level.isClientSide) {
                    consumer.accept(newContext);
                    if (player != null) newContext.getItemInHand().hurtAndBreak(1, player, LivingEntity.getSlotForHand(newContext.getHand()));
                }
                success = true;
            }
        }
        return success ? InteractionResult.sidedSuccess(level.isClientSide) : InteractionResult.PASS;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.neoforged.neoforge.common.ItemAbility itemAbility) {
        return AllItemAbilities.DEFAULT_SCYTHE_ACTIONS.contains(itemAbility);
    }

    private void breakConnectedBlocks(Level level, Player player, BlockPos origin, int maxCount, ItemStack tool) {
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new ArrayDeque<>();
        queue.add(origin);
        visited.add(origin);

        while (!queue.isEmpty() && visited.size() < maxCount) {
            BlockPos current = queue.poll();
            for (Direction dir : Direction.values()) {
                BlockPos neighbor = current.relative(dir);
                var neighborState = level.getBlockState(neighbor);
                if (!visited.contains(neighbor) && neighborState.is(AllTags.BULK_MINEABLE_WITH_SCYTHE)) {
                    if (!(neighborState.getBlock() instanceof CropBlock)) {
                        visited.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
        }

        List<BlockPos> list = new ArrayList<>(visited);
        Collections.shuffle(list);

        for (int i = 0; i < Math.min(maxCount, list.size()); i++) {
            var pos = list.get(i);
            if (pos.equals(origin)) continue;
            var state = level.getBlockState(pos);
            if (state.getBlock() instanceof LeavesBlock) {
                state.setValue(LeavesBlock.PERSISTENT, false);
                state.setValue(LeavesBlock.DISTANCE, 7);
                level.setBlock(pos, state, 2);
                Block.dropResources(state, level, pos);
                Block.dropResources(state, level, pos);
                level.destroyBlock(pos, false);
            } else breakBlock(tool, level, player, pos, state);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private boolean harvestConnectedCrops(Level level, Player player, BlockPos origin, int maxCount, ItemStack tool, boolean center) {
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new ArrayDeque<>();
        queue.add(origin);
        visited.add(origin);

        while (!queue.isEmpty() && visited.size() < maxCount) {
            BlockPos current = queue.poll();
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                BlockPos neighbor = current.relative(dir);
                if (!visited.contains(neighbor)) {
                    BlockState state = level.getBlockState(neighbor);
                    if (state.getBlock() instanceof CropBlock crop && crop.isMaxAge(state)) {
                        visited.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
        }

        List<BlockPos> list = new ArrayList<>(visited);
        Collections.shuffle(list);

        var success = false;
        for (int i = 0; i < Math.min(maxCount, list.size()); i++) {
            BlockPos pos = list.get(i);
            BlockState state = level.getBlockState(pos);
            CropBlock crop = (CropBlock) state.getBlock();

            //breakBlock(tool, level, player, pos, state);
            final boolean[] gatheredSeed = {false};
            if (!center && level instanceof ServerLevel serverLevel) Block.getDrops(state, serverLevel, pos, null, player, tool)
                    .forEach(drop -> {
                        if (drop.is(Tags.Items.SEEDS) && !gatheredSeed[0]) {
                            gatheredSeed[0] = true;
                            drop.shrink(1);
                        }
                        Block.popResource(level, pos, drop);
                    });

            if (gatheredSeed[0] || center) level.setBlock(pos, crop.getStateForAge(0), 3);
            else level.destroyBlock(pos, false, player);
            success = true;
        }
        return success;
    }

    public static void breakBlock(ItemStack pStack, Level pLevel, Player pBreaker, BlockPos pPos, BlockState pState) {
        if (pState.isAir() || pState.getDestroySpeed(pLevel, pPos) <= -1) return;
        Block block = pState.getBlock();
        if (block instanceof GameMasterBlock && !pBreaker.canUseGameMasterBlocks()) {
            pLevel.sendBlockUpdated(pPos, pState, pState, 3);
            return;
        }

        var type = getGameType(pBreaker);
        if (type != null && pBreaker.blockActionRestricted(pLevel, pPos, type)) return;

        if (pBreaker.isCreative()) pLevel.destroyBlock(pPos, false, pBreaker);
        else {
            ItemStack itemstack1 = pStack.copy();
            boolean flag1 = pState.canHarvestBlock(pLevel, pPos, pBreaker);
            pStack.mineBlock(pLevel, pState, pPos, pBreaker);
            boolean flag = pLevel.destroyBlock(pPos, false, pBreaker);

            if (flag1 && flag) pState.getBlock().playerDestroy(pLevel, pBreaker, pPos, pState, pLevel.getBlockEntity(pPos), itemstack1);
            if (pStack.isEmpty() && !itemstack1.isEmpty())
                net.neoforged.neoforge.event.EventHooks.onPlayerDestroyItem(pBreaker, itemstack1, InteractionHand.MAIN_HAND);
        }
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        super.postHurtEnemy(stack, target, attacker);
    }
}
