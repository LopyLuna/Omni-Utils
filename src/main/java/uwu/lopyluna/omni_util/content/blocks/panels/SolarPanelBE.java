package uwu.lopyluna.omni_util.content.blocks.panels;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import uwu.lopyluna.omni_util.content.blocks.base.PanelBlock;
import uwu.lopyluna.omni_util.content.blocks.base.PowerBlockEntity;
import uwu.lopyluna.omni_util.register.AllPowerSources;

public class SolarPanelBE extends PowerBlockEntity {
    public SolarPanelBE(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState, AllPowerSources.BASE_PANEL);
    }

    @Override
    public void onActive(boolean pClient) {
        super.onActive(pClient);
        assert level != null;
        var state = getBlockState();
        if (!state.getValue(PanelBlock.ENABLED)) level.setBlockAndUpdate(getBlockPos(), state.setValue(PanelBlock.ENABLED, true));
    }

    @Override
    public void onFailed(boolean pClient) {
        super.onFailed(pClient);
        assert level != null;
        var state = getBlockState();
        if (state.getValue(PanelBlock.ENABLED)) level.setBlockAndUpdate(getBlockPos(), state.setValue(PanelBlock.ENABLED, false));
    }

    @Override
    public boolean isGenerating() {
        var level = getLevel();
        if (level == null) return false;
        boolean hasSkylight = true;
        if (level.dimensionType().hasSkyLight()) for (int y = 1; (level.getMaxBuildHeight() - getBlockPos().getY()) > y; y++) {
            if (level.getBlockState(getBlockPos().offset(0, y, 0)).canOcclude()) {
                hasSkylight = false;
                break;
            }
        } else hasSkylight = false;
        return level.isDay() && hasSkylight;
    }
}
