package uwu.lopyluna.omni_util.content.utils.entry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredHolder;
import uwu.lopyluna.omni_util.content.datagen.ItemModelProvider;

import java.util.function.BiConsumer;

@SuppressWarnings("unused")
public record ItemEntry<T extends Item>(DeferredHolder<Item, T> holder, boolean noTab, String langKey, String name, BiConsumer<ItemModelProvider, T> model) {

    public T get() {
        return holder.get();
    }

    public T inTab() {
        if (noTab) return null;
        return holder.get();
    }

    public DeferredHolder<Item, T> getHolder() {
        return holder;
    }

    public String getLangKey() {
        return langKey;
    }

    public boolean is(ItemLike item) {
        return get().equals(item);
    }
    public boolean is(ItemStack item) {
        return get().equals(item.getItem());
    }

    public void model(ItemModelProvider provider) {
        if (model != null) model.accept(provider, get());
    }

    public BiConsumer<ItemModelProvider, T> getModel() {
        return model;
    }

    public String getName() {
        return name;
    }
}
