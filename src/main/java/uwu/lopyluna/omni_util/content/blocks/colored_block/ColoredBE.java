package uwu.lopyluna.omni_util.content.blocks.colored_block;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import uwu.lopyluna.omni_util.content.blocks.base.OmniBlockEntity;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ColoredBE extends OmniBlockEntity {
    public int colorR = 255;
    public int colorG = 255;
    public int colorB = 255;

    public ColoredBE(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public void onLoad(CompoundTag nbt) {
        super.onLoad(nbt);
        if (nbt.contains("ColorR")) this.colorR = nbt.getInt("ColorR");
        else this.colorR = 255;
        if (nbt.contains("ColorG")) this.colorG = nbt.getInt("ColorG");
        else this.colorG = 255;
        if (nbt.contains("ColorB")) this.colorB = nbt.getInt("ColorB");
        else this.colorB = 255;
    }

    @Override
    public void onSave(CompoundTag nbt) {
        super.onSave(nbt);
        nbt.putInt("ColorR", colorR);
        nbt.putInt("ColorG", colorG);
        nbt.putInt("ColorB", colorB);
    }
}
