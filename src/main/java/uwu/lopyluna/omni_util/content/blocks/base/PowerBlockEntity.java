package uwu.lopyluna.omni_util.content.blocks.base;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import uwu.lopyluna.omni_util.content.managers.PowerManager;
import uwu.lopyluna.omni_util.events.PowerTickHandler;
import uwu.lopyluna.omni_util.register.AllPowerSources;

import java.util.UUID;

@SuppressWarnings("unused")
public class PowerBlockEntity extends OmniBlockEntity {
    protected final AllPowerSources.PowerSource source;
    public UUID ownerUUID;

    public PowerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, AllPowerSources.PowerSource source) {
        super(type, pos, blockState);
        this.source = source;
        PowerTickHandler.blocks.put(blockUUID, this);
    }

    public void onProcessPower(ServerPlayer player) {
        ServerPlayer owner;
        if (ownerUUID == null) {
            ownerUUID = player.getUUID();
            owner = player;
        } else if (ownerUUID.equals(player.getUUID())) {
            owner = player;
        } else return;
        if (isGenerator()) PowerManager.addGeneratedRP(owner, getImpact());
        else PowerManager.addConsumedRP(owner, getImpact());
    }

    public int getImpact() {
        return (int) ((isGenerating() || isActive() ? source.impact : 0) * 0.5);
    }

    public boolean isGenerator() {
        return source.genertor;
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
    public void setRemoved() {
        PowerTickHandler.blocks.remove(blockUUID);
        super.setRemoved();
    }

    @Override
    public void onTick(boolean pClient) {
        if (level == null) return;
        if (!PowerTickHandler.blocks.containsKey(blockUUID)) PowerTickHandler.blocks.put(blockUUID, this);

        if (ownerUUID == null) return;
        var player = level.getPlayerByUUID(ownerUUID);
        if (player == null) return;

        if ((!isGenerator() && PowerManager.getNetRPOmni(player) >= 0) || (isGenerator() && isGenerating())) onActive(pClient);
        else onFailed(pClient);
    }

    @Override
    public void onLoad(CompoundTag nbt) {
        super.onLoad(nbt);
        if (nbt.hasUUID("OwnerUUID")) ownerUUID = nbt.getUUID("OwnerUUID");
        if (!PowerTickHandler.blocks.containsKey(blockUUID)) PowerTickHandler.blocks.put(blockUUID, this);
    }

    @Override
    public void onSave(CompoundTag nbt) {
        PowerTickHandler.blocks.remove(blockUUID);
        super.onSave(nbt);
        if (ownerUUID != null) nbt.putUUID("OwnerUUID", ownerUUID);
    }
}
