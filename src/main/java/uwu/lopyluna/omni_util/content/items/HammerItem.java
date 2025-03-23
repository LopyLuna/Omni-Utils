package uwu.lopyluna.omni_util.content.items;

import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import uwu.lopyluna.omni_util.register.AllTags;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class HammerItem extends DiggerItem {
    public HammerItem(Tier tier, Properties properties) {
        super(tier, AllTags.MINEABLE_WITH_HAMMER, properties);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.neoforged.neoforge.common.ItemAbility itemAbility) {
        return AllItemAbilities.DEFAULT_HAMMER_ACTIONS.contains(itemAbility);
    }
}
