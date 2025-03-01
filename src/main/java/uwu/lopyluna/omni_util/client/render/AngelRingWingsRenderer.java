package uwu.lopyluna.omni_util.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import uwu.lopyluna.omni_util.OmniUtils;
import uwu.lopyluna.omni_util.content.items.AngelRing;

public class AngelRingWingsRenderer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private final AngelRingWingsModel<AbstractClientPlayer> wingsModel;

    private static final ResourceLocation FEATHERED_WINGS = OmniUtils.loc("textures/entity/wings/feathered.png");
    private static final ResourceLocation ANGEL_WINGS = OmniUtils.loc("textures/entity/wings/angel.png");
    private static final ResourceLocation DEMON_WINGS = OmniUtils.loc("textures/entity/wings/demon.png");
    private static final ResourceLocation GILDED_WINGS = OmniUtils.loc("textures/entity/wings/gilded.png");
    private static final ResourceLocation BAT_WINGS = OmniUtils.loc("textures/entity/wings/bat.png");

    public AngelRingWingsRenderer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.wingsModel = new AngelRingWingsModel<>(modelSet.bakeLayer(ModelLayers.ELYTRA));
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int light, @NotNull AbstractClientPlayer player,
                       float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        if (!shouldRenderWings(player)) return;

        ResourceLocation wingTexture = getWingTexture(player);
        if (wingTexture == null) return;

        poseStack.pushPose();

        poseStack.translate(0.0D, 0.0D, 0.1D);
        poseStack.mulPose(new Quaternionf().rotateX(-((float) Math.PI / 8)).rotateY((float) Math.PI).rotateZ(0));
        poseStack.scale(1.0f, 1.0f, 1.0f);

        float flying = player.getAbilities().flying ? 1f : 0f;

        float flap = (float) Math.sin(ageInTicks * 0.1f) * 12.5f;
        float xRot = 1.0f;
        float yRot = 0.9f + ((float) Math.toRadians(flap) * 3f * flying);
        float zRot = -0.5f + ((float) Math.toRadians(flap) * flying);

        if (player.isCrouching()) {
            xRot += 0.5F;
            yRot *= 0.6F;
            zRot *= 1.2F;
            poseStack.translate(0.0D, 0.15D, -0.05D);
        }

        Quaternionf leftWingRotation = new Quaternionf().rotateX(-xRot).rotateY(yRot).rotateZ(zRot);
        Quaternionf rightWingRotation = new Quaternionf().rotateX(-xRot).rotateY(-yRot).rotateZ(-zRot);

        poseStack.pushPose();
        poseStack.translate(0.05D, 0.0D, 0.0D);
        poseStack.mulPose(leftWingRotation);
        wingsModel.renderLeftWing(poseStack, bufferSource, light, OverlayTexture.NO_OVERLAY, -1, wingTexture);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(-0.05D, 0.0D, 0.0D);
        poseStack.mulPose(rightWingRotation);
        wingsModel.renderRightWing(poseStack, bufferSource, light, OverlayTexture.NO_OVERLAY, -1, wingTexture);
        poseStack.popPose();

        poseStack.popPose();
    }

    private boolean shouldRenderWings(AbstractClientPlayer player) {
        return player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof AngelRing;
    }

    private ResourceLocation getWingTexture(AbstractClientPlayer player) {
        ItemStack ring = player.getItemBySlot(EquipmentSlot.CHEST);
        if (!(ring.getItem() instanceof AngelRing angelRing)) return null;

        return switch (angelRing.getWingType(ring)) {
            case "feathered" -> FEATHERED_WINGS;
            case "angel" -> ANGEL_WINGS;
            case "demon" -> DEMON_WINGS;
            case "gilded" -> GILDED_WINGS;
            case "bat" -> BAT_WINGS;
            default -> null;
        };
    }
}