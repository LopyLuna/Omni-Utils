package uwu.lopyluna.omni_util.content.managers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;

import static uwu.lopyluna.omni_util.client.ClientRPData.getCachedRP;

public class GoggleOverlayManager {
    private static final int FADE_DURATION = 15;

    private static int fadeTicks = 0;
    private static String displayText = "";

    public static void updateOverlay(String text, int type) {
        fadeTicks = Mth.clamp(type > 0 ? fadeTicks+1 : fadeTicks-1, 0, FADE_DURATION);
        if (!text.isEmpty()) displayText = text;
    }

    public static void render(GuiGraphics guiGraphics, Minecraft mc) {
        if (fadeTicks <= 0) return;
        float alpha = (float) fadeTicks / FADE_DURATION;

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        int yOffset = (int) ((float) screenHeight / 2f + 20f - (Mth.clamp(alpha * 2, 0, 1) * 10f));

        guiGraphics.drawCenteredString(mc.font, displayText, screenWidth / 2, yOffset, FastColor.ARGB32.color((int) (alpha * 255f), 255, 255, 255));
        guiGraphics.drawCenteredString(mc.font, "Net: " + getCachedRP() + " RP", screenWidth / 2, yOffset + 10, FastColor.ARGB32.color((int) (alpha * 255f), 255, 255, 255));
    }
}
