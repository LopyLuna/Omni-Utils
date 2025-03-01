package uwu.lopyluna.omni_util.content.utils.builders;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import uwu.lopyluna.omni_util.content.datagen.ItemModelProvider;
import uwu.lopyluna.omni_util.content.utils.entry.ItemEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

@SuppressWarnings("unused")
public class ItemBuilder<T extends Item> {
    public final String name;
    public final Function<Item.Properties, ? extends T> func;
    public Item.Properties properties = new Item.Properties();
    public String langKey;
    private BiConsumer<ItemModelProvider, T> model;
    private boolean noTab;

    public final DeferredRegister<Item> itemRegister;
    public static final List<ItemEntry<?>> ENTRIES = new ArrayList<>();

    public ItemBuilder(String name, Function<Item.Properties, ? extends T> func, DeferredRegister.Items itemRegister) {
        this.name = name;
        this.func = func;
        this.itemRegister = itemRegister;
        this.langKey = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase().replace('_', ' ');
        this.model = net.neoforged.neoforge.client.model.generators.ItemModelProvider::basicItem;
    }

    //SPECIAL PROPERTIES
    public ItemBuilder<T> properties(Function<Item.Properties, Item.Properties> propertiesFunction) {
        var item = this;
        item.properties = propertiesFunction.apply(this.properties);
        return item;
    }

    public ItemBuilder<T> lang(String langKey) {
        var item = this;
        item.langKey = langKey;
        return item;
    }

    public ItemBuilder<T> model(BiConsumer<ItemModelProvider, T> model) {
        var item = this;
        item.model = model;
        return item;
    }

    public ItemBuilder<T> noTab() {
        var block = this;
        block.noTab = true;
        return block;
    }

    //END OF SPECIAL PROPERTIES

    public ItemEntry<T> register() {
        DeferredHolder<Item, T> holder = this.itemRegister.register(this.name, () -> func.apply(properties));

        ItemEntry<T> entry = new ItemEntry<>(holder, noTab, langKey, name, model);

        ENTRIES.add(entry);
        return entry;
    }

    public static List<ItemEntry<? extends Item>> getEntries() {
        return ENTRIES;
    }
}
