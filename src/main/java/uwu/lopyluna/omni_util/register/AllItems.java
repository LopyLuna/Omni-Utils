package uwu.lopyluna.omni_util.register;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.common.Tags;
import uwu.lopyluna.omni_util.OmniUtils;
import uwu.lopyluna.omni_util.content.blocks.WitheredSoulLassoItem;
import uwu.lopyluna.omni_util.content.items.*;
import uwu.lopyluna.omni_util.content.items.sigils.CataclysmSigilItem;
import uwu.lopyluna.omni_util.content.items.sigils.HexSigilItem;
import uwu.lopyluna.omni_util.content.items.unstable_hexa.UnstableHexaIngot;
import uwu.lopyluna.omni_util.content.items.unstable_hexa.UnstableHexaNugget;
import uwu.lopyluna.omni_util.content.items.wands.BreakerWandItem;
import uwu.lopyluna.omni_util.content.items.wands.PlacerWandItem;
import uwu.lopyluna.omni_util.content.utils.datagen.ModelHelper;

import static com.tterrag.registrate.providers.RegistrateRecipeProvider.has;
import static net.minecraft.world.item.Tiers.*;
import static uwu.lopyluna.omni_util.OmniUtils.REG;

@SuppressWarnings("unused")
public class AllItems {
    public static final ItemEntry<Item> BLANK_TABLET = REG.item("blank_tablet", Item::new)
            .lang("Blank Tablet")
            .model(ModelHelper::forwardItem)
            .register();

    public static final ItemEntry<HexSigilItem> HEX_SIGIL = REG.item("hex_sigil", HexSigilItem::new)
            .lang("Hex Sigil")
            .model(ModelHelper::forwardItem)
            .tag(AllTags.itemC("sigils"))
            .register();

    public static final ItemEntry<CataclysmSigilItem> CATACLYSM_SIGIL = REG.item("cataclysm_sigil", CataclysmSigilItem::new)
            .lang("Cataclysm Sigil")
            .model(ModelHelper::forwardItem)
            .tag(AllTags.itemC("sigils"))
            .register();

    public static final ItemEntry<Item> GARNET = REG.item("garnet", Item::new)
            .lang("Garnet")
            .tag(AllTags.itemC("gems/garnet"), AllTags.itemC("gems"))
            .register();

    public static final ItemEntry<Item> ONYX = REG.item("onyx", Item::new)
            .lang("Onyx")
            .tag(AllTags.itemC("gems/onyx"), AllTags.itemC("gems"))
            .register();

    public static final ItemEntry<UnstableHexaIngot> UNSTABLE_HEXA_INGOT = REG.item("unstable_hexa_ingot", UnstableHexaIngot::new)
            .lang("Unstable Hexa Ingot")
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                    .pattern("RGR")
                    .pattern("RNR")
                    .pattern("RHR")
                    .define('R', Items.REDSTONE)
                    .define('N', Items.NETHERITE_INGOT)
                    .define('H', HEX_SIGIL.get())
                    .define('G', GARNET.get())
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
            .tag(AllTags.itemC("ingots/hexa"), AllTags.itemC("ingots"))
            .register();

    public static final ItemEntry<UnstableHexaNugget> UNSTABLE_HEXA_NUGGET = REG.item("unstable_hexa_nugget", UnstableHexaNugget::new)
            .lang("Unstable Hexa Nugget")
            .tag(AllTags.itemC("nuggets/hexa"), AllTags.itemC("nuggets"))
            .register();

