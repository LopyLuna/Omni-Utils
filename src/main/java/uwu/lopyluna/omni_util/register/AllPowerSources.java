package uwu.lopyluna.omni_util.register;

import net.minecraft.resources.ResourceLocation;
import uwu.lopyluna.omni_util.OmniUtils;

@SuppressWarnings("unused")
public class AllPowerSources {

    //CONSUMES
    public static PowerSource CONSUMOR_BLOCK = new PowerSource("consumor_block", 64, false, false);
    public static PowerSource ANGEL_FLIGHT = new PowerSource("angel_flight", 1024, false, false);
    //GENERATE
    public static PowerSource GENERATOR_BLOCK = new PowerSource("generator_block", 64, false, true);
    public static PowerSource BASE_PANEL = new PowerSource("base_panel", 8, false, true);
    public static PowerSource POWER_CRANK = new PowerSource("base_panel", 8, false, true);
    public static PowerSource COMBUSTION = new PowerSource("combustion", 24, false, true);
    public static PowerSource DRAGON_AMBIENT = new PowerSource("dragon_ambient", 256, false, true);
    //ADDITIVES
    public static PowerSource WATER_MILL = new PowerSource("water_mill", 8, true, true);
    public static PowerSource WIND_MILL = new PowerSource("wind_mill", 12, true, true);
    public static PowerSource HEAT_MILL = new PowerSource("heat_mill", 16, true, true);
    public static PowerSource REACTOR = new PowerSource("reactor", 64, true, true);

    public static void register() {}

    @SuppressWarnings("all")
    public static class PowerSource {
        public final ResourceLocation id;
        public final int impact;
        public final boolean additive;
        public final boolean genertor;

        public PowerSource(String id, int impact, boolean additive, boolean genertor) {
            this(OmniUtils.loc(id), impact, additive, genertor);
        }
        public PowerSource(ResourceLocation id, int impact, boolean additive, boolean genertor) {
            this.id = id;
            this.impact = impact;
            this.additive = additive;
            this.genertor = genertor;
        }
    }
}
