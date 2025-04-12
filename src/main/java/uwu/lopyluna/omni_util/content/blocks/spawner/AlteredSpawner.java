package uwu.lopyluna.omni_util.content.blocks.spawner;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.omni_util.mixin.BaseSpawnerAccessor;

import javax.annotation.Nullable;

import static uwu.lopyluna.omni_util.content.AllUtils.addParticles;
import static uwu.lopyluna.omni_util.content.AllUtils.playSound;

@SuppressWarnings("unused")
public class AlteredSpawner extends BaseSpawner {
    BlockEntity be;
    private boolean player = false;

    public AlteredSpawner(BlockEntity blockEntity) {
        be = blockEntity;
        setSpawnCount(1);
        setMinSpawnDelay(150);
        setMaxSpawnDelay(250);
        setMaxNearbyEntities(8);
        setRequiredPlayerRange(4);
    }

    @Override
    public void broadcastEvent(Level pLevel, @NotNull BlockPos pPos, int pID) {
        pLevel.blockEvent(pPos, Blocks.SPAWNER, pID, 0);
    }

    public boolean isNearPlayer(Level level, BlockPos pos) {
        for (Player player : level.players()) if (EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(player) && EntitySelector.LIVING_ENTITY_STILL_ALIVE.test(player)) {
            double d0 = player.distanceToSqr((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5);
            if (getRequiredPlayerRange() < 0.0 || d0 < getRequiredPlayerRange() * getRequiredPlayerRange()) return true;
        }
        return false;
    }

    @Override
    public void setNextSpawnData(@Nullable Level pLevel, @NotNull BlockPos pPos, @NotNull SpawnData pData) {
        super.setNextSpawnData(pLevel, pPos, pData);
        if (pLevel != null) {
            BlockState blockstate = pLevel.getBlockState(pPos);
            pLevel.sendBlockUpdated(pPos, blockstate, blockstate, 4);
        }
    }

    @Override
    public Either<BlockEntity, Entity> getOwner() {
        return Either.left(be);
    }

    public void serverTick(@NotNull ServerLevel level, @NotNull BlockPos pos) {
        var state = level.getBlockState(pos);
        var v = Vec3.atCenterOf(pos);
        var active = isNearPlayer(level, pos) && !level.hasNeighborSignal(pos);
        if (active) {
            var hasEntity = getNextSpawnData() != null && getNextSpawnData().getEntityToSpawn().contains("id");
            if (!player && hasEntity) {
                player = true;
                playSound(level, pos, SoundEvents.TRIAL_SPAWNER_OPEN_SHUTTER, SoundSource.BLOCKS, 1.0F, (level.random.nextFloat() - level.random.nextFloat()) * 0.2F + 1.0F);
                playSound(level, pos, SoundEvents.TRIAL_SPAWNER_DETECT_PLAYER, SoundSource.BLOCKS, 1.0F, (level.random.nextFloat() - level.random.nextFloat()) * 0.2F + 1.0F);
                addParticles(level, pos, getRequiredPlayerRange() + 8, 4, ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER);
                level.setBlock(pos, state.setValue(AlteredSpawnerBlock.ENABLED, true), 3);
            }
            if (player && !hasEntity) {
                player = false;
                playSound(level, pos, SoundEvents.TRIAL_SPAWNER_CLOSE_SHUTTER, SoundSource.BLOCKS, 1.0F, (level.random.nextFloat() - level.random.nextFloat()) * 0.2F + 1.0F);
                level.setBlock(pos, state.setValue(AlteredSpawnerBlock.ENABLED, false), 3);
            }

            if (getSpawnDelay() == -1) delay(level, pos);
            if (getSpawnDelay() > 0) tickSpawnDelay();
            else {
                var r = level.getRandom();
                var data = a.getOrCreateNextSpawnData$OmniUtils(level, r, pos);
                for (int i = 0; i < getSpawnCount(); i++) {
                    var compoundtag = data.getEntityToSpawn();
                    var optional = EntityType.by(compoundtag);
                    if (optional.isEmpty()) {
                        delay(level, pos);
                        return;
                    }
                    var listtag = compoundtag.getList("Pos", 6);
                    int j = listtag.size();
                    var d0 = j >= 1 ? listtag.getDouble(0) : (double)pos.getX() + (r.nextDouble() - r.nextDouble()) * (double)getSpawnRange() + 0.5;
                    var d1 = j >= 2 ? listtag.getDouble(1) : (double)(pos.getY() + r.nextInt(3) - 1);
                    var d2 = j >= 3 ? listtag.getDouble(2) : (double)pos.getZ() + (r.nextDouble() - r.nextDouble()) * (double)getSpawnRange() + 0.5;
                    if (level.noCollision(optional.get().getSpawnAABB(d0, d1, d2))) {
                        var blockpos = BlockPos.containing(d0, d1, d2);
                        if (data.getCustomSpawnRules().isPresent()) {
                            if (!optional.get().getCategory().isFriendly() && level.getDifficulty() == Difficulty.PEACEFUL) continue;
                            SpawnData.CustomSpawnRules rules = data.getCustomSpawnRules().get();
                            if (!rules.isValidPosition(blockpos, level)) continue;
                        } else if (!SpawnPlacements.checkSpawnRules(optional.get(), level, MobSpawnType.SPAWNER, blockpos, level.getRandom())) continue;

                        var entity = EntityType.loadEntityRecursive(compoundtag, level, e -> {e.moveTo(d0, d1, d2, e.getYRot(), e.getXRot()); return e; });
                        if (entity == null) {
                            delay(level, pos);
                            return;
                        }
                        int k = level.getEntities(EntityTypeTest.forExactClass(entity.getClass()), new AABB(pos.getX(), pos.getY(), pos.getZ(), (pos.getX() + 1), (pos.getY() + 1), (pos.getZ() + 1)).inflate(getSpawnRange()), EntitySelector.NO_SPECTATORS).size();
                        if (k >= getMaxNearbyEntities()) {
                            delay(level, pos);
                            return;
                        }

                        entity.moveTo(entity.getX(), entity.getY(), entity.getZ(), r.nextFloat() * 360.0F, 0.0F);
                        if (entity instanceof Mob mob) {
                            if (!net.neoforged.neoforge.event.EventHooks.checkSpawnPositionSpawner(mob, level, MobSpawnType.SPAWNER, data, this)) continue;
                            var flag1 = data.getEntityToSpawn().size() == 1 && data.getEntityToSpawn().contains("id", 8);
                            net.neoforged.neoforge.event.EventHooks.finalizeMobSpawnSpawner(mob, level, level.getCurrentDifficultyAt(entity.blockPosition()), MobSpawnType.SPAWNER, null, this, flag1);
                            data.getEquipment().ifPresent(mob::equip);
                        }

                        if (!level.tryAddFreshEntityWithPassengers(entity)) {
                            delay(level, pos);
                            return;
                        }
                        //serverLevel.levelEvent(2004, pos, 0);
                        level.levelEvent(3011, pos, TrialSpawner.FlameParticle.NORMAL.encode());
                        level.gameEvent(entity, GameEvent.ENTITY_PLACE, blockpos);
                        level.levelEvent(3012, entity.blockPosition(), TrialSpawner.FlameParticle.NORMAL.encode());
                        delay(level, pos);
                    }
                }
            }
        } else if (player) {
            player = false;
            playSound(level, pos, SoundEvents.TRIAL_SPAWNER_CLOSE_SHUTTER, SoundSource.BLOCKS, 1.0F, (level.random.nextFloat() - level.random.nextFloat()) * 0.2F + 1.0F);
            level.setBlock(pos, state.setValue(AlteredSpawnerBlock.ENABLED, false), 3);
        }
    }

    private void delay(Level level, BlockPos pos) {
        if (getMaxSpawnDelay() <= getMinSpawnDelay()) setSpawnDelay(getMinSpawnDelay());
        else setSpawnDelay(getMinSpawnDelay() + level.random.nextInt(getMaxSpawnDelay() - getMinSpawnDelay()));

        a.spawnPotentials$OmniUtils().getRandom(level.random).ifPresent(entry -> this.setNextSpawnData(level, pos, entry.data()));
        this.broadcastEvent(level, pos, 1);
        level.setBlock(pos, level.getBlockState(pos), 3);
    }

    BaseSpawnerAccessor a = ((BaseSpawnerAccessor)this);

    public void tickSpawnDelay() {
        setSpawnDelay(getSpawnDelay()-1);
    }

    public SpawnData getNextSpawnData() {
        return a.nextSpawnData$OmniUtils();
    }
    public int getSpawnDelay() {
        return a.spawnDelay$OmniUtils();
    }
    public int getMinSpawnDelay() {
        return a.minSpawnDelay$OmniUtils();
    }
    public int getMaxSpawnDelay() {
        return a.maxSpawnDelay$OmniUtils();
    }
    public int getSpawnCount() {
        return a.spawnCount$OmniUtils();
    }
    public int getMaxNearbyEntities() {
        return a.maxNearbyEntities$OmniUtils();
    }
    public int getRequiredPlayerRange() {
        return a.requiredPlayerRange$OmniUtils();
    }
    public int getSpawnRange() {
        return a.spawnRange$OmniUtils();
    }

    public void setSpawnDelay(int value) {
        a.setSpawnDelay$OmniUtils(value);
    }
    public void setMinSpawnDelay(int value) {
        a.setMinSpawnDelay$OmniUtils(value);
    }
    public void setMaxSpawnDelay(int value) {
        a.setMaxSpawnDelay$OmniUtils(value);
    }
    public void setSpawnCount(int value) {
        a.setSpawnCount$OmniUtils(value);
    }
    public void setMaxNearbyEntities(int value) {
        a.setMaxNearbyEntities$OmniUtils(value);
    }
    public void setRequiredPlayerRange(int value) {
        a.setRequiredPlayerRange$OmniUtils(value);
    }
    public void setSpawnRange(int value) {
        a.setSpawnRange$OmniUtils(value);
    }
}
