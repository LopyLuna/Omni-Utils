package uwu.lopyluna.omni_util.content.blocks.curse;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uwu.lopyluna.omni_util.register.worldgen.AllBiomes;
import uwu.lopyluna.omni_util.register.worldgen.AllBlocks;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class CurseRotatedPillarBlock extends RotatedPillarBlock implements Cursed {
    public CurseRotatedPillarBlock(Properties properties) {
        super(properties.randomTicks());
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        super.stepOn(level, pos, state, entity);
        var bool = !level.isClientSide && level.random.nextInt(100) == 1 && level.getDifficulty() != Difficulty.PEACEFUL;
        if (bool && entity instanceof LivingEntity livingentity) {
            if (level.random.nextBoolean() && !livingentity.hasEffect(MobEffects.DARKNESS)) livingentity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 20+16));
            else if (!livingentity.isInvulnerableTo(level.damageSources().wither())) livingentity.addEffect(new MobEffectInstance(MobEffects.WITHER, 20+32));
        }
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        super.entityInside(state, level, pos, entity);
        var bool = !level.isClientSide && level.random.nextInt(200) == 1 && level.getDifficulty() != Difficulty.PEACEFUL;
        if (bool && entity instanceof LivingEntity livingentity) {
            if (level.random.nextBoolean() && !livingentity.hasEffect(MobEffects.DARKNESS)) livingentity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 20+16));
            else if (!livingentity.isInvulnerableTo(level.damageSources().wither())) livingentity.addEffect(new MobEffectInstance(MobEffects.WITHER, 20+32));
        }
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource r) {
        super.randomTick(state, level, pos, r);
        if (r.nextInt(20) == 1 && level.isNight()) spreadCurse(level, pos, r, state);
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.is(Items.FLINT_AND_STEEL) || stack.is(Items.FIRE_CHARGE)) {
            var cursedBlock = AllBlocks.deadRemap(state);
            level.setBlockAndUpdate(pos, cursedBlock.withPropertiesOf(state));
            if (level instanceof ServerLevel serverLevel) playSounds(state, serverLevel, level.random, pos, true, true);

            Item item = stack.getItem();
            if (stack.is(Items.FLINT_AND_STEEL)) stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            else stack.consume(1, player);
            player.awardStat(Stats.ITEM_USED.get(item));
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        BlockPos pos = hit.getBlockPos();
        if (!level.isClientSide && projectile.isOnFire() && projectile.mayInteract(level, pos)) {
            var cursedBlock = AllBlocks.deadRemap(state);
            level.setBlockAndUpdate(pos, cursedBlock.withPropertiesOf(state));
            if (level instanceof ServerLevel serverLevel) playSounds(state, serverLevel, level.random, pos, true, true);
            projectile.discard();
        }
    }

    @Override
    public void onCaughtFire(BlockState state, Level level, BlockPos pos, @Nullable Direction direction, @Nullable LivingEntity igniter) {
        var cursedBlock = AllBlocks.deadRemap(state);
        level.setBlockAndUpdate(pos, cursedBlock.withPropertiesOf(state));
        if (level instanceof ServerLevel serverLevel) playSounds(state, serverLevel, level.random, pos, true, true);
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
            if (biome.is(AllBiomes.CURSED_BIOME) || !biome.is(BiomeTags.IS_OVERWORLD)) continue;
            bool = true;
            break;
        }
        if (bool) AllBiomes.fillBiome(level, from, to, AllBiomes.CURSED_BIOME, p -> p.is(BiomeTags.IS_OVERWORLD));

        boolean playedSound = false;
        for (var x = -2; 2 > x; x++) for (var y = -2; 2 > y; y++) for (var z = -2; 2 > z; z++) {
            if (r.nextBoolean()) continue;
            var offset = pos.offset(x, y, z);
            var offsetState = level.getBlockState(offset);
            if (offsetState.getBlock() instanceof Cursed || offsetState.isAir()) continue;
            var cursedBlock = AllBlocks.curseRemap(offsetState, level);
            if (offsetState.is(cursedBlock)) continue;
            level.setBlockAndUpdate(offset, cursedBlock.defaultBlockState());
            playSounds(originState, level, r, offset, false, !playedSound);
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
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource r) {
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
