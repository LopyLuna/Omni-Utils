package uwu.lopyluna.omni_util.content.items;

import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import uwu.lopyluna.omni_util.register.AllTags;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ScytheItem extends DiggerItem {
    public ScytheItem(Tier tier, Properties properties) {
        super(tier, AllTags.MINEABLE_WITH_SCYTHE, properties);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.neoforged.neoforge.common.ItemAbility itemAbility) {
        return AllItemAbilities.DEFAULT_SCYTHE_ACTIONS.contains(itemAbility);
    }
}
