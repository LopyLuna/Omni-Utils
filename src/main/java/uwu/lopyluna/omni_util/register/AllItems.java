package uwu.lopyluna.omni_util.register;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import uwu.lopyluna.omni_util.OmniUtils;
import uwu.lopyluna.omni_util.content.items.*;
import uwu.lopyluna.omni_util.content.items.hexa_ingot.UnstableHexaIngot;
import uwu.lopyluna.omni_util.content.items.hexa_ingot.UnstableHexaNugget;
import uwu.lopyluna.omni_util.content.utils.datagen.ModelHelper;

import static net.minecraft.world.item.Tiers.*;
import static uwu.lopyluna.omni_util.OmniUtils.REG;

@SuppressWarnings("unused")
public class AllItems {

    public static final ItemEntry<ScytheItem> NETHERITE_SCYTHE = REG.item("netherite_scythe", p -> new ScytheItem(3, 24, NETHERITE, p))
            .lang("Netherite Scythe")
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<ScytheItem> DIAMOND_SCYTHE = REG.item("diamond_scythe", p -> new ScytheItem(3, 24, DIAMOND, p))
            .lang("Diamond Scythe")
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<ScytheItem> EMERALD_SCYTHE = REG.item("emerald_scythe", p -> new ScytheItem(2, 16, DIAMOND, p))
            .lang("Emerald Scythe")
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<ScytheItem> IRON_SCYTHE = REG.item("iron_scythe", p -> new ScytheItem(2, 16, IRON, p))
            .lang("Iron Scythe")
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<ScytheItem> GOLDEN_SCYTHE = REG.item("golden_scythe", p -> new ScytheItem(1, 12, GOLD, p))
            .lang("Golden Scythe")
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<ScytheItem> COPPER_SCYTHE = REG.item("copper_scythe", p -> new ScytheItem(2, 12, STONE, p))
            .lang("Copper Scythe")
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<ScytheItem> STONE_SCYTHE = REG.item("stone_scythe", p -> new ScytheItem(1, 8, STONE, p))
            .lang("Stone Scythe")
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<ScytheItem> WOODEN_SCYTHE = REG.item("wooden_scythe", p -> new ScytheItem(1, 8, WOOD, p))
            .lang("Wooden Scythe")
            .model((c, p) -> p.handheldItem(c.get()))
            .register();

    public static final ItemEntry<PaxelItem> NETHERITE_PAXEL = REG.item("netherite_paxel", p -> new PaxelItem(NETHERITE, p))
            .lang("Netherite Paxel")
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<PaxelItem> DIAMOND_PAXEL = REG.item("diamond_paxel", p -> new PaxelItem(DIAMOND, p))
            .lang("Diamond Paxel")
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<PaxelItem> EMERALD_PAXEL = REG.item("emerald_paxel", p -> new PaxelItem(DIAMOND, p))
            .lang("Emerald Paxel")
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<PaxelItem> IRON_PAXEL = REG.item("iron_paxel", p -> new PaxelItem(IRON, p))
            .lang("Iron Paxel")
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<PaxelItem> GOLDEN_PAXEL = REG.item("golden_paxel", p -> new PaxelItem(GOLD, p))
            .lang("Golden Paxel")
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<PaxelItem> COPPER_PAXEL = REG.item("copper_paxel", p -> new PaxelItem(STONE, p))
            .lang("Copper Paxel")
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<PaxelItem> STONE_PAXEL = REG.item("stone_paxel", p -> new PaxelItem(STONE, p))
            .lang("Stone Paxel")
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<PaxelItem> WOODEN_PAXEL = REG.item("wooden_paxel", p -> new PaxelItem(WOOD, p))
            .lang("Wooden Paxel")
            .model((c, p) -> p.handheldItem(c.get()))
            .register();

    public static final ItemEntry<HammerItem> NETHERITE_HAMMER = REG.item("netherite_hammer", p -> new HammerItem(2, NETHERITE, p))
            .lang("Netherite Hammer")
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<HammerItem> DIAMOND_HAMMER = REG.item("diamond_hammer", p -> new HammerItem(2, DIAMOND, p))
            .lang("Diamond Hammer")
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<HammerItem> EMERALD_HAMMER = REG.item("emerald_hammer", p -> new HammerItem(1, DIAMOND, p))
            .lang("Emerald Hammer")
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<HammerItem> IRON_HAMMER = REG.item("iron_hammer", p -> new HammerItem(1, IRON, p))
            .lang("Iron Hammer")
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<HammerItem> GOLDEN_HAMMER = REG.item("golden_hammer", p -> new HammerItem(1, GOLD, p))
            .lang("Golden Hammer")
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<HammerItem> COPPER_HAMMER = REG.item("copper_hammer", p -> new HammerItem(1, STONE, p))
            .lang("Copper Hammer")
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<HammerItem> STONE_HAMMER = REG.item("stone_hammer", p -> new HammerItem(1, STONE, p))
            .lang("Stone Hammer")
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<HammerItem> WOODEN_HAMMER = REG.item("wooden_hammer", p -> new HammerItem(1, WOOD, p))
            .lang("Wooden Hammer")
            .model((c, p) -> p.handheldItem(c.get()))
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
            .model(ModelHelper::forwardItem)
            .register();

    public static final ItemEntry<HexSigilItem> HEX_SIGIL = REG.item("hex_sigil", HexSigilItem::new)
            .lang("Hex Sigil")
            .model(ModelHelper::forwardItem)
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
