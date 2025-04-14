package uwu.lopyluna.omni_util.client.render.angel_ring;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import uwu.lopyluna.omni_util.OmniUtils;

public class AngelRingWingsModel<T extends LivingEntity> extends ElytraModel<T> {
    private static final ResourceLocation DEFAULT_WINGS = OmniUtils.loc("textures/entity/wings/feathered.png");
    private final ModelPart rightWing;
    private final ModelPart leftWing;

    public AngelRingWingsModel(ModelPart root) {
        super(root);
        this.leftWing = root.getChild("left_wing");
        this.rightWing = root.getChild("right_wing");
    }

    public void renderLeftWing(PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay, int color, ResourceLocation wingTexture) {
        RenderType renderType = RenderType.entityCutoutNoCull(wingTexture != null ? wingTexture : DEFAULT_WINGS);
        this.leftWing.render(poseStack, bufferSource.getBuffer(renderType), light, overlay, color);
    }

    public void renderRightWing(PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay, int color, ResourceLocation wingTexture) {
        RenderType renderType = RenderType.entityCutoutNoCull(wingTexture != null ? wingTexture : DEFAULT_WINGS);
        this.rightWing.render(poseStack, bufferSource.getBuffer(renderType), light, overlay, color);
    }
}