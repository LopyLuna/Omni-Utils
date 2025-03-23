package uwu.lopyluna.omni_util.events;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import uwu.lopyluna.omni_util.OmniUtils;
import uwu.lopyluna.omni_util.client.ClientSanityData;
import uwu.lopyluna.omni_util.client.SanityAmbientSoundInstance;
import uwu.lopyluna.omni_util.register.worldgen.AllBlocks;

import static net.neoforged.neoforge.client.event.RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS;

@EventBusSubscriber(modid = OmniUtils.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class WorldClientEvents {
    private static final Minecraft mc = Minecraft.getInstance();

    public static SanityAmbientSoundInstance INSTANCE = null;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        var player = mc.player;
        if (player == null) return;
        SoundManager soundManager = mc.getSoundManager();
        if (!soundManager.isActive(INSTANCE)) soundManager.play(INSTANCE = new SanityAmbientSoundInstance(player));
    }


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
            renderBlackScreenOfDeath(pEvent.getGuiGraphics());
            renderSanityBar(pEvent.getGuiGraphics());
        }
    }

    public static void renderBlackScreenOfDeath(GuiGraphics pGuiGraphics) {
        float sanity = ClientSanityData.getSanity();
        float darkness = 1f - (sanity / 100f);
        if (darkness <= 0f) return;
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        int alpha = (int) (darkness * 255);
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
        pGuiGraphics.setColor(1.0f, 1.0f, 1.0f, Mth.clamp(darkness * 1.5f, 0f, 1f));
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
            renderSelectionBoxAngelBlock(pEvent.getStage(), player, level, pEvent.getPoseStack(), pEvent.getCamera());
        }
    }

    public static void renderSelectionBoxAngelBlock(RenderLevelStageEvent.Stage pStage, LocalPlayer pPlayer, ClientLevel pLevel, PoseStack pPose, Camera pCamera) {
        if (pStage == AFTER_TRIPWIRE_BLOCKS) {
            if (!AllBlocks.ANGEL_BLOCK.is(pPlayer.getMainHandItem()) && !AllBlocks.ANGEL_BLOCK.is(pPlayer.getOffhandItem())) return;

            var scale = pPlayer.blockInteractionRange() * (pPlayer.isShiftKeyDown() ? 0.5 : 1);
            Vec3 vec3 = pPlayer.getEyePosition();
            Vec3 vec31 = vec3.add(pPlayer.calculateViewVector(pPlayer.getXRot(), pPlayer.getYRot()).scale(scale));
            var rayTrace = pLevel.clip(new ClipContext(vec3, vec31, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, pPlayer));

            var pos = rayTrace.getBlockPos();
            if (rayTrace.getType() == HitResult.Type.MISS && mc.hitResult != null && mc.hitResult.getType() == HitResult.Type.MISS) {
                pPose.pushPose();
                var buffer = mc.renderBuffers().bufferSource().getBuffer(RenderType.lines());
                var camPos = pCamera.getPosition();

                var x = (double) pos.getX() - camPos.x();
                var y = (double) pos.getY() - camPos.y();
                var z = (double) pos.getZ() - camPos.z();
                var posStack = pPose.last();
                Shapes.block().forAllEdges((x1, y1, z1, x2, y2, z2) -> {
                    float f = (float) (x2 - x1);
                    float f1 = (float) (y2 - y1);
                    float f2 = (float) (z2 - z1);
                    float f3 = Mth.sqrt(f * f + f1 * f1 + f2 * f2);
                    f /= f3;
                    f1 /= f3;
                    f2 /= f3;
                    buffer.addVertex(posStack, (float) (x1 + x), (float) (y1 + y), (float) (z1 + z))
                            .setColor(0, 0, 0, 0.4f)
                            .setNormal(posStack, f, f1, f2);
                    buffer.addVertex(posStack, (float) (x2 + x), (float) (y2 + y), (float) (z2 + z))
                            .setColor(0, 0, 0, 0.4f)
                            .setNormal(posStack, f, f1, f2);
                });
                pPose.popPose();
            }
        }
    }
}
