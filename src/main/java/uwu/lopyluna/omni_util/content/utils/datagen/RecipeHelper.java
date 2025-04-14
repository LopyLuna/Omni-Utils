package uwu.lopyluna.omni_util.content.utils.datagen;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import uwu.lopyluna.omni_util.OmniUtils;
import uwu.lopyluna.omni_util.register.AllBlocks;

import static com.tterrag.registrate.providers.RegistrateRecipeProvider.has;

public class RecipeHelper {
    public static <T extends Block> void spikeRecipe(DataGenContext<Block, T> c, RegistrateRecipeProvider p, ItemLike i, ItemLike b) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 2)
                .pattern(" I ")
                .pattern("IBI")
                .pattern("PPP")
                .define('I', i)
                .define('B', b)
                .define('P', AllBlocks.POLISHED_STONE.get())
                .unlockedBy("has_" + c.getName(), has(c.get()))
                .save(p, OmniUtils.loc("crafting/" + c.getName()));
    }
}
