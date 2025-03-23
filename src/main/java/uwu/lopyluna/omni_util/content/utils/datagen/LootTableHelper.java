package uwu.lopyluna.omni_util.content.utils.datagen;

import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;

public class LootTableHelper {
    public static <T extends Block> void dropLikeNetherVines(RegistrateBlockLootTables p, T c) {
        HolderLookup.RegistryLookup<Enchantment> registrylookup = p.getRegistries().lookupOrThrow(Registries.ENCHANTMENT);
        LootTable.Builder loottable$builder = p.createSilkTouchOrShearsDispatchTable(c,
                LootItem.lootTableItem(c).when(BonusLevelTableCondition.bonusLevelFlatChance(registrylookup.getOrThrow(Enchantments.FORTUNE), 0.33F, 0.55F, 0.77F, 1.0F))
        );
        p.add(c, loottable$builder);
    }

    public static <T extends Block> void dropSelfSilkTouchOrOther(RegistrateBlockLootTables p, T c, Block other) {
        p.add(c, p.createSingleItemTableWithSilkTouch(c, other));
    }
}
