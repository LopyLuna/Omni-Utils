package uwu.lopyluna.omni_util.content.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import uwu.lopyluna.omni_util.content.utils.builders.BlockBuilder;

import static uwu.lopyluna.omni_util.OmniUtils.MOD_ID;

public class BlockStateProvider extends net.neoforged.neoforge.client.model.generators.BlockStateProvider {
    public BlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        BlockBuilder.getEntries().forEach(blockEntry -> blockEntry.blockState(this));
    }

    public void blockWithItem(Block block) {
        simpleBlockWithItem(block, cubeAll(block));
    }
}
