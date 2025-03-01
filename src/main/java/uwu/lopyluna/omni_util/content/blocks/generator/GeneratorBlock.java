package uwu.lopyluna.omni_util.content.blocks.generator;

import net.minecraft.world.level.block.entity.BlockEntityType;
import uwu.lopyluna.omni_util.content.blocks.base.BasePowerBlock;
import uwu.lopyluna.omni_util.content.blocks.base.BasePowerBlockEntity;
import uwu.lopyluna.omni_util.register.AllBlockEntities;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class GeneratorBlock extends BasePowerBlock {
    public GeneratorBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntityType<? extends BasePowerBlockEntity> getBlockEntityType() {
        return AllBlockEntities.GENERATOR.get();
    }
}
