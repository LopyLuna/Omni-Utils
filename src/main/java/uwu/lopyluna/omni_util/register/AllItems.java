package uwu.lopyluna.omni_util.register;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import uwu.lopyluna.omni_util.OmniUtils;
import uwu.lopyluna.omni_util.content.items.*;
import uwu.lopyluna.omni_util.content.items.hexa_ingot.UnstableHexaIngot;
import uwu.lopyluna.omni_util.content.items.hexa_ingot.UnstableHexaNugget;
import uwu.lopyluna.omni_util.content.utils.datagen.ModelHelper;

import static net.minecraft.world.item.Tiers.NETHERITE;
import static uwu.lopyluna.omni_util.OmniUtils.REG;

@SuppressWarnings("unused")
public class AllItems {

    public static final ItemEntry<PaxelItem> SIMPLE_PAXEL = REG.item("simple_paxel", p -> new PaxelItem(NETHERITE, p))
            .lang("Simple Paxel")
            .register();

    public static final ItemEntry<UnstableHexaIngot> UNSTABLE_HEXA_INGOT = REG.item("unstable_hexa_ingot", UnstableHexaIngot::new)
            .lang("Unstable Hexa Ingot")
            .register();

    public static final ItemEntry<UnstableHexaNugget> UNSTABLE_HEXA_NUGGET = REG.item("unstable_hexa_nugget", UnstableHexaNugget::new)
            .lang("Unstable Hexa Nugget")
            .register();

    public static final ItemEntry<Item> HEXA_INGOT = REG.item("hexa_ingot", Item::new)
            .lang("Hexa Ingot")
            .register();

    public static final ItemEntry<Item> HEXA_NUGGET = REG.item("hexa_nugget", Item::new)
            .lang("Hexa Nugget")
            .register();

    public static final ItemEntry<Item> BLANK_TABLET = REG.item("blank_tablet", Item::new)
            .lang("Blank Tablet")
            .register();

    public static final ItemEntry<HexSigilItem> HEX_SIGIL = REG.item("hex_sigil", HexSigilItem::new)
            .lang("Hex Sigil")
            .register();

    public static final ItemEntry<Item> WITHERED_TEAR = REG.item("withered_tear", Item::new)
            .lang("Withered Tear")
            .register();

    public static final ItemEntry<GogglesItem> GOGGLES = REG.item("goggles", GogglesItem::new)
            .lang("Goggles")
            .register();
    public static final ItemEntry<SonarGogglesItem> SONAR_GOGGLES = REG.item("sonar_goggles", SonarGogglesItem::new)
            .lang("Sonar Goggles")
            .register();

    public static final ItemEntry<WateringCanItem> WATERING_CAN = REG.item("watering_can", WateringCanItem::new)
            .lang("Watering Can")
            .model(ModelHelper::forwardItem)
            .register();

    public static final ItemEntry<EmptyWateringCanItem> EMPTY_WATERING_CAN = REG.item("empty_watering_can", EmptyWateringCanItem::new)
            .properties(p -> p.stacksTo(1))
            .lang("Empty Watering Can")
            .model(ModelHelper::forwardItem)
            .register();


    public static final ItemEntry<BundleOfHoldingItem> BUNDLE_OF_HOLDING = REG.item("bundle_of_holding", BundleOfHoldingItem::new)
            .properties(p -> p.fireResistant().rarity(Rarity.UNCOMMON))
            .lang("Bundle of Holding")
            .properties(p -> p.stacksTo(1))
            .model((c, p) -> p.basicItem(c.get()).override().model(p.basicItem(OmniUtils.loc("bundle_of_holding_open"))).predicate(OmniUtils.loc("open"), 1.0f).end())
            .register();

    public static final ItemEntry<AngelRing> ANGEL_RING = REG.item("angel_ring", AngelRing::new)
            .properties(p -> p.fireResistant().rarity(Rarity.UNCOMMON))
            .lang("Angel Ring")
            .properties(p -> p.stacksTo(1))
            .model((c, p) -> p.basicItem(c.get())
                    .override().model(p.basicItem(OmniUtils.loc("bat_angel_ring"))).predicate(OmniUtils.loc("wing_type"), 1.0f).end()
                    .override().model(p.basicItem(OmniUtils.loc("demon_angel_ring"))).predicate(OmniUtils.loc("wing_type"), 2.0f).end()
                    .override().model(p.basicItem(OmniUtils.loc("feathered_angel_ring"))).predicate(OmniUtils.loc("wing_type"), 3.0f).end()
                    .override().model(p.basicItem(OmniUtils.loc("gilded_angel_ring"))).predicate(OmniUtils.loc("wing_type"), 4.0f).end()
                    .override().model(p.basicItem(OmniUtils.loc("invisible_angel_ring"))).predicate(OmniUtils.loc("wing_type"), 5.0f).end())
            .register();

    public static void register() {}
}
