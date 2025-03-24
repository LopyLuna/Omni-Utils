package uwu.lopyluna.omni_util.content.utils.datagen;

import com.google.gson.JsonObject;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.omni_util.OmniUtils;

import java.util.Objects;

@SuppressWarnings("unused")
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

    public static void createPointedDripstoneLike(DataGenContext<Block, ? extends Block> c, RegistrateBlockstateProvider p) {
        Block block = c.get();
        p.models().withExistingParent("pointed_grimrock", "minecraft:block/pointed_dripstone").texture("cross", p.blockTexture(block));
        p.itemModels().basicItem(OmniUtils.loc(c.getName()));

        for (Direction direction : Direction.values()) {
            if (!direction.getAxis().isVertical()) continue;

            for (DripstoneThickness thickness : DripstoneThickness.values()) {
                var builder = p.getVariantBuilder(block);

                String suffix = "_" + direction.getSerializedName() + "_" + thickness.getSerializedName();
                ResourceLocation texture = TextureMapping.getBlockTexture(block, suffix);
                TextureMapping mapping = TextureMapping.cross(texture);

                ResourceLocation modelLoc = ModelTemplates.POINTED_DRIPSTONE.createWithSuffix(block, suffix, mapping,
                        (loc, jsonSupplier) -> {
                            var modelBuilder = new BlockModelBuilder(loc, p.models().existingFileHelper) {
                                @Override
                                public @NotNull JsonObject toJson() {
                                    return jsonSupplier.get().getAsJsonObject();
                                }
                            };
                            p.models().generatedModels.put(loc, modelBuilder);
                        });

                builder.partialState()
                        .with(BlockStateProperties.VERTICAL_DIRECTION, direction)
                        .with(BlockStateProperties.DRIPSTONE_THICKNESS, thickness)
                        .modelForState()
                        .modelFile(new ModelFile.UncheckedModelFile(modelLoc))
                        .addModel();
            }
        }
    }
}
