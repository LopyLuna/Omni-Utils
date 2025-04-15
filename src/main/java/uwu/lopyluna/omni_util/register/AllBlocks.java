package uwu.lopyluna.omni_util.register;

import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.Tags;
import uwu.lopyluna.omni_util.OmniUtils;
import uwu.lopyluna.omni_util.content.blocks.*;
import uwu.lopyluna.omni_util.content.blocks.colored_block.ColoredBlock;
import uwu.lopyluna.omni_util.content.blocks.curse.CurseBlock;
import uwu.lopyluna.omni_util.content.blocks.curse.CurseLeavesBlock;
import uwu.lopyluna.omni_util.content.blocks.curse.CurseRotatedPillarBlock;
import uwu.lopyluna.omni_util.content.blocks.dead.DeadBlock;
import uwu.lopyluna.omni_util.content.blocks.dead.DeadLeavesBlock;
import uwu.lopyluna.omni_util.content.blocks.dead.DeadRotatedPillarBlock;
import uwu.lopyluna.omni_util.content.blocks.forces.*;
import uwu.lopyluna.omni_util.content.blocks.generator.ConsumorBlock;
import uwu.lopyluna.omni_util.content.blocks.generator.GeneratorBlock;
import uwu.lopyluna.omni_util.content.blocks.grim_devour.GrimDevourBlock;
import uwu.lopyluna.omni_util.content.blocks.panels.LunarPanelBlock;
import uwu.lopyluna.omni_util.content.blocks.panels.SolarPanelBlock;
import uwu.lopyluna.omni_util.content.blocks.spawner.AlteredSpawnerBlock;
import uwu.lopyluna.omni_util.content.blocks.spike.SpikeBlock;
import uwu.lopyluna.omni_util.content.items.AngelBlockItem;
import uwu.lopyluna.omni_util.content.utils.datagen.LootTableHelper;
import uwu.lopyluna.omni_util.content.utils.datagen.ModelHelper;

import static com.tterrag.registrate.providers.RegistrateRecipeProvider.has;
import static uwu.lopyluna.omni_util.OmniUtils.REG;
import static uwu.lopyluna.omni_util.content.utils.datagen.ModelHelper.*;
import static uwu.lopyluna.omni_util.content.utils.datagen.RecipeHelper.spikeRecipe;
import static uwu.lopyluna.omni_util.content.utils.datagen.TagHelper.*;

@SuppressWarnings({"unused", "removal"})
public class AllBlocks {

