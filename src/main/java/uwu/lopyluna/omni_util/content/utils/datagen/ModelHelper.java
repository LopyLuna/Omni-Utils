package uwu.lopyluna.omni_util.content.utils.datagen;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import uwu.lopyluna.omni_util.OmniUtils;

import java.util.Objects;

public class ModelHelper {
    public static void forwardItem(DataGenContext<Item, ? extends Item> c, RegistrateItemModelProvider p) {
        var item = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(c.get()));
        p.getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile("item/goat_horn"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(item.getNamespace(), "item/" + item.getPath()));
    }
    public static void simpleBlockWithCT(DataGenContext<Block, ? extends Block> c, RegistrateBlockstateProvider p) {
        var model = p.models().singleTexture(c.getName(), OmniUtils.loc("block/cube_all_ct"), "all", p.blockTexture(c.get()));
        p.simpleBlock(c.get(), model);
        p.simpleBlockItem(c.get(), model);
    }
}
