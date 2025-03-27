package uwu.lopyluna.omni_util.content.blocks.generator;

import net.minecraft.world.level.block.entity.BlockEntityType;
import uwu.lopyluna.omni_util.content.blocks.base.OmniBlockEntity;
import uwu.lopyluna.omni_util.content.blocks.base.PowerBlockBlock;
import uwu.lopyluna.omni_util.register.AllBlockEntities;

public class GeneratorBlock extends PowerBlockBlock {
    public GeneratorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends OmniBlockEntity> getBlockEntityType() {
        return AllBlockEntities.GENERATOR.get();
    }
}
