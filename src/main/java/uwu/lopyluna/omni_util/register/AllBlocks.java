package uwu.lopyluna.omni_util.register;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import net.neoforged.neoforge.common.Tags;
import uwu.lopyluna.omni_util.OmniUtils;
import uwu.lopyluna.omni_util.content.blocks.GlowrockBlock;
import uwu.lopyluna.omni_util.content.blocks.GrimspiralBlock;
import uwu.lopyluna.omni_util.content.blocks.curse.CurseBlock;
import uwu.lopyluna.omni_util.content.blocks.curse.CurseLeavesBlock;
import uwu.lopyluna.omni_util.content.blocks.curse.CurseRotatedPillarBlock;
import uwu.lopyluna.omni_util.content.blocks.dead.DeadBlock;
import uwu.lopyluna.omni_util.content.blocks.dead.DeadLeavesBlock;
import uwu.lopyluna.omni_util.content.blocks.dead.DeadRotatedPillarBlock;
import uwu.lopyluna.omni_util.content.blocks.generator.GeneratorBlock;
import uwu.lopyluna.omni_util.content.blocks.spike.SpikeBlock;
import uwu.lopyluna.omni_util.content.datagen.BlockLootTablesProvider;
import uwu.lopyluna.omni_util.content.datagen.BlockStateProvider;
import uwu.lopyluna.omni_util.content.items.AngelBlockItem;
import uwu.lopyluna.omni_util.content.utils.entry.BlockEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import static uwu.lopyluna.omni_util.OmniUtils.REG;

@SuppressWarnings("unused")
public class AllBlocks {

    public static final BlockEntry<GeneratorBlock> GENERATOR = REG.block("generator_block", GeneratorBlock::new)
            .lang("Generator")
            .properties(p -> p.strength(2.0F, 3.0F).requiresCorrectToolForDrops())
            .noTab()
            .mineablePickaxe().needStoneTools()
            .simpleBlockItem()
            .register();

    public static final BlockEntry<Block> ANGEL_BLOCK = REG.block("angel_block", Block::new)
            .lang("Angel Block")
            .properties(p -> p.emissiveRendering(AllBlocks::always).mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(25.0F, 1200.0F))
            .mineablePickaxe().needIronTools()
            .blockstate((p, c) -> {
                var model = p.models().getExistingFile(OmniUtils.loc("block/angel_block"));
                p.simpleBlock(c, model);
                p.simpleBlockItem(c, model);
            })
            .blockItem(AngelBlockItem::new)
            .properties(p -> p.fireResistant().rarity(Rarity.EPIC))
            .buildBlockItem()
            .register();

