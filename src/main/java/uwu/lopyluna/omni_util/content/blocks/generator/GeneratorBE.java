package uwu.lopyluna.omni_util.content.blocks.generator;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import uwu.lopyluna.omni_util.content.blocks.base.BasePowerBlockEntity;
import uwu.lopyluna.omni_util.register.AllBlockEntities;
import uwu.lopyluna.omni_util.register.AllPowerSources;

public class GeneratorBE extends BasePowerBlockEntity {

    public GeneratorBE(BlockPos pos, BlockState blockState) {
        super(AllBlockEntities.GENERATOR.get(), pos, blockState);
    }

    @Override
    public int getPowerOutput() {
        return AllPowerSources.GENERATOR_BLOCK.power;
    }

    @Override
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        super.tick(pLevel, pPos, pState);
    }

    @Override
    public void read(CompoundTag nbt) {
        super.read(nbt);
    }

    @Override
    public void write(CompoundTag nbt) {
        super.write(nbt);
    }
}
