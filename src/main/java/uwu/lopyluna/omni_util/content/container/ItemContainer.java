package uwu.lopyluna.omni_util.content.container;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import org.jetbrains.annotations.NotNull;

public class ItemContainer extends SimpleContainer {
    int size;
    final ItemStack stack;
    final InteractionHand hand;
    final boolean inHand;

    public ItemContainer(InteractionHand hand, ItemStack stack, int rows, boolean inHand) {
        super(9*rows);
        this.size = 9*rows;
        this.stack = stack;
        this.hand = hand;
        this.inHand = inHand;

        ItemContainerContents contents = stack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
        contents.copyInto(this.getItems());
    }

    @Override
    public void setChanged() {
        super.setChanged();
        stack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(getItems()));
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return !stack.isEmpty() && (player.getItemInHand(hand).is(stack.getItem()) || !inHand);
    }
}