    public static final BlockEntry<Block> POLISHED_STONE = REG.block("polished_stone", Block::new)
            .lang("Polished Stone")
            .properties(p -> p.mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F))
            .mineablePickaxe().needStoneTools()
            .simpleBlockItem()
            .register();

    public static final BlockEntry<GrimspiralBlock> GRIMSPIRAL = REG.block("grimspiral", GrimspiralBlock::new)
            .lang("Grimspiral")
            .properties(p -> p.mapColor(MapColor.COLOR_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(75.0F, 5000.0F))
            .mineablePickaxe().needDiamondTools()
            .lootTable((p, c) -> p.dropOther(c, AllBlocks.GRIMROCK.get()))
            .blockItem()
            .properties(p -> p.fireResistant().rarity(Rarity.EPIC))
            .buildBlockItem()
            .register();

    public static final BlockEntry<Block> GRIMROCK = REG.block("grimrock", Block::new)
            .lang("Grimrock")
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(AllSoundTypes.GRIMROCK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(7.5F, 12.0F))
            .mineablePickaxe().needIronTools()
            .simpleBlockItem()
            .register();

    public static final BlockEntry<GlowrockBlock> GLOWROCK = REG.block("glowrock", GlowrockBlock::new)
            .lang("GlowrockBlock")
            .properties(p -> p.lightLevel(b -> b.getValue(GlowrockBlock.GLOWING) == 0 ? 4 : b.getValue(GlowrockBlock.GLOWING) == 1 ? 8 : 12).mapColor(MapColor.TERRACOTTA_ORANGE).sound(AllSoundTypes.GRIMROCK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(7.5F, 12.0F))
            .blockstate((p, c) -> {
                MultiPartBlockStateBuilder builder = p.getMultipartBuilder(c);

                List<ModelFile> modelDimVariants = new ArrayList<>();
                for (int i = 1; i < 4; i++) modelDimVariants.add(p.models().cubeAll("block/glowrock_dim_" + i, OmniUtils.loc("block/glowrock_dim_" + i)));
                List<ModelFile> modelNormalVariants = new ArrayList<>();
                for (int i = 1; i < 4; i++) modelNormalVariants.add(p.models().cubeAll("block/glowrock_" + i, OmniUtils.loc("block/glowrock_" + i)));

                var modelBright = p.models().cubeAll("block/glowrock_bright", OmniUtils.loc("block/glowrock_bright"));

                for (int level = 0; 3 > level; level++) {
                    if (level==2) builder.part().modelFile(modelBright).addModel().condition(GlowrockBlock.GLOWING, 2).end();
                    else if (level==1) for (var model : modelNormalVariants) builder.part().modelFile(model).addModel().condition(GlowrockBlock.GLOWING, 1).end();
                    else for (var model : modelDimVariants) builder.part().modelFile(model).addModel().condition(GlowrockBlock.GLOWING, 0).end();
                }
                p.simpleBlockItem(c, modelBright);
            })
            .mineablePickaxe().needIronTools()
            .simpleBlockItem()
            .register();


    //SPIKES

    public static final BlockEntry<SpikeBlock> COPPER_SPIKE = REG.block("copper_spike", p -> new SpikeBlock(p, 0.5f, true, false, false, false))
            .lang("Copper Spike")
            .properties(p -> p.sound(SoundType.COPPER).mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(1.5F, 5.0F).noOcclusion())
            .mineablePickaxe().needStoneTools()
            .blockstate(modelSpike("copper"))
            .simpleBlockItem()
            .register();
    public static final BlockEntry<SpikeBlock> STONE_SPIKE = REG.block("stone_spike", p -> new SpikeBlock(p, 0.5f, false, false, false, false))
            .lang("Stone Spike")
            .properties(p -> p.sound(SoundType.STONE).mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(1.5F, 5.0F).noOcclusion())
            .mineablePickaxe().needStoneTools()
            .blockstate(modelSpike("stone"))
            .simpleBlockItem()
            .register();
    public static final BlockEntry<SpikeBlock> GOLD_SPIKE = REG.block("gold_spike", p -> new SpikeBlock(p, 1.0f, true, false, true, false))
            .lang("Gold Spike")
            .properties(p -> p.sound(SoundType.METAL).mapColor(MapColor.GOLD).requiresCorrectToolForDrops().strength(3.0F, 6.0F).noOcclusion())
            .mineablePickaxe().needStoneTools()
            .blockstate(modelSpike("gold"))
            .simpleBlockItem()
            .register();
    public static final BlockEntry<SpikeBlock> IRON_SPIKE = REG.block("iron_spike", p -> new SpikeBlock(p, 1.0f, false, false, true, false))
            .lang("Iron Spike")
            .properties(p -> p.sound(SoundType.METAL).mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(5.0F, 7.0F).noOcclusion())
            .mineablePickaxe().needStoneTools()
            .blockstate(modelSpike("iron"))
            .simpleBlockItem()
            .register();
    public static final BlockEntry<SpikeBlock> DIAMOND_SPIKE = REG.block("diamond_spike", p -> new SpikeBlock(p, 1.5f, false, true, true, false))
            .lang("Diamond Spike")
            .properties(p -> p.sound(SoundType.METAL).mapColor(MapColor.DIAMOND).requiresCorrectToolForDrops().strength(5.0F, 8.0F).noOcclusion())
            .mineablePickaxe().needIronTools()
            .blockstate(modelSpike("diamond"))
            .simpleBlockItem()
            .register();
    public static final BlockEntry<SpikeBlock> NETHERITE_SPIKE = REG.block("netherite_spike", p -> new SpikeBlock(p, 2.0f, false, true, true, true))
            .lang("Netherite Spike")
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(20.0F, 600.0F).noOcclusion())
            .mineablePickaxe().needDiamondTools()
            .blockstate(modelSpike("netherite"))
            .simpleBlockItem()
            .register();

    //CURSED BLOCKS

    public static final BlockEntry<CurseBlock> CURSE_NYLIUM = REG.block("curse_nylium", p -> new CurseBlock(p, 1))
            .properties(p -> p.mapColor(DyeColor.BLACK).strength(0.8F).sound(AllSoundTypes.CURSED_NYLIUM))
            .lang("Curse Nylium")
            .blockstate((p, c) -> {
                var model = p.models()
                        .withExistingParent("block/curse_nylium", ResourceLocation.withDefaultNamespace("block/podzol"))
                        .texture("side", OmniUtils.loc("block/curse_nylium"))
                        .texture("top", OmniUtils.loc("block/curse_nylium_top"));
                p.simpleBlock(c, model);
                p.simpleBlockItem(c, model);
            })
            .lootTable((p, c) -> p.add(c, p.createSingleItemTableWithSilkTouch(c, Items.COARSE_DIRT)))
            .addTags(BlockTags.DIRT)
            .mineableShovel()
            .simpleBlockItem()
            .register();

    public static final BlockEntry<CurseBlock> CURSE_COARSE = REG.block("curse_coarse", CurseBlock::new)
            .properties(p -> p.mapColor(MapColor.TERRACOTTA_GRAY).strength(0.8F).sound(AllSoundTypes.CURSED_SOIL))
            .lang("Curse Coarse")
            .lootTable((p, c) -> p.add(c, p.createSingleItemTableWithSilkTouch(c, Items.COARSE_DIRT)))
            .addTags(BlockTags.DIRT)
            .mineableShovel()
            .simpleBlockItem()
            .register();

    public static final BlockEntry<CurseBlock> CURSE_STONE = REG.block("curse_stone", CurseBlock::new)
            .properties(p -> p.mapColor(DyeColor.BLACK).instrument(NoteBlockInstrument.BASEDRUM).sound(AllSoundTypes.CURSED_STONE).requiresCorrectToolForDrops().strength(2.0F, 8.0F))
            .lang("Curse Stone")
            .mineablePickaxe()
            .simpleBlockItem()
            .register();

    public static final BlockEntry<CurseBlock> CURSE_ROOTS = REG.block("curse_roots", p -> new CurseBlock(p, 2))
            .properties(p -> p.mapColor(DyeColor.BLACK).replaceable().noCollission().instabreak().sound(SoundType.ROOTS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY))
            .lang("Curse Roots")
            .blockstate((p, c) -> {
                p.simpleBlock(c, p.models().cross("block/curse_roots", OmniUtils.loc("block/curse_roots")).renderType(RenderType.CUTOUT.name));
                p.itemModels().basicItem(OmniUtils.loc("curse_roots"));
            })
            .lootTable(BlockLootTablesProvider::dropLikeNetherVines)
            .mineableSword()
            .simpleBlockItem()
            .register();

    public static final BlockEntry<CurseLeavesBlock> CURSE_LEAVES = REG.block("curse_leaves", CurseLeavesBlock::new)
            .properties(p -> p.mapColor(DyeColor.BLACK).strength(0.4F).noOcclusion().sound(AllSoundTypes.CURSED_LEAVES).isValidSpawn(Blocks::ocelotOrParrot).isSuffocating(AllBlocks::never).isViewBlocking(AllBlocks::never).ignitedByLava().pushReaction(PushReaction.DESTROY).isRedstoneConductor(AllBlocks::never))
            .lang("Curse Leaves")
            .lootTable(BlockLootTablesProvider::dropWhenSilkTouch)
            .blockstate((p, c) -> {
                var model = p.models().leaves("block/curse_leaves", OmniUtils.loc("block/curse_leaves")).renderType(RenderType.CUTOUT.name);
                p.simpleBlock(c, model);
                p.simpleBlockItem(c, model);
            })
            .mineableHoe()
            .simpleBlockItem()
            .register();

    public static final BlockEntry<CurseRotatedPillarBlock> CURSE_LOG = REG.block("curse_log", CurseRotatedPillarBlock::new)
            .properties(p -> p.mapColor(DyeColor.BLACK).instrument(NoteBlockInstrument.BASS).sound(AllSoundTypes.CURSED_LOG).strength(2.5F).ignitedByLava())
            .lang("Curse Log")
            .blockstate((p, c) -> {
                p.logBlock(c);
                p.simpleBlockItem(c, p.models().getExistingFile(OmniUtils.loc("block/curse_log")));
            })
            .mineableAxe()
            .simpleBlockItem()
            .register();

    public static final BlockEntry<DeadBlock> DEAD_NYLIUM = REG.block("dead_nylium", p -> new DeadBlock(p, 1))
            .properties(p -> p.mapColor(MapColor.TERRACOTTA_BLACK).strength(0.8F).sound(AllSoundTypes.CURSED_NYLIUM))
            .lang("Dead Nylium")
            .blockstate((p, c) -> {
                var model = p.models()
                        .withExistingParent("block/dead_nylium", ResourceLocation.withDefaultNamespace("block/podzol"))
                        .texture("side", OmniUtils.loc("block/dead_nylium"))
                        .texture("top", OmniUtils.loc("block/dead_nylium_top"));
                p.simpleBlock(c, model);
                p.simpleBlockItem(c, model);
            })
            .lootTable((p, c) -> p.add(c, p.createSingleItemTableWithSilkTouch(c, Items.COARSE_DIRT)))
            .addTags(BlockTags.DIRT)
            .mineableShovel()
            .simpleBlockItem()
            .register();

    public static final BlockEntry<DeadBlock> DEAD_COARSE = REG.block("dead_coarse", DeadBlock::new)
            .properties(p -> p.mapColor(MapColor.TERRACOTTA_BROWN).strength(0.8F).sound(AllSoundTypes.CURSED_SOIL))
            .lang("Dead Coarse")
            .lootTable((p, c) -> p.add(c, p.createSingleItemTableWithSilkTouch(c, Items.COARSE_DIRT)))
            .addTags(BlockTags.DIRT)
            .mineableShovel()
            .simpleBlockItem()
            .register();

    public static final BlockEntry<DeadBlock> DEAD_STONE = REG.block("dead_stone", DeadBlock::new)
            .properties(p -> p.mapColor(MapColor.TERRACOTTA_BLACK).instrument(NoteBlockInstrument.BASEDRUM).sound(AllSoundTypes.CURSED_STONE).requiresCorrectToolForDrops().strength(2.0F, 8.0F))
            .lang("Dead Stone")
            .mineablePickaxe()
            .simpleBlockItem()
            .register();

    public static final BlockEntry<DeadBlock> DEAD_ROOTS = REG.block("dead_roots", p -> new DeadBlock(p, 2))
            .properties(p -> p.mapColor(MapColor.TERRACOTTA_BLACK).replaceable().noCollission().instabreak().sound(SoundType.ROOTS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY))
            .lang("Dead Roots")
            .blockstate((p, c) -> {
                p.simpleBlock(c, p.models().cross("block/dead_roots", OmniUtils.loc("block/dead_roots")).renderType(RenderType.CUTOUT.name));
                p.itemModels().basicItem(OmniUtils.loc("dead_roots"));
            })
            .lootTable(BlockLootTablesProvider::dropLikeNetherVines)
            .mineableSword()
            .simpleBlockItem()
            .register();

    public static final BlockEntry<DeadLeavesBlock> DEAD_LEAVES = REG.block("dead_leaves", DeadLeavesBlock::new)
            .properties(p -> p.mapColor(MapColor.TERRACOTTA_BLACK).strength(0.4F).noOcclusion().sound(AllSoundTypes.CURSED_LEAVES).isValidSpawn(Blocks::ocelotOrParrot).isSuffocating(AllBlocks::never).isViewBlocking(AllBlocks::never).ignitedByLava().pushReaction(PushReaction.DESTROY).isRedstoneConductor(AllBlocks::never))
            .lang("Dead Leaves")
            .lootTable(BlockLootTablesProvider::dropWhenSilkTouch)
            .blockstate((p, c) -> {
                var model = p.models().leaves("block/dead_leaves", OmniUtils.loc("block/dead_leaves")).renderType(RenderType.CUTOUT.name);
                p.simpleBlock(c, model);
                p.simpleBlockItem(c, model);
            })
            .mineableHoe()
            .simpleBlockItem()
            .register();

    public static final BlockEntry<DeadRotatedPillarBlock> DEAD_LOG = REG.block("dead_log", DeadRotatedPillarBlock::new)
            .properties(p -> p.mapColor(MapColor.TERRACOTTA_BLACK).instrument(NoteBlockInstrument.BASS).sound(AllSoundTypes.CURSED_LOG).strength(2.5F).ignitedByLava())
            .lang("Dead Log")
            .blockstate((p, c) -> {
                p.logBlock(c);
                p.simpleBlockItem(c, p.models().getExistingFile(OmniUtils.loc("block/dead_log")));
            })
            .mineableAxe()
            .simpleBlockItem()
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

    private static BiConsumer<BlockStateProvider, SpikeBlock> modelSpike(String type) {
        return (p, c) -> {
            var model = p.models()
                    .withExistingParent("block/" + type + "_spike", OmniUtils.loc("block/base_spike"))
                    .texture("1", OmniUtils.loc("block/" + type + "_spike"))
                    .renderType(RenderType.CUTOUT.name);

            p.directionalBlock(c, model);
            p.itemModels().basicItem(c.asItem());
        };
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
