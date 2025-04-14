package uwu.lopyluna.omni_util.mixin;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;

@Mixin(VariantBlockStateBuilder.PartialBlockstate.class)
public interface  PartialBlockstateAccessor {

    @Invoker("<init>")
    static VariantBlockStateBuilder.PartialBlockstate omniUtil$create(Block owner, Map<Property<?>, Comparable<?>> setStates, @Nullable VariantBlockStateBuilder outerBuilder) {
        throw new AssertionError();
    }
}
