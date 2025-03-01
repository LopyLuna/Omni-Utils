package uwu.lopyluna.omni_util.content.container.bundle_of_holding;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BundleOfHoldingMenu extends ChestMenu {
    public BundleOfHoldingMenu(int containerId, Inventory playerInventory, Container container) {
        super(MenuType.GENERIC_9x6, containerId, playerInventory, container, 6);
    }

    @Override
    public void clicked(int slotId, int button, @NotNull ClickType clickType, @NotNull Player player) {
        if (slotId > -1) {
            if (clickType.equals(ClickType.SWAP) && player.getOffhandItem().getItem().canFitInsideContainerItems()) return;
            if (slots.get(slotId).hasItem() && !getSlot(slotId).getItem().getItem().canFitInsideContainerItems()) return;
        }
        super.clicked(slotId, button, clickType, player);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        if (slots.get(index).hasItem() && !getSlot(index).getItem().getItem().canFitInsideContainerItems()) return ItemStack.EMPTY;
        return super.quickMoveStack(player, index);
    }
}
