package uwu.lopyluna.omni_util.content.power;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import uwu.lopyluna.omni_util.content.blocks.base.BasePowerBlockEntity;
import uwu.lopyluna.omni_util.content.items.BasePowerItem;

import java.util.HashSet;
import java.util.Set;

import static uwu.lopyluna.omni_util.OmniUtils.MOD_ID;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class PowerTickHandler {
    private static final Set<BasePowerBlockEntity> activeGenerators = new HashSet<>();
    private static final Set<BasePowerItem> activeItems = new HashSet<>();

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        assert ServerLifecycleHooks.getCurrentServer() != null;
        for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) PowerManager.resetRP(player);

        for (var generator : activeGenerators) if (isGeneratorLoadedByPlayer(generator)) generator.processPower();

        for (var item : activeItems) item.processPower();
    }

    public static void registerGenerator(BasePowerBlockEntity generator) {
        activeGenerators.add(generator);
    }

    public static void unregisterGenerator(BasePowerBlockEntity generator) {
        activeGenerators.remove(generator);
    }

    public static void registerPowerItem(BasePowerItem item) {
        activeItems.add(item);
    }

    public static void unregisterPowerItem(BasePowerItem item) {
        activeItems.remove(item);
    }

    private static boolean isGeneratorLoadedByPlayer(BasePowerBlockEntity generator) {
        if (generator == null) return false;
        var level = generator.getLevel();
        if (level == null) return false;
        return level.isLoaded(generator.getBlockPos());
    }
}
