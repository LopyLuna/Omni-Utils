package uwu.lopyluna.omni_util.content.items;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class MagicMirror extends Item {
    public MagicMirror(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (player.onGround() || player.isShiftKeyDown()) {
            player.playNotifySound(SoundEvents.TRIAL_SPAWNER_ABOUT_TO_SPAWN_ITEM, SoundSource.PLAYERS, 0.4f, 1);
            player.startUsingItem(usedHand);
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (livingEntity instanceof Player player) {
            if (remainingUseDuration <= 10) player.releaseUsingItem();
            if (level.random.nextBoolean()) spawnParticles(level, player, level.random.nextInt(1) + 1);
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        if (livingEntity instanceof Player player) {
            if (timeCharged <= 10) {
                teleport(level, player);
                spawnParticles(level, player, 30);

                player.playNotifySound(SoundEvents.TRIAL_SPAWNER_SPAWN_ITEM_BEGIN, SoundSource.PLAYERS, 0.8f, 1);
                player.getCooldowns().addCooldown(this, 80);
            } else player.getCooldowns().addCooldown(this, 10);
            if (player instanceof ServerPlayer serverPlayer) serverPlayer.connection.send(new ClientboundStopSoundPacket(SoundEvents.TRIAL_SPAWNER_ABOUT_TO_SPAWN_ITEM.getLocation(), SoundSource.PLAYERS));
        }
    }

    public void spawnParticles(Level level, Player player, int count) {
        var box = player.getBoundingBox().inflate(0.2);
        for (int i = 0; i < count; i++) {
            double x = Mth.lerp(level.random.nextDouble(), box.minX, box.maxX);
            double y = Mth.lerp(level.random.nextDouble(), box.minY, box.maxY);
            double z = Mth.lerp(level.random.nextDouble(), box.minZ, box.maxZ);
            //if (level.isClientSide) level.addParticle(ParticleTypes.END_ROD, x, y, z, 0, level.random.nextDouble() * 0.05, 0);
            if (level instanceof ServerLevel serverLevel)
                serverLevel.sendParticles(ParticleTypes.END_ROD, x, y, z, 1, 0, 0, 0, level.random.nextDouble() * 0.05);
        }
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 40;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    public void teleport(Level pLevel, Player pPlayer) {
        var worldInfo = pLevel.getLevelData();
        var spn = worldInfo.getSpawnPos();

        if (pPlayer instanceof ServerPlayer pServerPlayer) {
            var spnLevel = pServerPlayer.server.getLevel(Level.OVERWORLD);
            var spnAngle = 0f;
            if (spnLevel != null) {
                worldInfo = spnLevel.getLevelData();
                spn = worldInfo.getSpawnPos();
                spnAngle = worldInfo.getSpawnAngle();
            }
            var blockPos = pServerPlayer.getRespawnPosition();
            var pos = blockPos == null ? null : Vec3.atBottomCenterOf(blockPos);
            var angle = pServerPlayer.getRespawnAngle();
            var level = pServerPlayer.server.getLevel(pServerPlayer.getRespawnDimension());
            if (blockPos != null && level != null) {
                var state = level.getBlockState(blockPos);
                var block = state.getBlock();
                if (block instanceof BedBlock) {
                    var optional = BedBlock.findStandUpPosition(EntityType.PLAYER, level, blockPos, state.getValue(BedBlock.FACING), angle)
                            .map(vec3 -> ServerPlayer.RespawnPosAngle.of(vec3, blockPos));
                    if (optional.isPresent()) {
                        pos = optional.get().position();
                        angle = optional.get().yaw();
                    }
                } else if (block instanceof RespawnAnchorBlock) {
                    var optional = RespawnAnchorBlock.findStandUpPosition(EntityType.PLAYER, level, blockPos);
                    if (optional.isPresent()) pos = optional.get();
                }
            }
            if (pos != null && level != null) pServerPlayer.teleportTo(level, pos.x, pos.y, pos.z, angle, 0);
            else pServerPlayer.teleportTo(spnLevel != null ? spnLevel : pServerPlayer.serverLevel(), spn.getX(), spn.getY(), spn.getZ(), spnAngle, 0);
        } else pPlayer.teleportTo(spn.getX(), spn.getY(), spn.getZ());
        pPlayer.resetFallDistance();
    }
}
