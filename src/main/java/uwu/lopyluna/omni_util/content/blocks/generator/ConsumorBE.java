package uwu.lopyluna.omni_util.content.blocks.generator;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.ParticleUtils;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import uwu.lopyluna.omni_util.content.blocks.base.PowerBlockEntity;
import uwu.lopyluna.omni_util.register.AllPowerSources;

public class ConsumorBE extends PowerBlockEntity {
    public ConsumorBE(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState, AllPowerSources.CONSUMOR_BLOCK);
    }
    public int delay = 0;
    @Override
    public void onActive(boolean pClient) {
        super.onActive(pClient);
        assert level != null;

        if (delay==0) ParticleUtils.spawnParticleInBlock(level, getBlockPos().above(), 1, ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER_OMINOUS);
        delay = ++delay % 5;
    }
}
