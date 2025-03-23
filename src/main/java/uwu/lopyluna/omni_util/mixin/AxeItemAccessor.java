package uwu.lopyluna.omni_util.mixin;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(AxeItem.class)
public interface AxeItemAccessor {
    @Accessor("STRIPPABLES")
    static Map<Block, Block> STRIPPABLES$OmniUtils() {
        throw new AssertionError();
    }
    @Invoker("playerHasShieldUseIntent")
    static boolean playerHasShieldUseIntent$OmniUtils(UseOnContext context) {
        throw new AssertionError();
    }
}
