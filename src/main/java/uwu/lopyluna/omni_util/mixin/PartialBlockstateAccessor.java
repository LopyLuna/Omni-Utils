package uwu.lopyluna.omni_util.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(VariantBlockStateBuilder.PartialBlockstate.class)
public interface  PartialBlockstateAccessor {

    @Invoker("<init>")
    static VariantBlockStateBuilder.PartialBlockstate omniUtil$create(Block owner, Map<Property<?>, Comparable<?>> setStates, @Nullable VariantBlockStateBuilder outerBuilder) {
        throw new AssertionError();
    }
}
