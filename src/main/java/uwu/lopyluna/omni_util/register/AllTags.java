package uwu.lopyluna.omni_util.register;

import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import uwu.lopyluna.omni_util.OmniUtils;

import java.util.function.Function;
import java.util.stream.Stream;

import static uwu.lopyluna.omni_util.OmniUtils.REG;

@SuppressWarnings({"deprecation", "unused"})
public class AllTags {
    public static final TagKey<Block> MINEABLE_WITH_PAXEL = block("mineable/paxel");
    public static final TagKey<Block> MINEABLE_WITH_SCYTHE = block("mineable/scythe");
    public static final TagKey<Block> BULK_MINEABLE_WITH_SCYTHE = block("bulk_mineable/scythe");
    public static final TagKey<Block> MINEABLE_WITH_HAMMER = block("mineable/hammer");

    public static void addGenerators() {
        REG.generateTags();
    }

    public static void genBlockTags(RegistrateTagsProvider<Block> provIn) {
        TagsProvider<Block> prov = new TagsProvider<>(provIn, Block::builtInRegistryHolder);
        prov.tag(MINEABLE_WITH_PAXEL)
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE)
                .addTag(BlockTags.MINEABLE_WITH_AXE)
                .addTag(BlockTags.MINEABLE_WITH_SHOVEL)
                .addTag(BlockTags.MINEABLE_WITH_HOE)
                .addTag(BlockTags.SWORD_EFFICIENT);

        prov.tag(MINEABLE_WITH_HAMMER)
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE)
                .addTag(BlockTags.MINEABLE_WITH_AXE);

        prov.tag(MINEABLE_WITH_SCYTHE)
                .addTag(BlockTags.MINEABLE_WITH_HOE)
                .addTag(BlockTags.SWORD_EFFICIENT);

        prov.tag(BULK_MINEABLE_WITH_SCYTHE)
                .add(Blocks.PUMPKIN)
                .add(Blocks.CARVED_PUMPKIN)
                .add(Blocks.JACK_O_LANTERN)
                .add(Blocks.MELON)
                .addTag(BlockTags.CAVE_VINES)
                .addTag(BlockTags.CROPS)
                .addTag(BlockTags.FLOWERS)
                .addTag(BlockTags.LEAVES)
                .addTag(BlockTags.SAPLINGS)
                .addTag(BlockTags.REPLACEABLE)
                .addTag(BlockTags.SMALL_FLOWERS)
                .addTag(BlockTags.TALL_FLOWERS);

        prov.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(Blocks.REINFORCED_DEEPSLATE)
                .add(Blocks.TRIAL_SPAWNER)
                .add(Blocks.VAULT)
                .add(Blocks.BEACON)
                .add(Blocks.GLOWSTONE)
                .add(Blocks.REDSTONE_LAMP)
                .add(Blocks.SEA_LANTERN)
                .add(Blocks.LEVER)
                .add(Blocks.FROSTED_ICE)
                .addTag(Tags.Blocks.GLASS_BLOCKS)
                .addTag(Tags.Blocks.GLASS_PANES)
                .addTag(Tags.Blocks.SKULLS)
        ;
        prov.tag(BlockTags.MINEABLE_WITH_AXE)
                .add(Blocks.BAMBOO_SAPLING)
                .addTag(BlockTags.BEDS)
        ;
        prov.tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(Blocks.CAKE)
                .addTag(BlockTags.CANDLE_CAKES)
        ;
        prov.tag(BlockTags.MINEABLE_WITH_HOE)
                .add(Blocks.OCHRE_FROGLIGHT)
                .add(Blocks.PEARLESCENT_FROGLIGHT)
                .add(Blocks.VERDANT_FROGLIGHT)
                .add(Blocks.HONEYCOMB_BLOCK)
                .add(Blocks.CACTUS)
                .add(Blocks.CAKE)
                .addTag(BlockTags.CANDLE_CAKES)
                .addTag(BlockTags.WOOL_CARPETS)
        ;
        prov.tag(BlockTags.SWORD_EFFICIENT)
                .add(Blocks.OCHRE_FROGLIGHT)
                .add(Blocks.PEARLESCENT_FROGLIGHT)
                .add(Blocks.VERDANT_FROGLIGHT)
                .add(Blocks.HONEYCOMB_BLOCK)
                .add(Blocks.COBWEB)
                .add(Blocks.BAMBOO)
                .add(Blocks.BAMBOO_SAPLING)
                .add(Blocks.CACTUS)
                .add(Blocks.CAKE)
                .addTag(BlockTags.CANDLE_CAKES)
                .addTag(BlockTags.CANDLES)
                .addTag(BlockTags.WOOL_CARPETS)
        ;
    }

    public static void genItemTags(RegistrateTagsProvider<Item> provIn) {
        TagsProvider<Item> prov = new TagsProvider<>(provIn, Item::builtInRegistryHolder);


    }


    private static TagKey<Block> block(String name) { return TagKey.create(Registries.BLOCK, OmniUtils.loc(name)); }
    private static TagKey<Block> blockMC(String name) { return TagKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace(name)); }
    private static TagKey<Item> item(String name) { return TagKey.create(Registries.ITEM, OmniUtils.loc(name)); }
    private static TagKey<Item> itemMC(String name) { return TagKey.create(Registries.ITEM, ResourceLocation.withDefaultNamespace(name)); }

    public static class TagsProvider<T> {

        private final RegistrateTagsProvider<T> provider;
        private final Function<T, ResourceKey<T>> keyExtractor;

        public TagsProvider(RegistrateTagsProvider<T> provider, Function<T, Holder.Reference<T>> refExtractor) {
            this.provider = provider;
            this.keyExtractor = refExtractor.andThen(Holder.Reference::key);
        }

        public TagAppender<T> tag(TagKey<T> tag) {
            TagBuilder tagbuilder = getOrCreateRawBuilder(tag);
            return new TagAppender<>(tagbuilder, keyExtractor);
        }

        public TagBuilder getOrCreateRawBuilder(TagKey<T> tag) {
            return provider.addTag(tag).getInternalBuilder();
        }

    }

    public static class TagAppender<T> extends net.minecraft.data.tags.TagsProvider.TagAppender<T> {

        private final Function<T, ResourceKey<T>> keyExtractor;

        public TagAppender(TagBuilder pBuilder, Function<T, ResourceKey<T>> pKeyExtractor) {
            super(pBuilder);
            this.keyExtractor = pKeyExtractor;
        }

        public TagAppender<T> add(T entry) {
            this.add(this.keyExtractor.apply(entry));
            return this;
        }

        @SafeVarargs
        public final TagAppender<T> add(T... entries) {
            Stream.of(entries)
                    .map(this.keyExtractor)
                    .forEach(this::add);
            return this;
        }

    }
}
