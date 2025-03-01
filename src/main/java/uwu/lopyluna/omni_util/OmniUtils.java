package uwu.lopyluna.omni_util;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredHolder;
import uwu.lopyluna.omni_util.content.utils.Registration;
import uwu.lopyluna.omni_util.network.SyncActivationToClientPacket;
import uwu.lopyluna.omni_util.network.SyncRPToClientPacket;
import uwu.lopyluna.omni_util.register.*;

@SuppressWarnings("unused")
@Mod(OmniUtils.MOD_ID)
public class OmniUtils {
    public static final String MOD_ID = "omni_util";
    public static final String NAME = "Omni Utils";
    public static Registration REG = new Registration(MOD_ID);

    public OmniUtils(IEventBus modEventBus, ModContainer modContainer) {
        REG.register(modEventBus);

        AllDataComponents.register();
        AllPowerSources.register();
        AllItems.register();
        AllBlocks.register();
        AllBlockEntities.register();

        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::onRegisterPayloadHandlers);
    }

    private void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(MOD_ID);
        registrar = registrar.executesOn(HandlerThread.NETWORK);

        registrar.playToClient(SyncRPToClientPacket.TYPE, SyncRPToClientPacket.CODEC, SyncRPToClientPacket::handle);
        registrar.playToClient(SyncActivationToClientPacket.TYPE, SyncActivationToClientPacket.CODEC, SyncActivationToClientPacket::handle);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(BASE_TAB.getKey())) {
            var item = AllItems.ANGEL_RING.get();
            event.accept(item);
            String[] variants = {"feathered", "demon", "gilded", "bat", "invisible"};
            for (String type : variants) {
                ItemStack stack = new ItemStack(item);
                stack.set(AllDataComponents.WINGS_COMPONENTS.get(), type);
                event.accept(stack);
            }
        }
    }

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BASE_TAB = REG.creativeTab().register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatableWithFallback("itemGroup." + MOD_ID + ".base", NAME))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(Items.DIAMOND::getDefaultInstance)
            .displayItems(REG.itemForCreativeTab())
            .build());

    public static ResourceLocation loc(String loc) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, loc);
    }
    public static ResourceLocation empty() {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, "none");
    }
}
