package uwu.lopyluna.omni_util.register;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.common.Tags;
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
            .properties(p -> p.fireResistant().attributes(ScytheItem.createAttributes(NETHERITE, 1.0F, -1.5F)))
            .lang("Netherite Scythe")
            .tag(ItemTags.HOES, ItemTags.SWORDS)
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<ScytheItem> DIAMOND_SCYTHE = REG.item("diamond_scythe", p -> new ScytheItem(3, 24, DIAMOND, p))
            .properties(p -> p.attributes(ScytheItem.createAttributes(DIAMOND, 1.0F, -1.5F)))
            .lang("Diamond Scythe")
            .tag(ItemTags.HOES, ItemTags.SWORDS)
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<ScytheItem> EMERALD_SCYTHE = REG.item("emerald_scythe", p -> new ScytheItem(2, 16, DIAMOND, p))
            .properties(p -> p.attributes(ScytheItem.createAttributes(DIAMOND, 1.0F, -1.5F)))
            .lang("Emerald Scythe")
            .tag(ItemTags.HOES, ItemTags.SWORDS)
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<ScytheItem> IRON_SCYTHE = REG.item("iron_scythe", p -> new ScytheItem(2, 16, IRON, p))
            .properties(p -> p.attributes(ScytheItem.createAttributes(IRON, 1.5F, -1.5F)))
            .lang("Iron Scythe")
            .tag(ItemTags.HOES, ItemTags.SWORDS)
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<ScytheItem> GOLDEN_SCYTHE = REG.item("golden_scythe", p -> new ScytheItem(1, 12, GOLD, p))
            .properties(p -> p.attributes(ScytheItem.createAttributes(GOLD, 1.5F, -1.5F)))
            .lang("Golden Scythe")
            .tag(ItemTags.HOES, ItemTags.SWORDS)
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<ScytheItem> COPPER_SCYTHE = REG.item("copper_scythe", p -> new ScytheItem(2, 12, STONE, p))
            .properties(p -> p.attributes(ScytheItem.createAttributes(STONE, 1.5F, -1.5F)))
            .lang("Copper Scythe")
            .tag(ItemTags.HOES, ItemTags.SWORDS)
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<ScytheItem> STONE_SCYTHE = REG.item("stone_scythe", p -> new ScytheItem(1, 8, STONE, p))
            .properties(p -> p.attributes(ScytheItem.createAttributes(STONE, 2.0F, -1.5F)))
            .lang("Stone Scythe")
            .tag(ItemTags.HOES, ItemTags.SWORDS)
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<ScytheItem> WOODEN_SCYTHE = REG.item("wooden_scythe", p -> new ScytheItem(1, 8, WOOD, p))
            .properties(p -> p.attributes(ScytheItem.createAttributes(WOOD, 1.5F, -1.5F)))
            .lang("Wooden Scythe")
            .tag(ItemTags.HOES, ItemTags.SWORDS)
            .model((c, p) -> p.handheldItem(c.get()))
            .register();

    public static final ItemEntry<PaxelItem> NETHERITE_PAXEL = REG.item("netherite_paxel", p -> new PaxelItem(NETHERITE, p))
            .properties(p -> p.fireResistant().attributes(PaxelItem.createAttributes(NETHERITE, 2.6f, -2.6f)))
            .lang("Netherite Paxel")
            .tag(ItemTags.AXES, ItemTags.SHOVELS, ItemTags.PICKAXES, ItemTags.HOES, ItemTags.SWORDS)
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<PaxelItem> DIAMOND_PAXEL = REG.item("diamond_paxel", p -> new PaxelItem(DIAMOND, p))
            .properties(p -> p.attributes(PaxelItem.createAttributes(DIAMOND, 2.6f, -2.6f)))
            .lang("Diamond Paxel")
            .tag(ItemTags.AXES, ItemTags.SHOVELS, ItemTags.PICKAXES, ItemTags.HOES, ItemTags.SWORDS)
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<PaxelItem> EMERALD_PAXEL = REG.item("emerald_paxel", p -> new PaxelItem(DIAMOND, p))
            .properties(p -> p.attributes(PaxelItem.createAttributes(DIAMOND, 2.6f, -2.6f)))
            .lang("Emerald Paxel")
            .tag(ItemTags.AXES, ItemTags.SHOVELS, ItemTags.PICKAXES, ItemTags.HOES, ItemTags.SWORDS)
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<PaxelItem> IRON_PAXEL = REG.item("iron_paxel", p -> new PaxelItem(IRON, p))
            .properties(p -> p.attributes(PaxelItem.createAttributes(IRON, 2.9f, -2.6f)))
            .lang("Iron Paxel")
            .tag(ItemTags.AXES, ItemTags.SHOVELS, ItemTags.PICKAXES, ItemTags.HOES, ItemTags.SWORDS)
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<PaxelItem> GOLDEN_PAXEL = REG.item("golden_paxel", p -> new PaxelItem(GOLD, p))
            .properties(p -> p.attributes(PaxelItem.createAttributes(GOLD, 2.9f, -2.6f)))
            .lang("Golden Paxel")
            .tag(ItemTags.AXES, ItemTags.SHOVELS, ItemTags.PICKAXES, ItemTags.HOES, ItemTags.SWORDS)
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<PaxelItem> COPPER_PAXEL = REG.item("copper_paxel", p -> new PaxelItem(STONE, p))
            .properties(p -> p.attributes(PaxelItem.createAttributes(STONE, 2.9f, -2.6f)))
            .lang("Copper Paxel")
            .tag(ItemTags.AXES, ItemTags.SHOVELS, ItemTags.PICKAXES, ItemTags.HOES, ItemTags.SWORDS)
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<PaxelItem> STONE_PAXEL = REG.item("stone_paxel", p -> new PaxelItem(STONE, p))
            .properties(p -> p.attributes(PaxelItem.createAttributes(STONE, 3.1f, -2.6f)))
            .lang("Stone Paxel")
            .tag(ItemTags.AXES, ItemTags.SHOVELS, ItemTags.PICKAXES, ItemTags.HOES, ItemTags.SWORDS)
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<PaxelItem> WOODEN_PAXEL = REG.item("wooden_paxel", p -> new PaxelItem(WOOD, p))
            .properties(p -> p.attributes(PaxelItem.createAttributes(WOOD, 2.9f, -2.6f)))
            .lang("Wooden Paxel")
            .tag(ItemTags.AXES, ItemTags.SHOVELS, ItemTags.PICKAXES, ItemTags.HOES, ItemTags.SWORDS)
            .model((c, p) -> p.handheldItem(c.get()))
            .register();

    public static final ItemEntry<HammerItem> NETHERITE_HAMMER = REG.item("netherite_hammer", p -> new HammerItem(2, NETHERITE, p))
            .properties(p -> p.fireResistant().attributes(ScytheItem.createAttributes(NETHERITE, 6.0F, -3.5F)))
            .lang("Netherite Hammer")
            .tag(ItemTags.MINING_ENCHANTABLE, ItemTags.MINING_LOOT_ENCHANTABLE, ItemTags.FIRE_ASPECT_ENCHANTABLE,
                    ItemTags.DURABILITY_ENCHANTABLE, ItemTags.SHARP_WEAPON_ENCHANTABLE, ItemTags.BREAKS_DECORATED_POTS,
                    Tags.Items.MINING_TOOL_TOOLS, Tags.Items.MELEE_WEAPON_TOOLS, Tags.Items.ENCHANTABLES)
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<HammerItem> DIAMOND_HAMMER = REG.item("diamond_hammer", p -> new HammerItem(2, DIAMOND, p))
            .properties(p -> p.attributes(ScytheItem.createAttributes(DIAMOND, 6.0F, -3.5F)))
            .lang("Diamond Hammer")
            .tag(ItemTags.MINING_ENCHANTABLE, ItemTags.MINING_LOOT_ENCHANTABLE, ItemTags.FIRE_ASPECT_ENCHANTABLE,
                    ItemTags.DURABILITY_ENCHANTABLE, ItemTags.SHARP_WEAPON_ENCHANTABLE, ItemTags.BREAKS_DECORATED_POTS,
                    Tags.Items.MINING_TOOL_TOOLS, Tags.Items.MELEE_WEAPON_TOOLS, Tags.Items.ENCHANTABLES)
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<HammerItem> EMERALD_HAMMER = REG.item("emerald_hammer", p -> new HammerItem(1, DIAMOND, p))
            .properties(p -> p.attributes(ScytheItem.createAttributes(DIAMOND, 6.0F, -3.5F)))
            .lang("Emerald Hammer")
            .tag(ItemTags.MINING_ENCHANTABLE, ItemTags.MINING_LOOT_ENCHANTABLE, ItemTags.FIRE_ASPECT_ENCHANTABLE,
                    ItemTags.DURABILITY_ENCHANTABLE, ItemTags.SHARP_WEAPON_ENCHANTABLE, ItemTags.BREAKS_DECORATED_POTS,
                    Tags.Items.MINING_TOOL_TOOLS, Tags.Items.MELEE_WEAPON_TOOLS, Tags.Items.ENCHANTABLES)
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<HammerItem> IRON_HAMMER = REG.item("iron_hammer", p -> new HammerItem(1, IRON, p))
            .properties(p -> p.attributes(ScytheItem.createAttributes(IRON, 7.0F, -3.5F)))
            .lang("Iron Hammer")
            .tag(ItemTags.MINING_ENCHANTABLE, ItemTags.MINING_LOOT_ENCHANTABLE, ItemTags.FIRE_ASPECT_ENCHANTABLE,
                    ItemTags.DURABILITY_ENCHANTABLE, ItemTags.SHARP_WEAPON_ENCHANTABLE, ItemTags.BREAKS_DECORATED_POTS,
                    Tags.Items.MINING_TOOL_TOOLS, Tags.Items.MELEE_WEAPON_TOOLS, Tags.Items.ENCHANTABLES)
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<HammerItem> GOLDEN_HAMMER = REG.item("golden_hammer", p -> new HammerItem(1, GOLD, p))
            .properties(p -> p.attributes(ScytheItem.createAttributes(GOLD, 7.0F, -3.5F)))
            .lang("Golden Hammer")
            .tag(ItemTags.MINING_ENCHANTABLE, ItemTags.MINING_LOOT_ENCHANTABLE, ItemTags.FIRE_ASPECT_ENCHANTABLE,
                    ItemTags.DURABILITY_ENCHANTABLE, ItemTags.SHARP_WEAPON_ENCHANTABLE, ItemTags.BREAKS_DECORATED_POTS,
                    Tags.Items.MINING_TOOL_TOOLS, Tags.Items.MELEE_WEAPON_TOOLS, Tags.Items.ENCHANTABLES)
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<HammerItem> COPPER_HAMMER = REG.item("copper_hammer", p -> new HammerItem(1, STONE, p))
            .properties(p -> p.attributes(ScytheItem.createAttributes(STONE, 7.0F, -3.5F)))
            .lang("Copper Hammer")
            .tag(ItemTags.MINING_ENCHANTABLE, ItemTags.MINING_LOOT_ENCHANTABLE, ItemTags.FIRE_ASPECT_ENCHANTABLE,
                    ItemTags.DURABILITY_ENCHANTABLE, ItemTags.SHARP_WEAPON_ENCHANTABLE, ItemTags.BREAKS_DECORATED_POTS,
                    Tags.Items.MINING_TOOL_TOOLS, Tags.Items.MELEE_WEAPON_TOOLS, Tags.Items.ENCHANTABLES)
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<HammerItem> STONE_HAMMER = REG.item("stone_hammer", p -> new HammerItem(1, STONE, p))
            .properties(p -> p.attributes(ScytheItem.createAttributes(STONE, 8.0F, -3.5F)))
            .lang("Stone Hammer")
            .tag(ItemTags.MINING_ENCHANTABLE, ItemTags.MINING_LOOT_ENCHANTABLE, ItemTags.FIRE_ASPECT_ENCHANTABLE,
                    ItemTags.DURABILITY_ENCHANTABLE, ItemTags.SHARP_WEAPON_ENCHANTABLE, ItemTags.BREAKS_DECORATED_POTS,
                    Tags.Items.MINING_TOOL_TOOLS, Tags.Items.MELEE_WEAPON_TOOLS, Tags.Items.ENCHANTABLES)
            .model((c, p) -> p.handheldItem(c.get()))
            .register();
    public static final ItemEntry<HammerItem> WOODEN_HAMMER = REG.item("wooden_hammer", p -> new HammerItem(1, WOOD, p))
            .properties(p -> p.attributes(ScytheItem.createAttributes(WOOD, 7.0F, -3.5F)))
            .lang("Wooden Hammer")
            .tag(ItemTags.MINING_ENCHANTABLE, ItemTags.MINING_LOOT_ENCHANTABLE, ItemTags.FIRE_ASPECT_ENCHANTABLE,
                    ItemTags.DURABILITY_ENCHANTABLE, ItemTags.SHARP_WEAPON_ENCHANTABLE, ItemTags.BREAKS_DECORATED_POTS,
                    Tags.Items.MINING_TOOL_TOOLS, Tags.Items.MELEE_WEAPON_TOOLS, Tags.Items.ENCHANTABLES)
            .model((c, p) -> p.handheldItem(c.get()))
            .register();

    public static final ItemEntry<MagicMirror> MAGIC_MIRROR = REG.item("magic_mirror", MagicMirror::new)
            .lang("Magic Mirror")
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
