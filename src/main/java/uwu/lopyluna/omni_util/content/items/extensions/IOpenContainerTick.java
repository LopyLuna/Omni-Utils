package uwu.lopyluna.omni_util.content.items.extensions;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IOpenContainerTick {
    default void containerTick(ItemStack pStack, Level pLevel, Player pPlayer, Slot pSlot, Container pContainer, AbstractContainerMenu pMenu) {}

    default boolean validContainersForUnstableHexa(Container pContainer, AbstractContainerMenu pMenu) {
        return pMenu instanceof InventoryMenu ||
                pMenu instanceof CraftingMenu ||
                pMenu instanceof ItemCombinerMenu ||
                pMenu instanceof BeaconMenu ||
                pContainer instanceof Inventory ||
                pContainer instanceof CraftingContainer ||
                pContainer instanceof ResultContainer
                ;
    }
}
