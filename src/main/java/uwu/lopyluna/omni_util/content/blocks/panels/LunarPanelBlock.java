package uwu.lopyluna.omni_util.content.blocks.panels;

import net.minecraft.world.level.block.entity.BlockEntityType;
import uwu.lopyluna.omni_util.content.blocks.base.OmniBlockEntity;
import uwu.lopyluna.omni_util.content.blocks.base.PanelBlock;
import uwu.lopyluna.omni_util.register.AllBlockEntities;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class LunarPanelBlock extends PanelBlock {
    public LunarPanelBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends OmniBlockEntity> getBlockEntityType() {
        return AllBlockEntities.LUNAR_PANEL.get();
    }
}
