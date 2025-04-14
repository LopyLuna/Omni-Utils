package uwu.lopyluna.omni_util.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.FastColor;
import net.minecraft.world.level.Level;
import org.joml.Matrix4f;
import uwu.lopyluna.omni_util.OmniUtils;
import uwu.lopyluna.omni_util.content.AllUtils;

import java.awt.*;

public class ColoredBlockRendering {

    public static void renderRGBBlock(Level level, BlockPos pos, String name, PoseStack poseStack, MultiBufferSource buffer, int overlay, int color) {
        var BLACK_TEXTURE = OmniUtils.loc("textures/block/" + name + "_black.png");
        var RED_TEXTURE = OmniUtils.loc("textures/block/" + name + "_red.png");
        var GREEN_TEXTURE = OmniUtils.loc("textures/block/" + name + "_green.png");
        var BLUE_TEXTURE = OmniUtils.loc("textures/block/" + name + "_blue.png");
        var YELLOW_TEXTURE = OmniUtils.loc("textures/block/" + name + "_yellow.png");
        var CYAN_TEXTURE = OmniUtils.loc("textures/block/" + name + "_teal.png");
        var MAGENTA_TEXTURE = OmniUtils.loc("textures/block/" + name + "_pink.png");
        var WHITE_TEXTURE = OmniUtils.loc("textures/block/" + name + "_white.png");

        float[] hsv = new float[3];
        Color.RGBtoHSB(FastColor.ARGB32.red(color), FastColor.ARGB32.green(color), FastColor.ARGB32.blue(color), hsv);
        float hue = hsv[0] * 360f;
        float saturation = hsv[1];
        float value = hsv[2];

        float brightness = 1.0f - saturation;
        float darkness = 1.0f - value;

        float yellow = 0f, green = 0f, cyan = 0f, blue = 0f, magenta = 0f, redEnd = 0f;

        if (hue >= 0f && hue < 60f) { yellow = (hue - 0f) / 60f; }
        else if (hue >= 60f && hue < 120f) { green = (hue - 60f) / 60f; yellow = 1f - green; }
        else if (hue >= 120f && hue < 180f) { cyan = (hue - 120f) / 60f; green = 1f - cyan; }
        else if (hue >= 180f && hue < 240f) { blue = (hue - 180f) / 60f; cyan = 1f - blue; }
        else if (hue >= 240f && hue < 300f) { magenta = (hue - 240f) / 60f; blue = 1f - magenta; }
        else { redEnd = (hue - 300f) / 60f; magenta = 1f - redEnd; }

        renderQuad(level, pos, poseStack, buffer.getBuffer(RenderType.entityTranslucentCull(RED_TEXTURE)), 1f, overlay);
        renderQuad(level, pos, poseStack, buffer.getBuffer(RenderType.entityTranslucentCull(YELLOW_TEXTURE)), yellow, overlay);
        renderQuad(level, pos, poseStack, buffer.getBuffer(RenderType.entityTranslucentCull(GREEN_TEXTURE)), green, overlay);
        renderQuad(level, pos, poseStack, buffer.getBuffer(RenderType.entityTranslucentCull(CYAN_TEXTURE)), cyan, overlay);
        renderQuad(level, pos, poseStack, buffer.getBuffer(RenderType.entityTranslucentCull(BLUE_TEXTURE)), blue, overlay);
        renderQuad(level, pos, poseStack, buffer.getBuffer(RenderType.entityTranslucentCull(MAGENTA_TEXTURE)), magenta, overlay);
        renderQuad(level, pos, poseStack, buffer.getBuffer(RenderType.entityTranslucentCull(RED_TEXTURE)), redEnd, overlay);

        if (brightness > 0.01f) renderQuad(level, pos, poseStack, buffer.getBuffer(RenderType.entityTranslucentCull(WHITE_TEXTURE)), brightness, overlay);
        if (darkness > 0.01f) renderQuad(level, pos, poseStack, buffer.getBuffer(RenderType.entityTranslucentCull(BLACK_TEXTURE)), darkness, overlay);
    }

    private static void renderQuad(Level level, BlockPos pos, PoseStack poseStack, VertexConsumer consumer, float alpha, int overlay) {
        var matrix = poseStack.last().pose();
        var normal = poseStack.last();

        for (var dir : Direction.values()) {
            var relative = pos.relative(dir);
            if (!level.getBlockState(relative).isFaceSturdy(level, relative, dir.getOpposite())) renderQuadFace(matrix, normal, consumer, dir, alpha, AllUtils.getClientLight(level, relative), overlay);
        }
    }

