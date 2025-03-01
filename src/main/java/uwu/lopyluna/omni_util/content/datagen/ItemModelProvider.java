package uwu.lopyluna.omni_util.content.datagen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import uwu.lopyluna.omni_util.content.utils.builders.BlockItemBuilder;
import uwu.lopyluna.omni_util.content.utils.builders.ItemBuilder;

import java.util.Objects;

import static uwu.lopyluna.omni_util.OmniUtils.MOD_ID;

public class ItemModelProvider extends net.neoforged.neoforge.client.model.generators.ItemModelProvider {
    public ItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ItemBuilder.getEntries().forEach(entry -> entry.model(this));
        BlockItemBuilder.getEntries().forEach(entry -> entry.model(this));
    }

    @SuppressWarnings("all")
    public ItemModelBuilder simpleBlockItem(BlockItem block) {
        return simpleBlockItem(Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(block.getBlock())));
    }
}
