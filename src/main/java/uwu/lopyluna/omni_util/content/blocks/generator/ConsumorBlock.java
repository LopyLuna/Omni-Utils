package uwu.lopyluna.omni_util.content.blocks.generator;

import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.omni_util.content.blocks.base.OmniBlockEntity;
import uwu.lopyluna.omni_util.content.blocks.base.PowerBlockBlock;
import uwu.lopyluna.omni_util.register.AllBlockEntities;

public class ConsumorBlock extends PowerBlockBlock {
    public ConsumorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull BlockEntityType<? extends OmniBlockEntity> getBlockEntityType() {
        return AllBlockEntities.CONSUMOR.get();
    }
}
