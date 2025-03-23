package uwu.lopyluna.omni_util.register;

import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.common.util.DeferredSoundType;

public class AllSoundTypes {
    public static final DeferredSoundType GRIMROCK = new DeferredSoundType(
            1.0F,
            0.75F,
            () -> SoundEvents.DEEPSLATE_BREAK,
            () -> SoundEvents.DEEPSLATE_STEP,
            () -> SoundEvents.GILDED_BLACKSTONE_PLACE,
            () -> SoundEvents.GILDED_BLACKSTONE_HIT,
            () -> SoundEvents.GILDED_BLACKSTONE_FALL
    );
    public static final DeferredSoundType COBBLED_GRIMROCK = new DeferredSoundType(
            1.0F,
            0.65F,
            () -> SoundEvents.DEEPSLATE_BREAK,
            () -> SoundEvents.DEEPSLATE_STEP,
            () -> SoundEvents.GILDED_BLACKSTONE_PLACE,
            () -> SoundEvents.GILDED_BLACKSTONE_HIT,
            () -> SoundEvents.GILDED_BLACKSTONE_FALL
    );
    public static final DeferredSoundType POLISHED_GRIMROCK = new DeferredSoundType(
            1.0F,
            0.75F,
            () -> SoundEvents.DEEPSLATE_TILES_BREAK,
            () -> SoundEvents.DEEPSLATE_TILES_HIT,
            () -> SoundEvents.POLISHED_DEEPSLATE_PLACE,
            () -> SoundEvents.POLISHED_DEEPSLATE_HIT,
            () -> SoundEvents.POLISHED_DEEPSLATE_FALL
    );
    public static final DeferredSoundType CURSED_LEAVES = new DeferredSoundType(
            1.0F,
            0.75F,
            () -> SoundEvents.CHERRY_LEAVES_BREAK,
            () -> SoundEvents.CHERRY_LEAVES_STEP,
            () -> SoundEvents.CHERRY_LEAVES_PLACE,
            () -> SoundEvents.CHERRY_LEAVES_HIT,
            () -> SoundEvents.CHERRY_LEAVES_FALL
    );
    public static final DeferredSoundType CURSED_LOG = new DeferredSoundType(
            1.0F,
            0.5F,
            () -> SoundEvents.CHERRY_WOOD_BREAK,
            () -> SoundEvents.CHERRY_WOOD_STEP,
            () -> SoundEvents.CHERRY_WOOD_PLACE,
            () -> SoundEvents.CHERRY_WOOD_HIT,
            () -> SoundEvents.CHERRY_WOOD_FALL
    );
    public static final DeferredSoundType CURSED_STONE = new DeferredSoundType(
            1.0F,
            0.5F,
            () -> SoundEvents.GILDED_BLACKSTONE_BREAK,
            () -> SoundEvents.GILDED_BLACKSTONE_STEP,
            () -> SoundEvents.GILDED_BLACKSTONE_PLACE,
            () -> SoundEvents.GILDED_BLACKSTONE_HIT,
            () -> SoundEvents.GILDED_BLACKSTONE_FALL
    );
    public static final DeferredSoundType CURSED_NYLIUM = new DeferredSoundType(
            1.0F,
            0.5F,
            () -> SoundEvents.GRASS_BREAK,
            () -> SoundEvents.GRASS_STEP,
            () -> SoundEvents.GRASS_PLACE,
            () -> SoundEvents.GRASS_HIT,
            () -> SoundEvents.GRASS_FALL
    );
    public static final DeferredSoundType CURSED_SOIL = new DeferredSoundType(
            1.0F,
            0.65F,
            () -> SoundEvents.ROOTED_DIRT_BREAK,
            () -> SoundEvents.ROOTED_DIRT_STEP,
            () -> SoundEvents.ROOTED_DIRT_PLACE,
            () -> SoundEvents.ROOTED_DIRT_HIT,
            () -> SoundEvents.ROOTED_DIRT_FALL
    );
}
