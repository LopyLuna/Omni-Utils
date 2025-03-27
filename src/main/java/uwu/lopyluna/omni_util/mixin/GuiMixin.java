package uwu.lopyluna.omni_util.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import uwu.lopyluna.omni_util.register.AllBlocks;
import uwu.lopyluna.omni_util.register.worldgen.AllDimensions;

@Mixin(Gui.class)
public class GuiMixin {

    @Shadow @Final private Minecraft minecraft;

    @WrapOperation(method = "renderCameraOverlays(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderPortalOverlay(Lnet/minecraft/client/gui/GuiGraphics;F)V"))
    private void renderCameraOverlays(Gui instance, GuiGraphics orignalGuiGraphics, float alpha, Operation<Void> original) {
        var player = minecraft.player;
        assert player != null;
        var ignore = false;
        if (player.portalProcess != null) ignore = player.portalProcess.isSamePortal(AllBlocks.GRIMSPIRAL.get()) || player.level().dimension().equals(AllDimensions.GRIMSPIRE);
        if (!ignore) original.call(instance, orignalGuiGraphics, alpha);
    }
}
