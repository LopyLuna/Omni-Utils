package uwu.lopyluna.omni_util.content.utils.datagen;

import com.google.gson.JsonObject;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.omni_util.OmniUtils;
import uwu.lopyluna.omni_util.content.blocks.GlowrockBlock;
import uwu.lopyluna.omni_util.content.blocks.spike.SpikeBlock;

import java.util.ArrayList;
import java.util.List;
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

    public static void getExistingModel(DataGenContext<Block, ? extends Block> c, RegistrateBlockstateProvider p) {
        var model = p.models().getExistingFile(OmniUtils.loc(c.getName()));
        p.simpleBlock(c.get(), model);
        p.simpleBlockItem(c.get(), model);
    }

    public static NonNullBiConsumer<DataGenContext<Block, SpikeBlock>, RegistrateBlockstateProvider> modelSpike(String type) {
        return (c, p) -> {
            var model = p.models().withExistingParent("block/" + type + "_spike", OmniUtils.loc("block/base_spike"))
                    .texture("1", OmniUtils.loc("block/" + type + "_spike"))
                    .renderType(RenderType.CUTOUT.name);
            p.directionalBlock(c.get(), model);
            p.itemModels().basicItem(c.get().asItem());
        };
    }

    public static void createGlowrock(DataGenContext<Block, ? extends Block> c, RegistrateBlockstateProvider p) {
        Block block = c.get();
        IntegerProperty GLOWING = GlowrockBlock.GLOWING;
        VariantBlockStateBuilder builder = p.getVariantBuilder(block);
        for (int value = 0; value <= 2; value++) {
            List<String> textures = switch (value) {
                case 0 -> List.of("glowrock_dim_1", "glowrock_dim_2", "glowrock_dim_3");
                case 1 -> List.of("glowrock_1", "glowrock_2", "glowrock_3");
                case 2 -> List.of("glowrock_bright");
                default -> throw new IllegalArgumentException("Invalid glow value: " + value);
            };
            List<ConfiguredModel> variants = new ArrayList<>();
            for (String tex : textures) variants.add(new ConfiguredModel(p.models().cubeAll(tex, OmniUtils.loc("block/" + tex))));
            builder.partialState().with(GLOWING, value).addModels(variants.toArray(new ConfiguredModel[0]));
        }
        p.simpleBlockItem(block, p.models().cubeAll(c.getName(), OmniUtils.loc("block/glowrock_bright")));
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
