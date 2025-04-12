package uwu.lopyluna.omni_util.content.managers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;

import java.util.List;

public class GoggleOverlayManager {
    private static final int FADE_DURATION = 15;

    private static int fadeTicks = 0;
    private static List<Component> displayText = List.of();

    public static void updateOverlay(List<Component> text, int type) {
        fadeTicks = Mth.clamp(type > 0 ? fadeTicks+1 : fadeTicks-1, 0, FADE_DURATION);
        if (!text.isEmpty()) displayText = text;
    }

    public static void render(GuiGraphics guiGraphics, Minecraft mc) {
        if (fadeTicks <= 0) return;
        float alpha = (float) fadeTicks / FADE_DURATION;

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        int yOffset = (int) ((float) screenHeight / 2f + 20f - (Mth.clamp(alpha * 2, 0, 1) * 10f));

        int i = 0;
        if (!displayText.isEmpty()) for (Component component : displayText) {
            var textColor = component.getStyle().getColor();
            var color = textColor != null ? FastColor.ARGB32.color((int) (alpha * 255f), textColor.getValue()) : FastColor.ARGB32.color((int) (alpha * 255f), 255, 255, 255);
            guiGraphics.drawCenteredString(mc.font, component, screenWidth / 2, yOffset + (i * 10), color);
            i++;
        }
    }
}
