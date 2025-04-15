package uwu.lopyluna.omni_util;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredHolder;
import uwu.lopyluna.omni_util.content.utils.OmniRegistration;
import uwu.lopyluna.omni_util.content.utils.Registration;
import uwu.lopyluna.omni_util.network.SyncActivationToClientPacket;
import uwu.lopyluna.omni_util.network.SyncRPToClientPacket;
import uwu.lopyluna.omni_util.network.SyncSanityToClientPacket;
import uwu.lopyluna.omni_util.register.*;
import uwu.lopyluna.omni_util.register.worldgen.AllFeature;

@SuppressWarnings("unused")
@Mod(OmniUtils.MOD_ID)
public class OmniUtils {
    public static final String MOD_ID = "omni_util";
    public static final String NAME = "Omni Utils";
    public static OmniRegistration REGISTER = new OmniRegistration(MOD_ID);
    public static Registration REG = new Registration(MOD_ID);

    public OmniUtils(IEventBus modEventBus, ModContainer modContainer) {
        REGISTER.register(modEventBus);
        REG.registerEventListeners(modEventBus);
        REG.defaultCreativeTab(BASE_TAB, "base_tab");

        AllLangProvider.addTranslations();
        AllTags.addGenerators();
        AllArmorMaterials.register(modEventBus);
        AllDataComponents.register();
        AllPowerSources.register();
        AllItems.register();
        AllBlocks.register();
        AllBlockEntities.register();
        AllFeature.register(modEventBus);

        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::onRegisterPayloadHandlers);
    }

    private void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(MOD_ID);
        registrar = registrar.executesOn(HandlerThread.NETWORK);

        registrar.playToClient(SyncRPToClientPacket.TYPE, SyncRPToClientPacket.CODEC, SyncRPToClientPacket::handle);
        registrar.playToClient(SyncActivationToClientPacket.TYPE, SyncActivationToClientPacket.CODEC, SyncActivationToClientPacket::handle);
        registrar.playToClient(SyncSanityToClientPacket.TYPE, SyncSanityToClientPacket.CODEC, SyncSanityToClientPacket::handle);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(BASE_TAB.getKey())) {
            var item = AllItems.ANGEL_RING.get().getDefaultInstance();
            String[] variants = {"feathered", "demon", "gilded", "bat", "invisible", "allay", "vexxed"};
            for (String type : variants) {
                var newItem = item.copy();
                newItem.set(AllDataComponents.WING_TYPE.get(), type);
                event.insertAfter(item, newItem, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
        }
    }

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BASE_TAB = REGISTER.creativeTab().register("base_tab", () -> CreativeModeTab.builder()
            .title(Component.translatableWithFallback("itemGroup." + MOD_ID + ".base", NAME))
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .icon(AllItems.ANGEL_RING.get()::getDefaultInstance)
            .build());

    public static ResourceLocation loc(String loc) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, loc);
    }
    public static ResourceLocation empty() {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, "none");
    }
}
