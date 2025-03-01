package uwu.lopyluna.omni_util.content.items;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.omni_util.content.container.bundle_of_holding.BundleOfHoldingContainer;
import uwu.lopyluna.omni_util.content.container.bundle_of_holding.BundleOfHoldingMenu;
import uwu.lopyluna.omni_util.content.container.ContainerTooltipComponent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
public class BundleOfHoldingItem extends ItemWithContents {
    public BundleOfHoldingItem(Properties properties) {
        super(properties, 9*6);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemstack = player.getItemInHand(usedHand);
        if (!player.isShiftKeyDown()) {
            if (!level.isClientSide) {
                player.openMenu(getMenu(itemstack, usedHand));
                PiglinAi.angerNearbyPiglins(player, true);
            } else playDropContentsSound(player);
            return InteractionResultHolder.success(itemstack);
        } else if (dropContents(itemstack, player)) {
            this.playDropContentsSound(player);
            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
        }
        return InteractionResultHolder.fail(itemstack);
    }

    public MenuProvider getMenu(ItemStack stack, InteractionHand hand) {
        return new SimpleMenuProvider((id, inv, player) -> new BundleOfHoldingMenu(id, inv, new BundleOfHoldingContainer(hand, stack)), getDescription());
    }

    public void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    public void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    public void playDropContentsSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_DROP_CONTENTS, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (stack.getCount() != 1 || (player.hasContainerOpen() && player.containerMenu instanceof BundleOfHoldingMenu)) return false;
        if (action == ClickAction.SECONDARY && slot.allowModification(player)) {
            var contents = new BundleOfHoldingContainer(stack);
            if (other.isEmpty()) {
                ItemStack itemstack = contents.takeItem(other);
                if (!itemstack.isEmpty()) {
                    playRemoveOneSound(player);
                    access.set(itemstack);
                }
            } else if (other.getItem().canFitInsideContainerItems() && contents.insertItem(other)) playInsertSound(player);
            return true;
        }
        return false;
    }

    @Override
    public int getContentSize(ItemStack stack) {
        var contents = new BundleOfHoldingContainer(stack);
        return contents.getContentSize();
    }

    @Override
    public float getContentSizePercentage(ItemStack stack) {
        var contents = new BundleOfHoldingContainer(stack);
        return contents.getContentSizePercentage();
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        if (stack.getCount() != 1 || action != ClickAction.SECONDARY) return false;
        else {
            var contents = new BundleOfHoldingContainer(stack);
            ItemStack itemstack = slot.getItem();
            if (itemstack.isEmpty()) {
                this.playRemoveOneSound(player);
                ItemStack itemstack1 = contents.takeItem(itemstack);
                if (itemstack1 != null && !itemstack1.isEmpty() && itemstack1.getItem().canFitInsideContainerItems()) {
                    ItemStack itemstack2 = slot.safeInsert(itemstack1);
                    contents.insertItem(itemstack2);
                }
            } else if (itemstack.getItem().canFitInsideContainerItems()) if (contents.insertItem(slot.getItem())) this.playInsertSound(player);
            return true;
        }
    }

    public boolean dropContents(ItemStack stack, Player player) {
        var contents = new BundleOfHoldingContainer(stack);
        if (player instanceof ServerPlayer) contents.getItemsCopy().forEach(i -> player.drop(i, true));
        contents.clearContent();
        return true;
    }

    @Override
    public void onDestroyed(@NotNull ItemEntity itemEntity, @NotNull DamageSource damageSource) {
        var contents = new BundleOfHoldingContainer(itemEntity.getItem());
        ItemUtils.onContainerDestroyed(itemEntity, contents.getItemsCopy());
    }

    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        var contents = new BundleOfHoldingContainer(stack);
        return !stack.has(DataComponents.HIDE_TOOLTIP) && !stack.has(DataComponents.HIDE_ADDITIONAL_TOOLTIP)
                ? Optional.of(contents).map(ContainerTooltipComponent::new) : Optional.empty();
    }

}
