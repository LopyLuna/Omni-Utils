package uwu.lopyluna.omni_util.content.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.extensions.IPlayerExtension;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.omni_util.content.blocks.spawner.AlteredSpawnerBE;
import uwu.lopyluna.omni_util.register.AllDataComponents;
import uwu.lopyluna.omni_util.register.AllItems;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class SoulLassoItem extends Item {
    public SoulLassoItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public @NotNull ItemStack getCraftingRemainingItem(ItemStack stack) {
        return new ItemStack(AllItems.SOUL_LASSO.get());
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        var pStack = pContext.getItemInHand();
        var pPlayer = pContext.getPlayer();
        var pClickedPos = pContext.getClickedPos();
        var pLevel = pContext.getLevel();
        var pState = pLevel.getBlockState(pClickedPos);

        if (pLevel.getBlockEntity(pClickedPos) instanceof AlteredSpawnerBE pSpawner) {
            var pTarget = getEntity(pStack, pLevel, pClickedPos, 3);
            if (pTarget == null) return InteractionResult.PASS;
            if (!pLevel.isClientSide) {
                var pType = pTarget.getType();
                var chance = pType.getCategory().isFriendly() ? pLevel.random.nextBoolean() || pLevel.random.nextBoolean() : pLevel.random.nextBoolean() && pLevel.random.nextBoolean();
                if (chance) {
                    pSpawner.setEntityId(pType, pLevel.getRandom());
                    pLevel.sendBlockUpdated(pClickedPos, pState, pState, 3);
                    pLevel.gameEvent(pPlayer, GameEvent.BLOCK_CHANGE, pClickedPos);
                    clearEntity(pStack);
                    if (pPlayer != null) pPlayer.playNotifySound(SoundEvents.TRIAL_SPAWNER_SPAWN_MOB, SoundSource.PLAYERS, 0.8f, 1.0f);
                } else {
                    if (pLevel instanceof ServerLevel pServer)
                        pState.getBlock().popExperience(pServer, pClickedPos, 15 + pLevel.random.nextInt(15) + pLevel.random.nextInt(15));
                    clearEntity(pStack);
                    if (pPlayer != null) pPlayer.playNotifySound(SoundEvents.TRIAL_SPAWNER_SPAWN_ITEM, SoundSource.PLAYERS, 0.8f, 0.85f);
                }
            }
            return InteractionResult.sidedSuccess(pLevel.isClientSide());
        } else if (pContext.getClickedFace() == Direction.UP) {
            var pTarget = getEntity(pStack, pLevel, pClickedPos, 0);
            if (pTarget == null) return InteractionResult.PASS;
            pLevel.addFreshEntity(pTarget);
            clearEntity(pStack);
            if (pPlayer != null) pPlayer.playNotifySound(SoundEvents.TRIAL_SPAWNER_SPAWN_MOB, SoundSource.PLAYERS, 0.8f, 1.0f);
            return InteractionResult.sidedSuccess(pLevel.isClientSide());
        }
        return InteractionResult.PASS;
    }

    public LivingEntity getEntity(ItemStack pStack, Level pLevel, BlockPos pPos, int i) {
        if (!pStack.has(AllDataComponents.HAS_ENTITY)) return null;
        var entity = pStack.get(AllDataComponents.ENTITY);
        var name = pStack.get(AllDataComponents.ENTITY_CUSTOM_NAME); // 1
        var data = pStack.get(AllDataComponents.ENTITY_DATA); // 2
        var addData = pStack.get(AllDataComponents.ADD_ENTITY_DATA); // 3
        var perData = pStack.get(AllDataComponents.PER_ENTITY_DATA); // 4
        var has = pStack.get(AllDataComponents.HAS_ENTITY);
        var bData = data == null && i!=2;
        var bAddData = addData == null && i!=3;
        var bPerData = perData == null && i!=4;
        if (entity == null || bData || bAddData || bPerData || has == null) return null;
        var loadEntity = EntityType.loadEntityRecursive(entity.copyTag(), pLevel, e -> e);
        if (!(loadEntity instanceof LivingEntity pTarget)) return null;
        if (i!=2) pTarget.load(data.copyTag());
        if (i!=3) pTarget.readAdditionalSaveData(addData.copyTag());
        if (i!=4) pTarget.getPersistentData().merge(perData.copyTag());
        pTarget.resetFallDistance();
        pTarget.moveTo(Vec3.atBottomCenterOf(pPos.above()));
        if (name != null) pTarget.setCustomName(name);
        return pTarget;
    }

    public void clearEntity(ItemStack pStack) {
        pStack.remove(AllDataComponents.ENTITY);
        pStack.remove(AllDataComponents.ENTITY_CUSTOM_NAME);
        pStack.remove(AllDataComponents.ENTITY_DATA);
        pStack.remove(AllDataComponents.ADD_ENTITY_DATA);
        pStack.remove(AllDataComponents.PER_ENTITY_DATA);
        pStack.remove(AllDataComponents.HAS_ENTITY);
    }

    public boolean flagMob(LivingEntity pTarget) {
        return pTarget instanceof Enemy || !pTarget.getType().getCategory().isFriendly();
    }

    public boolean flagHealth(LivingEntity pTarget) {
        return pTarget.getHealth() > (pTarget.getMaxHealth() * 0.5f);
    }

    public Component getTargetName(ItemStack pStack, LivingEntity pTarget) {
        var custom = pStack.get(AllDataComponents.ENTITY_CUSTOM_NAME);
        var name = custom != null ? custom : pTarget.getName();
        var type = pTarget.getType().getDescription();
        var component = Component.empty();
        if (type != name) component.append(name).append(" ");
        return component.append(type);
    }

    public Component invalidMob(ItemStack pStack, LivingEntity pTarget) {
        return Component.empty().append(getTargetName(pStack, pTarget)).append(" is not Friendly").withStyle(ChatFormatting.RED);
    }
    public Component invalidHealth(ItemStack pStack, LivingEntity pTarget) {
        return Component.empty().append(getTargetName(pStack, pTarget)).append(" has too much Health").withStyle(ChatFormatting.RED);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pTarget, InteractionHand pHand) {
        if (pStack.has(AllDataComponents.HAS_ENTITY) || pTarget instanceof Player || pTarget instanceof IPlayerExtension || pTarget.isMultipartEntity() || pTarget.isDeadOrDying() || pTarget.getMaxHealth() > 100) return InteractionResult.PASS;
        if (flagMob(pTarget)) {
            pPlayer.displayClientMessage(invalidMob(pStack, pTarget), true);
            pPlayer.playNotifySound(SoundEvents.NOTE_BLOCK_BASS.value(), SoundSource.PLAYERS, 0.8f, 0.7f);
            return InteractionResult.PASS;
        } else if (flagHealth(pTarget)) {
            pPlayer.displayClientMessage(invalidHealth(pStack, pTarget), true);
            pPlayer.playNotifySound(SoundEvents.NOTE_BLOCK_BASS.value(), SoundSource.PLAYERS, 0.8f, 0.7f);
            return InteractionResult.PASS;
        }
        var dataTag = new CompoundTag();
        var addDataTag = new CompoundTag();
        var entityID = new CompoundTag();
        var entity = CustomData.EMPTY;
        var data = CustomData.EMPTY;
        var addData = CustomData.EMPTY;
        var perData = CustomData.EMPTY;

        perData = CustomData.of(pTarget.getPersistentData());
        pTarget.saveWithoutId(dataTag);
        pTarget.addAdditionalSaveData(addDataTag);
        addData = CustomData.of(addDataTag);
        entityID.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(pTarget.getType()).toString());
        entity = CustomData.of(entityID);
        if (pTarget.hasCustomName()) pStack.set(AllDataComponents.ENTITY_CUSTOM_NAME, pTarget.getCustomName());
        pStack.set(AllDataComponents.ENTITY, entity);
        pStack.set(AllDataComponents.ENTITY_DATA, data);
        pStack.set(AllDataComponents.ADD_ENTITY_DATA, addData);
        pStack.set(AllDataComponents.PER_ENTITY_DATA, perData);
        pStack.set(AllDataComponents.HAS_ENTITY, Unit.INSTANCE);
        pTarget.discard();
        pPlayer.playNotifySound(SoundEvents.SOUL_ESCAPE.value(), SoundSource.PLAYERS, 1.5f, 1.0f);
        return InteractionResult.sidedSuccess(pTarget.level().isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltip, TooltipFlag pFlag) {
        var pLevel = pContext.level();
        if (!pStack.has(AllDataComponents.HAS_ENTITY) || pLevel == null) return;
        var data = pStack.get(AllDataComponents.ENTITY);
        if (data == null) return;
        var entity = EntityType.loadEntityRecursive(data.copyTag(), pLevel, e -> e);
        if (entity == null) return;
        pTooltip.add(Component.empty().append(Component.literal("Mob: ").withStyle(ChatFormatting.GRAY)));
        var custom = pStack.get(AllDataComponents.ENTITY_CUSTOM_NAME);
        var name = custom != null ? custom : entity.getName();
        var type = entity.getType().getDescription();
        if (name != type) pTooltip.add(Component.empty().append(name).withStyle(ChatFormatting.BLUE));
        pTooltip.add(Component.empty().append(entity.getType().getDescription()).withStyle(ChatFormatting.BLUE));
    }
}
