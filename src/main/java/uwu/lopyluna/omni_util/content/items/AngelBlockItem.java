package uwu.lopyluna.omni_util.content.items;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.omni_util.register.AllBlocks;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AngelBlockItem extends BlockItem {
    public AngelBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        var level = entity.level();
        var pos = entity.position();
        if (level.isClientSide) {
            if (level.random.nextInt(entity.getItem().getCount()) != 0) {
                Vec3 offset = pos.offsetRandom(level.random, 0.5f);
                for (int i=0; entity.getItem().getCount() > i; i++) {
                    if (level.random.nextBoolean()) continue;
                    level.addParticle(ParticleTypes.END_ROD, offset.x, pos.y, offset.z, 0, -.1f, 0);
                }
            }
        }
        entity.setNoGravity(true);
        return super.onEntityItemUpdate(stack, entity);
    }

    static int tick = 0;
    public static void angelBlockTick(Level pLevel, Player player) {
        tick = ++tick % 2;
        if (tick==0) {
            Vec3 center = getCenterPos(player);
            for (ItemEntity entity : pLevel.getEntitiesOfClass(ItemEntity.class, new AABB(center, center).inflate(64))) {
                if (entity == null || entity.isRemoved() || !entity.getItem().is(AllBlocks.ANGEL_BLOCK.asItem())) continue;
                Vec3 diff = getCenterPos(entity).subtract(center);
                double distance = diff.length();
                int range = 64;
                if (distance > range) continue;

                Vec3 force = diff.normalize().scale((range - distance) * -1).scale(1 / 128f);
                entity.push(force.x, force.y, force.z);
                entity.fallDistance = 0;
                entity.hurtMarked = true;
                Vec3 currentMovement = entity.getDeltaMovement();
                if (currentMovement.length() > 4f) entity.setDeltaMovement(currentMovement.normalize().scale(2f));
            }
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        var scale = player.blockInteractionRange() * (player.isShiftKeyDown() ? 0.5 : 1);
        Vec3 eyePos = player.getEyePosition();
        Vec3 lookVec = eyePos.add(player.calculateViewVector(player.getXRot(), player.getYRot()).scale(scale));
        var rayTrace = level.clip(new ClipContext(eyePos, lookVec, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
        return new InteractionResultHolder<>(this.useOn(new UseOnContext(player, usedHand, rayTrace)), player.getItemInHand(usedHand));
    }

    public static Vec3 getCenterPos(Entity entity) {
        AABB boundingBox = entity.getBoundingBox();
        double centerX = (boundingBox.minX + boundingBox.maxX) * 0.5;
        double centerY = (boundingBox.minY + boundingBox.maxY) * 0.5;
        double centerZ = (boundingBox.minZ + boundingBox.maxZ) * 0.5;
        return new Vec3(centerX, centerY, centerZ);
    }
}
