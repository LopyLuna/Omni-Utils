package uwu.lopyluna.omni_util.content.datagen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.omni_util.content.utils.Registration;
import uwu.lopyluna.omni_util.content.utils.builders.BlockBuilder;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Set;
import java.util.function.Function;

@ParametersAreNonnullByDefault
public class BlockLootTablesProvider extends BlockLootSubProvider {

    public BlockLootTablesProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        BlockBuilder.getEntries().forEach(blockEntry -> blockEntry.lootTable(this));
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return Registration.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }

    @Override
    public void addNetherVinesDropTable(Block vines, Block plant) {
        HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        LootTable.Builder loottable$builder = this.createSilkTouchOrShearsDispatchTable(
                vines,
                LootItem.lootTableItem(vines)
                        .when(BonusLevelTableCondition.bonusLevelFlatChance(registrylookup.getOrThrow(Enchantments.FORTUNE), 0.33F, 0.55F, 0.77F, 1.0F))
        );
        this.add(vines, loottable$builder);
        this.add(plant, loottable$builder);
    }

    @Override
    public LootTable.@NotNull Builder createSingleItemTableWithSilkTouch(Block block, ItemLike item) {
        return this.createSilkTouchDispatchTable(block, this.applyExplosionCondition(block, LootItem.lootTableItem(item)));
    }

    public void dropLikeNetherVines(Block block) {
        HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        LootTable.Builder loottable$builder = this.createSilkTouchOrShearsDispatchTable(block,
                LootItem.lootTableItem(block).when(BonusLevelTableCondition.bonusLevelFlatChance(registrylookup.getOrThrow(Enchantments.FORTUNE), 0.33F, 0.55F, 0.77F, 1.0F))
        );
        this.add(block, loottable$builder);
    }

    @Override
    public void dropOther(Block block, ItemLike item) {
        super.dropOther(block, item);
    }

    @Override
    public void dropPottedContents(Block flowerPot) {
        super.dropPottedContents(flowerPot);
    }

    @Override
    public void dropSelf(Block block) {
        super.dropSelf(block);
    }

    @Override
    public void dropWhenSilkTouch(Block block) {
        super.dropWhenSilkTouch(block);
    }

    @Override
    public void add(Block block, LootTable.Builder builder) {
        super.add(block, builder);
    }

    @Override
    public void add(Block block, Function<Block, LootTable.Builder> factory) {
        super.add(block, factory);
    }
}
