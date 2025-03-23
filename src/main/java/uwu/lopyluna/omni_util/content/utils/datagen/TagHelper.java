package uwu.lopyluna.omni_util.content.utils.datagen;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;

public class TagHelper {

    public static TagKey<Block> mineablePickaxe() {
        return BlockTags.MINEABLE_WITH_PICKAXE;
    }

    public static TagKey<Block> mineableAxe() {
        return BlockTags.MINEABLE_WITH_AXE;
    }

    public static TagKey<Block> mineableHoe() {
        return BlockTags.MINEABLE_WITH_HOE;
    }

    public static TagKey<Block> mineableShovel() {
        return BlockTags.MINEABLE_WITH_SHOVEL;
    }

    public static TagKey<Block> mineableSword() {
        return BlockTags.SWORD_EFFICIENT;
    }

    public static TagKey<Block> badDiamondTools() {
        return BlockTags.INCORRECT_FOR_DIAMOND_TOOL;
    }

    public static TagKey<Block> needDiamondTools() {
        return BlockTags.NEEDS_DIAMOND_TOOL;
    }

    public static TagKey<Block> badIronTools() {
        return BlockTags.INCORRECT_FOR_IRON_TOOL;
    }

    public static TagKey<Block> needIronTools() {
        return BlockTags.NEEDS_IRON_TOOL;
    }

    public static TagKey<Block> needStoneTools() {
        return BlockTags.NEEDS_STONE_TOOL;
    }

    public static TagKey<Block> badStoneTools() {
        return BlockTags.INCORRECT_FOR_STONE_TOOL;
    }

    public static TagKey<Block> needGoldTools() {
        return Tags.Blocks.NEEDS_GOLD_TOOL;
    }

    public static TagKey<Block> badGoldTools() {
        return BlockTags.INCORRECT_FOR_GOLD_TOOL;
    }

    public static TagKey<Block> needWoodTools() {
        return Tags.Blocks.NEEDS_WOOD_TOOL;
    }

    public static TagKey<Block> badWoodTools() {
        return BlockTags.INCORRECT_FOR_WOODEN_TOOL;
    }

    public static TagKey<Block> needNetheriteTools() {
        return Tags.Blocks.NEEDS_NETHERITE_TOOL;
    }

    public static TagKey<Block> badNetheriteTools() {
        return BlockTags.INCORRECT_FOR_NETHERITE_TOOL;
    }
}
