package uwu.lopyluna.omni_util.content.items;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import uwu.lopyluna.omni_util.content.items.base.BlockBreakingDiggerItem;
import uwu.lopyluna.omni_util.register.AllTags;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class HammerItem extends BlockBreakingDiggerItem {
    public int size;
    public HammerItem(int size, Tier tier, Properties properties) {
        super(tier, AllTags.MINEABLE_WITH_HAMMER, properties);
        this.size = size;
    }

    //*public void destroyProgress(BlockState state, BlockPos pos, BlockPos center, Level level, Player player) {
    //*    if (!(player instanceof ServerPlayer serverPlayer)) return;
    //*    var server = (ServerPlayerGameModeAccessor) serverPlayer.gameMode;
    //*    int i = server.gameTicks$OmniUtils() - server.delayedTickStart$OmniUtils();
    //*    float f = state.getDestroyProgress(player, player.level(), center) * (float)(i + 1);
    //*    int j = (int)(f * 10.0F);
    //*    if (j != server.lastSentState$OmniUtils()) level.destroyBlockProgress(player.getId() + pos.hashCode(), pos, j);
    //*    else level.destroyBlockProgress(player.getId() + pos.hashCode(), pos, -1);
    //*}

    @Override
    public void onBlockBreak(ItemStack pStack, Level pLevel, Player pBreaker, BlockPos pPos, BlockState pState, BlockHitResult pRayTrace, BlockEvent.BreakEvent pEvent) {
        if (pBreaker.isShiftKeyDown()) return;
        int radius = size;
        Direction face = pRayTrace.getDirection();
        for (BlockPos pos : getAoEBox(pPos, face, radius)) {
            if (pos.equals(pPos)) continue;
            BlockState state = pLevel.getBlockState(pos);
            float hardness = state.getDestroySpeed(pLevel, pos);
            if (state.isAir() || hardness <= -1) continue;
            if (hardness > pState.getDestroySpeed(pLevel, pPos) + 2) continue;
            BlockEntity blockentity = pLevel.getBlockEntity(pos);
            Block block = state.getBlock();
            if (block instanceof GameMasterBlock && !pBreaker.canUseGameMasterBlocks()) {
                pLevel.sendBlockUpdated(pos, state, state, 3);
                continue;
            }

            var type = getGameType(pBreaker);
            if (type != null && pBreaker.blockActionRestricted(pLevel, pos, type)) continue;


            //*pLevel.levelEvent(pBreaker, 2001, pos, Block.getId(state));
            //*pLevel.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(pBreaker, state));
            if (state.is(BlockTags.GUARDED_BY_PIGLINS)) PiglinAi.angerNearbyPiglins(pBreaker, false);
            if (pBreaker.isCreative()) pLevel.destroyBlock(pos, false, pBreaker);
            else {
                ItemStack itemstack1 = pStack.copy();
                boolean flag1 = state.canHarvestBlock(pLevel, pos, pBreaker);
                pStack.mineBlock(pLevel, state, pos, pBreaker);
                boolean flag = pLevel.destroyBlock(pos, false, pBreaker);

                if (flag1 && flag) block.playerDestroy(pLevel, pBreaker, pos, state, blockentity, itemstack1);
                if (pStack.isEmpty() && !itemstack1.isEmpty())
                    net.neoforged.neoforge.event.EventHooks.onPlayerDestroyItem(pBreaker, itemstack1, InteractionHand.MAIN_HAND);
            }

        }
    }

    @Override
    public float breakingSpeed(ItemStack pStack, Level pLevel, Player pBreaker, BlockPos pPos, BlockState pState, BlockHitResult pRayTrace, float pNewSpeed, float pOriginalSpeed, PlayerEvent.BreakSpeed pEvent) {
        if (pBreaker.isShiftKeyDown()) return pNewSpeed;
        int radius = size;
        Direction face = pRayTrace.getDirection();
        List<BlockPos> positions = getAoEBox(pPos, face, radius);
        float totalHardness = 0f;
        int validBlocks = 0;

        for (BlockPos pos : positions) {
            if (pos == pPos) continue;
            BlockState state = pLevel.getBlockState(pos);
            float hardness = state.getDestroySpeed(pLevel, pos);
            if (state.isAir() || hardness <= -1) continue;

            Block block = state.getBlock();
            if (block instanceof GameMasterBlock && !pBreaker.canUseGameMasterBlocks()) continue;
            var type = getGameType(pBreaker);
            if (type != null && pBreaker.blockActionRestricted(pLevel, pos, type)) continue;
            if (!state.canHarvestBlock(pLevel, pos, pBreaker)) continue;

            if (hardness > pState.getDestroySpeed(pLevel, pPos) + 2) continue;

            if (hardness >= 0) {
                totalHardness += hardness;
                validBlocks++;
            }
        }
        totalHardness = totalHardness * (Mth.clamp(100 / totalHardness, 0.01f, 1));
        if (validBlocks == 0 || totalHardness == 0) return pNewSpeed;
        float effectiveSpeed = pNewSpeed / totalHardness;
        return Mth.clamp(effectiveSpeed, 0.01f, pNewSpeed);
    }

    public static GameType getGameType(Player player) {
        GameType type;
        if (player.level().isClientSide) {
            var connection = Minecraft.getInstance().getConnection();
            var info = connection != null ? connection.getPlayerInfo(player.getUUID()) : null;
            type = info != null ? info.getGameMode() : null;
        } else type = ((ServerPlayer) player).gameMode.getGameModeForPlayer();
        return type;
    }

    public List<BlockPos> getAoEBox(BlockPos center, Direction face, int radius) {
        List<BlockPos> positions = new ArrayList<>();
        for (int dx = -radius; dx <= radius; dx++) for (int dy = -radius; dy <= radius; dy++) {
            BlockPos offset = center;
            switch (face.getAxis()) {
                case Y -> offset = center.offset(dx, 0, dy);
                case Z -> offset = center.offset(dx, dy, 0);
                case X -> offset = center.offset(0, dy, dx);
            }
            positions.add(offset);
        }
        return positions;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.neoforged.neoforge.common.ItemAbility itemAbility) {
        return AllItemAbilities.DEFAULT_HAMMER_ACTIONS.contains(itemAbility);
    }
}
