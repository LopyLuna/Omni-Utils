package uwu.lopyluna.omni_util.content.items.wands;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.event.EventHooks;

import java.util.List;

import static uwu.lopyluna.omni_util.content.AllUtils.fixStateByContext;

public class PlacerWandItem extends WandItem {
    public PlacerWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getMaxCount(Level level, Direction face, Player player, BlockState state, BlockPos pos) {
        var paintState = state;
        var result = PlacerWandItem.getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        var stackOff = player.getItemInHand(InteractionHand.OFF_HAND);
        var offItem = stackOff.getItem();
        if (offItem instanceof BlockItem block) paintState = fixStateByContext(block.getBlock().defaultBlockState(), player, result.withPosition(pos));
        else paintState = fixStateByContext(paintState, player, result.withPosition(pos));

        var maxCount = super.getMaxCount(level, face, player, state, pos);
        return player.getAbilities().instabuild ? maxCount : paintState != null && !paintState.isAir() ? Mth.clamp(findWhatInInventory(paintState, player, maxCount), 0, maxCount) : 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void processBlocks(ItemStack stack, ItemStack stackOff, Level level, Player player, Direction face, BlockPos origin, BlockState state, List<BlockPos> positions) {
        var paintState = state.getBlock().defaultBlockState();
        var hit = PlacerWandItem.getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        hit.withDirection(face);

        var item = stackOff.getItem();
        var newState = paintState;
        if (item instanceof BlockItem block) newState = block.getBlock().defaultBlockState();

        for (var pos : positions) {
            if (!state.equals(newState)) paintState = fixStateByContext(newState, player, hit.withPosition(pos));
            else paintState = fixStateByContext(paintState, player, hit.withPosition(pos));
            if (paintState == null) continue;
            if (paintState instanceof EntityBlock) continue;
            if (check(level, pos)) continue;

            if (!player.getAbilities().instabuild && findAndRemoveInInventory(paintState, player, 1) == 0) break;
            var itemstack1 = stack.copy();
            boolean flag = level.setBlockAndUpdate(pos, paintState);
            level.gameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of(player, paintState));
            if (flag) {
                var asItem = paintState.getBlock().asItem();
                var asStack = new ItemStack(asItem);
                asStack.setCount(asStack.getMaxStackSize());
                paintState.getBlock().setPlacedBy(level, pos, paintState, player, asStack);
                player.awardStat(Stats.ITEM_USED.get(asItem));
                player.causeFoodExhaustion(0.005F);
                if (pos.equals(origin.relative(face))) {
                    var soundType = paintState.getSoundType();
                    level.playSound(player, origin, soundType.getPlaceSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
                } else if (level.random.nextInt(10) == 0) {
                    var soundType = paintState.getSoundType();
                    level.playSound(player, pos, soundType.getPlaceSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
                }
            }
            if (stack.isEmpty() && !itemstack1.isEmpty()) EventHooks.onPlayerDestroyItem(player, itemstack1, InteractionHand.MAIN_HAND);
        }
    }

    public boolean check(Level level, BlockPos pos) {
        if (!level.isAreaLoaded(pos, 1)) return true;
        return !level.isInWorldBounds(pos);
    }

    @Override
    public int isValid(BlockState state, Direction face, Level level, BlockPos pos) {
        if (!level.isAreaLoaded(pos, 1)) return 0;
        if (!level.isInWorldBounds(pos)) return 0;
        if (state.canBeReplaced()) return 2;
        if (!state.isAir()) return 0;
        return 2;
    }

    //Mostly Copied from BlockHelper from Create
    private static final List<IntegerProperty> COUNT_STATES = List.of(BlockStateProperties.EGGS, BlockStateProperties.PICKLES, BlockStateProperties.CANDLES);
    private static final List<Block> VINE_LIKE_BLOCKS = List.of(Blocks.VINE, Blocks.GLOW_LICHEN);
    private static final List<BooleanProperty> VINE_LIKE_STATES = List.of(BlockStateProperties.UP, BlockStateProperties.NORTH, BlockStateProperties.EAST, BlockStateProperties.SOUTH, BlockStateProperties.WEST, BlockStateProperties.DOWN);

    public static int findAndRemoveInInventory(BlockState block, Player player, int amount) {
        return findOrRemoveInInventory(block, player, amount, false);
    }

    public static int findWhatInInventory(BlockState block, Player player, int amount) {
        return findOrRemoveInInventory(block, player, amount, true);
    }

    public static int findOrRemoveInInventory(BlockState block, Player player, int amount, boolean visual) {
        int amountFound = 0;
        var required = getRequiredItem(block).getItem();
        boolean needsTwo = block.hasProperty(BlockStateProperties.SLAB_TYPE) && block.getValue(BlockStateProperties.SLAB_TYPE) == SlabType.DOUBLE;
        if (needsTwo) amount *= 2;

        for (var property : COUNT_STATES) if (block.hasProperty(property)) amount *= block.getValue(property);

        if (VINE_LIKE_BLOCKS.contains(block.getBlock())) {
            int vineCount = 0;
            for (var vineState : VINE_LIKE_STATES) if (block.hasProperty(vineState) && block.getValue(vineState)) vineCount++;
            amount += vineCount - 1;
        }

        int preferredSlot = player.getInventory().selected;
        var itemstack = player.getInventory().getItem(preferredSlot);
        int count = itemstack.getCount();
        if (itemstack.getItem() == required && count > 0) {
            int taken = Math.min(count, amount - amountFound);
            if (!visual) player.getInventory().setItem(preferredSlot, new ItemStack(itemstack.getItem(), count - taken));
            amountFound += taken;
        }

        for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
            if (amountFound == amount) break;
            var itemstack1 = player.getInventory().getItem(i);
            int count1 = itemstack1.getCount();
            if (itemstack1.getItem() == required && count1 > 0) {
                int taken = Math.min(count1, amount - amountFound);
                if (!visual) player.getInventory().setItem(i, new ItemStack(itemstack1.getItem(), count1 - taken));
                amountFound += taken;
            }
        }

        if (needsTwo) {
            if (!visual) if (amountFound % 2 != 0) player.getInventory().add(new ItemStack(required));
            amountFound /= 2;
        }
        return amountFound;
    }

    public static ItemStack getRequiredItem(BlockState state) {
        var itemStack = new ItemStack(state.getBlock());
        var item = itemStack.getItem();
        if (item == Items.FARMLAND || item == Items.DIRT_PATH) itemStack = new ItemStack(Items.DIRT);
        return itemStack;
    }
}
