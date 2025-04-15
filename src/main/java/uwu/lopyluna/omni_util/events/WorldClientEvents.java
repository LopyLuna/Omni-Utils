package uwu.lopyluna.omni_util.events;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import uwu.lopyluna.omni_util.OmniUtils;
import uwu.lopyluna.omni_util.client.ClientSanityData;
import uwu.lopyluna.omni_util.client.SanityAmbientSoundInstance;
import uwu.lopyluna.omni_util.content.blocks.base.OmniBlockEntity;
import uwu.lopyluna.omni_util.content.items.wands.WandItem;
import uwu.lopyluna.omni_util.content.managers.GoggleOverlayManager;
import uwu.lopyluna.omni_util.register.AllBlocks;
import uwu.lopyluna.omni_util.register.AllTags;

import java.util.List;

import static net.neoforged.neoforge.client.event.RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS;

@EventBusSubscriber(modid = OmniUtils.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class WorldClientEvents {
    private static final Minecraft mc = Minecraft.getInstance();

    public static SanityAmbientSoundInstance INSTANCE = null;


    @SubscribeEvent
    public static void onCameraSetup(ViewportEvent.ComputeCameraAngles event) {
        var player = mc.player;
        var level = mc.level;
        if (player == null || level == null) return;

        float sanity = ClientSanityData.getSanity();
        if (sanity >= 100f) return;

        float shakeFactor = Mth.clamp(1f - (sanity / 100f), 0f, 1f);

        float maxAngle = 1.5f;
        float shakeYaw = (level.random.nextFloat() - 0.5f) * 2f * maxAngle * shakeFactor;
        float shakePitch = (level.random.nextFloat() - 0.5f) * 2f * maxAngle * shakeFactor;
        float shakeRoll = (level.random.nextFloat() - 0.5f) * 2f * maxAngle * shakeFactor;

        event.setYaw(event.getYaw() + shakeYaw);
        event.setPitch(event.getPitch() + shakePitch);
        event.setRoll(event.getRoll() + shakeRoll);
    }

    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiEvent.Post pEvent) {
        if (mc.getConnection() != null && mc.player != null && mc.level != null) {
            renderBlackScreenOfDeath(pEvent.getGuiGraphics(), pEvent.getPartialTick(), mc.player);
            renderSanityBar(pEvent.getGuiGraphics());
        }
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        var player = mc.player;
        if (player == null) return;
        SoundManager soundManager = mc.getSoundManager();
        if (!soundManager.isActive(INSTANCE)) soundManager.play(INSTANCE = new SanityAmbientSoundInstance(player));

        if (mc.getConnection() != null && mc.player != null && mc.level != null && mc.hitResult != null) {
            var text = renderGogglesOverlay(player, mc.hitResult);
            GoggleOverlayManager.updateOverlay(text, text.isEmpty() ? 0 : 1);
        }
    }

    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiLayerEvent.Post pEvent) {
        if (mc.getConnection() != null && mc.player != null && mc.level != null && pEvent.getName().equals(VanillaGuiLayers.CROSSHAIR))
            GoggleOverlayManager.render(pEvent.getGuiGraphics(), mc);
    }

    public static List<Component> renderGogglesOverlay(LocalPlayer pPlayer, HitResult pHitResult) {
        if (!pPlayer.getItemBySlot(EquipmentSlot.HEAD).is(AllTags.itemC("goggles"))) return List.of();
        if (!(pHitResult.getType() == HitResult.Type.BLOCK && pHitResult instanceof BlockHitResult pRaytrace)) return List.of();
        if (pRaytrace.getType() == HitResult.Type.MISS) return List.of();
        if (!(pPlayer.level().getBlockEntity(pRaytrace.getBlockPos()) instanceof OmniBlockEntity pBlockEntity)) return List.of();
        return pBlockEntity.getGoggleTooltipLines(pPlayer.level(), pPlayer);
    }

    public static void renderBlackScreenOfDeath(GuiGraphics pGuiGraphics, DeltaTracker tracker, LocalPlayer player) {
        var intensity = Mth.lerp(tracker.getGameTimeDeltaPartialTick(false), player.oSpinningEffectIntensity, player.spinningEffectIntensity);
        if (intensity < 1.0F) {
            intensity *= intensity;
            intensity *= intensity;
            intensity = intensity * 0.8F;
        }
        float sanity = ClientSanityData.getSanity();
        float darkness = 1f - (sanity / 100f);
        if (!(darkness > 0f || intensity > 0f)) return;
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        float both = Math.clamp(Math.max(darkness, intensity), 0f, 1f);
        float bothTint = Math.clamp(Math.max(darkness, (intensity * 0.75f)), 0f, 1f);
        int alpha = (int) (Math.clamp(bothTint, 0f, 1f) * 255f);
        int color = (alpha << 24);

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(0, 0, 1900);
        pGuiGraphics.fill(0, 0, screenWidth, screenHeight, color);
        pGuiGraphics.pose().popPose();

        pGuiGraphics.pose().pushPose();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        pGuiGraphics.pose().translate(0, 0, 2000);
        pGuiGraphics.setColor(1.0f, 1.0f, 1.0f, Mth.clamp(both * 1.5f, 0f, 1f));
        pGuiGraphics.blit(OmniUtils.loc("textures/misc/insanity.png"), 0, 0, 0, 0, screenWidth, screenHeight, screenWidth, screenHeight);
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        pGuiGraphics.pose().popPose();
    }

    public static void renderSanityBar(GuiGraphics pGuiGraphics) {
        float sanity = ClientSanityData.getSanity();
        if (sanity >= 100f) return;
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        int textureWidth = 182;
        int textureHeight = 10;
        int fullWidth = 182;
        int barHeight = 5;
        int x = screenWidth / 2 - 91;
        int y = screenHeight - 32 + 6;

        int filledWidth = (int) (Mth.clamp(sanity / 100f, 0f, 1f) * fullWidth);

        float shakeFactor = Mth.clamp(1f - (sanity / 100f), 0f, 1f);
        int maxShake = 3;
        int shakeX = (int) ((Math.random() - 0.5) * 2 * maxShake * shakeFactor);
        int shakeY = (int) ((Math.random() - 0.5) * 2 * maxShake * shakeFactor);

        int drawX = x + shakeX;
        int drawY = y + shakeY;

        pGuiGraphics.pose().pushPose();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(0, 0, 2100);
        pGuiGraphics.blit(OmniUtils.loc("textures/gui/sanity_bar.png"), drawX, drawY, 0, 0f, 0f, fullWidth, barHeight, textureWidth, textureHeight);
        pGuiGraphics.pose().popPose();

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(0, 0, 2150);
        if (filledWidth > 0) pGuiGraphics.blit(OmniUtils.loc("textures/gui/sanity_bar.png"), drawX, drawY, 0, 0f, 5f, filledWidth, barHeight, textureWidth, textureHeight);
        pGuiGraphics.pose().popPose();

        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        pGuiGraphics.pose().popPose();
    }


    @SubscribeEvent
    public static void onRenderStage(RenderLevelStageEvent pEvent) {
        if (mc.getConnection() != null && mc.player != null && mc.level != null) {
            var player = mc.player;
            var level = mc.level;
            var stage = pEvent.getStage();
            var pose = pEvent.getPoseStack();
            var cam = pEvent.getCamera();
            if (stage != AFTER_TRIPWIRE_BLOCKS) return;
            var main = player.getMainHandItem();
            var off = player.getOffhandItem();
            renderSelectionBoxAngelBlock(main, off, player, level, pose, cam);
            renderWandBlockSelection(main, off, player, level, pose, cam);
        }
    }

    public static void renderWandBlockSelection(ItemStack main, ItemStack off, LocalPlayer pPlayer, ClientLevel pLevel, PoseStack pPose, Camera pCamera) {
        if (pPlayer.isSpectator()) return;
        var hand = main.getItem() instanceof WandItem ? main : off.getItem() instanceof WandItem ? off : ItemStack.EMPTY;
        if (!(hand.getItem() instanceof WandItem wand) || hand.isEmpty()) return;
        var hit = Item.getPlayerPOVHitResult(pLevel, pPlayer, ClipContext.Fluid.NONE);
        var origin = hit.getBlockPos();
        var positions = WandItem.getConnectedBlocks(pLevel, hit.getDirection(), pPlayer, pLevel.getBlockState(origin), origin, wand);
        if (positions == null || positions.isEmpty()) return;
        if (!wand.outsideBlocks()) positions.add(origin);
        var buffer = mc.renderBuffers().bufferSource();
        if (hit.getType() != HitResult.Type.MISS) for (var pos : positions) {
            var state = pLevel.getBlockState(pos);
            var shape = state.isAir() ? Shapes.block() : state.getShape(pLevel, pos);
            highlightPosition(pos, shape, pPose, pCamera, buffer, 1f, 0.8f);
        }
    }

    public static void renderSelectionBoxAngelBlock(ItemStack main, ItemStack off, LocalPlayer pPlayer, ClientLevel pLevel, PoseStack pPose, Camera pCamera) {
        if (!(main.is(AllBlocks.ANGEL_BLOCK.asItem()) || off.is(AllBlocks.ANGEL_BLOCK.asItem()))) return;
        var hitResult = mc.hitResult;
        if (hitResult == null) return;
        if (!(hitResult instanceof BlockHitResult hit)) return;
        var scale = pPlayer.blockInteractionRange() * (pPlayer.isShiftKeyDown() ? 0.5 : 1);
        var eyePos = pPlayer.getEyePosition();
        var lookVec = eyePos.add(pPlayer.calculateViewVector(pPlayer.getXRot(), pPlayer.getYRot()).scale(scale));
        var rayTrace = pLevel.clip(new ClipContext(eyePos, lookVec, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, pPlayer));
        if (hit.getType() == HitResult.Type.MISS) {
            var pos = rayTrace.getBlockPos();
            var buffer = mc.renderBuffers().bufferSource();
            highlightPosition(pos, Shapes.block(), pPose, pCamera, buffer, 0f, 0.4f);
        }
    }

    private static void highlightPosition(BlockPos pos, VoxelShape shape, PoseStack poseStack, Camera pCamera, MultiBufferSource buffer, float shade, float alpha) {
        var camPos = pCamera.getPosition();
        renderHitOutline(poseStack, shape, buffer.getBuffer(RenderType.lines()), camPos.x(), camPos.y(), camPos.z(), pos, shade, alpha);
    }

    private static void renderHitOutline(PoseStack poseStack, VoxelShape shape, VertexConsumer consumer, double camX, double camY, double camZ, BlockPos pos, float shade, float alpha) {
        renderShape(poseStack, consumer, shape, (double)pos.getX() - camX, (double)pos.getY() - camY, (double)pos.getZ() - camZ, shade, alpha);
    }

    private static void renderShape(PoseStack poseStack, VertexConsumer consumer, VoxelShape shape, double x, double y, double z, float shade, float alpha) {
        PoseStack.Pose pose = poseStack.last();
        shape.forAllEdges((x1, y1, z1, x2, y2, z2) -> {
            float f = (float)(x2 - x1); float f1 = (float)(y2 - y1); float f2 = (float)(z2 - z1);
            float f3 = Mth.sqrt(f * f + f1 * f1 + f2 * f2);
            f /= f3; f1 /= f3; f2 /= f3;
            consumer.addVertex(pose, (float)(x1 + x), (float)(y1 + y), (float)(z1 + z))
                    .setColor(shade, shade, shade, alpha)
                    .setNormal(pose, f, f1, f2);
            consumer.addVertex(pose, (float)(x2 + x), (float)(y2 + y), (float)(z2 + z))
                    .setColor(shade, shade, shade, alpha)
                    .setNormal(pose, f, f1, f2);
        });
    }
}
