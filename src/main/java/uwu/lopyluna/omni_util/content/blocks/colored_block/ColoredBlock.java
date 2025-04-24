package uwu.lopyluna.omni_util.content.blocks.colored_block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.omni_util.content.blocks.base.OmniBlock;
import uwu.lopyluna.omni_util.content.blocks.base.OmniBlockEntity;
import uwu.lopyluna.omni_util.register.AllBlockEntities;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ColoredBlock extends OmniBlock {
    public ColoredBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull BlockEntityType<? extends OmniBlockEntity> getBlockEntityType() {
        return AllBlockEntities.COLORED_BLOCK.get();
    }

    @Override
    protected @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }


    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!(stack.getItem() instanceof DyeItem dyeItem)) return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
        if (level.getBlockEntity(pos) instanceof ColoredBE be) {

            int color = switch (dyeItem.getDyeColor()) {
                case WHITE -> FastColor.ARGB32.color(255, 255, 255);
                case LIGHT_GRAY -> FastColor.ARGB32.color(128, 128, 96);
                case GRAY -> FastColor.ARGB32.color(48, 48, 48);
                case BLACK -> FastColor.ARGB32.color(0, 0, 0);
                case BROWN -> FastColor.ARGB32.color(128, 48, 0);
                case RED -> FastColor.ARGB32.color(255, 0, 0);
                case ORANGE -> FastColor.ARGB32.color(255, 112, 0);
                case YELLOW -> FastColor.ARGB32.color(255, 255, 0);
                case LIME -> FastColor.ARGB32.color(0, 255, 0);
                case GREEN -> FastColor.ARGB32.color(0, 192, 32);
                case CYAN -> FastColor.ARGB32.color(0, 192, 144);
                case LIGHT_BLUE -> FastColor.ARGB32.color(0, 255, 255);
                case BLUE -> FastColor.ARGB32.color(0, 0, 255);
                case PURPLE -> FastColor.ARGB32.color(112, 0, 255);
                case MAGENTA -> FastColor.ARGB32.color(255, 0, 255);
                case PINK -> FastColor.ARGB32.color(255, 128, 224);
            };
            int r = Mth.clamp(FastColor.ARGB32.red(color), 0, 255);
            int g = Mth.clamp(FastColor.ARGB32.green(color), 0, 255);
            int b = Mth.clamp(FastColor.ARGB32.blue(color), 0, 255);

            var blend = player.getItemInHand(hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND).is(Items.FEATHER);

            int newR = Mth.clamp(blend ? blend(be.colorR, r) : r, 0, 255);
            int newG = Mth.clamp(blend ? blend(be.colorG, g) : g, 0, 255);
            int newB = Mth.clamp(blend ? blend(be.colorB, b) : b, 0, 255);

            if (be.colorR != newR || be.colorG != newG || be.colorB != newB) {
                be.colorR = newR;
                be.colorG = newG;
                be.colorB = newB;
                level.updateNeighborsAt(pos, this);
                level.playSound(player, pos, SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0f, 1.0f);
                return ItemInteractionResult.SUCCESS;
            }
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    private static int blend(int base, int dye) {
        return Mth.clamp((int) (((double) base * 0.5) + ((double) dye * 0.5)), 0, 255);
    }
}
