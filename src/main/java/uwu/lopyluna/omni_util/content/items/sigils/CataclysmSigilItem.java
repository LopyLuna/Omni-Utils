package uwu.lopyluna.omni_util.content.items.sigils;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.omni_util.mixin.CreeperAccessor;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CataclysmSigilItem extends Item {
    public CataclysmSigilItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        var dir = context.getClickedFace().getOpposite();
        var stack = context.getItemInHand();
        var count = context.getPlayer() != null && context.getPlayer().isShiftKeyDown() ? stack.getCount() : 1;
        spawnExplosionsInBox(context.getLevel(), context.getClickedPos().relative(dir), dir, count);
        stack.shrink(count);
        return super.useOn(context);
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.translatable(this.getDescriptionId()).withStyle(ChatFormatting.GOLD);
    }

    public void spawnExplosionsInBox(Level level, BlockPos pos, Direction direction, int count) {
        if (direction.getAxis().isVertical()) return;
        if (count > 0) {
            explode(level, Vec3.atCenterOf(pos), Mth.clamp(count / 4, 4, 12));
            explode(level, Vec3.atCenterOf(pos), Mth.clamp(count / 4, 4, 12));
        }
        var pos1 = pos.relative(direction).relative(direction.getClockWise(), 3).relative(Direction.DOWN); // ->
        var pos2 = pos.relative(direction, count).relative(direction.getCounterClockWise(), 3).relative(Direction.UP); // <-
        var vec1 = Vec3.atCenterOf(pos1);
        var vec2 = Vec3.atCenterOf(pos2);
        var box = new AABB(vec1, vec2);

        var newCount = 0;
        newCount += count-1;
        newCount *= 2;
        if (newCount > 0) for (int i = 0; i < newCount; i++) {
            double x = Mth.lerp(level.random.nextDouble(), box.minX, box.maxX);
            double y = Mth.lerp(level.random.nextDouble(), box.minY, box.maxY);
            double z = Mth.lerp(level.random.nextDouble(), box.minZ, box.maxZ);

            explode(level, new Vec3(x, y, z), Mth.clamp((newCount - i) / 4, 4, 12));
        }
    }

    public void explode(Level level, Vec3 pos, int strength) {
        var entity = EntityType.CREEPER.create(level);
        if (entity == null) return;
        entity.setInvisible(true);
        entity.noPhysics = true;
        entity.setPos(pos);
        entity.setCustomName(getDescription());
        ((CreeperAccessor) entity).sexExplosionRadius$OmniUtils(strength);
        ((CreeperAccessor) entity).explodeCreeper$OmniUtils();
    }
}
