package uwu.lopyluna.omni_util.content.blocks.panels;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import uwu.lopyluna.omni_util.content.blocks.base.PanelBlock;
import uwu.lopyluna.omni_util.content.blocks.base.PowerBlockEntity;
import uwu.lopyluna.omni_util.events.PowerTickHandler;
import uwu.lopyluna.omni_util.register.AllPowerSources;

public class LunarPanelBE extends PowerBlockEntity {
    public LunarPanelBE(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState, AllPowerSources.BASE_PANEL);
    }

    @Override
    public void onActive(boolean pClient) {
        super.onActive(pClient);
        assert level != null;
        var state = getBlockState();
        var r = level.random.nextInt(20) == 0;
        if (r && !state.getValue(PanelBlock.ENABLED)) level.setBlockAndUpdate(getBlockPos(), state.setValue(PanelBlock.ENABLED, true));
    }

    @Override
    public void onFailed(boolean pClient) {
        super.onFailed(pClient);
        assert level != null;
        var state = getBlockState();
        var r = level.random.nextInt(20) == 0;
        if (r && state.getValue(PanelBlock.ENABLED)) level.setBlockAndUpdate(getBlockPos(), state.setValue(PanelBlock.ENABLED, false));
    }

    @Override
    public void onTick(boolean pClient) {
        if (level == null) return;
        var pos = getBlockPos();
        PowerTickHandler.blocksToAdd.add(pos);

        if (ownerUUID == null) return;
        var player = level.getPlayerByUUID(ownerUUID);
        if (player == null) return;

        if (getImpact(level, player, pClient) > 0) onActive(pClient);
        else onFailed(pClient);
    }

    //*@Override
    //*public int getImpact(Level pLevel, Player pPlayer, boolean pClient) {
    //*    var multi = multiplier(pLevel, pPlayer, pClient);
    //*    return Mth.clamp((int) ((float) source.impact * multi) + (multi > 0 ? 1 : 0), 0, source.impact);
    //*}

    @Override
    public float multiplier(Level pLevel, Player pPlayer, boolean pClient) {
        var time = getTime(pLevel);
        return isGenerating(pLevel, pPlayer, pClient) ? time != 99999 ? Mth.clamp(time * 2f, 0f, 1f) : 0 : 0;
    }

    @Override
    public boolean isActive(Level pLevel, Player pPlayer, boolean pClient) {
        return false;
    }

    @Override
    public boolean isGenerating(Level pLevel, Player pPlayer, boolean pClient) {
        return flag(pLevel, pPlayer, pClient);
    }

    @Override
    public boolean flag(Level pLevel, Player pPlayer, boolean pClient) {
        if (pLevel == null) return false;
        var time = getTime(pLevel);
        return hasSkylight(pLevel) && time != 99999;
    }

    public float getTime(Level level) {
        if (level == null) return 99999;
        var time = level.dayTime() % 24000;
        var multiplier = (time - 12000) / 6000f;
        return time > 12000 ? multiplier > 1 ? 1 - (multiplier - 1) : multiplier : 99999;
    }

    public boolean hasSkylight(Level level) {
        if (level == null) return false;
        boolean hasSkylight = true;
        if (level.dimensionType().hasSkyLight()) for (int y = 1; (level.getMaxBuildHeight() - getBlockPos().getY()) > y; y++) {
            if (level.getBlockState(getBlockPos().offset(0, y, 0)).canOcclude()) {
                hasSkylight = false;
                break;
            }
        } else hasSkylight = false;
        return hasSkylight;
    }
}
