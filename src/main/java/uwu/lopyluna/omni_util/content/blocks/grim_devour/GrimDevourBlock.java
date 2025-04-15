package uwu.lopyluna.omni_util.content.blocks.grim_devour;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.omni_util.content.blocks.base.OmniBlock;
import uwu.lopyluna.omni_util.content.blocks.base.OmniBlockEntity;
import uwu.lopyluna.omni_util.register.AllBlockEntities;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Locale;

@ParametersAreNonnullByDefault
public class GrimDevourBlock extends OmniBlock {
    public GrimDevourBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof GrimDevourBE be) {
            var formattedConsumed = String.format(Locale.US, "%,d", be.consumed);
            var formattedGoal = "263,520,000";
            player.displayClientMessage(Component.empty()
                    .append(Component.literal(formattedConsumed).withStyle(ChatFormatting.GOLD))
                    .append(" / ")
                    .append(Component.literal(formattedGoal).withStyle(ChatFormatting.YELLOW))
                    , true
            );
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public BlockEntityType<? extends OmniBlockEntity> getBlockEntityType() {
        return AllBlockEntities.GRIM_DEVOUR.get();
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.empty()
                .append(Component.literal("Feed ").withStyle(ChatFormatting.GRAY))
                .append(Component.literal("263,520,000").withStyle(ChatFormatting.GOLD))
                .append(Component.literal(" Stone related materials to make the ").withStyle(ChatFormatting.GRAY))
                .append(Component.literal("Grimspiral").withStyle(ChatFormatting.LIGHT_PURPLE))
        );
        tooltipComponents.add(Component.empty());
        tooltipComponents.add(Component.literal("Place Blocks within area of the Devourer to Feed it").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.literal("Area is within radius of 2 blocks from the Devourer").withStyle(ChatFormatting.GRAY));
    }
}
