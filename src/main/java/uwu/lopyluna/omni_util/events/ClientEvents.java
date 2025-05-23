package uwu.lopyluna.omni_util.events;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import uwu.lopyluna.omni_util.OmniUtils;
import uwu.lopyluna.omni_util.client.AllModelLayers;
import uwu.lopyluna.omni_util.client.render.angel_ring.AngelRingWingsRenderer;
import uwu.lopyluna.omni_util.client.screens.TrashCanScreen;
import uwu.lopyluna.omni_util.content.blocks.colored_block.ColoredBE;
import uwu.lopyluna.omni_util.content.container.ContainerTooltip;
import uwu.lopyluna.omni_util.content.container.ContainerTooltipComponent;
import uwu.lopyluna.omni_util.content.container.bundle_of_holding.BundleOfHoldingContainer;
import uwu.lopyluna.omni_util.content.items.AngelRing;
import uwu.lopyluna.omni_util.content.items.BundleOfHoldingItem;
import uwu.lopyluna.omni_util.register.AllBlocks;
import uwu.lopyluna.omni_util.register.AllDataComponents;
import uwu.lopyluna.omni_util.register.AllItems;
import uwu.lopyluna.omni_util.register.AllMenuTypes;
import uwu.lopyluna.omni_util.register.worldgen.AllDimensions;

@EventBusSubscriber(modid = OmniUtils.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onRegisterScreens(final RegisterMenuScreensEvent event) {
        event.register(AllMenuTypes.TRASH_CAN.get(), TrashCanScreen::new);
    }

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
    public static final BlockColor COLORED_BLOCK_COLOR = (state, level, pos, tintIndex) -> {
        if (level == null || pos == null || !(level.getBlockEntity(pos) instanceof ColoredBE be)) return 0xFFFFFFFF;
        return FastColor.ARGB32.color(255, be.colorR, be.colorG, be.colorB);
    };

    @SubscribeEvent
    public static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register(COLORED_BLOCK_COLOR, AllBlocks.COLORED_BLOCK.get());
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(AllItems.SOUL_LASSO.get(), OmniUtils.loc("entity"), (stack, level, entity, seed) -> stack.has(AllDataComponents.HAS_ENTITY) ? 1f : 0f);
            ItemProperties.register(AllItems.WITHERED_SOUL_LASSO.get(), OmniUtils.loc("entity"), (stack, level, entity, seed) -> stack.has(AllDataComponents.HAS_ENTITY) ? 1f : 0f);
            ItemProperties.register(AllItems.BUNDLE_OF_HOLDING.get(), OmniUtils.loc("open"), (stack, level, entity, seed) -> {
                if (!(stack.getItem() instanceof BundleOfHoldingItem) || !(entity instanceof Player)) return 0.0F;
                var contents = new BundleOfHoldingContainer(stack);
                var openMenu = (stack.has(DataComponents.NOTE_BLOCK_SOUND));
                return contents.isEmpty() || openMenu ? 1f : 0f;
            });
            ItemProperties.register(AllItems.ANGEL_RING.get(), OmniUtils.loc("wing_type"), (stack, level, entity, seed) -> {
                if (!(stack.getItem() instanceof AngelRing ring)) return 0.0F;
                String wingType = ring.getWingType(stack);
                return switch (wingType) {
                    case "bat" -> 1.0F;
                    case "demon" -> 2.0F;
                    case "feathered" -> 3.0F;
                    case "gilded" -> 4.0F;
                    case "invisible" -> 5.0F;
                    case "allay" -> 6.0F;
                    case "vexxed" -> 7.0F;
                    default -> 0.0F;
                };
            });
        });
    }
}
