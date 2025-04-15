package uwu.lopyluna.omni_util.register;

import static uwu.lopyluna.omni_util.OmniUtils.REG;

public class AllLangProvider {
    public static void addTranslations() {
        var path = "item.omni_util.";
        var pathEnd = "_angel_ring";

        REG.addRawLang(path + "feathered" + pathEnd, "Feathered Angel Ring");
        REG.addRawLang(path + "demon" + pathEnd, "Demonic Angel Ring");
        REG.addRawLang(path + "gilded" + pathEnd, "Gilded Angel Ring");
        REG.addRawLang(path + "bat" + pathEnd, "Bat Angel Ring");
        REG.addRawLang(path + "allay" + pathEnd, "Allay Angel Ring");
        REG.addRawLang(path + "vexxed" + pathEnd, "Vexxed Angel Ring");

        REG.addRawLang("biome.omni_util.grimspire_biome", "The Grimspire");
        REG.addRawLang("biome.omni_util.cursed_biome", "Cursed Lands");
        REG.addRawLang("biome.omni_util.dead_biome", "Dead Lands");
    }
}
