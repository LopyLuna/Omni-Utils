package uwu.lopyluna.omni_util.register;

import net.minecraft.world.item.Rarity;
import uwu.lopyluna.omni_util.OmniUtils;
import uwu.lopyluna.omni_util.content.items.AngelRing;
import uwu.lopyluna.omni_util.content.items.BundleOfHoldingItem;
import uwu.lopyluna.omni_util.content.utils.entry.ItemEntry;

import static uwu.lopyluna.omni_util.OmniUtils.REG;

@SuppressWarnings("unused")
public class AllItems {

    public static final ItemEntry<BundleOfHoldingItem> BUNDLE_OF_HOLDING = REG.item("bundle_of_holding", BundleOfHoldingItem::new)
            .properties(p -> p.fireResistant().rarity(Rarity.UNCOMMON))
            .lang("Bundle of Holding")
            .properties(p -> p.stacksTo(1))
            .model((p, c) -> p.basicItem(c).override().model(p.basicItem(OmniUtils.loc("bundle_of_holding_open"))).predicate(OmniUtils.loc("open"), 1.0f).end())
            .register();

    public static final ItemEntry<AngelRing> ANGEL_RING = REG.item("angel_ring", AngelRing::new)
            .properties(p -> p.fireResistant().rarity(Rarity.UNCOMMON))
            .lang("Angel Ring")
            .properties(p -> p.stacksTo(1))
            .model((p, c) -> p.basicItem(c)
                    .override().model(p.basicItem(OmniUtils.loc("bat_angel_ring"))).predicate(OmniUtils.loc("wing_type"), 1.0f).end()
                    .override().model(p.basicItem(OmniUtils.loc("demon_angel_ring"))).predicate(OmniUtils.loc("wing_type"), 2.0f).end()
                    .override().model(p.basicItem(OmniUtils.loc("feathered_angel_ring"))).predicate(OmniUtils.loc("wing_type"), 3.0f).end()
                    .override().model(p.basicItem(OmniUtils.loc("gilded_angel_ring"))).predicate(OmniUtils.loc("wing_type"), 4.0f).end()
                    .override().model(p.basicItem(OmniUtils.loc("invisible_angel_ring"))).predicate(OmniUtils.loc("wing_type"), 5.0f).end())
            .noTab()
            .register();

    public static void register() {}
}
