package uwu.lopyluna.omni_util.content.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import uwu.lopyluna.omni_util.register.AllBlocks;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class UnstableHexaTnt extends TntBlock {
    public UnstableHexaTnt(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(UNSTABLE, true));
    }

    @Override
    public void onCaughtFire(BlockState state, Level world, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
        explode(world, pos, igniter);
    }

    @Override
    public void wasExploded(Level level, BlockPos pos, Explosion explosion) {
        if (!level.isClientSide) {
            PrimedTnt primedtnt = new PrimedTnt(level, (double)pos.getX() + 0.5, pos.getY(), (double)pos.getZ() + 0.5, explosion.getIndirectSourceEntity());
            primedtnt.setBlockState(AllBlocks.UNSTABLE_HEXA_TNT.getDefaultState());
            int i = primedtnt.getFuse();
            primedtnt.setFuse(((short)(level.random.nextInt(i / 4) + i / 8)) + 20);
            level.addFreshEntity(primedtnt);
        }
    }

    private static void explode(Level level, BlockPos pos, @Nullable LivingEntity entity) {
        if (!level.isClientSide) {
            PrimedTnt primedtnt = new PrimedTnt(level, (double)pos.getX() + 0.5, pos.getY(), (double)pos.getZ() + 0.5, entity);
            primedtnt.setBlockState(AllBlocks.UNSTABLE_HEXA_TNT.getDefaultState());
            primedtnt.setFuse(primedtnt.getFuse() + 20);
            level.addFreshEntity(primedtnt);
            level.playSound(null, primedtnt.getX(), primedtnt.getY(), primedtnt.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 0.5F);
            level.gameEvent(entity, GameEvent.PRIME_FUSE, pos);
        }
    }
}
