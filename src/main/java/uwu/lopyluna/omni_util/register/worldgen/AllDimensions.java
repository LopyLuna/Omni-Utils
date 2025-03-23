package uwu.lopyluna.omni_util.register.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import uwu.lopyluna.omni_util.OmniUtils;

import java.util.OptionalLong;

public class AllDimensions {
    public static final ResourceLocation GRIMSPIRE_LOC = OmniUtils.loc("the_grimspire");
    public static final ResourceKey<Level> GRIMSPIRE = ResourceKey.create(Registries.DIMENSION, GRIMSPIRE_LOC);
    public static final ResourceKey<DimensionType> GRIMSPIRE_TYPE_KEY = ResourceKey.create(Registries.DIMENSION_TYPE, GRIMSPIRE_LOC);


    public static void register() {
    }

    public static void bootstrap(BootstrapContext<DimensionType> entries) {
        entries.register(
                GRIMSPIRE_TYPE_KEY,
                new DimensionType(
                OptionalLong.of(18000L),
                false,
                true,
                false,
                false,
                10,
                false,
                false,
                -64,
                384,
                384,
                BlockTags.INFINIBURN_END,
                GRIMSPIRE_LOC,
                0.0f,
                new DimensionType.MonsterSettings(false, false, UniformInt.of(0, 7), 0)
        ));
    }
}
