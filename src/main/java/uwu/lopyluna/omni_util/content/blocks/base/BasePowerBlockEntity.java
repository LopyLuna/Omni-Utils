package uwu.lopyluna.omni_util.content.blocks.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.omni_util.content.power.PowerManager;
import uwu.lopyluna.omni_util.content.power.PowerTickHandler;

import java.util.Objects;
import java.util.UUID;

public class BasePowerBlockEntity extends BlockEntity {
    private UUID ownerUUID;

    public BasePowerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public void setOwner(ServerPlayer player) {
        this.ownerUUID = player.getUUID();
        setChanged();
    }

    public UUID getOwner() {
        return ownerUUID;
    }

    public int getPowerOutput() {
        return 0;
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
    }

    public void processPower() {
        if (level != null && !level.isClientSide && ownerUUID != null) {
            ServerPlayer player = Objects.requireNonNull(level.getServer()).getPlayerList().getPlayer(ownerUUID);
            if (player != null) PowerManager.addGeneratedRP(player, getPowerOutput());
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (level != null && !level.isClientSide) PowerTickHandler.registerGenerator(this);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (level != null && !level.isClientSide) PowerTickHandler.unregisterGenerator(this);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        read(tag);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        write(tag);
    }

    public void read(CompoundTag nbt) {
        if (nbt.hasUUID("OwnerUUID")) this.ownerUUID = nbt.getUUID("OwnerUUID");
    }

    public void write(CompoundTag nbt) {
        if (ownerUUID != null) nbt.putUUID("OwnerUUID", ownerUUID);
    }
}
