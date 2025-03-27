package uwu.lopyluna.omni_util.content.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import uwu.lopyluna.omni_util.mixin.CreeperAccessor;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class UnstableHexaBlock extends Block {
    public UnstableHexaBlock(Properties properties) {
        super(properties.randomTicks());
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
        var random = level.random;
        if (!level.isClientSide && random.nextBoolean() && random.nextBoolean() && random.nextBoolean()) explode(level, pos.getCenter());
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        var random = level.random;
        if (!level.isClientSide && random.nextBoolean() && random.nextBoolean() && random.nextBoolean()) explode(level, pos.getCenter());
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        super.fallOn(level, state, pos, entity, fallDistance);
        var random = level.random;
        if (!level.isClientSide && random.nextBoolean() && random.nextBoolean() && random.nextBoolean()) explode(level, pos.getCenter());
    }

    @Override
    public void onCaughtFire(BlockState state, Level level, BlockPos pos, @Nullable Direction direction, @Nullable LivingEntity igniter) {
        if (!level.isClientSide) explode(level, pos.getCenter());
    }

    @Override
    public void wasExploded(Level level, BlockPos pos, Explosion explosion) {
        if (!level.isClientSide) explode(level, pos.getCenter());
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextBoolean() && random.nextBoolean() && random.nextBoolean()) explode(level, pos.getCenter());
    }

    public void explode(Level level, Vec3 pos) {
        var entity = EntityType.CREEPER.create(level);
        if (entity == null) return;
        entity.setInvisible(true);
        entity.noPhysics = true;
        entity.setPos(pos);
        entity.setCustomName(getName());
        ((CreeperAccessor) entity).sexExplosionRadius$OmniUtils(24);
        ((CreeperAccessor) entity).explodeCreeper$OmniUtils();
    }
}
