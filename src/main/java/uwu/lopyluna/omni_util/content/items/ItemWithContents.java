package uwu.lopyluna.omni_util.content.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemWithContents extends Item {
    private static final int BAR_COLOR = Mth.color(0.4F, 0.4F, 1.0F);
    public final int size;

    public ItemWithContents(Properties properties, int size) {
        super(properties);
        this.size = size;
    }

    public int getContentSize(ItemStack stack) {
        return 0;
    }

    public float getContentSizePercentage(ItemStack stack) {
        return 0f;
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return getContentSize(stack) > 0;
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack) {
        return Math.min(1 + (int)(getContentSizePercentage(stack) * 12f), 13);
    }

    @Override
    public int getBarColor(@NotNull ItemStack stack) {
        return BAR_COLOR;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("item.minecraft.bundle.fullness", getContentSize(stack), size*64).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public boolean canFitInsideContainerItems() {
        return false;
    }
}
