package uwu.lopyluna.omni_util.register;

import static uwu.lopyluna.omni_util.OmniUtils.REG;

public class AllLangProvider {
    public static void addTranslations() {
        var path = "item.omni_util.";
        var pathEnd = "_angel_ring";

        REG.addRawLang(path + "feathered" + pathEnd, "Feathered Angel Ring");
        REG.addRawLang(path + "demon" + pathEnd, "Demon Angel Ring");
        REG.addRawLang(path + "gilded" + pathEnd, "Gilded Angel Ring");
        REG.addRawLang(path + "bat" + pathEnd, "Bat Angel Ring");
    }
}
