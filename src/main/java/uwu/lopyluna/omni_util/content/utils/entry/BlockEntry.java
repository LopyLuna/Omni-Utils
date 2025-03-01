package uwu.lopyluna.omni_util.content.utils.entry;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import uwu.lopyluna.omni_util.content.datagen.BlockLootTablesProvider;
import uwu.lopyluna.omni_util.content.datagen.BlockStateProvider;
import uwu.lopyluna.omni_util.content.datagen.BlockTagProvider;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public record BlockEntry<T extends Block>(DeferredHolder<Block, T> holder, BiConsumer<BlockStateProvider, T> blockstate, BiConsumer<BlockLootTablesProvider, T> lootTable, BiConsumer<BlockTagProvider, T> blockTag, String langKey, String name) {
    public Supplier<T> getSupplier() {
        return holder;
    }

    public T get() {
        return holder.get();
    }

    public DeferredHolder<Block, T> getHolder() {
        return holder;
    }

    public String getLangKey() {
        return langKey;
    }

    public boolean is(ItemLike item) {
        return get().asItem().equals(item);
    }
    public boolean is(ItemStack item) {
        return get().asItem().equals(item.getItem());
    }

    public void lootTable(BlockLootTablesProvider provider) {
        if (lootTable != null) lootTable.accept(provider, get());
    }
    public void blockState(BlockStateProvider provider) {
        if (blockstate != null) blockstate.accept(provider, get());
    }
    public void blockTag(BlockTagProvider provider) {
        if (blockTag != null) blockTag.accept(provider, get());
    }

    public String getName() {
        return name;
    }

}

