package uwu.lopyluna.omni_util.content.container.bundle_of_holding;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BundleOfHoldingMenu extends ChestMenu {

    public BundleOfHoldingMenu(int containerId, Inventory playerInventory, Container container) {
        super(MenuType.GENERIC_9x6, containerId, playerInventory, container, 6);
    }

    @Override
    public void clicked(int pSlotId, int pButton, @NotNull ClickType pClickType, @NotNull Player pPlayer) {
        if (pSlotId > -1) {
            if (pClickType.equals(ClickType.SWAP) && pPlayer.getOffhandItem().getItem().canFitInsideContainerItems()) return;
            if (slots.get(pSlotId).hasItem() && !getSlot(pSlotId).getItem().getItem().canFitInsideContainerItems()) return;
        }
        super.clicked(pSlotId, pButton, pClickType, pPlayer);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        if (slots.get(index).hasItem() && !getSlot(index).getItem().getItem().canFitInsideContainerItems()) return ItemStack.EMPTY;
        return super.quickMoveStack(player, index);
    }
}
