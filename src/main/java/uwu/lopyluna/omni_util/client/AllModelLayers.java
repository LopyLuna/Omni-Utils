package uwu.lopyluna.omni_util.client;

import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import uwu.lopyluna.omni_util.OmniUtils;

public class AllModelLayers {
    public static final ModelLayerLocation ANGEL_RING_WINGS = new ModelLayerLocation(
            OmniUtils.loc("angel_ring_wings"), "main");

    public static LayerDefinition createWingLayer() {
        return ElytraModel.createLayer();
    }
}
