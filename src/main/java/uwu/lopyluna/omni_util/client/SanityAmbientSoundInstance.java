package uwu.lopyluna.omni_util.client;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SanityAmbientSoundInstance extends AbstractTickableSoundInstance {
    private final LocalPlayer player;

    public SanityAmbientSoundInstance(LocalPlayer player) {
        super(SoundEvents.CONDUIT_AMBIENT, SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.player = player;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.0F;
        this.relative = true;
        this.pitch = 0.5F;
    }

    @Override
    public void tick() {
        if (player == null || player.isRemoved() || !player.isAlive()) {
            this.stop();
            return;
        }
        float sanity = ClientSanityData.getSanity();
        float darkness = 1f - (sanity / 100f);

        this.volume = Mth.clamp(darkness, 0.0F, 1.0F);
    }
}