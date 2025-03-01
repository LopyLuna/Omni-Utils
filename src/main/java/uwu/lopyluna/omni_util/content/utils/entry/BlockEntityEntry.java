package uwu.lopyluna.omni_util.content.utils.entry;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;

@SuppressWarnings("unused")
public record BlockEntityEntry<T extends BlockEntity> (DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> holder, String name) {

    public BlockEntityType<T> get() {
        return holder.get();
    }

    public DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> getHolder() {
        return holder;
    }

    public String getName() {
        return name;
    }
}
