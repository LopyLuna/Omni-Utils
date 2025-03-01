package uwu.lopyluna.omni_util.register;

import net.minecraft.resources.ResourceLocation;
import uwu.lopyluna.omni_util.OmniUtils;

public class AllPowerSources {

    //CONSUMES
    public static PowerSource ANGEL_FLIGHT = new PowerSource(OmniUtils.loc("angel_flight"), 64);
    //GENERATE
    public static PowerSource GENERATOR_BLOCK = new PowerSource(OmniUtils.loc("generator_block"), 8);
    public static PowerSource WATER_MILL = new PowerSource(OmniUtils.loc("water_mill"), 4);

    public static void register() {}

    @SuppressWarnings("all")
    public static class PowerSource {
        public final ResourceLocation id;
        public final int power;
        public PowerSource(ResourceLocation id, int power) {
            this.id = id;
            this.power = power;
        }
    }
}
