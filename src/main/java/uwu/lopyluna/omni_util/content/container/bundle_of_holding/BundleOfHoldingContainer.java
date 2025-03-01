package uwu.lopyluna.omni_util.content.container.bundle_of_holding;

import com.google.common.collect.Iterables;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import uwu.lopyluna.omni_util.content.container.ItemContainer;

public class BundleOfHoldingContainer extends ItemContainer {
    public BundleOfHoldingContainer(InteractionHand hand, ItemStack stack) {
        super(hand, stack, 6, true);
    }
    public BundleOfHoldingContainer(ItemStack stack) {
        super(InteractionHand.MAIN_HAND, stack, 6, false);
    }

    public Iterable<ItemStack> getItemsCopy() {
        return Iterables.transform(getItems(), ItemStack::copy);
    }

    public int getContentSize() {
        int i = 0;
        for (var item : getItems()) if (!item.isEmpty()) i += (int) (((float)item.getCount() / (float)item.getMaxStackSize()) * 64f);
        return i;
    }

    public float getContentSizePercentage() {
        return (float) getContentSize() / (float) (9*6*64);
    }

    public ItemStack quickMoveItem(ItemStack pStack, boolean insert) {
        ItemStack itemstack = ItemStack.EMPTY;
        if (getItems().contains(pStack)) {
            if (insert) {
                if (canAddItem(pStack)) addItem(pStack);
            } else {
                for (int i = 0; getItems().size() > i; i++) {
                    var slotStack = getItems().get(i);
                    if (!slotStack.isEmpty()) itemstack = slotStack.copy();
                }

            }
        }
        return itemstack;
    }

    public ItemStack takeItem(ItemStack pOutput) {
        for (int i = 0; getItems().size() > i; i++) {
            var slotStack = getItems().get(i);
            if (!slotStack.isEmpty()) {
                if (pOutput.isEmpty()) {
                    getItems().set(i, ItemStack.EMPTY);
                    setChanged();
                    return slotStack;
                } else if (slotStack == pOutput) {
                    int max = pOutput.getMaxStackSize();
                    int count = slotStack.getCount();
                    int inputCount = pOutput.getCount();
                    int size = count + inputCount;
                    int diff = size - max;
                    if (count < max) {
                        if (size > max) {
                            slotStack.shrink(diff);
                            pOutput.setCount(max);
                        } else {
                            slotStack.shrink(size);
                            pOutput.setCount(size);
                        }
                        getItems().set(i, slotStack);
                        setChanged();
                        return pOutput;
                    }
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public boolean insertItem(ItemStack pInput) {
        for (int i = 0; getItems().size() > i; i++) {
            var stack = getItems().get(i);
            if (stack == pInput) {
                int max = pInput.getMaxStackSize();
                int count = stack.getCount();
                int inputCount = pInput.getCount();
                int size = count + inputCount;
                int diff = size - max;

                if (count < max) {
                    stack.setCount(Math.min(size, max));
                    setItem(i, stack);
                    if (size > max) pInput.shrink(diff);
                    else pInput.shrink(size);
                    return true;
                }
            } else if (stack.isEmpty()) {
                setItem(i, pInput);
                pInput.shrink(pInput.getCount());
                return true;
            }
        }
        return false;
    }
}
