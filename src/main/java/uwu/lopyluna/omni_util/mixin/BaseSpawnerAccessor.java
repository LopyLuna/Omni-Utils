package uwu.lopyluna.omni_util.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BaseSpawner.class)
public interface BaseSpawnerAccessor {
    @Accessor("displayEntity")
    Entity displayEntity$OmniUtils();
    @Accessor("nextSpawnData")
    SpawnData nextSpawnData$OmniUtils();
    @Accessor("spawnPotentials")
    SimpleWeightedRandomList<SpawnData> spawnPotentials$OmniUtils();
    @Accessor("spawnDelay")
    int spawnDelay$OmniUtils();
    @Accessor("spawnDelay")
    void setSpawnDelay$OmniUtils(int value);
    @Accessor("oSpin")
    void setOSpin$OmniUtils(double value);
    @Accessor("spin")
    void setSpin$OmniUtils(double value);
    @Invoker("getOrCreateNextSpawnData")
    SpawnData getOrCreateNextSpawnData$OmniUtils(Level level, RandomSource random, BlockPos pos);


    @Accessor("minSpawnDelay") int minSpawnDelay$OmniUtils();
    @Accessor("maxSpawnDelay") int maxSpawnDelay$OmniUtils();
    @Accessor("spawnCount") int spawnCount$OmniUtils();
    @Accessor("maxNearbyEntities") int maxNearbyEntities$OmniUtils();
    @Accessor("requiredPlayerRange") int requiredPlayerRange$OmniUtils();
    @Accessor("spawnRange") int spawnRange$OmniUtils();

    @Accessor("minSpawnDelay") void setMinSpawnDelay$OmniUtils(int value);
    @Accessor("maxSpawnDelay") void setMaxSpawnDelay$OmniUtils(int value);
    @Accessor("spawnCount") void setSpawnCount$OmniUtils(int value);
    @Accessor("maxNearbyEntities") void setMaxNearbyEntities$OmniUtils(int value);
    @Accessor("requiredPlayerRange") void setRequiredPlayerRange$OmniUtils(int value);
    @Accessor("spawnRange") void setSpawnRange$OmniUtils(int value);
}
