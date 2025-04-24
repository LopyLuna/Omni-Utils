package uwu.lopyluna.omni_util.content.blocks.power_crank;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import uwu.lopyluna.omni_util.content.blocks.base.PowerBlockEntity;
import uwu.lopyluna.omni_util.register.AllPowerSources;

public class PowerCrankBE extends PowerBlockEntity {
    public float multiplier = 0.0f;
    public int delay = 0;
    public PowerCrankBE(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState, AllPowerSources.POWER_CRANK);
    }

    @Override
    public void onTick(boolean pClient) {
        if (level == null) return;
        super.onTick(pClient);


        if (delay == 0) {
            if (multiplier > 0f) multiplier = Mth.clamp(multiplier - 0.01f, 0f, 1.04f);
            delay = 1;
        } else delay = 0;
    }

    @Override
    public float multiplier(Level pLevel, Player pPlayer, boolean pClient) {
        return multiplier;
    }

    @Override
    public InteractionResult onUse(Player pPlayer, BlockHitResult hitResult, boolean pClient) {
        if (!getBlockState().getValue(BlockStateProperties.FACING).getOpposite().equals(hitResult.getDirection())) {
            if (0.1f > multiplier) multiplier = 0.1f;
            multiplier = Mth.clamp(multiplier * 1.5f + 0.01f, 0f, 1.04f);
            pPlayer.causeFoodExhaustion(0.005F);
            return InteractionResult.sidedSuccess(pClient);
        }
        return InteractionResult.FAIL;
    }

    @Override
    public void onLoad(CompoundTag nbt) {
        super.onLoad(nbt);
        if (nbt.contains("Multiplier")) multiplier = nbt.getFloat("Multiplier");
        if (nbt.contains("DelayTimer")) delay = nbt.getInt("DelayTimer");
    }

    @Override
    public void onSave(CompoundTag nbt) {
        super.onSave(nbt);
        nbt.putFloat("Multiplier", multiplier);
        nbt.putInt("DelayTimer", delay);
    }
}