    public static final ItemEntry<Item> HEXA_INGOT = REG.item("hexa_ingot", Item::new)
            .lang("Hexa Ingot")
            .recipe((c, p) -> {
                ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, c.get(), 2)
                        .requires(UNSTABLE_HEXA_INGOT.get())
                        .requires(UNSTABLE_HEXA_INGOT.get())
                        .requires(ONYX.get())
                        .requires(Items.PRISMARINE_CRYSTALS)
                        .requires(Items.GHAST_TEAR)
                        .unlockedBy("has_" + c.getName(), has(c.get()))
                        .save(p, OmniUtils.loc("crafting/" + c.getName()));


                ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, c.get(), 9)
                        .requires(AllBlocks.HEXA_BLOCK.get())
                        .unlockedBy("has_" + c.getName(), has(c.get()))
                        .save(p, OmniUtils.loc("crafting/ingot_from_block_hexa"));
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AllBlocks.HEXA_BLOCK.get(), 1)
                        .pattern("HHH")
                        .pattern("HHH")
                        .pattern("HHH")
                        .define('H', c.get())
                        .unlockedBy("has_" + c.getName(), has(c.get()))
                        .save(p, OmniUtils.loc("crafting/ingot_to_block_hexa"));
            })
            .tag(AllTags.itemC("ingots/hexa"), AllTags.itemC("ingots"))
            .register();

    public static final ItemEntry<Item> HEXA_NUGGET = REG.item("hexa_nugget", Item::new)
            .lang("Hexa Nugget")
            .recipe((c, p) -> {
                ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, c.get(), 9)
                        .requires(HEXA_INGOT.get())
                        .unlockedBy("has_" + c.getName(), has(c.get()))
                        .save(p, OmniUtils.loc("crafting/nugget_from_ingot_hexa"));
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HEXA_INGOT.get(), 1)
                        .pattern("HHH")
                        .pattern("HHH")
                        .pattern("HHH")
                        .define('H', c.get())
                        .unlockedBy("has_" + c.getName(), has(c.get()))
                        .save(p, OmniUtils.loc("crafting/nugget_to_ingot_hexa"));
            })
            .tag(AllTags.itemC("nuggets/hexa"), AllTags.itemC("nuggets"))
            .register();


    public static final ItemEntry<Item> WITHERED_TEAR = REG.item("withered_tear", Item::new)
            .lang("Withered Tear")
            .register();

    public static final ItemEntry<GogglesItem> GOGGLES = REG.item("goggles", GogglesItem::new)
            .lang("Goggles")
            .tag(AllTags.itemC("goggles"))
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                    .pattern(" I ")
                    .pattern("GLG")
                    .pattern(" I ")
                    .define('G', Tags.Items.GLASS_BLOCKS_COLORLESS)
                    .define('I', Items.GOLD_INGOT)
                    .define('L', Items.LEATHER)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
            .register();

    public static final ItemEntry<SonarGogglesItem> SONAR_GOGGLES = REG.item("sonar_goggles", SonarGogglesItem::new)
            .lang("Sonar Goggles")
            .tag(AllTags.itemC("sonar_goggles"))
            .register();

    public static final ItemEntry<WateringCanItem> WATERING_CAN = REG.item("watering_can", WateringCanItem::new)
            .lang("Watering Can")
            .model(ModelHelper::forwardItem)
            .register();

    public static final ItemEntry<EmptyWateringCanItem> EMPTY_WATERING_CAN = REG.item("empty_watering_can", EmptyWateringCanItem::new)
            .lang("Empty Watering Can")
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                    .pattern("NBI")
                    .pattern(" I ")
                    .define('B', Items.BUCKET)
                    .define('I', Items.IRON_INGOT)
                    .define('N', Items.IRON_NUGGET)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
            .model(ModelHelper::forwardItem)
            .register();

    public static final ItemEntry<SoulLassoItem> SOUL_LASSO = REG.item("soul_lasso", SoulLassoItem::new)
            .lang("Soul Lasso")
            .model((c, p) -> p.basicItem(c.get()).override().model(p.basicItem(OmniUtils.loc("soul_lasso_mob"))).predicate(OmniUtils.loc("entity"), 1.0f).end())
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                    .pattern("GS ")
                    .pattern("SN ")
                    .pattern("  G")
                    .define('S', Items.STRING)
                    .define('G', Items.GOLD_INGOT)
                    .define('N', AllTags.itemC("ingots/hexa"))
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
            .register();

    public static final ItemEntry<WitheredSoulLassoItem> WITHERED_SOUL_LASSO = REG.item("withered_soul_lasso", WitheredSoulLassoItem::new)
            .lang("Withered Soul Lasso")
            .model((c, p) -> p.basicItem(c.get()).override().model(p.basicItem(OmniUtils.loc("withered_soul_lasso_mob"))).predicate(OmniUtils.loc("entity"), 1.0f).end())
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                    .pattern(" W ")
                    .pattern("WSW")
                    .pattern(" W ")
                    .define('S', SOUL_LASSO)
                    .define('W', WITHERED_TEAR)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
            .register();

    public static final ItemEntry<MagicMirror> MAGIC_MIRROR = REG.item("magic_mirror", MagicMirror::new)
            .lang("Magic Mirror")
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                    .pattern(" I ")
                    .pattern("IHI")
                    .pattern(" I ")
                    .define('I', Items.IRON_INGOT)
                    .define('H', HEXA_INGOT)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
            .register();

    public static final ItemEntry<BreakerWandItem> BREAKER_WAND = REG.item("breaker_wand", BreakerWandItem::new)
            .lang("Wand Of Deconstruct")
            .model((c, p) -> p.handheldItem(c.get()))
            .register();

    public static final ItemEntry<PlacerWandItem> PLACER_WAND = REG.item("placer_wand", PlacerWandItem::new)
            .lang("Wand Of Construct")
            .model((c, p) -> p.handheldItem(c.get()))
            .register();

    public static final ItemEntry<BundleOfHoldingItem> BUNDLE_OF_HOLDING = REG.item("bundle_of_holding", BundleOfHoldingItem::new)
            .properties(p -> p.fireResistant().rarity(Rarity.UNCOMMON))
            .lang("Bundle of Holding")
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                    .pattern("NIN")
                    .pattern("IBI")
                    .pattern("NIN")
                    .define('B', Items.BUNDLE)
                    .define('I', Items.GOLD_INGOT)
                    .define('N', AllTags.itemC("nuggets/hexa"))
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
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
                    .override().model(p.basicItem(OmniUtils.loc("invisible_angel_ring"))).predicate(OmniUtils.loc("wing_type"), 5.0f).end()
                    .override().model(p.basicItem(OmniUtils.loc("allay_angel_ring"))).predicate(OmniUtils.loc("wing_type"), 6.0f).end()
                    .override().model(p.basicItem(OmniUtils.loc("vexxed_angel_ring"))).predicate(OmniUtils.loc("wing_type"), 7.0f).end())
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                    .pattern("FGF")
                    .pattern("HSH")
                    .pattern("FGF")
                    .define('F', Items.FEATHER)
                    .define('G', Items.GOLD_INGOT)
                    .define('S', Items.NETHER_STAR)
                    .define('H', HEXA_INGOT)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
            .register();

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
            .recipe((c, p) -> {
                RegistrateRecipeProvider.netheriteSmithing(p, c.get(), RecipeCategory.TOOLS, NETHERITE_SCYTHE.get());
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                        .pattern("IIB")
                        .pattern("  S")
                        .pattern("  S")
                        .define('B', Tags.Items.STORAGE_BLOCKS_DIAMOND)
                        .define('I', Tags.Items.GEMS_DIAMOND)
                        .define('S', Tags.Items.RODS_WOODEN)
                        .unlockedBy("has_" + c.getName(), has(c.get()))
                        .save(p, OmniUtils.loc("crafting/" + c.getName()));
            })
            .register();
    public static final ItemEntry<ScytheItem> EMERALD_SCYTHE = REG.item("emerald_scythe", p -> new ScytheItem(2, 16, DIAMOND, p))
            .properties(p -> p.attributes(ScytheItem.createAttributes(DIAMOND, 1.0F, -1.5F)))
            .lang("Emerald Scythe")
            .tag(ItemTags.HOES, ItemTags.SWORDS)
            .model((c, p) -> p.handheldItem(c.get()))
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                    .pattern("IIB")
                    .pattern("  S")
                    .pattern("  S")
                    .define('B', Tags.Items.STORAGE_BLOCKS_EMERALD)
                    .define('I', Tags.Items.GEMS_EMERALD)
                    .define('S', Tags.Items.RODS_WOODEN)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
            .register();
    public static final ItemEntry<ScytheItem> IRON_SCYTHE = REG.item("iron_scythe", p -> new ScytheItem(2, 16, IRON, p))
            .properties(p -> p.attributes(ScytheItem.createAttributes(IRON, 1.5F, -1.5F)))
            .lang("Iron Scythe")
            .tag(ItemTags.HOES, ItemTags.SWORDS)
            .model((c, p) -> p.handheldItem(c.get()))
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                    .pattern("IIB")
                    .pattern("  S")
                    .pattern("  S")
                    .define('B', Tags.Items.STORAGE_BLOCKS_IRON)
                    .define('I', Tags.Items.INGOTS_IRON)
                    .define('S', Tags.Items.RODS_WOODEN)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
            .register();
    public static final ItemEntry<ScytheItem> GOLDEN_SCYTHE = REG.item("golden_scythe", p -> new ScytheItem(1, 12, GOLD, p))
            .properties(p -> p.attributes(ScytheItem.createAttributes(GOLD, 1.5F, -1.5F)))
            .lang("Golden Scythe")
            .tag(ItemTags.HOES, ItemTags.SWORDS)
            .model((c, p) -> p.handheldItem(c.get()))
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                    .pattern("IIB")
                    .pattern("  S")
                    .pattern("  S")
                    .define('B', Tags.Items.STORAGE_BLOCKS_GOLD)
                    .define('I', Tags.Items.INGOTS_GOLD)
                    .define('S', Tags.Items.RODS_WOODEN)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
            .register();
    public static final ItemEntry<ScytheItem> COPPER_SCYTHE = REG.item("copper_scythe", p -> new ScytheItem(2, 12, STONE, p))
            .properties(p -> p.attributes(ScytheItem.createAttributes(STONE, 1.5F, -1.5F)))
            .lang("Copper Scythe")
            .tag(ItemTags.HOES, ItemTags.SWORDS)
            .model((c, p) -> p.handheldItem(c.get()))
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                    .pattern("IIB")
                    .pattern("  S")
                    .pattern("  S")
                    .define('B', Tags.Items.STORAGE_BLOCKS_COPPER)
                    .define('I', Tags.Items.INGOTS_COPPER)
                    .define('S', Tags.Items.RODS_WOODEN)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
            .register();
    public static final ItemEntry<ScytheItem> STONE_SCYTHE = REG.item("stone_scythe", p -> new ScytheItem(1, 8, STONE, p))
            .properties(p -> p.attributes(ScytheItem.createAttributes(STONE, 2.0F, -1.5F)))
            .lang("Stone Scythe")
            .tag(ItemTags.HOES, ItemTags.SWORDS)
            .model((c, p) -> p.handheldItem(c.get()))
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                    .pattern("IIB")
                    .pattern("  S")
                    .pattern("  S")
                    .define('B', AllBlocks.BORDER_STONE)
                    .define('I', ItemTags.STONE_TOOL_MATERIALS)
                    .define('S', Tags.Items.RODS_WOODEN)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
            .register();
    public static final ItemEntry<ScytheItem> WOODEN_SCYTHE = REG.item("wooden_scythe", p -> new ScytheItem(1, 8, WOOD, p))
            .properties(p -> p.attributes(ScytheItem.createAttributes(WOOD, 1.5F, -1.5F)))
            .lang("Wooden Scythe")
            .tag(ItemTags.HOES, ItemTags.SWORDS)
            .model((c, p) -> p.handheldItem(c.get()))
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                    .pattern("IIB")
                    .pattern("  S")
                    .pattern("  S")
                    .define('B', ItemTags.LOGS)
                    .define('I', ItemTags.PLANKS)
                    .define('S', Tags.Items.RODS_WOODEN)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
            .register();

    public static final ItemEntry<PaxelItem> NETHERITE_PAXEL = REG.item("netherite_paxel", p -> new PaxelItem(NETHERITE, p))
            .properties(p -> p.fireResistant().attributes(PaxelItem.createAttributes(NETHERITE, 2.6f, -2.6f)))
            .lang("Netherite Paxel")
            .tag(ItemTags.AXES, ItemTags.SHOVELS, ItemTags.PICKAXES, ItemTags.HOES, ItemTags.SWORDS)
            .model((c, p) -> p.handheldItem(c.get()))
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PLACER_WAND.get(), 1)
                    .pattern(" IH")
                    .pattern(" TI")
                    .pattern("R  ")
                    .define('H', AllTags.itemC("ingots/hexa"))
                    .define('I', Items.GOLD_INGOT)
                    .define('R', Items.BLAZE_ROD)
                    .define('T', c.get())
                    .unlockedBy("has_" + PLACER_WAND.getId().getPath(), has(PLACER_WAND.get()))
                    .save(p, OmniUtils.loc("crafting/" + PLACER_WAND.getId().getPath())))
            .register();
    public static final ItemEntry<PaxelItem> DIAMOND_PAXEL = REG.item("diamond_paxel", p -> new PaxelItem(DIAMOND, p))
            .properties(p -> p.attributes(PaxelItem.createAttributes(DIAMOND, 2.6f, -2.6f)))
            .lang("Diamond Paxel")
            .tag(ItemTags.AXES, ItemTags.SHOVELS, ItemTags.PICKAXES, ItemTags.HOES, ItemTags.SWORDS)
            .model((c, p) -> p.handheldItem(c.get()))
            .recipe((c, p) -> {
                RegistrateRecipeProvider.netheriteSmithing(p, c.get(), RecipeCategory.TOOLS, NETHERITE_PAXEL.get());
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                        .pattern("BII")
                        .pattern(" S ")
                        .pattern(" S ")
                        .define('B', Tags.Items.STORAGE_BLOCKS_DIAMOND)
                        .define('I', Tags.Items.GEMS_DIAMOND)
                        .define('S', Tags.Items.RODS_WOODEN)
                        .unlockedBy("has_" + c.getName(), has(c.get()))
                        .save(p, OmniUtils.loc("crafting/" + c.getName()));
            })
            .register();
    public static final ItemEntry<PaxelItem> EMERALD_PAXEL = REG.item("emerald_paxel", p -> new PaxelItem(DIAMOND, p))
            .properties(p -> p.attributes(PaxelItem.createAttributes(DIAMOND, 2.6f, -2.6f)))
            .lang("Emerald Paxel")
            .tag(ItemTags.AXES, ItemTags.SHOVELS, ItemTags.PICKAXES, ItemTags.HOES, ItemTags.SWORDS)
            .model((c, p) -> p.handheldItem(c.get()))
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                    .pattern("BII")
                    .pattern(" S ")
                    .pattern(" S ")
                    .define('B', Tags.Items.STORAGE_BLOCKS_EMERALD)
                    .define('I', Tags.Items.GEMS_EMERALD)
                    .define('S', Tags.Items.RODS_WOODEN)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
            .register();
    public static final ItemEntry<PaxelItem> IRON_PAXEL = REG.item("iron_paxel", p -> new PaxelItem(IRON, p))
            .properties(p -> p.attributes(PaxelItem.createAttributes(IRON, 2.9f, -2.6f)))
            .lang("Iron Paxel")
            .tag(ItemTags.AXES, ItemTags.SHOVELS, ItemTags.PICKAXES, ItemTags.HOES, ItemTags.SWORDS)
            .model((c, p) -> p.handheldItem(c.get()))
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                    .pattern("BII")
                    .pattern(" S ")
                    .pattern(" S ")
                    .define('B', Tags.Items.STORAGE_BLOCKS_IRON)
                    .define('I', Tags.Items.INGOTS_IRON)
                    .define('S', Tags.Items.RODS_WOODEN)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
            .register();
    public static final ItemEntry<PaxelItem> GOLDEN_PAXEL = REG.item("golden_paxel", p -> new PaxelItem(GOLD, p))
            .properties(p -> p.attributes(PaxelItem.createAttributes(GOLD, 2.9f, -2.6f)))
            .lang("Golden Paxel")
            .tag(ItemTags.AXES, ItemTags.SHOVELS, ItemTags.PICKAXES, ItemTags.HOES, ItemTags.SWORDS)
            .model((c, p) -> p.handheldItem(c.get()))
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                    .pattern("BII")
                    .pattern(" S ")
                    .pattern(" S ")
                    .define('B', Tags.Items.STORAGE_BLOCKS_GOLD)
                    .define('I', Tags.Items.INGOTS_GOLD)
                    .define('S', Tags.Items.RODS_WOODEN)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
            .register();
    public static final ItemEntry<PaxelItem> COPPER_PAXEL = REG.item("copper_paxel", p -> new PaxelItem(STONE, p))
            .properties(p -> p.attributes(PaxelItem.createAttributes(STONE, 2.9f, -2.6f)))
            .lang("Copper Paxel")
            .tag(ItemTags.AXES, ItemTags.SHOVELS, ItemTags.PICKAXES, ItemTags.HOES, ItemTags.SWORDS)
            .model((c, p) -> p.handheldItem(c.get()))
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                    .pattern("BII")
                    .pattern(" S ")
                    .pattern(" S ")
                    .define('B', Tags.Items.STORAGE_BLOCKS_COPPER)
                    .define('I', Tags.Items.INGOTS_COPPER)
                    .define('S', Tags.Items.RODS_WOODEN)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
            .register();
    public static final ItemEntry<PaxelItem> STONE_PAXEL = REG.item("stone_paxel", p -> new PaxelItem(STONE, p))
            .properties(p -> p.attributes(PaxelItem.createAttributes(STONE, 3.1f, -2.6f)))
            .lang("Stone Paxel")
            .tag(ItemTags.AXES, ItemTags.SHOVELS, ItemTags.PICKAXES, ItemTags.HOES, ItemTags.SWORDS)
            .model((c, p) -> p.handheldItem(c.get()))
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                    .pattern("BII")
                    .pattern(" S ")
                    .pattern(" S ")
                    .define('B', AllBlocks.BORDER_STONE)
                    .define('I', ItemTags.STONE_TOOL_MATERIALS)
                    .define('S', Tags.Items.RODS_WOODEN)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
            .register();
    public static final ItemEntry<PaxelItem> WOODEN_PAXEL = REG.item("wooden_paxel", p -> new PaxelItem(WOOD, p))
            .properties(p -> p.attributes(PaxelItem.createAttributes(WOOD, 2.9f, -2.6f)))
            .lang("Wooden Paxel")
            .tag(ItemTags.AXES, ItemTags.SHOVELS, ItemTags.PICKAXES, ItemTags.HOES, ItemTags.SWORDS)
            .model((c, p) -> p.handheldItem(c.get()))
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                    .pattern("BII")
                    .pattern(" S ")
                    .pattern(" S ")
                    .define('B', ItemTags.LOGS)
                    .define('I', ItemTags.PLANKS)
                    .define('S', Tags.Items.RODS_WOODEN)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
            .register();

    public static final ItemEntry<HammerItem> NETHERITE_HAMMER = REG.item("netherite_hammer", p -> new HammerItem(2, NETHERITE, p))
            .properties(p -> p.fireResistant().attributes(ScytheItem.createAttributes(NETHERITE, 6.0F, -3.5F)))
            .lang("Netherite Hammer")
            .tag(ItemTags.MINING_ENCHANTABLE, ItemTags.MINING_LOOT_ENCHANTABLE, ItemTags.FIRE_ASPECT_ENCHANTABLE,
                    ItemTags.DURABILITY_ENCHANTABLE, ItemTags.SHARP_WEAPON_ENCHANTABLE, ItemTags.BREAKS_DECORATED_POTS,
                    Tags.Items.MINING_TOOL_TOOLS, Tags.Items.MELEE_WEAPON_TOOLS, Tags.Items.ENCHANTABLES)
            .model((c, p) -> p.handheldItem(c.get()))
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BREAKER_WAND.get(), 1)
                    .pattern(" IH")
                    .pattern(" TI")
                    .pattern("R  ")
                    .define('H', AllTags.itemC("ingots/hexa"))
                    .define('I', Items.GOLD_INGOT)
                    .define('R', Items.BLAZE_ROD)
                    .define('T', c.get())
                    .unlockedBy("has_" + BREAKER_WAND.getId().getPath(), has(BREAKER_WAND.get()))
                    .save(p, OmniUtils.loc("crafting/" + BREAKER_WAND.getId().getPath())))
            .register();
    public static final ItemEntry<HammerItem> DIAMOND_HAMMER = REG.item("diamond_hammer", p -> new HammerItem(2, DIAMOND, p))
            .properties(p -> p.attributes(ScytheItem.createAttributes(DIAMOND, 6.0F, -3.5F)))
            .lang("Diamond Hammer")
            .tag(ItemTags.MINING_ENCHANTABLE, ItemTags.MINING_LOOT_ENCHANTABLE, ItemTags.FIRE_ASPECT_ENCHANTABLE,
                    ItemTags.DURABILITY_ENCHANTABLE, ItemTags.SHARP_WEAPON_ENCHANTABLE, ItemTags.BREAKS_DECORATED_POTS,
                    Tags.Items.MINING_TOOL_TOOLS, Tags.Items.MELEE_WEAPON_TOOLS, Tags.Items.ENCHANTABLES)
            .model((c, p) -> p.handheldItem(c.get()))
            .recipe((c, p) -> {
                RegistrateRecipeProvider.netheriteSmithing(p, c.get(), RecipeCategory.TOOLS, NETHERITE_HAMMER.get());
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                        .pattern("BIB")
                        .pattern(" S ")
                        .pattern(" S ")
                        .define('B', Tags.Items.STORAGE_BLOCKS_DIAMOND)
                        .define('I', Tags.Items.GEMS_DIAMOND)
                        .define('S', Tags.Items.RODS_WOODEN)
                        .unlockedBy("has_" + c.getName(), has(c.get()))
                        .save(p, OmniUtils.loc("crafting/" + c.getName()));
            })
            .register();
    public static final ItemEntry<HammerItem> EMERALD_HAMMER = REG.item("emerald_hammer", p -> new HammerItem(1, DIAMOND, p))
            .properties(p -> p.attributes(ScytheItem.createAttributes(DIAMOND, 6.0F, -3.5F)))
            .lang("Emerald Hammer")
            .tag(ItemTags.MINING_ENCHANTABLE, ItemTags.MINING_LOOT_ENCHANTABLE, ItemTags.FIRE_ASPECT_ENCHANTABLE,
                    ItemTags.DURABILITY_ENCHANTABLE, ItemTags.SHARP_WEAPON_ENCHANTABLE, ItemTags.BREAKS_DECORATED_POTS,
                    Tags.Items.MINING_TOOL_TOOLS, Tags.Items.MELEE_WEAPON_TOOLS, Tags.Items.ENCHANTABLES)
            .model((c, p) -> p.handheldItem(c.get()))
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                    .pattern("BIB")
                    .pattern(" S ")
                    .pattern(" S ")
                    .define('B', Tags.Items.STORAGE_BLOCKS_EMERALD)
                    .define('I', Tags.Items.GEMS_EMERALD)
                    .define('S', Tags.Items.RODS_WOODEN)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
            .register();
    public static final ItemEntry<HammerItem> IRON_HAMMER = REG.item("iron_hammer", p -> new HammerItem(1, IRON, p))
            .properties(p -> p.attributes(ScytheItem.createAttributes(IRON, 7.0F, -3.5F)))
            .lang("Iron Hammer")
            .tag(ItemTags.MINING_ENCHANTABLE, ItemTags.MINING_LOOT_ENCHANTABLE, ItemTags.FIRE_ASPECT_ENCHANTABLE,
                    ItemTags.DURABILITY_ENCHANTABLE, ItemTags.SHARP_WEAPON_ENCHANTABLE, ItemTags.BREAKS_DECORATED_POTS,
                    Tags.Items.MINING_TOOL_TOOLS, Tags.Items.MELEE_WEAPON_TOOLS, Tags.Items.ENCHANTABLES)
            .model((c, p) -> p.handheldItem(c.get()))
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                    .pattern("BIB")
                    .pattern(" S ")
                    .pattern(" S ")
                    .define('B', Tags.Items.STORAGE_BLOCKS_IRON)
                    .define('I', Tags.Items.INGOTS_IRON)
                    .define('S', Tags.Items.RODS_WOODEN)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
            .register();
    public static final ItemEntry<HammerItem> GOLDEN_HAMMER = REG.item("golden_hammer", p -> new HammerItem(1, GOLD, p))
            .properties(p -> p.attributes(ScytheItem.createAttributes(GOLD, 7.0F, -3.5F)))
            .lang("Golden Hammer")
            .tag(ItemTags.MINING_ENCHANTABLE, ItemTags.MINING_LOOT_ENCHANTABLE, ItemTags.FIRE_ASPECT_ENCHANTABLE,
                    ItemTags.DURABILITY_ENCHANTABLE, ItemTags.SHARP_WEAPON_ENCHANTABLE, ItemTags.BREAKS_DECORATED_POTS,
                    Tags.Items.MINING_TOOL_TOOLS, Tags.Items.MELEE_WEAPON_TOOLS, Tags.Items.ENCHANTABLES)
            .model((c, p) -> p.handheldItem(c.get()))
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                    .pattern("BIB")
                    .pattern(" S ")
                    .pattern(" S ")
                    .define('B', Tags.Items.STORAGE_BLOCKS_GOLD)
                    .define('I', Tags.Items.INGOTS_GOLD)
                    .define('S', Tags.Items.RODS_WOODEN)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
            .register();
    public static final ItemEntry<HammerItem> COPPER_HAMMER = REG.item("copper_hammer", p -> new HammerItem(1, STONE, p))
            .properties(p -> p.attributes(ScytheItem.createAttributes(STONE, 7.0F, -3.5F)))
            .lang("Copper Hammer")
            .tag(ItemTags.MINING_ENCHANTABLE, ItemTags.MINING_LOOT_ENCHANTABLE, ItemTags.FIRE_ASPECT_ENCHANTABLE,
                    ItemTags.DURABILITY_ENCHANTABLE, ItemTags.SHARP_WEAPON_ENCHANTABLE, ItemTags.BREAKS_DECORATED_POTS,
                    Tags.Items.MINING_TOOL_TOOLS, Tags.Items.MELEE_WEAPON_TOOLS, Tags.Items.ENCHANTABLES)
            .model((c, p) -> p.handheldItem(c.get()))
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                    .pattern("BIB")
                    .pattern(" S ")
                    .pattern(" S ")
                    .define('B', Tags.Items.STORAGE_BLOCKS_COPPER)
                    .define('I', Tags.Items.INGOTS_COPPER)
                    .define('S', Tags.Items.RODS_WOODEN)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
            .register();
    public static final ItemEntry<HammerItem> STONE_HAMMER = REG.item("stone_hammer", p -> new HammerItem(1, STONE, p))
            .properties(p -> p.attributes(ScytheItem.createAttributes(STONE, 8.0F, -3.5F)))
            .lang("Stone Hammer")
            .tag(ItemTags.MINING_ENCHANTABLE, ItemTags.MINING_LOOT_ENCHANTABLE, ItemTags.FIRE_ASPECT_ENCHANTABLE,
                    ItemTags.DURABILITY_ENCHANTABLE, ItemTags.SHARP_WEAPON_ENCHANTABLE, ItemTags.BREAKS_DECORATED_POTS,
                    Tags.Items.MINING_TOOL_TOOLS, Tags.Items.MELEE_WEAPON_TOOLS, Tags.Items.ENCHANTABLES)
            .model((c, p) -> p.handheldItem(c.get()))
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                    .pattern("BIB")
                    .pattern(" S ")
                    .pattern(" S ")
                    .define('B', AllBlocks.BORDER_STONE)
                    .define('I', ItemTags.STONE_TOOL_MATERIALS)
                    .define('S', Tags.Items.RODS_WOODEN)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
            .register();
    public static final ItemEntry<HammerItem> WOODEN_HAMMER = REG.item("wooden_hammer", p -> new HammerItem(1, WOOD, p))
            .properties(p -> p.attributes(ScytheItem.createAttributes(WOOD, 7.0F, -3.5F)))
            .lang("Wooden Hammer")
            .tag(ItemTags.MINING_ENCHANTABLE, ItemTags.MINING_LOOT_ENCHANTABLE, ItemTags.FIRE_ASPECT_ENCHANTABLE,
                    ItemTags.DURABILITY_ENCHANTABLE, ItemTags.SHARP_WEAPON_ENCHANTABLE, ItemTags.BREAKS_DECORATED_POTS,
                    Tags.Items.MINING_TOOL_TOOLS, Tags.Items.MELEE_WEAPON_TOOLS, Tags.Items.ENCHANTABLES)
            .model((c, p) -> p.handheldItem(c.get()))
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                    .pattern("BIB")
                    .pattern(" S ")
                    .pattern(" S ")
                    .define('B', ItemTags.LOGS)
                    .define('I', ItemTags.PLANKS)
                    .define('S', Tags.Items.RODS_WOODEN)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, OmniUtils.loc("crafting/" + c.getName())))
            .register();

    public static void register() {}
}
