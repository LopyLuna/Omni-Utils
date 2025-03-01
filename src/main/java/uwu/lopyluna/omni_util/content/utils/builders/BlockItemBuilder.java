package uwu.lopyluna.omni_util.content.utils.builders;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import uwu.lopyluna.omni_util.content.datagen.ItemModelProvider;
import uwu.lopyluna.omni_util.content.utils.entry.BlockEntry;
import uwu.lopyluna.omni_util.content.utils.entry.ItemEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class BlockItemBuilder<T extends Block, I extends BlockItem> {
    public final String name;
    public final BiFunction<T, Item.Properties, I> func;
    public Item.Properties properties = new Item.Properties();
    public String langKey;

    private boolean noTab;
    private BiConsumer<ItemModelProvider, I> model;
    public final DeferredRegister<Item> itemRegister;
    public final BlockEntry<T> blockEntry;
    public static final List<ItemEntry<?>> ENTRIES = new ArrayList<>();

    private final BlockBuilder<T> blockBuilder;

    public BlockItemBuilder(BiFunction<T, Item.Properties, I> factory, BlockBuilder<T> blockBuilder, String name, DeferredRegister.Items itemRegister, boolean noTab, BlockEntry<T> blockEntry, String langKey) {
        this.blockBuilder = blockBuilder;
        this.name = name;
        this.func = factory;
        this.itemRegister = itemRegister;
        this.langKey = langKey;
        this.model = null;
        this.noTab = noTab;
        this.blockEntry = blockEntry;
    }

    // SPECIAL PROPERTIES
    public BlockItemBuilder<T, I> properties(Function<Item.Properties, Item.Properties> propertiesFunction) {
        this.properties = propertiesFunction.apply(this.properties);
        return this;
    }

    public BlockItemBuilder<T, I> lang(String langKey) {
        this.langKey = langKey;
        return this;
    }

    public BlockItemBuilder<T, I> model(BiConsumer<ItemModelProvider, I> model) {
        var item = this;
        item.model = model;
        return item;
    }

    public BlockItemBuilder<T, I> noTab() {
        var block = this;
        block.noTab = true;
        return block;
    }

    //END OF SPECIAL PROPERTIES

    private ItemEntry<? extends BlockItem> buildEntry(BlockEntry<T> blockEntry) {
        DeferredHolder<Item, I> holder = this.itemRegister.register(name, () -> func.apply(blockEntry.get(), properties));
        ItemEntry<? extends BlockItem> entry = new ItemEntry<>(holder, noTab, langKey, name, model);
        ENTRIES.add(entry);
        return entry;
    }

    public BlockBuilder<T> buildBlockItem() {
        this.blockBuilder.setBlockItemEntry(this.buildEntry(blockEntry));
        return blockBuilder;
    }

    public static List<ItemEntry<? extends Item>> getEntries() {
        return ENTRIES;
    }
}