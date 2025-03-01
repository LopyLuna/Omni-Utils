package uwu.lopyluna.omni_util.content.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import uwu.lopyluna.omni_util.content.utils.builders.BlockBuilder;

import static uwu.lopyluna.omni_util.OmniUtils.MOD_ID;
import static uwu.lopyluna.omni_util.content.utils.Registration.getItemEntries;

public class LangProvider extends LanguageProvider {

    public LangProvider(PackOutput output) {
        super(output, MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        var path = "item.omni_util.";
        var pathEnd = "_angel_ring";

        add(path + "feathered" + pathEnd, "Feathered Angel Ring");
        add(path + "demon" + pathEnd, "Demon Angel Ring");
        add(path + "gilded" + pathEnd, "Gilded Angel Ring");
        add(path + "bat" + pathEnd, "Bat Angel Ring");

        getItemEntries().forEach(entry -> {
            String itemName = entry.getName();
            String langKey = "item." + MOD_ID + "." + itemName;

            add(langKey, entry.getLangKey());
        });
        BlockBuilder.getEntries().forEach(entry -> {
            String blockName = entry.getName();
            String langKey = "block." + MOD_ID + "." + blockName;

            add(langKey, entry.getLangKey());
        });
    }
}
