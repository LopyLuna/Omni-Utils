package uwu.lopyluna.omni_util.register;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import uwu.lopyluna.omni_util.content.blocks.generator.ConsumorBE;
import uwu.lopyluna.omni_util.content.blocks.generator.GeneratorBE;

import static uwu.lopyluna.omni_util.OmniUtils.REG;

@SuppressWarnings("unused")
public class AllBlockEntities {

    public static final BlockEntityEntry<GeneratorBE> GENERATOR = REG.blockEntity("generator_be", GeneratorBE::new)
            .validBlock(AllBlocks.GENERATOR)
            .register();

    public static final BlockEntityEntry<ConsumorBE> CONSUMOR = REG.blockEntity("consumor_be", ConsumorBE::new)
            .validBlock(AllBlocks.CONSUMOR)
            .register();

    public static void register() {}
}
