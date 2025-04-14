package uwu.lopyluna.omni_util.content;

import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;

public class AllUtils {

    public static int getClientLight(Level level, BlockPos pos) {
        return Mth.clamp(LightTexture.pack(level.getBrightness(LightLayer.BLOCK, pos), level.getBrightness(LightLayer.SKY, pos)), 0, 15728880);
    }

    public static void playSound(Level level, BlockPos pos, SoundEvent sound, SoundSource category, float volume, float pitch) {
        var v = Vec3.atCenterOf(pos);
        level.playSound(null, v.x, v.y, v.z, sound, category, volume, pitch);
    }

    public static void addParticles(ServerLevel level, BlockPos pos, double distance, ParticleOptions particle) {
        addParticles(level, pos, distance, false, 1, 0, new Vec3(0,0,0), particle);
    }

    public static void addParticles(ServerLevel level, BlockPos pos, double distance, boolean force, ParticleOptions particle) {
        addParticles(level, pos, distance, force, 1, 0, new Vec3(0,0,0), particle);
    }

    public static void addParticles(ServerLevel level, BlockPos pos, double distance, int count, ParticleOptions particle) {
        addParticles(level, pos, distance, false, count, 0, new Vec3(0,0,0), particle);
    }

    public static void addParticles(ServerLevel level, BlockPos pos, double distance, boolean force, int count, ParticleOptions particle) {
        addParticles(level, pos, distance, force, count, 0, new Vec3(0,0,0), particle);
    }

    public static void addParticles(ServerLevel level, Vec3 pos, double distance, ParticleOptions particle) {
        addParticles(level, pos, distance, false, 1, 0, new Vec3(0,0,0), particle);
    }

    public static void addParticles(ServerLevel level, Vec3 pos, double distance, boolean force, ParticleOptions particle) {
        addParticles(level, pos, distance, force, 1, 0, new Vec3(0,0,0), particle);
    }

    public static void addParticles(ServerLevel level, Vec3 pos, double distance, int count, ParticleOptions particle) {
        addParticles(level, pos, distance, false, count, 0, new Vec3(0,0,0), particle);
    }

    public static void addParticles(ServerLevel level, Vec3 pos, double distance, boolean force, int count, ParticleOptions particle) {
        addParticles(level, pos, distance, force, count, 0, new Vec3(0,0,0), particle);
    }

    public static void addParticles(ServerLevel level, BlockPos pos, double distance, boolean force, int count, float speed, Vec3 delta, ParticleOptions particle) {
        var random = level.random;
        double d0 = (double)(2.0F * random.nextFloat() - 1.0F) * 0.65;
        double d1 = (double)(2.0F * random.nextFloat() - 1.0F) * 0.65;
        double d2 = (double)pos.getX() + 0.5 + d0;
        double d3 = (double)pos.getY() + 0.1 + (double)random.nextFloat() * 0.8;
        double d4 = (double)pos.getZ() + 0.5 + d1;
        addParticles(level, new Vec3(d2, d3, d4), distance, force, count, speed, delta, particle);
    }

    public static void addParticles(ServerLevel level, Vec3 pos, double distance, boolean force, int count, float speed, Vec3 delta, ParticleOptions particle) {
        for (ServerPlayer player : level.players()) if (EntitySelector.NO_SPECTATORS.test(player) && EntitySelector.LIVING_ENTITY_STILL_ALIVE.test(player)) {
            double d0 = player.distanceToSqr(pos.x, pos.y, pos.z);
            if (distance < 0.0 || d0 < distance * distance) level.sendParticles(player, particle, force, pos.x, pos.y, pos.z, count, delta.x, delta.y, delta.z, speed);
        }
    }

    public static int getHourOfTime(Level level) {
        var time = (level.dayTime() + 6000f) % 12000f;
        float multi = time / 12000f;
        int hour = (int) (multi * 12f);
        if (hour == 0) hour = 12;
        return hour;
    }

    public static boolean isAmOfTime(Level level) {
        return ((level.dayTime() + 6000f) % 24000f) < 12000f;
    }

    @SuppressWarnings("unused")
    public enum TimeCycle {
        SUNRISE("Sunrise", 5, true),
        DAY_PRE("Day", 6, true),
        AFTERNOON("Afternoon", 10, true),
        DAY_POST("Day", 4, false),
        SUNSET("Sunset", 6, false),
        MOONRISE("Moonrise", 7, false),
        NIGHT_PRE("Night", 8, false),
        MIDNIGHT("Midnight", 12, false),
        NIGHT_POST("Night", 3, true),
        MOONSET("Moonset", 4, true);

        final String label;
        final int hour;
        final boolean am;

        TimeCycle(String label,  int hour, boolean am) {
            this.label = label;
            this.hour = hour;
            this.am = am;
        }

        public boolean hasDaylight() {
            return this == SUNRISE || this == DAY_PRE || this == AFTERNOON || this == DAY_POST || this == SUNSET;
        }

        public static TimeCycle fromHour(int hour, boolean am) {
            hour = ((hour - 1 + 12) % 12) + 1;
            TimeCycle closest = null;
            int smallestDiff = Integer.MAX_VALUE;
            for (TimeCycle cycle : values()) {
                if (cycle.am != am) continue;
                int diff = Math.abs(cycle.hour - hour);
                if (diff < smallestDiff) {
                    smallestDiff = diff;
                    closest = cycle;
                }
            }
            if (closest == null) {
                for (TimeCycle cycle : values()) {
                    int diff = Math.abs(cycle.hour - hour);
                    if (diff < smallestDiff) {
                        smallestDiff = diff;
                        closest = cycle;
                    }
                }
            }
            if (hour == 12 && am) closest = MIDNIGHT;
            if (hour == 12 && !am) closest = AFTERNOON;
            if (closest == null) closest = AFTERNOON;
            return closest;
        }

        public String getLabel() {
            return label + ": " + hour + (am ? " AM" : " PM");
        }
        public String getLabel(int hour) {
            return label + ": " + hour + (am ? " AM" : " PM");
        }
        public String getLabel(int hour, boolean am) {
            return label + ": " + hour + (am ? " AM" : " PM");
        }
        public String getLabel(boolean am) {
            return label + ": " + hour + (am ? " AM" : " PM");
        }

        public int toGameTime() {
            return ((((hour + 16) % 12) * 1000) + (am ? 0 : 12000)) % 24000;
        }

        public int getHour() {
            return hour;
        }

        public int getFullHour() {
            return hour + (am ? 0 : 12);
        }

        public boolean isAm() {
            return am;
        }
    }
}
