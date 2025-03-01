package uwu.lopyluna.omni_util.events;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;
import uwu.lopyluna.omni_util.OmniUtils;
import uwu.lopyluna.omni_util.client.AllModelLayers;
import uwu.lopyluna.omni_util.client.render.AngelRingWingsRenderer;
import uwu.lopyluna.omni_util.content.container.ContainerTooltip;
import uwu.lopyluna.omni_util.content.container.ContainerTooltipComponent;
import uwu.lopyluna.omni_util.content.items.AngelRing;
import uwu.lopyluna.omni_util.register.AllDimensions;
import uwu.lopyluna.omni_util.register.AllItems;

@EventBusSubscriber(modid = OmniUtils.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onRegisterTooltipComponent(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(ContainerTooltipComponent.class, component -> new ContainerTooltip(component.contents()));
    }

    @SubscribeEvent
    public static void onRegisterDimEffects(RegisterDimensionSpecialEffectsEvent event) {
        event.register(AllDimensions.GRIMSPIRE_LOC, new DimensionSpecialEffects.NetherEffects());
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(AllModelLayers.ANGEL_RING_WINGS, AllModelLayers::createWingLayer);
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.AddLayers event) {
        event.getSkins().forEach(skin -> {
            LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> playerModel = event.getSkin(skin);
            if (playerModel != null) {
                playerModel.addLayer(new AngelRingWingsRenderer(playerModel, event.getEntityModels()));
            }
        });
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ItemProperties.register(AllItems.ANGEL_RING.get(), OmniUtils.loc("wing_type"),
                (stack, level, entity, seed) -> {
            if (!(stack.getItem() instanceof AngelRing ring)) return 0.0F;
            String wingType = ring.getWingType(stack);
            return switch (wingType) {
                case "bat" -> 1.0F;
                case "demon" -> 2.0F;
                case "feathered" -> 3.0F;
                case "gilded" -> 4.0F;
                case "invisible" -> 5.0F;
                default -> 0.0F;
            };
        }));
    }
}