    private static void renderQuadFace(Matrix4f matrix, PoseStack. Pose normal, VertexConsumer consumer, Direction direction, float alpha, int light, int overlay) {
        float min = 0f;
        float max = 1f;
        switch (direction) {
            case UP -> {
                consumer.addVertex(matrix, min, max, min).setColor(1f, 1f, 1f, alpha).setUv(0f, 0f).setOverlay(overlay).setLight(light).setNormal(normal, 0f, 1f, 0f);
                consumer.addVertex(matrix, min, max, max).setColor(1f, 1f, 1f, alpha).setUv(0f, 1f).setOverlay(overlay).setLight(light).setNormal(normal, 0f, 1f, 0f);
                consumer.addVertex(matrix, max, max, max).setColor(1f, 1f, 1f, alpha).setUv(1f, 1f).setOverlay(overlay).setLight(light).setNormal(normal, 0f, 1f, 0f);
                consumer.addVertex(matrix, max, max, min).setColor(1f, 1f, 1f, alpha).setUv(1f, 0f).setOverlay(overlay).setLight(light).setNormal(normal, 0f, 1f, 0f);
            }
            case DOWN -> {
                consumer.addVertex(matrix, min, min, min).setColor(1f, 1f, 1f, alpha).setUv(0f, 0f).setOverlay(overlay).setLight(light).setNormal(normal, 0f, -1f, 0f);
                consumer.addVertex(matrix, max, min, min).setColor(1f, 1f, 1f, alpha).setUv(1f, 0f).setOverlay(overlay).setLight(light).setNormal(normal, 0f, -1f, 0f);
                consumer.addVertex(matrix, max, min, max).setColor(1f, 1f, 1f, alpha).setUv(1f, 1f).setOverlay(overlay).setLight(light).setNormal(normal, 0f, -1f, 0f);
                consumer.addVertex(matrix, min, min, max).setColor(1f, 1f, 1f, alpha).setUv(0f, 1f).setOverlay(overlay).setLight(light).setNormal(normal, 0f, -1f, 0f);
            }
            case NORTH -> {
                consumer.addVertex(matrix, min, min, min).setColor(1f, 1f, 1f, alpha).setUv(0f, 0f).setOverlay(overlay).setLight(light).setNormal(normal, 0f, 0f, -1f);
                consumer.addVertex(matrix, min, max, min).setColor(1f, 1f, 1f, alpha).setUv(0f, 1f).setOverlay(overlay).setLight(light).setNormal(normal, 0f, 0f, -1f);
                consumer.addVertex(matrix, max, max, min).setColor(1f, 1f, 1f, alpha).setUv(1f, 1f).setOverlay(overlay).setLight(light).setNormal(normal, 0f, 0f, -1f);
                consumer.addVertex(matrix, max, min, min).setColor(1f, 1f, 1f, alpha).setUv(1f, 0f).setOverlay(overlay).setLight(light).setNormal(normal, 0f, 0f, -1f);
            }
            case SOUTH -> {
                consumer.addVertex(matrix, max, min, max).setColor(1f, 1f, 1f, alpha).setUv(0f, 0f).setOverlay(overlay).setLight(light).setNormal(normal, 0f, 0f, 1f);
                consumer.addVertex(matrix, max, max, max).setColor(1f, 1f, 1f, alpha).setUv(0f, 1f).setOverlay(overlay).setLight(light).setNormal(normal, 0f, 0f, 1f);
                consumer.addVertex(matrix, min, max, max).setColor(1f, 1f, 1f, alpha).setUv(1f, 1f).setOverlay(overlay).setLight(light).setNormal(normal, 0f, 0f, 1f);
                consumer.addVertex(matrix, min, min, max).setColor(1f, 1f, 1f, alpha).setUv(1f, 0f).setOverlay(overlay).setLight(light).setNormal(normal, 0f, 0f, 1f);
            }
            case WEST -> {
                consumer.addVertex(matrix, min, min, max).setColor(1f, 1f, 1f, alpha).setUv(0f, 0f).setOverlay(overlay).setLight(light).setNormal(normal, -1f, 0f, 0f);
                consumer.addVertex(matrix, min, max, max).setColor(1f, 1f, 1f, alpha).setUv(0f, 1f).setOverlay(overlay).setLight(light).setNormal(normal, -1f, 0f, 0f);
                consumer.addVertex(matrix, min, max, min).setColor(1f, 1f, 1f, alpha).setUv(1f, 1f).setOverlay(overlay).setLight(light).setNormal(normal, -1f, 0f, 0f);
                consumer.addVertex(matrix, min, min, min).setColor(1f, 1f, 1f, alpha).setUv(1f, 0f).setOverlay(overlay).setLight(light).setNormal(normal, -1f, 0f, 0f);
            }
            case EAST -> {
                consumer.addVertex(matrix, max, min, min).setColor(1f, 1f, 1f, alpha).setUv(0f, 0f).setOverlay(overlay).setLight(light).setNormal(normal, 1f, 0f, 0f);
                consumer.addVertex(matrix, max, max, min).setColor(1f, 1f, 1f, alpha).setUv(0f, 1f).setOverlay(overlay).setLight(light).setNormal(normal, 1f, 0f, 0f);
                consumer.addVertex(matrix, max, max, max).setColor(1f, 1f, 1f, alpha).setUv(1f, 1f).setOverlay(overlay).setLight(light).setNormal(normal, 1f, 0f, 0f);
                consumer.addVertex(matrix, max, min, max).setColor(1f, 1f, 1f, alpha).setUv(1f, 0f).setOverlay(overlay).setLight(light).setNormal(normal, 1f, 0f, 0f);
            }
        }
    }
}
