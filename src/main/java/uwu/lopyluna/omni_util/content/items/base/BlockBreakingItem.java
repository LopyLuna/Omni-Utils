package uwu.lopyluna.omni_util.content.items.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.List;

public abstract class BlockBreakingItem extends Item {
    public BlockBreakingItem(Properties properties) {
        super(properties);
    }

    public void onStartLeftClickBlock(ItemStack pStack, Level pLevel, Player pBreaker, BlockPos pPos, BlockState pState, InteractionHand pHand, Direction pFace, BlockHitResult pRayTrace, PlayerInteractEvent.LeftClickBlock pEvent) {
    }
    public void onStoppedLeftClickBlock(ItemStack pStack, Level pLevel, Player pBreaker, BlockPos pPos, BlockState pState, InteractionHand pHand, Direction pFace, BlockHitResult pRayTrace, PlayerInteractEvent.LeftClickBlock pEvent) {
    }
    public void onAbortLeftClickBlock(ItemStack pStack, Level pLevel, Player pBreaker, BlockPos pPos, BlockState pState, InteractionHand pHand, Direction pFace, BlockHitResult pRayTrace, PlayerInteractEvent.LeftClickBlock pEvent) {
    }
    public void onLeftClickBlock(ItemStack pStack, Level pLevel, Player pBreaker, BlockPos pPos, BlockState pState, InteractionHand pHand, Direction pFace, BlockHitResult pRayTrace, PlayerInteractEvent.LeftClickBlock pEvent) {
    }
    public float breakingSpeed(ItemStack pStack, Level pLevel, Player pBreaker, BlockPos pPos, BlockState pState, BlockHitResult pRayTrace, float pNewSpeed, float pOriginalSpeed, PlayerEvent.BreakSpeed pEvent) {
        return pNewSpeed;
    }
    public void onBlockBreak(ItemStack pStack, Level pLevel, Player pBreaker, BlockPos pPos, BlockState pState, BlockHitResult pRayTrace, BlockEvent.BreakEvent pEvent) {
    }
    public void onBlockDrops(ItemStack pStack, Level pLevel, Player pBreaker, BlockPos pPos, BlockState pState, BlockEntity pBlockEntity, List<ItemEntity> pDrops, BlockHitResult pRayTrace, BlockDropsEvent pEvent) {
    }

}
