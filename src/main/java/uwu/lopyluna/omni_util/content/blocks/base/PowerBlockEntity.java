package uwu.lopyluna.omni_util.content.blocks.base;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import uwu.lopyluna.omni_util.content.managers.PowerManager;
import uwu.lopyluna.omni_util.events.PowerTickHandler;
import uwu.lopyluna.omni_util.register.AllPowerSources;

import java.util.UUID;

import static uwu.lopyluna.omni_util.client.ClientRPData.getCachedRP;

@SuppressWarnings("unused")
public class PowerBlockEntity extends OmniBlockEntity {
    protected final AllPowerSources.PowerSource source;
    public UUID ownerUUID;

    public PowerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, AllPowerSources.PowerSource source) {
        super(type, pos, blockState);
        this.source = source;
    }

    public void onProcessPower(ServerPlayer player) {
        ServerPlayer owner;
        if (ownerUUID == null) {
            ownerUUID = player.getUUID();
            owner = player;
        } else if (ownerUUID.equals(player.getUUID())) {
            owner = player;
        } else return;
        if (isGenerator()) PowerManager.adjustGeneratedRP(owner, getImpact());
        else PowerManager.adjustConsumedRP(owner, getImpact());
    }

    public int getImpact() {
        return isGenerating() || isActive() ? (int) ((float) source.impact * multiplier()) : 0;
    }

    public boolean isGenerator() {
        return source.genertor;
    }

    public float multiplier() {
        return 1.0f;
    }

    public boolean isGenerating() {
        return isGenerator();
    }

    public boolean isActive() {
        return !isGenerator();
    }

    @Override
    public void onInitialize(boolean pClient) {
        super.onInitialize(pClient);
    }

    public void onActive(boolean pClient) {
    }

    public void onFailed(boolean pClient) {
    }

    @Override
    public void clearRemoved() {
        PowerTickHandler.blocks.remove(getBlockPos());
        super.clearRemoved();
    }

    @Override
    public void setRemoved() {
        PowerTickHandler.blocks.remove(getBlockPos());
        super.setRemoved();
    }

    @Override
    public void onTick(boolean pClient) {
        if (level == null) return;
        PowerTickHandler.blocks.add(getBlockPos());

        if (ownerUUID == null) return;
        var player = level.getPlayerByUUID(ownerUUID);
        if (player == null) return;

        if ((!isGenerator() && (pClient ? getCachedRP() : PowerManager.getNetRPOmni(player)) >= 0) || (isGenerator() && isGenerating())) onActive(pClient);
        else onFailed(pClient);
    }

    @Override
    public void onLoad(CompoundTag nbt) {
        super.onLoad(nbt);
        if (nbt.hasUUID("OwnerUUID")) ownerUUID = nbt.getUUID("OwnerUUID");
        PowerTickHandler.blocks.add(getBlockPos());
    }

    @Override
    public void onSave(CompoundTag nbt) {
        super.onSave(nbt);
        if (ownerUUID != null) nbt.putUUID("OwnerUUID", ownerUUID);
        PowerTickHandler.blocks.remove(getBlockPos());
    }

    public static boolean isLoaded(Level level, BlockPos pos) {
        if (level == null) return false;
        if (level.isClientSide) return false;
        if (!level.isInWorldBounds(pos)) return false;
        return level.isLoaded(pos);
    }
}
