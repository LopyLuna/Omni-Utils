package uwu.lopyluna.omni_util.events;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import uwu.lopyluna.omni_util.content.blocks.base.PowerBlockEntity;
import uwu.lopyluna.omni_util.content.items.AngelRing;
import uwu.lopyluna.omni_util.content.items.base.PowerItem;
import uwu.lopyluna.omni_util.content.managers.PowerManager;
import uwu.lopyluna.omni_util.register.AllItems;
import uwu.lopyluna.omni_util.register.worldgen.AllBiomes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static uwu.lopyluna.omni_util.OmniUtils.MOD_ID;
import static uwu.lopyluna.omni_util.events.ServerEvents.containsItem;
import static uwu.lopyluna.omni_util.register.AllPowerSources.ANGEL_FLIGHT;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class PowerTickHandler {
    public static final Set<BlockPos> blocks = new HashSet<>();
    public static final Set<BlockPos> blocksToRemove = new HashSet<>();
    public static final Set<BlockPos> blocksToAdd = new HashSet<>();

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player p = event.getEntity();
        if (!(p instanceof ServerPlayer pPlayer)) return;
        PowerManager.resetRP(pPlayer);

        ServerLevel serverLevel = pPlayer.serverLevel();
        List<BlockPos> remove = new ArrayList<>();
        if (!serverLevel.isClientSide) {
            if (!blocksToRemove.isEmpty()) {
                blocksToRemove.forEach(blocks::remove);
                blocksToRemove.clear();
            }
            if (!blocksToAdd.isEmpty()) {
                blocks.addAll(blocksToAdd);
                blocksToAdd.clear();
            }
            if (!blocks.isEmpty()) for (var pos : blocks) {
                if (pos == null) continue;
                var be = serverLevel.getBlockEntity(pos);
                if (be instanceof PowerBlockEntity powerBlockEntity && PowerBlockEntity.isLoaded(serverLevel, pos)) powerBlockEntity.onProcessPower(pPlayer);
                else remove.add(pos);
            }
            remove.forEach(blocks::remove);
        }

        List<ItemStack> ignoreStacks = new ArrayList<>();

        for (ItemStack armorStack : pPlayer.getArmorSlots()) if (!armorStack.isEmpty()) ignoreStacks.add(armorStack);
        for (int i = 0; i < pPlayer.getInventory().getContainerSize(); i++) {
            var stack = pPlayer.getInventory().getItem(i);
            if (stack.isEmpty() || ignoreStacks.contains(stack)) continue;
            processPowerItem(pPlayer, serverLevel, stack);
            processItemTick(pPlayer, serverLevel, stack, i, false);
        }
        int j = 0;
        for (ItemStack armorStack : pPlayer.getArmorSlots()) {
            int slot = switch (j) { case 0 -> 36; case 1 -> 37; case 2 -> 38; case 3 -> 39; default -> 9999; };
            if (armorStack.isEmpty()) continue;
            processPowerItem(pPlayer, serverLevel, armorStack);
            processItemTick(pPlayer, serverLevel, armorStack, slot, true);
            j++;
        }

        var isCreative = pPlayer.isCreative() || pPlayer.isSpectator();
        var inGrimspire = pPlayer.level().getBiome(pPlayer.blockPosition()).is(AllBiomes.GRIMSPIRE_BIOME);

        if (!isCreative) {
            var stacks = pPlayer.getInventory().armor;
            if (!inGrimspire && containsItem(AllItems.ANGEL_RING, stacks)) for (var stack : stacks) {
                if (!(stack.getItem() instanceof AngelRing)) continue;
                if (stack.equals(pPlayer.getItemBySlot(EquipmentSlot.CHEST))) {
                    AngelRing.onProcessPower(ANGEL_FLIGHT.impact, pPlayer);
                    if (PowerManager.getNetRP(pPlayer) >= 0) PowerManager.adjustDelay(pPlayer, ANGEL_FLIGHT.id, 1);
                    else PowerManager.adjustDelay(pPlayer, ANGEL_FLIGHT.id, -1);
                } else PowerManager.adjustDelay(pPlayer, ANGEL_FLIGHT.id, -1);
                break;
            } else PowerManager.adjustDelay(pPlayer, ANGEL_FLIGHT.id, -1);

            if (PowerManager.getDelay(pPlayer, ANGEL_FLIGHT.id) > 0) AngelRing.onActive(pPlayer);
            else AngelRing.onFailed(pPlayer);
        }
    }

    private static void processPowerItem(ServerPlayer player, ServerLevel serverLevel, ItemStack stack) {
        if (stack.getItem() instanceof PowerItem powerItem) powerItem.onProcessPower(player, serverLevel, stack);
    }

    private static void processItemTick(ServerPlayer player, ServerLevel serverLevel, ItemStack stack, int slot, boolean armor) {
        if (stack.getItem() instanceof PowerItem powerItem) powerItem.onPlayerTick(serverLevel, stack, player, slot, armor);
    }
}
