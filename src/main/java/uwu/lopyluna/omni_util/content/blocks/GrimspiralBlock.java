package uwu.lopyluna.omni_util.content.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uwu.lopyluna.omni_util.register.worldgen.AllDimensions;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class GrimspiralBlock extends Block implements Portal {
    public GrimspiralBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (entity.canUsePortal(false)) entity.setAsInsidePortal(this, pos);
    }

    @Override
    public int getPortalTransitionTime(ServerLevel level, Entity entity) {
        return entity instanceof Player player ? Math.max(1, level.getGameRules().getInt(player.getAbilities().invulnerable
                        ? GameRules.RULE_PLAYERS_NETHER_PORTAL_CREATIVE_DELAY : GameRules.RULE_PLAYERS_NETHER_PORTAL_DEFAULT_DELAY)) : 0;
    }

    @Override
    public @NotNull Transition getLocalTransition() {
        return Transition.CONFUSION;
    }

    @Nullable
    @Override
    public DimensionTransition getPortalDestination(ServerLevel level, Entity entity, BlockPos blockpos) {
        ResourceKey<Level> resourcekey = level.dimension() == AllDimensions.GRIMSPIRE ? Level.OVERWORLD : AllDimensions.GRIMSPIRE;
        ServerLevel serverlevel = level.getServer().getLevel(resourcekey);
        if (serverlevel != null) {
            boolean flag = resourcekey == AllDimensions.GRIMSPIRE;
            WorldBorder worldborder = serverlevel.getWorldBorder();
            double d0 = DimensionType.getTeleportationScale(level.dimensionType(), serverlevel.dimensionType());
            BlockPos pos = worldborder.clampToBounds(blockpos.getX() * d0, 256, blockpos.getZ() * d0);

            if (flag) {
                for (var offset : BlockPos.betweenClosed(pos.offset(2, 3, 2), pos.offset(-2, 0, -2))) serverlevel.setBlock(offset, Blocks.AIR.defaultBlockState(), 3);
                for (var direction : Direction.values()) if (!direction.getAxis().isVertical()) serverlevel.setBlock(pos.relative(direction), Blocks.TORCH.defaultBlockState(), 18);
            }
            if (entity instanceof LivingEntity livingEntity) livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 60, 0, true, false, false));

            serverlevel.setBlock(pos, this.defaultBlockState(), 18);
            Vec3 vec3 = pos.above().getBottomCenter();

            if (!flag) {
                if (entity instanceof ServerPlayer serverplayer) return serverplayer.findRespawnPositionAndUseSpawnBlock(false, DimensionTransition.DO_NOTHING);
                vec3 = entity.adjustSpawnLocation(serverlevel, pos.above()).getBottomCenter();
            }

            return new DimensionTransition(
                    serverlevel, vec3,
                    entity.getDeltaMovement(),
                    entity.getYRot(), entity.getXRot(),
                    DimensionTransition.PLAY_PORTAL_SOUND.then(DimensionTransition.PLACE_PORTAL_TICKET)
            );
        }
        return null;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource r) {
        if (r.nextInt(20) == 0) {
            level.playLocalSound(
                    (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5,
                    SoundEvents.PORTAL_AMBIENT, SoundSource.BLOCKS,
                    0.25F, r.nextFloat() * 0.4F + 0.4F, false
            );
        }

        for (int i = 0; r.nextInt(10) > i; i++) {
            Direction direction = Direction.getRandom(r);
            if (level.getBlockState(pos.relative(direction)).isAir()) level.addParticle(ParticleTypes.SMOKE,
                    (double) pos.getX() + (direction.getStepX() == 0 ? r.nextDouble() : 0.5 + (double) direction.getStepX() * 0.6),
                    (double) pos.getY() + (direction.getStepY() == 0 ? r.nextDouble() : 0.5 + (double) direction.getStepY() * 0.6),
                    (double) pos.getZ() + (direction.getStepZ() == 0 ? r.nextDouble() : 0.5 + (double) direction.getStepZ() * 0.6),
                    0.0, 0.0, 0.0);
        }
    }
}
