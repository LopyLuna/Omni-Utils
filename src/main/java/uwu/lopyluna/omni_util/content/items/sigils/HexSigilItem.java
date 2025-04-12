package uwu.lopyluna.omni_util.content.items.sigils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.omni_util.register.AllBlocks;
import uwu.lopyluna.omni_util.register.AllItems;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class HexSigilItem extends Item {
    public HexSigilItem(Properties properties) {
        super(properties.craftRemainder(AllItems.BLANK_TABLET.get()));
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext pContext) {
        var pStack = pContext.getItemInHand();
        var pPlayer = pContext.getPlayer();
        var pClickedPos = pContext.getClickedPos();
        var pLevel = pContext.getLevel();
        var pState = pLevel.getBlockState(pClickedPos);
        if (pStack.isEmpty()) return InteractionResult.PASS;

        if (pState.is(Blocks.SPAWNER)) {
            pLevel.setBlockAndUpdate(pClickedPos, AllBlocks.SPAWNER.getDefaultState());
            pLevel.gameEvent(pPlayer, GameEvent.BLOCK_CHANGE, pClickedPos);
            pStack.shrink(1);
        } else return InteractionResult.PASS;

        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Override
    public @NotNull ItemStack getCraftingRemainingItem(ItemStack stack) {
        return new ItemStack(AllItems.HEX_SIGIL.get());
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return super.isFoil(stack);
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.translatable(this.getDescriptionId()).withStyle(ChatFormatting.RED);
    }
}
