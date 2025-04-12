package uwu.lopyluna.omni_util.content.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public interface GoggleOverlay {
    default void appendHoverText(BlockEntity be, Level level, BlockPos pos, BlockState state, Player player, List<Component> tooltipComponents) {
    }
}
