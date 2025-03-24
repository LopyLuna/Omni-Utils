package uwu.lopyluna.omni_util.mixin;

import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractCauldronBlock.class)
public interface AbstractCauldronBlockAccessor {
    @Invoker("canReceiveStalactiteDrip")
    boolean canReceiveStalactiteDrip$OmniUtils(Fluid fluid);
}
