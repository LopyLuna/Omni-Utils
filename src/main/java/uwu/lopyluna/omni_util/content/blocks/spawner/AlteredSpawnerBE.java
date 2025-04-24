package uwu.lopyluna.omni_util.content.blocks.spawner;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Spawner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.omni_util.content.blocks.base.OmniBlockEntity;
import uwu.lopyluna.omni_util.content.utils.GoggleOverlay;
import uwu.lopyluna.omni_util.mixin.BaseSpawnerAccessor;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public class AlteredSpawnerBE extends OmniBlockEntity implements Spawner, GoggleOverlay {
    private final AlteredSpawner spawner = new AlteredSpawner(AlteredSpawnerBE.this);
    public AlteredSpawnerBE(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public void setEntityId(EntityType<?> pType, RandomSource pRandom) {
        spawner.setEntityId(pType, this.level, pRandom, this.worldPosition);
        setChanged();
    }

    public static void onClientTick(Level pLevel, BlockPos pPos, BlockState pState, AlteredSpawnerBE pSpawner) {
        var s = pSpawner.spawner;
        var a = (BaseSpawnerAccessor) s;

        var active = s.isNearPlayer(pLevel, pPos) && !pLevel.hasNeighborSignal(pPos);
        if (!active) {
            a.setOSpin$OmniUtils(s.getSpin());
        } else if (a.displayEntity$OmniUtils() != null) {
            RandomSource r = pLevel.getRandom();
            if (r.nextBoolean() || r.nextBoolean()) {
                double d0 = (double) pPos.getX() + r.nextDouble();
                double d1 = (double) pPos.getY() + r.nextDouble();
                double d2 = (double) pPos.getZ() + r.nextDouble();
                pLevel.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0, 0.0, 0.0);
                pLevel.addParticle(ParticleTypes.FLAME, d0, d1, d2, 0.0, 0.0, 0.0);
                if (r.nextBoolean()) pLevel.addParticle(ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER, d0, d1, d2, 0.0, 0.0, 0.0);
            }
            if (a.spawnDelay$OmniUtils() > 0) a.setSpawnDelay$OmniUtils(a.spawnDelay$OmniUtils()-1);

            a.setOSpin$OmniUtils(s.getSpin());
            a.setSpin$OmniUtils((s.getSpin() + (double)(1000.0F / ((float)a.spawnDelay$OmniUtils() + 800.0F))) % 360.0);
        }
    }

    public static void onServerTick(Level pLevel, BlockPos pPos, BlockState pState, AlteredSpawnerBE pSpawner) {
        pSpawner.spawner.serverTick((ServerLevel)pLevel, pPos);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    @Override
    public void onLoad(CompoundTag nbt) {
        spawner.load(level, worldPosition, nbt);
    }
    @Override
    public void onSave(CompoundTag nbt) {
        spawner.save(nbt);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        var compoundtag = this.saveCustomOnly(registries);
        compoundtag.remove("SpawnPotentials");
        return compoundtag;
    }

    @Override
    public boolean triggerEvent(int pID, int pType) {
        assert level != null;
        return this.spawner.onEventTriggered(level, pID) || super.triggerEvent(pID, pType);
    }

    public BaseSpawner getSpawner() {
        return spawner;
    }

    @Override
    public void appendHoverText(BlockEntity blockEntity, Level level, BlockPos pos, BlockState state, Player player, List<Component> tooltip) {
        GoggleOverlay.super.appendHoverText(blockEntity, level, pos, state, player, tooltip);
        if (!(blockEntity instanceof AlteredSpawnerBE be)) return;
        var s = be.spawner;
        var component = getSpawnEntityDisplayName(s);
        tooltip.add(Component.translatableWithFallback("", "Mob: ").append(Objects.requireNonNullElseGet(component, () -> Component.translatableWithFallback("", "Empty").withStyle(ChatFormatting.BLUE))));
        tooltip.add(Component.translatableWithFallback("", "Delay Min: " + s.getMinSpawnDelay() + " | Delay Max: " + s.getMaxSpawnDelay()));
        tooltip.add(Component.translatableWithFallback("", "Spawn Count: " + s.getSpawnCount() + " | Max Count: " + s.getMaxNearbyEntities()));
        tooltip.add(Component.translatableWithFallback("", "Active Range: " + s.getRequiredPlayerRange() + " | Spawn Range: " + s.getSpawnRange()));
    }

    static Component getSpawnEntityDisplayName(AlteredSpawner spawner) {
        var loc = spawner.getNextSpawnData() != null ? getEntityKey(spawner.getNextSpawnData().getEntityToSpawn()) : null;
        return loc != null ? BuiltInRegistries.ENTITY_TYPE.getOptional(loc)
                .map(id -> Component.translatable(id.getDescriptionId()).withStyle(ChatFormatting.BLUE)).orElse(null) : null;
    }
    private static ResourceLocation getEntityKey(CompoundTag tag) {
        return tag.contains("id", 8) ? ResourceLocation.tryParse(tag.getString("id")) : null;
    }
}
