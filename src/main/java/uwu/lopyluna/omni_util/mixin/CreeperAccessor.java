package uwu.lopyluna.omni_util.mixin;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Creeper.class)
public interface CreeperAccessor {
    @Invoker("explodeCreeper")
    void explodeCreeper$OmniUtils();
    @Accessor("explosionRadius")
    void sexExplosionRadius$OmniUtils(int value);
    @Accessor("DATA_IS_POWERED")
    static EntityDataAccessor<Boolean> dataIsPowered$OmniUtils() {
        throw new AssertionError();
    }
}
