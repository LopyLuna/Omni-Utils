package uwu.lopyluna.omni_util.content.utils.builders;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredRegister;
import uwu.lopyluna.omni_util.content.datagen.BlockLootTablesProvider;
import uwu.lopyluna.omni_util.content.datagen.BlockStateProvider;
import uwu.lopyluna.omni_util.content.datagen.BlockTagProvider;
import uwu.lopyluna.omni_util.content.utils.entry.BlockEntry;
import uwu.lopyluna.omni_util.content.utils.entry.ItemEntry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

@SuppressWarnings("unused")
public class BlockBuilder<T extends Block> {
    private static final List<BlockEntry<?>> BLOCK_ENTRIES = new ArrayList<>();
    private static final List<ItemEntry<? extends BlockItem>> ITEM_ENTRIES = new ArrayList<>();

    private final String name;
    private final Function<BlockBehaviour.Properties, T> func;
    private BlockBehaviour.Properties properties;
    private final DeferredRegister.Blocks blockRegister;
    private final DeferredRegister.Items itemRegister;
    private BlockEntry<T> blockEntry;
    private String blockLangKey;
    private String blockItemLangKey;
    private BiConsumer<BlockLootTablesProvider, T> lootTable;
    private BiConsumer<BlockStateProvider, T> blockstate;
    private BiConsumer<BlockTagProvider, T> blockTag;
    private boolean noTab;
    private final Set<TagKey<Block>> blockTags = new HashSet<>();

    public BlockBuilder(String name, Function<BlockBehaviour.Properties, T> func, DeferredRegister.Blocks blockRegister, DeferredRegister.Items itemRegister) {
        this.name = name;
        this.func = func;
        this.blockRegister = blockRegister;
        this.itemRegister = itemRegister;
        this.properties = Block.Properties.of();
        String defaultLang = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase().replace('_', ' ');
        this.blockLangKey = defaultLang;
        this.blockItemLangKey = defaultLang;
        this.lootTable = BlockLootTablesProvider::dropSelf;
        this.blockstate = BlockStateProvider::blockWithItem;
        this.blockTag = null;
        this.noTab = false;
    }

    public BlockBuilder<T> properties(Function<BlockBehaviour.Properties, BlockBehaviour.Properties> propertyModifier) {
        var block = this;
        block.properties = propertyModifier.apply(this.properties);
        return block;
    }

    public BlockBuilder<T> lang(String langKey) {
        var block = this;
        block.blockLangKey = langKey;
        block.blockItemLangKey = langKey;
        return block;
    }

    public BlockBuilder<T> langBlock(String langKey) {
        var block = this;
        block.blockLangKey = langKey;
        return block;
    }

    public BlockBuilder<T> simpleBlockItem(BiFunction<T, Item.Properties, ? extends BlockItem> factory) {
        var block = this;
        return block.blockItem(factory).buildBlockItem();
    }

    public BlockBuilder<T> simpleBlockItem() {
        var block = this;
        return block.blockItem().buildBlockItem();
    }

    public BlockItemBuilder<T, ? extends BlockItem> blockItem() {
        return this.blockItem(BlockItem::new);
    }

    public BlockItemBuilder<T, ? extends BlockItem> blockItem(BiFunction<T, Item.Properties, ? extends BlockItem> factory) {
        this.blockTags(blockTags);
        if (blockEntry == null) {
            blockEntry = new BlockEntry<>(this.blockRegister.register(this.name, () -> func.apply(properties)), blockstate, lootTable, blockTag, blockLangKey, name);
            BLOCK_ENTRIES.add(blockEntry);
        }
        return new BlockItemBuilder<>(factory, this, this.name, this.itemRegister, noTab, blockEntry, blockItemLangKey);
    }

    public BlockBuilder<T> langItem(String langKey) {
        var block = this;
        block.blockItemLangKey = langKey;
        return block;
    }

    public BlockBuilder<T> lootTable(BiConsumer<BlockLootTablesProvider, T> lootTable) {
        var block = this;
        block.lootTable = lootTable;
        return block;
    }

    public BlockBuilder<T> blockstate(BiConsumer<BlockStateProvider, T> blockstate) {
        var block = this;
        block.blockstate = blockstate;
        return block;
    }

    public BlockBuilder<T> mineablePickaxe() {
        var block = this;
        block.blockTags.add(BlockTags.MINEABLE_WITH_PICKAXE);
        return block;
    }

