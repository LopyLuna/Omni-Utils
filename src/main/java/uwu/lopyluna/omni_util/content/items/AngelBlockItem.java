package uwu.lopyluna.omni_util.content.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AngelBlockItem extends BlockItem {
    public AngelBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        Vec3 vec3 = player.getEyePosition();
        Vec3 vec31 = player.getViewVector(1f);
        var dist = player.isShiftKeyDown() ? 2.5 : 4.5;
        return new InteractionResultHolder<>(this.useOn(new UseOnContext(player, usedHand, level.clip(new ClipContext(vec3, vec3.add(vec31.x * dist, vec31.y * dist, vec31.z * dist), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)))), player.getItemInHand(usedHand));
    }
}
