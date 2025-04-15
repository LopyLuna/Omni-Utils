package uwu.lopyluna.omni_util.content.blocks.grim_devour;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;
import uwu.lopyluna.omni_util.content.blocks.base.OmniBlockEntity;
import uwu.lopyluna.omni_util.register.AllBlocks;

public class GrimDevourBE extends OmniBlockEntity {
    public long consumed = 0;
    public GrimDevourBE(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public void onTick(boolean pClient) {
        if (pClient || level == null) return;
        var pos = getBlockPos();
        var area = BlockPos.betweenClosed(pos.offset(-2, -2, -2), pos.offset(2, 2, 2));
        var effect = true;
        if (consumed >= 263520000L) {
            level.setBlockAndUpdate(pos, AllBlocks.GRIMSPIRAL.getDefaultState());
        } else for (var off : area) {
            var state = level.getBlockState(off);
            var value = state.is(Tags.Blocks.COBBLESTONES) || state.is(BlockTags.BASE_STONE_OVERWORLD) ? 1 : state.is(Tags.Blocks.OBSIDIANS) ? 5 : 0;
            if (value > 0) {
                if (effect ? level.destroyBlock(off, false) : level.removeBlock(off, false)) {
                    if (263520000L > consumed) consumed = Mth.clamp(consumed + value, 0L, 263520000L);
                    effect = false;
                }
            }
        }
    }

    @Override
    public void onLoad(CompoundTag nbt) {
        super.onLoad(nbt);
        if (nbt.contains("Consumed")) this.consumed = Mth.clamp(nbt.getLong("Consumed"), 0L, 263520000L);
        else this.consumed = 0;
    }

    @Override
    public void onSave(CompoundTag nbt) {
        super.onSave(nbt);
        nbt.putLong("Consumed", Mth.clamp(consumed , 0L, 263520000L));
    }
}