    public BlockBuilder<T> mineableAxe() {
        var block = this;
        block.blockTags.add(BlockTags.MINEABLE_WITH_AXE);
        return block;
    }

    public BlockBuilder<T> mineableHoe() {
        var block = this;
        block.blockTags.add(BlockTags.MINEABLE_WITH_HOE);
        return block;
    }

    public BlockBuilder<T> mineableShovel() {
        var block = this;
        block.blockTags.add(BlockTags.MINEABLE_WITH_SHOVEL);
        return block;
    }

    public BlockBuilder<T> mineableSword() {
        var block = this;
        block.blockTags.add(BlockTags.SWORD_EFFICIENT);
        return block;
    }

    public BlockBuilder<T> badDiamondTools() {
        var block = this;
        block.blockTags.add(BlockTags.INCORRECT_FOR_DIAMOND_TOOL);
        return block;
    }

    public BlockBuilder<T> needDiamondTools() {
        var block = this;
        block.blockTags.add(BlockTags.NEEDS_DIAMOND_TOOL);
        return block;
    }

    public BlockBuilder<T> badIronTools() {
        var block = this;
        block.blockTags.add(BlockTags.INCORRECT_FOR_IRON_TOOL);
        return block;
    }

    public BlockBuilder<T> needIronTools() {
        var block = this;
        block.blockTags.add(BlockTags.NEEDS_IRON_TOOL);
        return block;
    }

    public BlockBuilder<T> needStoneTools() {
        var block = this;
        block.blockTags.add(BlockTags.NEEDS_STONE_TOOL);
        return block;
    }

    public BlockBuilder<T> badStoneTools() {
        var block = this;
        block.blockTags.add(BlockTags.INCORRECT_FOR_STONE_TOOL);
        return block;
    }

    public BlockBuilder<T> needGoldTools() {
        var block = this;
        block.blockTags.add(Tags.Blocks.NEEDS_GOLD_TOOL);
        return block;
    }

    public BlockBuilder<T> badGoldTools() {
        var block = this;
        block.blockTags.add(BlockTags.INCORRECT_FOR_GOLD_TOOL);
        return block;
    }

    public BlockBuilder<T> needWoodTools() {
        var block = this;
        block.blockTags.add(Tags.Blocks.NEEDS_WOOD_TOOL);
        return block;
    }

    public BlockBuilder<T> badWoodTools() {
        var block = this;
        block.blockTags.add(BlockTags.INCORRECT_FOR_WOODEN_TOOL);
        return block;
    }

    public BlockBuilder<T> needNetheriteTools() {
        var block = this;
        block.blockTags.add(Tags.Blocks.NEEDS_NETHERITE_TOOL);
        return block;
    }

    public BlockBuilder<T> badNetheriteTools() {
        var block = this;
        block.blockTags.add(BlockTags.INCORRECT_FOR_NETHERITE_TOOL);
        return block;
    }

    @SafeVarargs
    public final BlockBuilder<T> addTags(TagKey<Block>... tags) {
        var block = this;
        var tag = List.of(tags);
        if (tag.isEmpty()) return block;
        block.blockTags.addAll(tag);
        return block;
    }

    public BlockBuilder<T> noTab() {
        var block = this;
        block.noTab = true;
        return block;
    }

    private void blockTags(Set<TagKey<Block>> tags) {
        BiConsumer<BlockTagProvider, T> t = (p, c) -> { for (var tag : tags) p.tag(tag).add(c); };
        if (!tags.isEmpty()) this.blockTag = t;
    }

    protected void setBlockItemEntry(ItemEntry<? extends BlockItem> blockItemEntry) {
        ITEM_ENTRIES.add(blockItemEntry);
    }

    DeferredRegister.Items getItemRegister() {
        return this.itemRegister;
    }

    public BlockEntry<T> register() {
        this.blockTags(blockTags);
        if (blockEntry == null) {
            blockEntry = new BlockEntry<>(this.blockRegister.register(this.name, () -> func.apply(properties)), blockstate, lootTable, blockTag, blockLangKey, name);
            BLOCK_ENTRIES.add(blockEntry);
        }
        return blockEntry;
    }

    public static List<BlockEntry<?>> getEntries() {
        return BLOCK_ENTRIES;
    }

    public static List<ItemEntry<? extends BlockItem>> getItemEntries() {
        return ITEM_ENTRIES;
    }
}