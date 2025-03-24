package uwu.lopyluna.omni_util.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uwu.lopyluna.omni_util.content.blocks.PointedGrimrockBlock;

@Mixin(AbstractCauldronBlock.class)
public abstract class AbstractCauldronBlockMixin {
    @Shadow protected abstract boolean canReceiveStalactiteDrip(Fluid fluid);
    @Shadow protected abstract void receiveStalactiteDrip(BlockState state, Level level, BlockPos pos, Fluid fluid);

    @Inject(method = "tick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V", at = @At(value = "HEAD"))
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        BlockPos blockpos = PointedGrimrockBlock.findStalactiteTipAboveCauldron(level, pos);
        if (blockpos != null) {
            Fluid fluid = PointedGrimrockBlock.getCauldronFillFluidType(level, blockpos);
            if (fluid != Fluids.EMPTY && this.canReceiveStalactiteDrip(fluid)) this.receiveStalactiteDrip(state, level, pos, fluid);
        }
    }
}