    public static final BlockEntry<Block> BORDER_STONE = REG.block("border_stone", Block::new)
            .lang("Border Stone")
            .properties(p -> p.strength(2.0F, 3.0F).requiresCorrectToolForDrops())
            .tag(mineablePickaxe(), needStoneTools())
            .blockstate((c, p) -> {
                var model = p.models().getExistingFile(OmniUtils.loc(c.getName()));
                p.simpleBlock(c.get(), model);
                p.simpleBlockItem(c.get(), model);
            })
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 2)
                    .pattern("CCC")
                    .pattern("CSC")
                    .pattern("CCC")
                    .define('S', Items.STONE)
                    .define('C', Items.COBBLESTONE)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
            .simpleItem()
            .register();

    public static final BlockEntry<Block> POLISHED_STONE = REG.block("polished_stone", Block::new)
            .lang("Polished Stone")
            .properties(p -> p.mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F))
            .tag(mineablePickaxe(), needStoneTools())
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 4)
                    .pattern("SS")
                    .pattern("SS")
                    .define('S', Items.STONE)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
            .simpleItem()
            .register();

    public static final BlockEntry<ColoredBlock> COLORED_BLOCK = REG.block("colored_block", ColoredBlock::new)
            .lang("Colorable Block")
            .properties(p -> p.strength(2.0F, 6.0F))
            .blockstate((c, p) -> p.simpleBlockWithItem(c.get(), p.models().cubeAll(c.getName(), p.modLoc("block/" + c.getName()))))
            .tag(mineablePickaxe(), needWoodTools())
            .simpleItem()
            .register();

    public static final BlockEntry<LightForceBlock> LIGHT_FORCE = REG.block("force_light", LightForceBlock::new)
            .lang("Force of Light")
            .properties(p -> p.strength(2.0F, 6.0F))
            .tag(mineablePickaxe(), needWoodTools())
            .addLayer(() -> RenderType::translucent)
            .blockstate((c, p) -> p.simpleBlockWithItem(c.get(), p.models().getExistingFile(OmniUtils.loc(c.getName()))))
            .simpleItem()
            .register();

    public static final BlockEntry<DarkForceBlock> DARK_FORCE = REG.block("force_dark", DarkForceBlock::new)
            .lang("Force of Darkness")
            .properties(p -> p.strength(2.0F, 6.0F))
            .tag(mineablePickaxe(), needWoodTools())
            .addLayer(() -> RenderType::translucent)
            .blockstate((c, p) -> p.simpleBlockWithItem(c.get(), p.models().getExistingFile(OmniUtils.loc(c.getName()))))
            .simpleItem()
            .register();

    public static final BlockEntry<HeatForceBlock> HEAT_FORCE = REG.block("force_heat", HeatForceBlock::new)
            .lang("Force of Heat")
            .properties(p -> p.strength(2.0F, 6.0F))
            .tag(mineablePickaxe(), needWoodTools())
            .addLayer(() -> RenderType::translucent)
            .blockstate((c, p) -> p.simpleBlockWithItem(c.get(), p.models().getExistingFile(OmniUtils.loc(c.getName()))))
            .simpleItem()
            .register();

    public static final BlockEntry<ColdForceBlock> COLD_FORCE = REG.block("force_cold", ColdForceBlock::new)
            .lang("Force of Cold")
            .properties(p -> p.strength(2.0F, 6.0F))
            .tag(mineablePickaxe(), needWoodTools())
            .addLayer(() -> RenderType::translucent)
            .blockstate((c, p) -> p.simpleBlockWithItem(c.get(), p.models().getExistingFile(OmniUtils.loc(c.getName()))))
            .simpleItem()
            .register();

    public static final BlockEntry<WarmForceBlock> WARM_FORCE = REG.block("force_warm", WarmForceBlock::new)
            .lang("Force of Warmth")
            .properties(p -> p.strength(2.0F, 6.0F))
            .tag(mineablePickaxe(), needWoodTools())
            .addLayer(() -> RenderType::translucent)
            .blockstate((c, p) -> p.simpleBlockWithItem(c.get(), p.models().getExistingFile(OmniUtils.loc(c.getName()))))
            .simpleItem()
            .register();

    public static final BlockEntry<ClockBlock> CLOCK = REG.block("clock", ClockBlock::new)
            .lang("Clock")
            .properties(p -> p.sound(SoundType.WOOD).strength(2.0F, 3.0F).requiresCorrectToolForDrops())
            .tag(mineablePickaxe())
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((c, p) -> {
                p.getVariantBuilder(c.get()).forAllStates(state -> {
                    int hour = state.getValue(ClockBlock.HOUR);
                    var model = p.models().withExistingParent("clock_" + hour, OmniUtils.loc("block/clock")).texture("2", OmniUtils.loc("block/clock_" + hour));

                    var builder = ConfiguredModel.builder();
                    builder.modelFile(model);
                    generateHoriztonalDirectional(builder, state);
                    return builder.build();
                });
                p.simpleBlockItem(c.get(), p.models().getExistingFile(OmniUtils.loc("block/clock")));
            })
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                    .pattern("WWW")
                    .pattern("CQG")
                    .pattern("WWW")
                    .define('W', ItemTags.PLANKS)
                    .define('G', Tags.Items.GLASS_BLOCKS_COLORLESS)
                    .define('Q', Items.QUARTZ)
                    .define('C', Items.CLOCK)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
            .simpleItem()
            .register();

    public static final BlockEntry<BreakerBlock> BREAKER = REG.block("breaker", BreakerBlock::new)
            .lang("Breaker")
            .properties(p -> p.strength(2.0F, 3.0F).requiresCorrectToolForDrops())
            .tag(mineablePickaxe(), needStoneTools())
            .blockstate((c, p) -> {
                var model = p.models().getExistingFile(OmniUtils.loc("block/breaker"));
                var modelOn = p.models().getExistingFile(OmniUtils.loc("block/breaker_on"));

                p.getVariantBuilder(c.get()).forAllStates(state -> {
                    boolean triggered = state.getValue(BlockStateProperties.TRIGGERED);
                    var builder = ConfiguredModel.builder();
                    builder.modelFile(triggered ? modelOn : model);
                    generateDirectional(builder, state);
                    return builder.build();
                });
                p.simpleBlockItem(c.get(), model);
            })
            .simpleItem()
            .register();

    public static final BlockEntry<InteractorBlock> INTERACTOR = REG.block("interactor", InteractorBlock::new)
            .lang("Interactor")
            .properties(p -> p.strength(2.0F, 3.0F).requiresCorrectToolForDrops())
            .tag(mineablePickaxe(), needStoneTools())
            .blockstate((c, p) -> {
                var model = p.models().getExistingFile(OmniUtils.loc("block/interactor"));
                var modelOn = p.models().getExistingFile(OmniUtils.loc("block/interactor_on"));

                p.getVariantBuilder(c.get()).forAllStates(state -> {
                    boolean enabled = state.getValue(BlockStateProperties.ENABLED);
                    var builder = ConfiguredModel.builder();
                    builder.modelFile(enabled ? modelOn : model);
                    generateDirectional(builder, state);
                    return builder.build();
                });
                p.simpleBlockItem(c.get(), model);
            })
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                    .pattern("CCC")
                    .pattern("IIQ")
                    .pattern("CCC")
                    .define('Q', Items.QUARTZ)
                    .define('I', Items.IRON_INGOT)
                    .define('C', Items.COBBLESTONE)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
            .simpleItem()
            .register();

    public static final BlockEntry<MagmaExtruderBlock> MAGMA_EXTRUDER = REG.block("magma_extruder", MagmaExtruderBlock::new)
            .lang("Magma Extruder")
            .properties(p -> p.sound(SoundType.STONE).strength(2.0F, 3.0F).requiresCorrectToolForDrops())
            .tag(mineablePickaxe())
            .blockstate((c, p) -> {
                p.getVariantBuilder(c.get()).forAllStates(state -> {
                    var string = "magma_extruder";
                    if (state.getValue(MagmaExtruderBlock.HAS_COBBLE)) string = string + "_cobble";
                    if (state.getValue(MagmaExtruderBlock.ENABLED)) string = string + "_on";

                    var model = p.models().getExistingFile(OmniUtils.loc("block/" + string));

                    var builder = ConfiguredModel.builder();
                    builder.modelFile(model);
                    generateHoriztonalDirectional(builder, state);
                    return builder.build();
                });
                p.simpleBlockItem(c.get(), p.models().getExistingFile(OmniUtils.loc("block/magma_extruder")));
            })
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                    .pattern("III")
                    .pattern("WDL")
                    .pattern("PRP")
                    .define('P', POLISHED_STONE)
                    .define('W', Items.WATER_BUCKET)
                    .define('L', Items.LAVA_BUCKET)
                    .define('R', Items.REDSTONE)
                    .define('I', Items.IRON_INGOT)
                    .define('D', Items.DROPPER)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
            .simpleItem()
            .register();

    public static final BlockEntry<AlteredSpawnerBlock> SPAWNER = REG.block("altered_spawner", AlteredSpawnerBlock::new)
            .lang("Altered Spawner")
            .properties(p -> p.noOcclusion().sound(SoundType.TRIAL_SPAWNER).strength(2.0F, 3.0F).requiresCorrectToolForDrops())
            .tag(mineablePickaxe(), needStoneTools())
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((c, p) -> {
                var modelOff = p.models().cubeColumn("altered_spawner", OmniUtils.loc("block/altered_spawner"), OmniUtils.loc("block/altered_spawner_top"));
                var modelActive = p.models().cubeColumn("altered_spawner_active", OmniUtils.loc("block/altered_spawner_active"), OmniUtils.loc("block/altered_spawner_active_top"));
                p.getVariantBuilder(c.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(state.getValue(AlteredSpawnerBlock.ENABLED) ? modelActive : modelOff).build());
                p.simpleBlockItem(c.get(), modelActive);
            })
            .simpleItem()
            .register();

    public static final BlockEntry<GeneratorBlock> GENERATOR = REG.block("generator_block", GeneratorBlock::new)
            .lang("Generator")
            .properties(p -> p.strength(2.0F, 3.0F).requiresCorrectToolForDrops())
            .tag(mineablePickaxe(), needStoneTools())
            .simpleItem()
            .register();

    public static final BlockEntry<ConsumorBlock> CONSUMOR = REG.block("consumor_block", ConsumorBlock::new)
            .lang("Consumer")
            .properties(p -> p.strength(2.0F, 3.0F).requiresCorrectToolForDrops())
            .tag(mineablePickaxe(), needStoneTools())
            .simpleItem()
            .register();

    public static final BlockEntry<SolarPanelBlock> SOLAR_PANEL = REG.block("solar_panel", SolarPanelBlock::new)
            .lang("Solar Panel")
            .properties(p -> p.strength(0.5F, 2.0F).requiresCorrectToolForDrops())
            .tag(mineablePickaxe(), needWoodTools())
            .blockstate(ModelHelper::getExistingModel)
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                    .pattern("GGG")
                    .pattern("AQA")
                    .pattern("PIP")
                    .define('P', POLISHED_STONE)
                    .define('G', Tags.Items.GLASS_BLOCKS_COLORLESS)
                    .define('A', Items.AMETHYST_SHARD)
                    .define('Q', Items.QUARTZ)
                    .define('I', Items.GOLD_INGOT)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
            .simpleItem()
            .register();

    public static final BlockEntry<LunarPanelBlock> LUNAR_PANEL = REG.block("lunar_panel", LunarPanelBlock::new)
            .lang("Lunar Panel")
            .properties(p -> p.strength(0.5F, 2.0F).requiresCorrectToolForDrops())
            .tag(mineablePickaxe(), needWoodTools())
            .blockstate(ModelHelper::getExistingModel)
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                    .pattern("GGG")
                    .pattern("AQA")
                    .pattern("PIP")
                    .define('P', POLISHED_STONE)
                    .define('G', Tags.Items.GLASS_BLOCKS_COLORLESS)
                    .define('A', Items.AMETHYST_SHARD)
                    .define('Q', Items.QUARTZ)
                    .define('I', Items.IRON_INGOT)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
            .simpleItem()
            .register();

    public static final BlockEntry<UnstableHexaTnt> UNSTABLE_HEXA_TNT = REG.block("hexa_tnt", UnstableHexaTnt::new)
            .lang("Unstable Hexa TNT")
            .properties(p -> p.strength(8.0F, 8.0F))
            .tag(mineablePickaxe(), mineableHoe(), mineableAxe(), mineableShovel(), mineableSword(), needIronTools())
            .blockstate((c, p) -> {
                var model = p.models().cubeBottomTop(c.getName(),
                        OmniUtils.loc("block/hexa_tnt_side"),
                        OmniUtils.loc("block/hexa_tnt_bottom"),
                        OmniUtils.loc("block/hexa_tnt_top"));
                p.simpleBlock(c.get(), model);
                p.simpleBlockItem(c.get(), model);
            })
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 4)
                    .pattern("HTH")
                    .pattern("THT")
                    .pattern("HTH")
                    .define('T', Items.TNT)
                    .define('H', AllItems.UNSTABLE_HEXA_INGOT)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
            .simpleItem()
            .register();

    public static final BlockEntry<UnstableHexaBlock> UNSTABLE_HEXA_BLOCK = REG.block("unstable_hexa_block", UnstableHexaBlock::new)
            .lang("Block of Unstable Hexa")
            .properties(p -> p.strength(10.0F, 12.0F).requiresCorrectToolForDrops())
            .tag(mineablePickaxe(), needIronTools())
            .simpleItem()
            .register();

    public static final BlockEntry<Block> HEXA_BLOCK = REG.block("hexa_block", Block::new)
            .lang("Block of Hexa")
            .properties(p -> p.strength(10.0F, 12.0F).requiresCorrectToolForDrops())
            .tag(mineablePickaxe(), needIronTools())
            .simpleItem()
            .register();

    public static final BlockEntry<AngelBlock> ANGEL_BLOCK = REG.block("angel_block", AngelBlock::new)
            .lang("Angel Block")
            .properties(p -> p.emissiveRendering(AllBlocks::always).mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(25.0F, 1200.0F))
            .tag(mineablePickaxe(), needIronTools())
            .blockstate((c, p) -> {
                var model = p.models().getExistingFile(OmniUtils.loc("block/angel_block"));
                p.simpleBlock(c.get(), model);
                p.simpleBlockItem(c.get(), model);
            })
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                    .pattern("GOG")
                    .pattern("OCO")
                    .pattern("GOG")
                    .define('O', Tags.Items.OBSIDIANS)
                    .define('C', Items.END_CRYSTAL)
                    .define('G', Items.GOLD_INGOT)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
            .item(AngelBlockItem::new)
            .properties(p -> p.fireResistant().rarity(Rarity.UNCOMMON))
            .build()
            .register();

    public static final BlockEntry<GrimDevourBlock> GRIM_DEVOUR = REG.block("grim_devour", GrimDevourBlock::new)
            .lang("Grim Devour")
            .properties(p -> p.mapColor(MapColor.COLOR_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(37.5F, 5000.0F))
            .tag(mineablePickaxe(), needIronTools())
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                    .pattern("GOG")
                    .pattern("OCO")
                    .pattern("GOG")
                    .define('O', AllTags.itemC("ingots/hexa"))
                    .define('C', Items.SCULK_CATALYST)
                    .define('G', BORDER_STONE)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
            .simpleItem()
            .register();

    public static final BlockEntry<GrimspiralBlock> GRIMSPIRAL = REG.block("grimspiral", GrimspiralBlock::new)
            .lang("Grimspiral")
            .properties(p -> p.mapColor(MapColor.COLOR_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(75.0F, 5000.0F))
            .tag(mineablePickaxe(), needDiamondTools())
            .loot((p, c) -> p.dropOther(c, AllBlocks.GRIMROCK.get()))
            .item()
            .properties(p -> p.fireResistant().rarity(Rarity.EPIC))
            .build()
            .register();

    public static final BlockEntry<PointedGrimrockBlock> POINTED_GRIMROCK = REG.block("pointed_grimrock", PointedGrimrockBlock::new)
            .lang("Pointed Grimrock")
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY)
                    .noOcclusion()
                    .sound(AllSoundTypes.POINTED_GRIMROCK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(9.0F, 12.0F)
                    .dynamicShape()
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .pushReaction(PushReaction.DESTROY)
                    .isRedstoneConductor(AllBlocks::never))
            .addLayer(() -> RenderType::cutoutMipped)
            .tag(mineablePickaxe(), needIronTools())
            .blockstate(ModelHelper::createPointedDripstoneLike)
            .simpleItem()
            .register();

    public static final BlockEntry<Block> COBBLED_GRIMROCK = REG.block("cobbled_grimrock", Block::new)
            .lang("Cobbled Grimrock")
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY)
                    .sound(AllSoundTypes.GRIMROCK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(12.0F, 12.0F))
            .tag(mineablePickaxe(), needIronTools())
            .simpleItem()
            .register();

    public static final BlockEntry<Block> GRIMROCK = REG.block("grimrock", Block::new)
            .lang("Grimrock")
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY)
                    .sound(AllSoundTypes.COBBLED_GRIMROCK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(9.0F, 12.0F))
            .tag(mineablePickaxe(), needIronTools())
            .loot((p, c) -> p.add(c, p.createSingleItemTableWithSilkTouch(c, AllBlocks.COBBLED_GRIMROCK.get())))
            .simpleItem()
            .register();

    public static final BlockEntry<Block> SMOOTH_GRIMROCK = REG.block("smooth_grimrock", Block::new)
            .lang("Smooth Grimrock")
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY)
                    .sound(AllSoundTypes.GRIMROCK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(11.25F, 12.0F))
            .tag(mineablePickaxe(), needIronTools())
            .simpleItem()
            .register();

    public static final BlockEntry<Block> POLISHED_GRIMROCK = REG.block("polished_grimrock", Block::new)
            .lang("Polished Grimrock")
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY)
                    .sound(AllSoundTypes.POLISHED_GRIMROCK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(11.25F, 12.0F))
            .tag(mineablePickaxe(), needIronTools())
            .simpleItem()
            .register();

    public static final BlockEntry<StairBlock> POLISHED_GRIMROCK_STAIRS = REG.block("polished_grimrock_stairs", p -> new StairBlock(COBBLED_GRIMROCK.getDefaultState(), p))
            .lang("Polished Grimrock Stairs")
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY)
                    .sound(AllSoundTypes.POLISHED_GRIMROCK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(11.25F, 12.0F))
            .tag(mineablePickaxe(), needIronTools())
            .blockstate((c, p) -> p.stairsBlock(c.get(), OmniUtils.loc("block/polished_grimrock")))
            .simpleItem()
            .register();

    public static final BlockEntry<SlabBlock> POLISHED_GRIMROCK_SLAB = REG.block("polished_grimrock_slab", SlabBlock::new)
            .lang("Polished Grimrock Slab")
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY)
                    .sound(AllSoundTypes.POLISHED_GRIMROCK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(11.25F, 12.0F))
            .tag(mineablePickaxe(), needIronTools())
            .blockstate((c, p) -> p.slabBlock(c.get(), OmniUtils.loc("block/polished_grimrock"), OmniUtils.loc("block/polished_grimrock_slab"), OmniUtils.loc("block/polished_grimrock"), OmniUtils.loc("block/polished_grimrock")))
            .loot((p, c) -> p.add(c, p.createSlabItemTable(c)))
            .simpleItem()
            .register();

    public static final BlockEntry<Block> GRIMROCK_BRICKS = REG.block("grimrock_bricks", Block::new)
            .lang("Grimrock Bricks")
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY)
                    .sound(AllSoundTypes.POLISHED_GRIMROCK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(11.25F, 12.0F))
            .tag(mineablePickaxe(), needIronTools())
            .simpleItem()
            .register();

    public static final BlockEntry<StairBlock> GRIMROCK_BRICK_STAIRS = REG.block("grimrock_brick_stairs", p -> new StairBlock(COBBLED_GRIMROCK.getDefaultState(), p))
            .lang("Grimrock Brick Stairs")
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY)
                    .sound(AllSoundTypes.POLISHED_GRIMROCK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(11.25F, 12.0F))
            .tag(mineablePickaxe(), needIronTools())
            .blockstate((c, p) -> p.stairsBlock(c.get(), OmniUtils.loc("block/grimrock_bricks")))
            .simpleItem()
            .register();

    public static final BlockEntry<SlabBlock> GRIMROCK_BRICK_SLAB = REG.block("grimrock_brick_slab", SlabBlock::new)
            .lang("Grimrock Brick Slab")
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY)
                    .sound(AllSoundTypes.POLISHED_GRIMROCK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(11.25F, 12.0F))
            .tag(mineablePickaxe(), needIronTools())
            .blockstate((c, p) -> p.slabBlock(c.get(), OmniUtils.loc("block/grimrock_bricks"), OmniUtils.loc("block/grimrock_bricks")))
            .loot((p, c) -> p.add(c, p.createSlabItemTable(c)))
            .simpleItem()
            .register();

    public static final BlockEntry<Block> CHISELED_GRIMROCK_BRICKS = REG.block("chiseled_grimrock_bricks", Block::new)
            .lang("Chiseled Grimrock")
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY)
                    .sound(AllSoundTypes.POLISHED_GRIMROCK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(11.25F, 12.0F))
            .tag(mineablePickaxe(), needIronTools())
            .simpleItem()
            .register();

    public static final BlockEntry<GlowrockBlock> GLOWROCK = REG.block("glowrock", GlowrockBlock::new)
            .lang("Glowrock")
            .properties(p -> p.lightLevel(b -> b.getValue(GlowrockBlock.GLOWING) == 0 ? 4 : b.getValue(GlowrockBlock.GLOWING) == 1 ? 8 : 12)
                    .mapColor(MapColor.TERRACOTTA_ORANGE)
                    .sound(AllSoundTypes.GRIMROCK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(13.5F, 12.0F))
            .blockstate(ModelHelper::createGlowrock)
            .tag(mineablePickaxe(), needIronTools())
            .loot((p, c) -> p.add(c, p.createSilkTouchDispatchTable(c,
                    p.applyExplosionDecay(c, LootItem.lootTableItem(Items.GLOWSTONE_DUST)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F)))
                            .apply(ApplyBonusCount.addOreBonusCount(p.getRegistries().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE))))
            )))
            .simpleItem()
            .register();

    public static final BlockEntry<Block> GRIMROCK_GOLD_ORE = REG.block("grimrock_gold_ore", Block::new)
            .lang("Grimrock Gold Ore")
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY)
                    .sound(AllSoundTypes.COBBLED_GRIMROCK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(13.5F, 12.0F))
            .tag(mineablePickaxe(), needDiamondTools())
            .loot((p, c) -> p.add(c, p.createOreDrop(c, Items.RAW_GOLD)))
            .simpleItem()
            .register();

    public static final BlockEntry<Block> GRIMROCK_IRON_ORE = REG.block("grimrock_iron_ore", Block::new)
            .lang("Grimrock Iron Ore")
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY)
                    .sound(AllSoundTypes.COBBLED_GRIMROCK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(13.5F, 12.0F))
            .tag(mineablePickaxe(), needIronTools())
            .loot((p, c) -> p.add(c, p.createOreDrop(c, Items.RAW_IRON)))
            .simpleItem()
            .register();

    public static final BlockEntry<DropExperienceBlock> GRIMROCK_COAL_ORE = REG.block("grimrock_coal_ore", p -> new DropExperienceBlock(UniformInt.of(2, 4), p))
            .lang("Grimrock Coal Ore")
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY)
                    .sound(AllSoundTypes.COBBLED_GRIMROCK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(13.5F, 12.0F))
            .tag(mineablePickaxe(), needIronTools())
            .loot((p, c) -> p.add(c, p.createOreDrop(c, Items.COAL)))
            .simpleItem()
            .register();

    public static final BlockEntry<DropExperienceBlock> GRIMROCK_GILDED_ORE = REG.block("grimrock_gilded_ore", p -> new DropExperienceBlock(UniformInt.of(1, 2), p))
            .lang("Grimrock Gilded Ore")
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY)
                    .sound(AllSoundTypes.COBBLED_GRIMROCK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(10.0F, 12.0F))
            .tag(mineablePickaxe(), needIronTools())
            .loot((p, c) -> p.add(c, p.createSilkTouchDispatchTable(c,
                    p.applyExplosionDecay(c, LootItem.lootTableItem(Items.GOLD_NUGGET)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 12.0F)))
                            .apply(ApplyBonusCount.addOreBonusCount(p.getRegistries().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE))))
            )))
            .simpleItem()
            .register();

    public static final BlockEntry<DropExperienceBlock> GRIMROCK_LAPIS_ORE = REG.block("grimrock_lapis_ore", p -> new DropExperienceBlock(UniformInt.of(5, 10), p))
            .lang("Grimrock Lapis Ore")
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY)
                    .sound(AllSoundTypes.COBBLED_GRIMROCK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(13.5F, 12.0F))
            .tag(mineablePickaxe(), needIronTools())
            .loot((p, c) -> p.add(c, p.createSilkTouchDispatchTable(c,
                    p.applyExplosionDecay(c, LootItem.lootTableItem(Items.LAPIS_LAZULI)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F)))
                            .apply(ApplyBonusCount.addOreBonusCount(p.getRegistries().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE))))
            )))
            .simpleItem()
            .register();

    public static final BlockEntry<DropExperienceBlock> GRIMROCK_DIAMOND_ORE = REG.block("grimrock_diamond_ore", p -> new DropExperienceBlock(UniformInt.of(7, 14), p))
            .lang("Grimrock Diamond Ore")
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY)
                    .sound(AllSoundTypes.COBBLED_GRIMROCK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(13.5F, 12.0F))
            .tag(mineablePickaxe(), needDiamondTools())
            .loot((p, c) -> p.add(c, p.createOreDrop(c, Items.DIAMOND)))
            .simpleItem()
            .register();

    public static final BlockEntry<DropExperienceBlock> GRIMROCK_REDSTONE_ORE = REG.block("grimrock_redstone_ore", p -> new DropExperienceBlock(UniformInt.of(5, 10), p))
            .lang("Grimrock Redstone Ore")
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY)
                    .sound(AllSoundTypes.COBBLED_GRIMROCK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(13.5F, 12.0F))
            .tag(mineablePickaxe(), needDiamondTools())
            .loot((p, c) -> p.add(c, p.createSilkTouchDispatchTable(c,
                    p.applyExplosionDecay(c, LootItem.lootTableItem(Items.REDSTONE)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 6.0F)))
                            .apply(ApplyBonusCount.addOreBonusCount(p.getRegistries().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE))))
            )))
            .simpleItem()
            .register();

    public static final BlockEntry<DropExperienceBlock> GRIMROCK_EMERALD_ORE = REG.block("grimrock_emerald_ore", p -> new DropExperienceBlock(UniformInt.of(7, 14), p))
            .lang("Grimrock Emerald Ore")
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY)
                    .sound(AllSoundTypes.COBBLED_GRIMROCK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(13.5F, 12.0F))
            .tag(mineablePickaxe(), needDiamondTools())
            .loot((p, c) -> p.add(c, p.createOreDrop(c, Items.EMERALD)))
            .simpleItem()
            .register();

    public static final BlockEntry<DropExperienceBlock> GRIMROCK_QUARTZ_ORE = REG.block("grimrock_quartz_ore", p -> new DropExperienceBlock(UniformInt.of(5, 10), p))
            .lang("Grimrock Quartz Ore")
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY)
                    .sound(AllSoundTypes.COBBLED_GRIMROCK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(10.0F, 12.0F))
            .tag(mineablePickaxe(), needDiamondTools())
            .loot((p, c) -> p.add(c, p.createOreDrop(c, Items.QUARTZ)))
            .simpleItem()
            .register();

    public static final BlockEntry<Block> GRIMROCK_COPPER_ORE = REG.block("grimrock_copper_ore", Block::new)
            .lang("Grimrock Copper Ore")
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY)
                    .sound(AllSoundTypes.COBBLED_GRIMROCK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(13.5F, 12.0F))
            .tag(mineablePickaxe(), needIronTools())
            .loot((p, c) -> p.add(c, p.createSilkTouchDispatchTable(c,
                    p.applyExplosionDecay(c, LootItem.lootTableItem(Items.RAW_COPPER)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F)))
                            .apply(ApplyBonusCount.addOreBonusCount(p.getRegistries().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE))))
            )))
            .simpleItem()
            .register();

    public static final BlockEntry<DropExperienceBlock> GRIMROCK_AMETHYST_ORE = REG.block("grimrock_amethyst_ore", p -> new DropExperienceBlock(UniformInt.of(1, 4), p))
            .lang("Grimrock Amethyst Ore")
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY)
                    .sound(AllSoundTypes.COBBLED_GRIMROCK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(10.0F, 12.0F))
            .tag(mineablePickaxe(), needIronTools())
            .loot((p, c) -> p.add(c, p.createSilkTouchDispatchTable(c,
                    p.applyExplosionDecay(c, LootItem.lootTableItem(Items.AMETHYST_SHARD)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 6.0F)))
                            .apply(ApplyBonusCount.addOreBonusCount(p.getRegistries().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE))))
            )))
            .simpleItem()
            .register();

    public static final BlockEntry<Block> GRIMROCK_ANCIENT_ORE = REG.block("grimrock_ancient_ore", Block::new)
            .lang("Grimrock Ancient Ore")
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY)
                    .sound(AllSoundTypes.COBBLED_GRIMROCK)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(60.0F, 1200.0F))
            .tag(mineablePickaxe(), needNetheriteTools())
            .loot((p, c) -> p.add(c, p.createOreDrop(c, Items.ANCIENT_DEBRIS)))
            .simpleItem()
            .register();


    //SPIKES

    public static final BlockEntry<SpikeBlock> COPPER_SPIKE = REG.block("copper_spike", p -> new SpikeBlock(p, 1.0f, true, false, false, false))
            .lang("Copper Spike")
            .properties(p -> p.sound(SoundType.COPPER).mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(1.5F, 5.0F).noOcclusion())
            .tag(mineablePickaxe(), needStoneTools())
            .blockstate(modelSpike("copper"))
            .recipe((c, p) -> spikeRecipe(c, p, Items.COPPER_INGOT, Items.COPPER_BLOCK))
            .simpleItem()
            .register();
    public static final BlockEntry<SpikeBlock> STONE_SPIKE = REG.block("stone_spike", p -> new SpikeBlock(p, 1.0f, false, false, false, false))
            .lang("Stone Spike")
            .properties(p -> p.sound(SoundType.STONE).mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(1.5F, 5.0F).noOcclusion())
            .tag(mineablePickaxe(), needStoneTools())
            .blockstate(modelSpike("stone"))
            .recipe((c, p) -> spikeRecipe(c, p, Items.COBBLESTONE, Items.SMOOTH_STONE))
            .simpleItem()
            .register();
    public static final BlockEntry<SpikeBlock> GOLD_SPIKE = REG.block("gold_spike", p -> new SpikeBlock(p, 2.5f, true, false, true, false))
            .lang("Gold Spike")
            .properties(p -> p.sound(SoundType.METAL).mapColor(MapColor.GOLD).requiresCorrectToolForDrops().strength(3.0F, 6.0F).noOcclusion())
            .tag(mineablePickaxe(), needStoneTools())
            .blockstate(modelSpike("gold"))
            .recipe((c, p) -> spikeRecipe(c, p, Items.GOLD_INGOT, Items.GOLD_BLOCK))
            .simpleItem()
            .register();
    public static final BlockEntry<SpikeBlock> IRON_SPIKE = REG.block("iron_spike", p -> new SpikeBlock(p, 2.5f, false, false, true, false))
            .lang("Iron Spike")
            .properties(p -> p.sound(SoundType.METAL).mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(5.0F, 7.0F).noOcclusion())
            .tag(mineablePickaxe(), needStoneTools())
            .blockstate(modelSpike("iron"))
            .recipe((c, p) -> spikeRecipe(c, p, Items.IRON_INGOT, Items.IRON_BLOCK))
            .simpleItem()
            .register();
    public static final BlockEntry<SpikeBlock> DIAMOND_SPIKE = REG.block("diamond_spike", p -> new SpikeBlock(p, 5.0f, false, true, true, false))
            .lang("Diamond Spike")
            .properties(p -> p.sound(SoundType.METAL).mapColor(MapColor.DIAMOND).requiresCorrectToolForDrops().strength(5.0F, 8.0F).noOcclusion())
            .tag(mineablePickaxe(), needStoneTools())
            .blockstate(modelSpike("diamond"))
            .recipe((c, p) -> spikeRecipe(c, p, Items.DIAMOND, Items.DIAMOND_BLOCK))
            .simpleItem()
            .register();
    public static final BlockEntry<SpikeBlock> NETHERITE_SPIKE = REG.block("netherite_spike", p -> new SpikeBlock(p, 7.5f, false, true, true, true))
            .lang("Netherite Spike")
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(20.0F, 600.0F).noOcclusion())
            .tag(mineablePickaxe(), needStoneTools())
            .blockstate(modelSpike("netherite"))
            .recipe((c, p) -> spikeRecipe(c, p, Items.NETHERITE_SCRAP, Items.NETHERITE_INGOT))
            .item()
            .properties(Item.Properties::fireResistant)
            .build()
            .register();

    //CURSED BLOCKS

    public static final BlockEntry<CurseBlock> CURSE_NYLIUM = REG.block("curse_nylium", p -> new CurseBlock(p, 1))
            .properties(p -> p.mapColor(DyeColor.BLACK).strength(0.8F).sound(AllSoundTypes.CURSED_NYLIUM))
            .lang("Cursed Nylium")
            .blockstate((c, p) -> {
                var model = p.models()
                        .withExistingParent("block/curse_nylium", ResourceLocation.withDefaultNamespace("block/podzol"))
                        .texture("side", OmniUtils.loc("block/curse_nylium"))
                        .texture("top", OmniUtils.loc("block/curse_nylium_top"));
                p.simpleBlock(c.get(), model);
                p.simpleBlockItem(c.get(), model);
            })
            .loot((p, c) -> p.add(c, p.createSingleItemTableWithSilkTouch(c, Items.COARSE_DIRT)))
            .tag(BlockTags.DIRT, mineableShovel())
            .simpleItem()
            .register();

    public static final BlockEntry<CurseBlock> CURSE_COARSE = REG.block("curse_coarse", CurseBlock::new)
            .properties(p -> p.mapColor(MapColor.TERRACOTTA_GRAY).strength(0.8F).sound(AllSoundTypes.CURSED_SOIL))
            .lang("Cursed Coarse Soil")
            .loot((p, c) -> p.add(c, p.createSingleItemTableWithSilkTouch(c, Items.COARSE_DIRT)))
            .tag(BlockTags.DIRT, mineableShovel())
            .simpleItem()
            .register();

    public static final BlockEntry<CurseBlock> CURSE_STONE = REG.block("curse_stone", CurseBlock::new)
            .properties(p -> p.mapColor(DyeColor.BLACK).instrument(NoteBlockInstrument.BASEDRUM).sound(AllSoundTypes.CURSED_STONE).requiresCorrectToolForDrops().strength(2.0F, 8.0F))
            .lang("Cursed Stone")
            .tag(mineablePickaxe())
            .simpleItem()
            .register();

    public static final BlockEntry<CurseBlock> CURSE_ROOTS = REG.block("curse_roots", p -> new CurseBlock(p, 2))
            .properties(p -> p.mapColor(DyeColor.BLACK).replaceable().noCollission().instabreak().sound(SoundType.ROOTS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY))
            .lang("Cursed Roots")
            .blockstate((c, p) -> {
                p.simpleBlock(c.get(), p.models().cross("block/curse_roots", OmniUtils.loc("block/curse_roots")).renderType(RenderType.CUTOUT.name));
                p.itemModels().basicItem(OmniUtils.loc("curse_roots"));
            })
            .loot(LootTableHelper::dropLikeNetherVines)
            .tag(mineableSword())
            .simpleItem()
            .register();

    public static final BlockEntry<CurseLeavesBlock> CURSE_LEAVES = REG.block("curse_leaves", CurseLeavesBlock::new)
            .properties(p -> p.mapColor(DyeColor.BLACK).strength(0.4F).noOcclusion().sound(AllSoundTypes.CURSED_LEAVES).isValidSpawn(Blocks::ocelotOrParrot).isSuffocating(AllBlocks::never).isViewBlocking(AllBlocks::never).ignitedByLava().pushReaction(PushReaction.DESTROY).isRedstoneConductor(AllBlocks::never))
            .lang("Cursed Leaves")
            .loot(RegistrateBlockLootTables::dropWhenSilkTouch)
            .blockstate((c, p) -> {
                var model = p.models().leaves("block/curse_leaves", OmniUtils.loc("block/curse_leaves")).renderType(RenderType.CUTOUT.name);
                p.simpleBlock(c.get(), model);
                p.simpleBlockItem(c.get(), model);
            })
            .tag(mineableHoe())
            .simpleItem()
            .register();

    public static final BlockEntry<CurseRotatedPillarBlock> CURSE_LOG = REG.block("curse_log", CurseRotatedPillarBlock::new)
            .properties(p -> p.mapColor(DyeColor.BLACK).instrument(NoteBlockInstrument.BASS).sound(AllSoundTypes.CURSED_LOG).strength(2.5F).ignitedByLava())
            .lang("Cursed Log")
            .blockstate((c, p) -> {
                p.logBlock(c.get());
                p.simpleBlockItem(c.get(), p.models().getExistingFile(OmniUtils.loc("block/curse_log")));
            })
            .tag(mineableAxe())
            .simpleItem()
            .register();

    public static final BlockEntry<DeadBlock> DEAD_NYLIUM = REG.block("dead_nylium", p -> new DeadBlock(p, 1))
            .properties(p -> p.mapColor(MapColor.TERRACOTTA_BLACK).strength(0.8F).sound(AllSoundTypes.CURSED_NYLIUM))
            .lang("Dead Nylium")
            .blockstate((c, p) -> {
                var model = p.models()
                        .withExistingParent("block/dead_nylium", ResourceLocation.withDefaultNamespace("block/podzol"))
                        .texture("side", OmniUtils.loc("block/dead_nylium"))
                        .texture("top", OmniUtils.loc("block/dead_nylium_top"));
                p.simpleBlock(c.get(), model);
                p.simpleBlockItem(c.get(), model);
            })
            .loot((p, c) -> p.add(c, p.createSingleItemTableWithSilkTouch(c, Items.COARSE_DIRT)))
            .tag(BlockTags.DIRT, mineableShovel())
            .simpleItem()
            .register();

    public static final BlockEntry<DeadBlock> DEAD_COARSE = REG.block("dead_coarse", DeadBlock::new)
            .properties(p -> p.mapColor(MapColor.TERRACOTTA_BROWN).strength(0.8F).sound(AllSoundTypes.CURSED_SOIL))
            .lang("Dead Coarse Soil")
            .loot((p, c) -> p.add(c, p.createSingleItemTableWithSilkTouch(c, Items.COARSE_DIRT)))
            .tag(BlockTags.DIRT, mineableShovel())
            .simpleItem()
            .register();

    public static final BlockEntry<DeadBlock> DEAD_STONE = REG.block("dead_stone", DeadBlock::new)
            .properties(p -> p.mapColor(MapColor.TERRACOTTA_BLACK).instrument(NoteBlockInstrument.BASEDRUM).sound(AllSoundTypes.CURSED_STONE).requiresCorrectToolForDrops().strength(2.0F, 8.0F))
            .lang("Dead Stone")
            .tag(mineablePickaxe())
            .simpleItem()
            .register();

    public static final BlockEntry<DeadBlock> DEAD_ROOTS = REG.block("dead_roots", p -> new DeadBlock(p, 2))
            .properties(p -> p.mapColor(MapColor.TERRACOTTA_BLACK).replaceable().noCollission().instabreak().sound(SoundType.ROOTS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY))
            .lang("Dead Roots")
            .blockstate((c, p) -> {
                p.simpleBlock(c.get(), p.models().cross("block/dead_roots", OmniUtils.loc("block/dead_roots")).renderType(RenderType.CUTOUT.name));
                p.itemModels().basicItem(OmniUtils.loc("dead_roots"));
            })
            .loot(LootTableHelper::dropLikeNetherVines)
            .tag(mineableSword())
            .simpleItem()
            .register();

    public static final BlockEntry<DeadLeavesBlock> DEAD_LEAVES = REG.block("dead_leaves", DeadLeavesBlock::new)
            .properties(p -> p.mapColor(MapColor.TERRACOTTA_BLACK).strength(0.4F).noOcclusion().sound(AllSoundTypes.CURSED_LEAVES).isValidSpawn(Blocks::ocelotOrParrot).isSuffocating(AllBlocks::never).isViewBlocking(AllBlocks::never).ignitedByLava().pushReaction(PushReaction.DESTROY).isRedstoneConductor(AllBlocks::never))
            .lang("Dead Leaves")
            .loot(RegistrateBlockLootTables::dropWhenSilkTouch)
            .blockstate((c, p) -> {
                var model = p.models().leaves("block/dead_leaves", OmniUtils.loc("block/dead_leaves")).renderType(RenderType.CUTOUT.name);
                p.simpleBlock(c.get(), model);
                p.simpleBlockItem(c.get(), model);
            })
            .tag(mineableHoe())
            .simpleItem()
            .register();

    public static final BlockEntry<DeadRotatedPillarBlock> DEAD_LOG = REG.block("dead_log", DeadRotatedPillarBlock::new)
            .properties(p -> p.mapColor(MapColor.TERRACOTTA_BLACK).instrument(NoteBlockInstrument.BASS).sound(AllSoundTypes.CURSED_LOG).strength(2.5F).ignitedByLava())
            .lang("Dead Log")
            .blockstate((c, p) -> {
                p.logBlock(c.get());
                p.simpleBlockItem(c.get(), p.models().getExistingFile(OmniUtils.loc("block/dead_log")));
            })
            .tag(mineableAxe())
            .simpleItem()
            .register();


    public static Block curseRemap(BlockState targetBlock, Level level) {
        if (targetBlock.is(Blocks.GRASS_BLOCK) || targetBlock.is(Blocks.MYCELIUM) || targetBlock.is(Blocks.PODZOL) || targetBlock.is(Blocks.DIRT_PATH))
            return CURSE_NYLIUM.get();
        if (targetBlock.is(BlockTags.DIRT))
            return CURSE_COARSE.get();
        if (targetBlock.is(BlockTags.BASE_STONE_OVERWORLD) || targetBlock.is(Tags.Blocks.COBBLESTONES))
            return CURSE_STONE.get();
        if (targetBlock.is(BlockTags.SMALL_FLOWERS))
            return level.random.nextBoolean() ? Blocks.WITHER_ROSE : CURSE_ROOTS.get();
        if (targetBlock.is(Blocks.SHORT_GRASS) || targetBlock.is(Blocks.FERN) || targetBlock.is(Blocks.DEAD_BUSH) || targetBlock.is(BlockTags.SAPLINGS))
            return CURSE_ROOTS.get();
        if (targetBlock.is(BlockTags.LEAVES))
            return CURSE_LEAVES.get();
        if (targetBlock.is(BlockTags.OVERWORLD_NATURAL_LOGS))
            return CURSE_LOG.get();
        return targetBlock.getBlock();
    }
    public static Block deadRemap(BlockState targetBlock) {
        if (targetBlock.is(CURSE_NYLIUM.get()))
            return DEAD_NYLIUM.get();
        if (targetBlock.is(CURSE_COARSE.get()))
            return DEAD_COARSE.get();
        if (targetBlock.is(CURSE_STONE.get()))
            return DEAD_STONE.get();
        if (targetBlock.is(CURSE_ROOTS.get()))
            return DEAD_ROOTS.get();
        if (targetBlock.is(CURSE_LEAVES.get()))
            return DEAD_LEAVES.get();
        if (targetBlock.is(CURSE_LOG.get()))
            return DEAD_LOG.get();
        return targetBlock.getBlock();
    }

    public static Boolean always(BlockState state, BlockGetter blockGetter, BlockPos pos, EntityType<?> entity) {
        return true;
    }
    private static boolean always(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return true;
    }

    public static Boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos, EntityType<?> entity) {
        return false;
    }
    private static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return false;
    }

    public static void register() {}
}
