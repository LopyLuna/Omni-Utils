package uwu.lopyluna.omni_util.content.items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BambooLeaves;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.omni_util.register.AllItems;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class WateringCanItem extends Item {
    public WateringCanItem(Properties properties) {
        super(properties.durability(1000));
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.PASS;
        Level level = player.level();
        BlockHitResult ray = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        if (ray.getType() == HitResult.Type.BLOCK) player.startUsingItem(context.getHand());
        return InteractionResult.CONSUME;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (remainingUseDuration < 0 || !(livingEntity instanceof Player player)) { livingEntity.releaseUsingItem(); return; }
        if (!(getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE) instanceof BlockHitResult context && context.getType() == HitResult.Type.BLOCK)) { player.releaseUsingItem(); return; }
        RandomSource random = level.getRandom();

        var empty = AllItems.EMPTY_WATERING_CAN.get();
        var newStack = consumeConvert(stack, random.nextInt(5) + 1, player, empty);
        if (newStack.is(empty)) player.setItemSlot(LivingEntity.getSlotForHand(player.getUsedItemHand()), newStack);

        BlockPos clickedPos = context.getBlockPos();
        var clickedState = level.getBlockState(clickedPos);
        if (clickedState.isAir()) return;

        addSprinkling(level, clickedPos, random);
        for (BlockPos pos : BlockPos.betweenClosed(clickedPos.offset(-2, -1, -2), clickedPos.offset(2, 0, 2))) {
            if (random.nextInt(40) != 0) continue;
            var state = level.getBlockState(pos);
            if (!(state.getBlock() instanceof FarmBlock)) continue;
            int i = state.getValue(FarmBlock.MOISTURE);
            if (i < 7) level.setBlock(pos, state.setValue(FarmBlock.MOISTURE, i + 1), 2);
        }
        for (BlockPos pos : BlockPos.betweenClosed(clickedPos.offset(-2, 0, -2), clickedPos.offset(2, 0, 2))) {
            if (random.nextInt(60) != 0) continue;
            if (pos == clickedPos) continue;
            BlockPos relativePos = pos;
            if (!clickedState.getCollisionShape(level, clickedPos).isEmpty()) relativePos = relativePos.above();
            ParticleUtils.spawnParticles(level, relativePos, random.nextInt(8) + 2, 0.5, 0, true, ParticleTypes.SPLASH);
            if (applyWatering(stack, level, pos, random, player)) if (!level.isClientSide) player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
        }
        if (random.nextInt(30) != 0) return;
        BlockPos relative = clickedPos;
        if (!clickedState.getCollisionShape(level, clickedPos).isEmpty()) relative = relative.above();
        ParticleUtils.spawnParticleInBlock(level, relative, random.nextInt(8) + 2, ParticleTypes.SPLASH);
        if (applyWatering(stack, level, clickedPos, random, player)) if (!level.isClientSide) player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        var item = AllItems.WATERING_CAN.get();
        return !(oldStack.is(item) && newStack.is(item));
    }

    @Override
    public @NotNull SoundEvent getBreakingSound() {
        return SoundEvents.EMPTY;
    }

    public static boolean applyWatering(ItemStack stack, Level level, BlockPos pos, RandomSource random, @Nullable Player player) {
        BlockState state = level.getBlockState(pos);
        var event = net.neoforged.neoforge.event.EventHooks.fireBonemealEvent(player, level, pos, state, stack);
        if (event.isCanceled()) return event.isSuccessful();
        var block = state.getBlock();
        if (level instanceof ServerLevel serverLevel) {
            switch (block) {
                case CropBlock target -> {
                    int i = target.getAge(state) + random.nextInt(1) + 1;
                    int j = target.getMaxAge();
                    if (i > j) i = j;
                    level.setBlock(pos, target.getStateForAge(i), 2);
                    return true;
                }
                case SaplingBlock target -> {
                    if (random.nextInt(6) == 0)
                        target.advanceTree(serverLevel, pos, state, random);
                    return true;
                }
                case AzaleaBlock ignored -> {
                    if (random.nextInt(6) == 0)
                        TreeGrower.AZALEA.growTree(serverLevel, serverLevel.getChunkSource().getGenerator(), pos, state, random);
                    return true;
                }
                case StemBlock ignored -> {
                    int i = Math.min(7, state.getValue(StemBlock.AGE) + random.nextInt(1) + 1);
                    BlockState blockstate = state.setValue(StemBlock.AGE, i);
                    level.setBlock(pos, blockstate, 2);
                    if (i == 7) blockstate.randomTick(serverLevel, pos, level.random);
                    return true;
                }
                case SweetBerryBushBlock ignored -> {
                    int i = Math.min(3, state.getValue(SweetBerryBushBlock.AGE) + 1);
                    level.setBlock(pos, state.setValue(SweetBerryBushBlock.AGE, i), 2);
                    return true;
                }
                case SmallDripleafBlock target -> {
                    if (state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER) {
                        BlockPos above = pos.above();
                        level.setBlock(above, level.getFluidState(above).createLegacyBlock(), 18);
                        BigDripleafBlock.placeWithRandomHeight(level, random, pos, state.getValue(SmallDripleafBlock.FACING));
                    } else {
                        BlockPos below = pos.below();
                        target.performBonemeal(serverLevel, random, below, level.getBlockState(below));
                    }
                    return true;
                }
                case BambooStalkBlock target -> {
                    int i = getHeightAboveUpToMax(level, pos), j = getHeightBelowUpToMax(level, pos);
                    int k = i + j + 1, l = 1 + random.nextInt(2);
                    for (int i1 = 0; i1 < l; i1++) {
                        BlockPos blockpos = pos.above(i);
                        BlockState blockstate = level.getBlockState(blockpos);
                        if (k >= 16 || blockstate.getValue(BambooStalkBlock.STAGE) == 1 || !level.isEmptyBlock(blockpos.above()))
                            break;
                        growBamboo(target, blockstate, level, blockpos, random, k);
                        i++;
                        k++;
                    }
                    return true;
                }
                case BambooSaplingBlock ignored -> {
                    level.setBlock(pos.above(), Blocks.BAMBOO.defaultBlockState().setValue(BambooStalkBlock.LEAVES, BambooLeaves.SMALL), 3);
                    return true;
                }
                case BonemealableBlock bonemealableblock -> {
                    if (!state.getCollisionShape(level, pos).isEmpty()) return false;
                    if (bonemealableblock.isBonemealSuccess(level, level.random, pos, state))
                        bonemealableblock.performBonemeal(serverLevel, level.random, pos, state);
                    return true;
                }
                default -> {}
            }
        }
        return false;
    }

    public static void addSprinkling(Level level, BlockPos pos, RandomSource random) {
        //if (random.nextInt(10) != 0) ParticleUtils.spawnParticles(level, pos, random.nextInt(4) + 1, 2.0, 1.0, false, ParticleTypes.SPLASH);
        if (random.nextInt(10) == 0) level.playLocalSound(pos, SoundEvents.WATER_AMBIENT, SoundSource.PLAYERS, 0.1F, 2.0F - (random.nextFloat() * 0.1F), false);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 7200;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return Mth.color(0.4F, 0.4F, 1.0F);
    }

    @SuppressWarnings("deprecation")
    public ItemStack consumeConvert(ItemStack stack, int amount, Player player, ItemLike convert) {
        amount = this.damageItem(stack, amount, player, item -> {});
        if (!player.hasInfiniteMaterials()) {
            if (amount > 0) {
                if (player instanceof ServerPlayer sp) amount = EnchantmentHelper.processDurabilityChange(sp.serverLevel(), stack, amount);
                if (amount <= 0) return stack;
            }
            if (player instanceof ServerPlayer sp && amount != 0) CriteriaTriggers.ITEM_DURABILITY_CHANGED.trigger(sp, stack, stack.getDamageValue() + amount);
            int i = stack.getDamageValue() + amount;
            stack.setDamageValue(i);
            if (i >= stack.getMaxDamage()) stack.shrink(1);
        }
        if (stack.isEmpty()) {
            ItemStack itemstack = new ItemStack(convert.asItem().builtInRegistryHolder(), 1, stack.getComponentsPatch());
            if (itemstack.isDamageableItem()) itemstack.setDamageValue(0);
            return itemstack;
        } else return stack;
    }

    protected static void growBamboo(BambooStalkBlock block, BlockState state, Level level, BlockPos pos, RandomSource random, int age) {
        BlockState blockstate = level.getBlockState(pos.below());
        BlockPos blockpos = pos.below(2);
        BlockState blockstate1 = level.getBlockState(blockpos);
        BambooLeaves bambooleaves = BambooLeaves.NONE;
        if (age >= 1) {
            if (!blockstate.is(Blocks.BAMBOO) || blockstate.getValue(BambooStalkBlock.LEAVES) == BambooLeaves.NONE) bambooleaves = BambooLeaves.SMALL;
            else if (blockstate.is(Blocks.BAMBOO) && blockstate.getValue(BambooStalkBlock.LEAVES) != BambooLeaves.NONE) { bambooleaves = BambooLeaves.LARGE;
                if (blockstate1.is(Blocks.BAMBOO)) {
                    level.setBlock(pos.below(), blockstate.setValue(BambooStalkBlock.LEAVES, BambooLeaves.SMALL), 3);
                    level.setBlock(blockpos, blockstate1.setValue(BambooStalkBlock.LEAVES, BambooLeaves.NONE), 3);
                }
            }
        }
        int i = state.getValue(BambooStalkBlock.AGE) != 1 && !blockstate1.is(Blocks.BAMBOO) ? 0 : 1;
        int j = (age < 11 || !(random.nextFloat() < 0.25F)) && age != 15 ? 0 : 1;
        level.setBlock(pos.above(), block.defaultBlockState().setValue(BambooStalkBlock.AGE, i).setValue(BambooStalkBlock.LEAVES, bambooleaves).setValue(BambooStalkBlock.STAGE, j), 3);
    }

    protected static int getHeightAboveUpToMax(BlockGetter level, BlockPos pos) {
        int i = 0;
        while (i < 16 && level.getBlockState(pos.above(i + 1)).is(Blocks.BAMBOO)) i++;
        return i;
    }
    protected static  int getHeightBelowUpToMax(BlockGetter level, BlockPos pos) {
        int i = 0;
        while (i < 16 && level.getBlockState(pos.below(i + 1)).is(Blocks.BAMBOO)) i++;
        return i;
    }
}
