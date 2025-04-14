package uwu.lopyluna.omni_util.content.items.hexa_ingot;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import uwu.lopyluna.omni_util.mixin.CreeperAccessor;
import uwu.lopyluna.omni_util.register.AllItems;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class UnstableHexaIngot extends Item {
    public UnstableHexaIngot(Properties properties) {
        super(properties.durability(200));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (entity instanceof LivingEntity livingEntity && level instanceof ServerLevel serverLevel) run(stack, livingEntity, serverLevel);
        if (stack.has(DataComponents.BASE_COLOR)) stack.shrink(1);
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        if (player.level() instanceof ServerLevel serverLevel) run(stack, player, serverLevel);
        return super.overrideStackedOnOther(stack, slot, action, player);
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (player.level() instanceof ServerLevel serverLevel) run(stack, player, serverLevel);
        return super.overrideOtherStackedOnMe(stack, other, slot, action, player, access);
    }

    public void run(ItemStack stack, LivingEntity entity, ServerLevel level) {
        stack.hurtAndBreak(1, level, entity, item -> {});
        if (stack.isEmpty()) explode(stack, entity.level(), entity.position());
    }

    public void explode(ItemStack stack, Level level, Vec3 pos) {
        if (!stack.has(DataComponents.BASE_COLOR)) {
            var entity = EntityType.CREEPER.create(level);
            if (entity == null) return;
            entity.getActiveEffects().clear();
            entity.setInvisible(true);
            entity.noPhysics = true;
            entity.setPos(pos);
            entity.setCustomName(getDescription());
            ((CreeperAccessor) entity).setExplosionRadius$OmniUtils(8);
            ((CreeperAccessor) entity).explodeCreeper$OmniUtils();
            stack.set(DataComponents.BASE_COLOR, DyeColor.RED);
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        var item = AllItems.UNSTABLE_HEXA_INGOT.get();
        return !(oldStack.is(item) && newStack.is(item));
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return stack.getDamageValue() > 0;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isDamaged(ItemStack stack) {
        return false;
    }
}
