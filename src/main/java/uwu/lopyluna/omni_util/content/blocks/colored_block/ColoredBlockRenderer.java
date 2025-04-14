package uwu.lopyluna.omni_util.content.blocks.colored_block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.FastColor;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.omni_util.client.render.ColoredBlockRendering;

public class ColoredBlockRenderer implements BlockEntityRenderer<ColoredBE> {

    @SuppressWarnings("unused")
    public ColoredBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(ColoredBE be, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int light, int overlay) {
        var level = be.getLevel();
        if (level == null) return;
        var pos = be.getBlockPos();
        poseStack.pushPose();
        ColoredBlockRendering.renderRGBBlock(level, pos, "colored_block", poseStack, buffer, overlay, FastColor.ARGB32.color(255, be.colorR, be.colorG, be.colorB));
        poseStack.popPose();
    }
}
