package uwu.lopyluna.omni_util.content.container;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.omni_util.content.container.bundle_of_holding.BundleOfHoldingContainer;

@OnlyIn(Dist.CLIENT)
public class ContainerTooltip implements ClientTooltipComponent {
    private static final ResourceLocation BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/background");
    private final BundleOfHoldingContainer contents;

    public ContainerTooltip(BundleOfHoldingContainer contents) {
        this.contents = contents;
    }

    @Override
    public int getHeight() {
        return this.backgroundHeight() + 4;
    }

    @Override
    public int getWidth(@NotNull Font font) {
        return this.backgroundWidth();
    }

    private int backgroundWidth() {
        return this.gridSizeX() * 18 + 2;
    }

    private int backgroundHeight() {
        return this.gridSizeY() * 20 + 2;
    }

    @Override
    public void renderImage(@NotNull Font font, int x, int y, GuiGraphics guiGraphics) {
        int i = this.gridSizeX();
        int j = this.gridSizeY();
        guiGraphics.blitSprite(BACKGROUND_SPRITE, x, y, this.backgroundWidth(), this.backgroundHeight());
        boolean flag = this.contents.getContentSize() >= 0;
        int k = 0;

        for (int l = 0; l < j; l++) for (int i1 = 0; i1 < i; i1++) {
            int j1 = x + i1 * 18 + 1;
            int k1 = y + l * 20 + 1;
            this.renderSlot(j1, k1, k++, flag, guiGraphics, font);
        }
    }

    private void renderSlot(int x, int y, int itemIndex, boolean isBundleFull, GuiGraphics guiGraphics, Font font) {
        if (itemIndex >= this.contents.getItems().size()) this.blit(guiGraphics, x, y, isBundleFull ? ContainerTooltip.Texture.BLOCKED_SLOT : ContainerTooltip.Texture.SLOT);
        else {
            ItemStack itemstack = this.contents.getItem(itemIndex);
            this.blit(guiGraphics, x, y, ContainerTooltip.Texture.SLOT);
            guiGraphics.renderItem(itemstack, x + 1, y + 1, itemIndex);
            guiGraphics.renderItemDecorations(font, itemstack, x + 1, y + 1);
        }
    }

    private void blit(GuiGraphics guiGraphics, int x, int y, ContainerTooltip.Texture texture) {
        guiGraphics.blitSprite(texture.sprite, x, y, 0, texture.w, texture.h);
    }

    private int gridSizeX() {
        return Math.max(2, (int)Math.ceil(Math.sqrt((double)this.contents.getItems().size() + 1.0)));
    }

    private int gridSizeY() {
        return (int)Math.ceil(((double)this.contents.getItems().size() + 1.0) / (double)this.gridSizeX());
    }

    @OnlyIn(Dist.CLIENT)
    enum Texture {
        BLOCKED_SLOT(ResourceLocation.withDefaultNamespace("container/bundle/blocked_slot"), 18, 20),
        SLOT(ResourceLocation.withDefaultNamespace("container/bundle/slot"), 18, 20);

        public final ResourceLocation sprite;
        public final int w;
        public final int h;

        Texture(ResourceLocation sprite, int w, int h) {
            this.sprite = sprite;
            this.w = w;
            this.h = h;
        }
    }
}