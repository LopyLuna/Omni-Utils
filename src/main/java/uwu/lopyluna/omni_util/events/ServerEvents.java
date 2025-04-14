package uwu.lopyluna.omni_util.events;

import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import uwu.lopyluna.omni_util.content.commands.DebugPowerCommand;
import uwu.lopyluna.omni_util.content.commands.DebugSanityCommand;
import uwu.lopyluna.omni_util.content.items.AngelBlockItem;
import uwu.lopyluna.omni_util.content.items.base.BlockBreakingDiggerItem;
import uwu.lopyluna.omni_util.content.items.base.BlockBreakingItem;
import uwu.lopyluna.omni_util.content.items.hexa_ingot.UnstableHexaIngot;
import uwu.lopyluna.omni_util.content.items.hexa_ingot.UnstableHexaNugget;
import uwu.lopyluna.omni_util.mixin.CreeperAccessor;
import uwu.lopyluna.omni_util.register.AllBlocks;
import uwu.lopyluna.omni_util.register.worldgen.AllBiomes;

import java.util.ArrayList;
import java.util.List;

import static uwu.lopyluna.omni_util.OmniUtils.MOD_ID;
import static uwu.lopyluna.omni_util.content.managers.SanityManager.adjustSanity;
import static uwu.lopyluna.omni_util.content.managers.SanityManager.getSanity;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ServerEvents {
    static int delay = 0;

    public static boolean isCheated(MobSpawnType spawnType) {
        return spawnType == MobSpawnType.SPAWN_EGG || spawnType == MobSpawnType.MOB_SUMMONED;
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        var accessor = event.getLevel();
        if (!(accessor instanceof Level level)) return;
        Player player = event.getPlayer();
        ItemStack stack = player.getMainHandItem();
        BlockHitResult rayTrace = Item.getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        if (stack.getItem() instanceof BlockBreakingItem item)
            item.onBlockBreak(stack, level, player, event.getPos(), event.getState(), rayTrace, event);
        if (stack.getItem() instanceof BlockBreakingDiggerItem item)
            item.onBlockBreak(stack, level, player, event.getPos(), event.getState(), rayTrace, event);
    }
    @SubscribeEvent
    public static void onBlockDrops(BlockDropsEvent event) {
        if (!(event.getBreaker() instanceof Player player)) return;
        var level = event.getLevel();
        BlockHitResult rayTrace = Item.getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        if (event.getTool().getItem() instanceof BlockBreakingItem item)
            item.onBlockDrops(event.getTool(), event.getLevel(), player, event.getPos(), event.getState(), event.getBlockEntity(), event.getDrops(), rayTrace, event);
        if (event.getTool().getItem() instanceof BlockBreakingDiggerItem item)
            item.onBlockDrops(event.getTool(), event.getLevel(), player, event.getPos(), event.getState(), event.getBlockEntity(), event.getDrops(), rayTrace, event);
    }
    @SubscribeEvent
    public static void breakingSpeed(PlayerEvent.BreakSpeed event) {
        if (event.getPosition().isEmpty()) return;
        Player player = event.getEntity();
        ItemStack stack = player.getMainHandItem();
        var level = player.level();
        BlockHitResult rayTrace = Item.getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        if (stack.getItem() instanceof BlockBreakingItem item)
            event.setNewSpeed(item.breakingSpeed(stack, level, player, event.getPosition().get(), event.getState(), rayTrace, event.getNewSpeed(), event.getOriginalSpeed(), event));
        if (stack.getItem() instanceof BlockBreakingDiggerItem item)
            event.setNewSpeed(item.breakingSpeed(stack, level, player, event.getPosition().get(), event.getState(), rayTrace, event.getNewSpeed(), event.getOriginalSpeed(), event));
    }
    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        var level = event.getLevel();
        ItemStack stack = event.getItemStack();
        var pos = event.getPos();
        BlockHitResult rayTrace = Item.getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        if (stack.getItem() instanceof BlockBreakingItem item) switch (event.getAction()) {
            case START -> item.onStartLeftClickBlock(stack, level, player, pos, level.getBlockState(pos), event.getHand(), event.getFace(), rayTrace, event);
            case STOP -> item.onStoppedLeftClickBlock(stack, level, player, pos, level.getBlockState(pos), event.getHand(), event.getFace(), rayTrace, event);
            case ABORT -> item.onAbortLeftClickBlock(stack, level, player, pos, level.getBlockState(pos), event.getHand(), event.getFace(), rayTrace, event);
            default -> item.onLeftClickBlock(stack, level, player, pos, level.getBlockState(pos), event.getHand(), event.getFace(), rayTrace, event);
        }
        if (stack.getItem() instanceof BlockBreakingDiggerItem item) switch (event.getAction()) {
            case START -> item.onStartLeftClickBlock(stack, level, player, pos, level.getBlockState(pos), event.getHand(), event.getFace(), rayTrace, event);
            case STOP -> item.onStoppedLeftClickBlock(stack, level, player, pos, level.getBlockState(pos), event.getHand(), event.getFace(), rayTrace, event);
            case ABORT -> item.onAbortLeftClickBlock(stack, level, player, pos, level.getBlockState(pos), event.getHand(), event.getFace(), rayTrace, event);
            default -> item.onLeftClickBlock(stack, level, player, pos, level.getBlockState(pos), event.getHand(), event.getFace(), rayTrace, event);
        }
    }

    public static boolean containsItem(ItemLike item, NonNullList<ItemStack> stacks) {
        for (var stack : stacks) if (stack.is(item.asItem())) return true;
        return false;
    }

    @SubscribeEvent
    public static void onLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        var e = event.getEntity();
        var level = e.level();
        if (level.isClientSide) return;
        if (!(e instanceof ServerPlayer)) return;
        PowerTickHandler.blocks.clear();
    }

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        var player = event.getEntity();
        var level = player.level();

        if (level.isClientSide) return;
        AngelBlockItem.angelBlockTick(level, player);
        if (!(player instanceof ServerPlayer pPlayer)) return;
        var pos = pPlayer.blockPosition();
        var isCreative = pPlayer.isCreative() || pPlayer.isSpectator();
        var inGrimspire = level.getBiome(pos).is(AllBiomes.GRIMSPIRE_BIOME);

        if (inGrimspire) {
            if (!isCreative) {
                var abilities = pPlayer.getAbilities();
                boolean update = false;
                if (abilities.mayfly) {
                    abilities.mayfly = false;
                    update = true;
                }
                if (abilities.flying) {
                    abilities.flying = false;
                    update = true;
                }
                if (update) pPlayer.onUpdateAbilities();
            }
            if (level.random.nextBoolean() && level.random.nextBoolean() && level.random.nextBoolean()) pPlayer.clearFire();
            if (!isCreative && pPlayer.hasEffect(MobEffects.NIGHT_VISION)) pPlayer.removeEffect(MobEffects.NIGHT_VISION);
            int light = level.getMaxLocalRawBrightness(pos);
            if (light < 7 && !isCreative) adjustSanity(pPlayer, -((7 - light) * 0.05f));
            else if (light > 7) adjustSanity(pPlayer, ((light - 7) * 0.03f));
        } else adjustSanity(pPlayer, 0.4f);

        if (getSanity(pPlayer) <= 0) {
            if (!pPlayer.isDeadOrDying()) pPlayer.kill();
        } else if (!isCreative) {
            if (getSanity(pPlayer) < 5) {
                if (level.random.nextBoolean() && level.random.nextBoolean())
                    pPlayer.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200, 0, true, false));
                if (level.random.nextBoolean() && level.random.nextBoolean())
                    pPlayer.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 200, 0, true, false));
            } else if (getSanity(pPlayer) < 10) {
                if (level.random.nextBoolean() && level.random.nextBoolean())
                    pPlayer.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200, 0, true, false));
            } else if (getSanity(pPlayer) < 25) {
                if (level.random.nextBoolean() && level.random.nextBoolean())
                    pPlayer.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 200, 0, true, false));
            }
        }
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        if (delay == 0) {
            event.getServer().getAllLevels().forEach(serverLevel -> {
                if (serverLevel == null) return;
                List<Entity> entities = new ArrayList<>();
                serverLevel.getAllEntities().forEach(entities::add);
                if (entities.isEmpty()) return;
                entities.forEach(entity -> {
                    if (entity == null) return;
                    if (entity instanceof LivingEntity living && serverLevel.getBiome(living.blockPosition()).is(AllBiomes.CURSED_BIOME)) {
                        if (living instanceof Enemy monster) {
                            if (!serverLevel.getGameRules().getBoolean(GameRules.RULE_DOINSOMNIA) &&
                                    monster instanceof Phantom phantom &&
                                    !isCheated(phantom.getSpawnType()) &&
                                    (!phantom.isPersistenceRequired() && !phantom.requiresCustomPersistence())
                            ) living.discard();

                            if (monster instanceof Creeper creeper) {
                                if (!living.hasEffect(MobEffects.DARKNESS)) living.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 20 * 30, 0, true, false));
                                if (!living.hasEffect(MobEffects.WEAKNESS)) living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20 * 45, 2, true, false));
                                if (!living.hasEffect(MobEffects.CONFUSION)) living.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 20 * 45, 2, true, false));
                                if (!living.hasEffect(MobEffects.BLINDNESS)) living.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20 * 15, 0, true, false));
                                if (!living.hasEffect(MobEffects.DIG_SLOWDOWN)) living.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 20 * 15, 2, true, false));
                                if (!living.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 45, 0, true, false));
                                if (!creeper.isPowered()) creeper.getEntityData().set(CreeperAccessor.dataIsPowered$OmniUtils(), true);
                                if (creeper.isIgnited()) ((CreeperAccessor) creeper).explodeCreeper$OmniUtils();
                            } else if (!(living instanceof Silverfish || living instanceof Endermite)) {
                                if (!living.hasEffect(MobEffects.FIRE_RESISTANCE)) living.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20 * 60 * 10, 0, true, false));
                                if (!living.hasEffect(MobEffects.ABSORPTION)) living.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 20 * 60 * 10, 0, true, false));
                                if (!living.hasEffect(MobEffects.HEALTH_BOOST)) living.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 20 * 60 * 10, 0, true, false));
                                if (!living.hasEffect(MobEffects.REGENERATION)) living.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 60 * 10, 1, true, false));
                                if (!living.hasEffect(MobEffects.DAMAGE_RESISTANCE)) living.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 60 * 10, 0, true, false));
                                if (!living.hasEffect(MobEffects.DAMAGE_BOOST)) living.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20 * 60 * 10, 2, true, false));
                                if (!living.hasEffect(MobEffects.MOVEMENT_SPEED)) living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * 60 * 10, 2, true, false));
                            }
                            living.wasOnFire = false;
                            living.setRemainingFireTicks(-1);
                            if (!living.onGround()) living.resetFallDistance();
                            living.setInvisible(serverLevel.isRaining() || living.isInLiquid());
                            if (!living.hasEffect(MobEffects.WEAVING)) living.addEffect(new MobEffectInstance(MobEffects.WEAVING, 20 * 45, 0, true, false));
                            if (!living.hasEffect(MobEffects.INFESTED)) living.addEffect(new MobEffectInstance(MobEffects.INFESTED, 20 * 45, 0, true, false));
                        } else {
                            if (living.getHealth() > (living.getMaxHealth() * 0.5f)) living.setHealth(living.getMaxHealth() * 0.5f);
                            if (living instanceof Player player && player.getFoodData().getFoodLevel() > 10) player.causeFoodExhaustion(0.5f);
                        }
                    }


                    if (entity instanceof ItemEntity itemEntity) {
                        var stack = itemEntity.getItem();
                        var item = stack.getItem();
                        var level = itemEntity.level();
                        var pos = itemEntity.position();

                        if (item instanceof UnstableHexaIngot ingot) {
                            itemEntity.discard();
                            ingot.explode(stack, level, pos);
                        } else if (item instanceof UnstableHexaNugget nugget) {
                            itemEntity.discard();
                            nugget.explode(stack, level, pos);
                        } else if (stack.is(AllBlocks.UNSTABLE_HEXA_BLOCK.asItem())) {
                            itemEntity.discard();
                            explode(stack, level, pos, 24);
                        }
                    }
                });
            });
        }
        delay = ++delay % 5;
    }

    public static void explode(ItemStack stack, Level level, Vec3 pos, int radius) {
        var entity = EntityType.CREEPER.create(level);
        if (entity == null) return;
        entity.getActiveEffects().clear();
        entity.setInvisible(true);
        entity.noPhysics = true;
        entity.setPos(pos);
        entity.setCustomName(stack.getDisplayName());
        ((CreeperAccessor) entity).setExplosionRadius$OmniUtils(radius);
        ((CreeperAccessor) entity).explodeCreeper$OmniUtils();
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        DebugPowerCommand.register(event.getDispatcher());
        DebugSanityCommand.register(event.getDispatcher());
    }
}
