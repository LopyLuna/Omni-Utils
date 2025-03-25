package uwu.lopyluna.omni_util.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uwu.lopyluna.omni_util.register.AllBlocks;

@Mixin(PrimedTnt.class)
public abstract class PrimedTntMixin extends Entity {
    @Shadow private boolean usedPortal;
    @Shadow @Final private static ExplosionDamageCalculator USED_PORTAL_DAMAGE_CALCULATOR;
    @Shadow public abstract BlockState getBlockState();

    public PrimedTntMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "explode()V", at = @At(value = "HEAD"), cancellable = true)
    protected void explode(CallbackInfo ci) {
        if (getBlockState().is(AllBlocks.UNSTABLE_HEXA_TNT)) {
            this.level().explode(
                    this, Explosion.getDefaultDamageSource(this.level(), this),
                    this.usedPortal ? USED_PORTAL_DAMAGE_CALCULATOR : null,
                    this.getX(), this.getY(0.0625), this.getZ(),
                    24.0F, false, Level.ExplosionInteraction.TNT
            );
            ci.cancel();
        }
    }
}
