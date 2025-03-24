package uwu.lopyluna.omni_util.events;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import uwu.lopyluna.omni_util.content.blocks.base.PowerBlockEntity;
import uwu.lopyluna.omni_util.content.items.base.PowerItem;
import uwu.lopyluna.omni_util.content.managers.PowerManager;

import javax.annotation.Nullable;
import java.util.*;

import static uwu.lopyluna.omni_util.OmniUtils.MOD_ID;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class PowerTickHandler {
    @Nullable public static final Map<UUID, PowerBlockEntity> blocks = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player p = event.getEntity();
        if (!(p instanceof ServerPlayer player)) return;
        PowerManager.resetRP(player);
        ServerLevel serverLevel = player.serverLevel();
        if (blocks != null && !blocks.isEmpty()) blocks.forEach((uuid, powerBlockEntity) -> { if (powerBlockEntity != null && uuid != null) powerBlockEntity.onProcessPower(player); });
        List<ItemStack> ignoreStacks = new ArrayList<>();

        for (ItemStack armorStack : player.getArmorSlots()) if (!armorStack.isEmpty()) ignoreStacks.add(armorStack);
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            var stack = player.getInventory().getItem(i);
            if (stack.isEmpty() || ignoreStacks.contains(stack)) continue;
            processPowerItem(player, serverLevel, stack);
            processItemTick(player, serverLevel, stack, i, false);
        }
        int j = 0;
        for (ItemStack armorStack : player.getArmorSlots()) {
            int slot = switch (j) { case 0 -> 36; case 1 -> 37; case 2 -> 38; case 3 -> 39; default -> 9999; };
            if (armorStack.isEmpty()) continue;
            processPowerItem(player, serverLevel, armorStack);
            processItemTick(player, serverLevel, armorStack, slot, true);
            j++;
        }

    }

    private static void processPowerItem(ServerPlayer player, ServerLevel serverLevel, ItemStack stack) {
        if (stack.getItem() instanceof PowerItem powerItem) powerItem.onProcessPower(player, serverLevel, stack);
    }

    private static void processItemTick(ServerPlayer player, ServerLevel serverLevel, ItemStack stack, int slot, boolean armor) {
        if (stack.getItem() instanceof PowerItem powerItem) powerItem.onPlayerTick(serverLevel, stack, player, slot, armor);
    }
}
