package uwu.lopyluna.omni_util.content.blocks.base;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.omni_util.content.utils.GoggleOverlay;

import java.util.List;

@SuppressWarnings("unused")
public class OmniBlockEntity extends BlockEntity {
    public boolean initialized = false;
    private int counter = 0;
    private int delayRate = 10;
    public OmniBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        onLoad(tag);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        onSave(tag);
    }

    public void onInitialize(boolean pClient) {
    }
    
    public void onTick(boolean pClient) {
    }

    public void onTickDelayed(boolean pClient) {
    }

    public void onLoad(CompoundTag nbt) {
    }

    public void onSave(CompoundTag nbt) {
    }

    public void onPlaced(LivingEntity pPlacer, boolean pClient) {
    }

    public void onUpdate(boolean pClient) {
    }

    public void onStep(Entity pEntity, boolean pClient) {
    }

    public void setDelayedRate(int pRate) {
        this.delayRate = pRate;
    }

    protected void onSpecialTick(boolean pClient) {
        if (!initialized) initialize(pClient);
        if (counter-- <= 0) {
            counter = delayRate;
            onTickDelayed(pClient);
        }
    }

    private void initialize(boolean pClient) {
        if (hasLevel()) {
            onInitialize(pClient);
            initialized = true;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public List<Component> getGoggleTooltipLines(Level level, Player player) {
        List<Component> list = Lists.newArrayList();
        if (this instanceof GoggleOverlay overlay) overlay.appendHoverText(this, level, getBlockPos(), getBlockState(), player, list);
        else return List.of();
        return list;
    }
}
