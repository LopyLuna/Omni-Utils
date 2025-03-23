package uwu.lopyluna.omni_util.content.items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.omni_util.register.AllItems;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class EmptyWateringCanItem extends Item {
    public EmptyWateringCanItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        BlockPos blockpos = blockhitresult.getBlockPos();
        BlockState blockstate = level.getBlockState(blockpos);
        if (blockstate.getBlock() instanceof BucketPickup bucketpickup && blockstate.getFluidState().is(FluidTags.WATER)) {
            ItemStack itemstack3 = bucketpickup.pickupBlock(player, level, blockpos, blockstate);
            if (itemstack3.is(Items.WATER_BUCKET)) {
                player.awardStat(Stats.ITEM_USED.get(this));
                bucketpickup.getPickupSound(blockstate).ifPresent(p_150709_ -> player.playSound(p_150709_, 1.0F, 1.0F));
                level.gameEvent(player, GameEvent.FLUID_PICKUP, blockpos);
                ItemStack itemstack2 = ItemUtils.createFilledResult(itemstack, player, AllItems.WATERING_CAN.get().getDefaultInstance());
                if (!level.isClientSide) CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)player, itemstack3);

                return InteractionResultHolder.sidedSuccess(itemstack2, level.isClientSide());
            }

        }

        return super.use(level, player, hand);
    }
}
