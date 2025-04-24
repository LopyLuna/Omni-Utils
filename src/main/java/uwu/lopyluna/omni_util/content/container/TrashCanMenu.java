package uwu.lopyluna.omni_util.content.container;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.omni_util.register.AllMenuTypes;

public class TrashCanMenu extends AbstractContainerMenu {
    public static final int CONTAINER_SIZE = 5;
    public final Container trashCan;

    public TrashCanMenu(int containerId, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        this(containerId, playerInventory, new SimpleContainer(5));
    }

    public TrashCanMenu(MenuType<?> type, int containerId) {
        super(type, containerId);
        trashCan = new SimpleContainer(5);
    }

    public TrashCanMenu(int containerId, Inventory playerInventory, Container container) {
        super(AllMenuTypes.TRASH_CAN.get(), containerId);
        this.trashCan = container;
        checkContainerSize(container, 5);
        container.startOpen(playerInventory.player);

        int i;
        for(i = 0; i < CONTAINER_SIZE; ++i) this.addSlot(new Slot(container, i, 44 + i * 18, 20));
        for(i = 0; i < 3; ++i) for(int k = 0; k < 9; ++k) this.addSlot(new Slot(playerInventory, k + i * 9 + 9, 8 + k * 18, i * 18 + 51));
        for(i = 0; i < 9; ++i) this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 109));
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        var itemstack = ItemStack.EMPTY;
        var slot = this.slots.get(index);
        if (slot.hasItem()) {
            var itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < this.trashCan.getContainerSize()) {
                if (!this.moveItemStackTo(itemstack1, this.trashCan.getContainerSize(), this.slots.size(), true)) return ItemStack.EMPTY;
            } else if (!this.moveItemStackTo(itemstack1, 0, this.trashCan.getContainerSize(), false)) return ItemStack.EMPTY;

            if (itemstack1.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
            else slot.setChanged();
        }
        return itemstack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return this.trashCan.stillValid(player);
    }

    public void removed(@NotNull Player player) {
        super.removed(player);
        this.trashCan.stopOpen(player);
    }
}
