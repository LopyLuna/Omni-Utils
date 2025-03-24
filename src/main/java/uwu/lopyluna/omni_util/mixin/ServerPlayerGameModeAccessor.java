package uwu.lopyluna.omni_util.mixin;

import net.minecraft.server.level.ServerPlayerGameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@SuppressWarnings("unused")
@Mixin(ServerPlayerGameMode.class)
public interface ServerPlayerGameModeAccessor {
    @Accessor("gameTicks")
    int gameTicks$OmniUtils();
    @Accessor("destroyProgressStart")
    int destroyProgressStart$OmniUtils();
    @Accessor("delayedTickStart")
    int delayedTickStart$OmniUtils();
    @Accessor("lastSentState")
    int lastSentState$OmniUtils();
}
