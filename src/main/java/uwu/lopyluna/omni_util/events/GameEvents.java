package uwu.lopyluna.omni_util.events;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import uwu.lopyluna.omni_util.content.commands.DebugPowerCommand;

import static uwu.lopyluna.omni_util.OmniUtils.MOD_ID;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class GameEvents {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        DebugPowerCommand.register(event.getDispatcher());
    }
}
