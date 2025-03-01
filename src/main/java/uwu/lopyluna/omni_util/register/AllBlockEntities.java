package uwu.lopyluna.omni_util.register;

import uwu.lopyluna.omni_util.content.blocks.generator.GeneratorBE;
import uwu.lopyluna.omni_util.content.blocks.generator.GeneratorBlock;
import uwu.lopyluna.omni_util.content.utils.entry.BlockEntityEntry;

import static uwu.lopyluna.omni_util.OmniUtils.REG;

@SuppressWarnings("unused")
public class AllBlockEntities {

    public static final BlockEntityEntry<GeneratorBE> GENERATOR = REG.blockEntity("generator_be", GeneratorBE::new)
            .validBlocks(() -> new GeneratorBlock[]{AllBlocks.GENERATOR.get()})
            .register();

    public static void register() {}
}
