package uwu.lopyluna.omni_util.content.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class HexSigilItem extends Item {
    public HexSigilItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return super.isFoil(stack);
    }

    @Override
    public @NotNull Component getDescription() {
        return ((MutableComponent) super.getDescription()).withStyle(ChatFormatting.RED);
    }
}
