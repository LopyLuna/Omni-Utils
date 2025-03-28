package uwu.lopyluna.omni_util.content.blocks.base;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
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
    public boolean isActive;

    public PowerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, AllPowerSources.PowerSource source) {
        super(type, pos, blockState);
        this.source = source;
    }

    public AllPowerSources.PowerSource getSource() {
        return source;
    }

    public void onProcessPower(ServerPlayer player) {
        ServerPlayer owner;
        if (ownerUUID == null) {
            ownerUUID = player.getUUID();
            owner = player;
        } else if (ownerUUID.equals(player.getUUID())) {
            owner = player;
        } else return;
        var level = player.level();
        if (isGenerator(level, player, false)) PowerManager.adjustGeneratedRP(owner, getImpact(level, player, false));
        else PowerManager.adjustConsumedRP(owner, getImpact(level, player, false));
    }

    public boolean isGenerator(Level pLevel, Player pPlayer, boolean pClient) {
        return source.genertor;
    }

    public int getImpact(Level pLevel, Player pPlayer, boolean pClient) {
        return (int) ((float) source.impact * multiplier(pLevel, pPlayer, pClient));
    }

    public float multiplier(Level pLevel, Player pPlayer, boolean pClient) {
        return 1.0f;
    }

    public boolean flag(Level pLevel, Player pPlayer, boolean pClient) {
        return true;
    }

    public boolean isGenerating(Level pLevel, Player pPlayer, boolean pClient) {
        return true;
    }

    public boolean isActive(Level pLevel, Player pPlayer, boolean pClient) {
        return (pClient ? getCachedRP() : PowerManager.getNetRPOmni(pPlayer)) >= 0;
    }

    @Override
    public void onInitialize(boolean pClient) {
        super.onInitialize(pClient);
    }

    public void onActive(boolean pClient) {
        isActive = true;
    }

    public void onFailed(boolean pClient) {
        isActive = false;
    }

    @Override
    public void clearRemoved() {
        var pos = getBlockPos();
        PowerTickHandler.blocksToRemove.add(pos);
        super.clearRemoved();
    }

    @Override
    public void setRemoved() {
        var pos = getBlockPos();
        PowerTickHandler.blocksToRemove.add(pos);
        super.setRemoved();
    }

    @Override
    public void onTick(boolean pClient) {
        if (level == null) return;
        var pos = getBlockPos();
        PowerTickHandler.blocksToAdd.add(pos);

        if (ownerUUID == null) return;
        var player = level.getPlayerByUUID(ownerUUID);
        if (player == null) return;

        var consume = isActive(level, player, pClient);
        var generate = isGenerating(level, player, pClient);
        if ((consume || generate) && flag(level, player, pClient)) onActive(pClient);
        else onFailed(pClient);
    }

    @Override
    public void onLoad(CompoundTag nbt) {
        super.onLoad(nbt);
        if (nbt.hasUUID("isActive")) isActive = nbt.getBoolean("isActive");
        if (nbt.hasUUID("OwnerUUID")) ownerUUID = nbt.getUUID("OwnerUUID");
        var pos = getBlockPos();
        PowerTickHandler.blocksToAdd.add(pos);
    }

    @Override
    public void onSave(CompoundTag nbt) {
        super.onSave(nbt);
        nbt.putBoolean("isActive", isActive);
        if (ownerUUID != null) nbt.putUUID("OwnerUUID", ownerUUID);
        var pos = getBlockPos();
        PowerTickHandler.blocksToRemove.add(pos);
    }

    public static boolean isLoaded(Level level, BlockPos pos) {
        if (level == null) return false;
        if (level.isClientSide) return false;
        if (!level.isInWorldBounds(pos)) return false;
        return level.isLoaded(pos);
    }
}
