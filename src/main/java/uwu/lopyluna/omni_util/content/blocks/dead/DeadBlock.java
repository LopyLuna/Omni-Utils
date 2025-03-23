package uwu.lopyluna.omni_util.content.blocks.dead;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.omni_util.content.blocks.curse.Cursed;
import uwu.lopyluna.omni_util.register.worldgen.AllBiomes;
import uwu.lopyluna.omni_util.register.worldgen.AllBlocks;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class DeadBlock extends Block implements Dead, Cursed {
    int type;

    public DeadBlock(Properties properties, int type) {
        super(properties.randomTicks());
        this.type = type;
    }

    public DeadBlock(Properties properties) {
        this(properties, 0);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        super.stepOn(level, pos, state, entity);
        var bool = !level.isClientSide && level.random.nextInt(200) == 1 && level.getDifficulty() != Difficulty.PEACEFUL;
        if (bool && entity instanceof LivingEntity livingentity) {
            if (level.random.nextBoolean() && !livingentity.hasEffect(MobEffects.DARKNESS)) livingentity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 20+8));
            else if (!livingentity.isInvulnerableTo(level.damageSources().wither())) livingentity.addEffect(new MobEffectInstance(MobEffects.WITHER, 20+16));
        }
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        super.entityInside(state, level, pos, entity);
        var bool = !level.isClientSide && level.random.nextInt(300) == 1 && level.getDifficulty() != Difficulty.PEACEFUL;
        if (bool && entity instanceof LivingEntity livingentity) {
            if (level.random.nextBoolean() && !livingentity.hasEffect(MobEffects.DARKNESS)) livingentity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 20+8));
            else if (!livingentity.isInvulnerableTo(level.damageSources().wither())) livingentity.addEffect(new MobEffectInstance(MobEffects.WITHER, 20+16));
        }
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource r) {
        super.randomTick(state, level, pos, r);
        spreadCurse(level, pos, r, state);
        if (type==1 && level.getBlockState(pos.above()).isSolidRender(level, pos.above())) level.setBlockAndUpdate(pos, AllBlocks.DEAD_COARSE.get().defaultBlockState());
    }

    public void spreadCurse(ServerLevel level, BlockPos pos, RandomSource r, BlockState originState) {
        var from = pos.offset(-2, -2, -2);
        var to = pos.offset(2, 2, 2);
        List<Holder<Biome>> biomes = new ArrayList<>();
        for (var x = -2; 2 > x; x++) for (var y = -2; 2 > y; y++) for (var z = -2; 2 > z; z++) {
            var offset = pos.offset(x, y, z);
            biomes.add(level.getBiome(offset));
        }
        var bool = false;
        for (var biome : biomes) {
            if (!(!biome.is(AllBiomes.DEAD_BIOME) && (biome.is(BiomeTags.IS_OVERWORLD) || biome.is(AllBiomes.CURSED_BIOME)))) continue;
            bool = true;
            break;
        }
        if (bool) AllBiomes.fillBiome(level, from, to, AllBiomes.DEAD_BIOME, p -> (p.is(BiomeTags.IS_OVERWORLD) || p.is(AllBiomes.CURSED_BIOME)));

        boolean playedSound = false;
        for (var x = -2; 2 > x; x++) for (var y = -2; 2 > y; y++) for (var z = -2; 2 > z; z++) {
            if (r.nextBoolean() || r.nextBoolean()) continue;
            var offset = pos.offset(x, y, z);
            var offsetState = level.getBlockState(offset);
            if (offsetState.getBlock() instanceof Dead || offsetState.isAir()) continue;
            var cursedBlock = AllBlocks.deadRemap(offsetState);
            if (offsetState.is(cursedBlock)) continue;
            level.setBlockAndUpdate(offset, cursedBlock.defaultBlockState());
            playSounds(originState, level, r, offset, true, !playedSound);
            playedSound = true;
        }
    }

    @SuppressWarnings("deprecation")
    public void playSounds(BlockState state, ServerLevel level, RandomSource r, BlockPos pos, boolean withered, boolean sound) {
        var cen = pos.getCenter();
        if (sound) {
            var soundType = state.getSoundType();
            if (withered) level.playSound(null, cen.x, cen.y, cen.z, SoundEvents.CHORUS_FLOWER_DEATH, SoundSource.BLOCKS, 1.0F, 0.5F);
            else level.playSound(null, cen.x, cen.y, cen.z, SoundEvents.CHORUS_FLOWER_GROW, SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
            level.playSound(null, cen.x, cen.y, cen.z, soundType.getBreakSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
        }
        for (int i = 0; r.nextInt(10) > i; i++) {
            Direction direction = Direction.getRandom(r);
            if (level.getBlockState(pos.relative(direction)).isAir()) level.sendParticles(ParticleTypes.WITCH,
                    cen.x + (direction.getStepX() == 0 ? r.nextDouble() : 0.5 + (double) direction.getStepX() * 0.6),
                    cen.y + (direction.getStepY() == 0 ? r.nextDouble() : 0.5 + (double) direction.getStepY() * 0.6),
                    cen.z + (direction.getStepZ() == 0 ? r.nextDouble() : 0.5 + (double) direction.getStepZ() * 0.6),
                    1 + r.nextInt(2), 0.0, 0.0, 0.0, 0);
        }
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return type==2 ? Block.box(2.0, 0.0, 2.0, 14.0, 13.0, 14.0) : super.getShape(state, level, pos, context);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return type==2 ? (pathComputationType == PathComputationType.AIR && !this.hasCollision || super.isPathfindable(state, pathComputationType)) : super.isPathfindable(state, pathComputationType);
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return type==2 ? state.getFluidState().isEmpty() : super.propagatesSkylightDown(state, level, pos);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (type==2) {
            BlockPos blockpos = pos.below();
            BlockState belowBlockState = level.getBlockState(blockpos);
            return belowBlockState.is(BlockTags.DIRT);
        }
        return super.canSurvive(state, level, pos);
    }

    @Override
    protected @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (type==2) return !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, neighborState, level, pos, neighborPos);
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }
}
