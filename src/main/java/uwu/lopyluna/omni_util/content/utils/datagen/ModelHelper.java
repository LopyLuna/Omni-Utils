package uwu.lopyluna.omni_util.content.utils.datagen;

import com.google.common.collect.Maps;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.omni_util.OmniUtils;
import uwu.lopyluna.omni_util.content.blocks.GlowrockBlock;
import uwu.lopyluna.omni_util.content.blocks.spike.SpikeBlock;
import uwu.lopyluna.omni_util.mixin.PartialBlockstateAccessor;

import java.util.*;
import java.util.function.Function;

@SuppressWarnings("unused")
public class ModelHelper {
    public static void genDirectional(ConfiguredModel.Builder<?> builder, BlockState state) {
        switch (state.getValue(BlockStateProperties.FACING)) {
            case DOWN -> builder.rotationX(90);
            case UP -> builder.rotationX(270);
            case NORTH -> {}
            case SOUTH -> builder.rotationY(180);
            case WEST -> builder.rotationY(270);
            case EAST -> builder.rotationY(90);
        }
    }

    public static void genHoriztonalDirectional(ConfiguredModel.Builder<?> builder, BlockState state) {
        switch (state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
            case NORTH -> {}
            case SOUTH -> builder.rotationY(180);
            case WEST -> builder.rotationY(270);
            case EAST -> builder.rotationY(90);
        }
    }

    public static void genHoriztonalAxis(ConfiguredModel.Builder<?> builder, BlockState state) {
        if (state.getValue(BlockStateProperties.HORIZONTAL_AXIS) == Direction.Axis.X) builder.rotationY(90);
    }

    public static int genHoriztonalAxis(BlockState state) {
        return state.getValue(BlockStateProperties.HORIZONTAL_AXIS) == Direction.Axis.X ? 90 : 0;
    }

    @SuppressWarnings("all")
    public static VariantBlockStateBuilder forAllStatesExcept(VariantBlockStateBuilder builder, Function<BlockState, ConfiguredModel[]> mapper, Property<?>... ignored) {
        Map<Property<?>, Comparable<?>> fixedProperties = Maps.newLinkedHashMap();

        for (Property<?> property : builder.getOwner().getStateDefinition().getProperties()) if (!Set.of(ignored).contains(property))
            fixedProperties.put(property, property.getPossibleValues().iterator().next());

        return builder.setModels(PartialBlockstateAccessor.omniUtil$create(builder.getOwner(), fixedProperties, builder), mapper.apply(builder.getOwner().defaultBlockState()));
    }

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
            p.getVariantBuilder(c.get()).forAllStates(state -> {
                var builder = ConfiguredModel.builder();
                builder.modelFile(model);
                genDirectional(builder, state);
                return builder.build();
            });
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
