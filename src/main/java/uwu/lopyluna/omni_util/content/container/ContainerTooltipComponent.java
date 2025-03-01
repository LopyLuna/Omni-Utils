package uwu.lopyluna.omni_util.content.container;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import uwu.lopyluna.omni_util.content.container.bundle_of_holding.BundleOfHoldingContainer;

public record ContainerTooltipComponent(BundleOfHoldingContainer contents) implements TooltipComponent {
}
