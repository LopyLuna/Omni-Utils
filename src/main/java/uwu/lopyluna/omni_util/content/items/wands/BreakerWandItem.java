package uwu.lopyluna.omni_util.content.items.wands;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import uwu.lopyluna.omni_util.register.AllItems;

import java.util.List;

import static uwu.lopyluna.omni_util.content.items.HammerItem.getGameType;

public class BreakerWandItem extends WandItem {
    public BreakerWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public void processBlocks(ItemStack stack, ItemStack stackOff, Level level, Player player, Direction face, BlockPos origin, BlockState state, List<BlockPos> positions) {
        breakBlock(stack, level, player, origin, state, true);
        for (var pos : positions) breakBlock(stack, level, player, pos, level.getBlockState(pos), false);
    }

    @Override
    public boolean outsideBlocks() {
        return false;
    }

    @Override
    public int isValid(BlockState state, Direction face, Level level, BlockPos pos) {
        if (state.isAir()) return 0;
        if (!level.isAreaLoaded(pos, 1)) return 0;
        if (!level.isInWorldBounds(pos)) return 0;
        if (state.getBlock() instanceof GameMasterBlock) return 1;
        if (state.canBeReplaced()) return 2;
        var speed = state.getDestroySpeed(level, pos);
        if (speed <= -1) return 1;
        if (speed > 50) return 1;
        if (state.is(BlockTags.INCORRECT_FOR_NETHERITE_TOOL)) return 1;
        return 2;
    }

    public static void breakBlock(ItemStack pStack, Level pLevel, Player pBreaker, BlockPos pPos, BlockState pState, boolean displayBreak) {
        var type = getGameType(pBreaker);
        if (type != null && pBreaker.blockActionRestricted(pLevel, pPos, type)) return;
        var random = pLevel.random;

        if (pBreaker.isCreative()) {
            if (displayBreak) pLevel.destroyBlock(pPos, false, pBreaker);
            else if (random.nextBoolean() || random.nextBoolean()) pLevel.removeBlock(pPos, false);
            else pLevel.destroyBlock(pPos, false, pBreaker);
        } else {
            var itemstack1 = pStack.copy();
            var stack = new ItemStack(AllItems.NETHERITE_PAXEL.get());
            stack.set(DataComponents.UNBREAKABLE, new Unbreakable(false));
            var flag2 = !pState.requiresCorrectToolForDrops() || stack.isCorrectToolForDrops(pState);
            var flag1 = NeoForge.EVENT_BUS.post(new PlayerEvent.HarvestCheck(pBreaker, pState, pLevel, pPos, flag2)).canHarvest();
            pStack.hurtAndBreak(2, pBreaker, EquipmentSlot.MAINHAND);
            pBreaker.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            boolean flag;
            if (displayBreak) flag = pLevel.destroyBlock(pPos, false, pBreaker);
            else if (random.nextBoolean() || random.nextBoolean()) flag = pLevel.removeBlock(pPos, false);
            else flag = pLevel.destroyBlock(pPos, false, pBreaker);
            if (flag1 && flag) pState.getBlock().playerDestroy(pLevel, pBreaker, pPos, pState, pLevel.getBlockEntity(pPos), stack);
            if (pStack.isEmpty() && !itemstack1.isEmpty()) net.neoforged.neoforge.event.EventHooks.onPlayerDestroyItem(pBreaker, itemstack1, InteractionHand.MAIN_HAND);
        }
    }
}
