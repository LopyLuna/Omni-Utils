package uwu.lopyluna.omni_util.content.utils.builders;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import uwu.lopyluna.omni_util.content.utils.entry.BlockEntityEntry;

import java.util.function.Supplier;

public class BlockEntityBuilder<T extends BlockEntity> {
    private final String name;
    private final BlockEntityType.BlockEntitySupplier<T> factory;
    public final DeferredRegister<BlockEntityType<?>> blockEntityRegister;
    private Supplier<Block[]> validBlocksSupplier;

    public BlockEntityBuilder(DeferredRegister<BlockEntityType<?>> blockEntityRegister, String name, BlockEntityType.BlockEntitySupplier<T> pFactory) {
        this.blockEntityRegister = blockEntityRegister;
        this.name = name;
        this.factory = pFactory;
    }

    public BlockEntityBuilder<T> validBlocks(Supplier<Block[]> blocks) {
        this.validBlocksSupplier = blocks;
        return this;
    }

    public BlockEntityEntry<T> register() {
        @SuppressWarnings("all")
        DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> holder = blockEntityRegister.register(name, () ->
                BlockEntityType.Builder.of(factory, validBlocksSupplier.get())
                        .build(null));

        return new BlockEntityEntry<>(holder, name);
    }
}
