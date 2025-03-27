package uwu.lopyluna.omni_util.register;

import com.tterrag.registrate.builders.BlockEntityBuilder;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.entity.BlockEntity;
import uwu.lopyluna.omni_util.content.blocks.generator.ConsumorBE;
import uwu.lopyluna.omni_util.content.blocks.generator.GeneratorBE;
import uwu.lopyluna.omni_util.content.blocks.panels.LunarPanelBE;
import uwu.lopyluna.omni_util.content.blocks.panels.SolarPanelBE;

import static uwu.lopyluna.omni_util.OmniUtils.REG;

@SuppressWarnings("unused")
public class AllBlockEntities {

    public static final BlockEntityEntry<GeneratorBE> GENERATOR = simpleBE("generator", AllBlocks.GENERATOR, GeneratorBE::new);
    public static final BlockEntityEntry<ConsumorBE> CONSUMOR = simpleBE("consumor", AllBlocks.CONSUMOR, ConsumorBE::new);
    public static final BlockEntityEntry<SolarPanelBE> SOLAR_PANEL = simpleBE("solar_panel", AllBlocks.SOLAR_PANEL, SolarPanelBE::new);
    public static final BlockEntityEntry<LunarPanelBE> LUNAR_PANEL = simpleBE("lunar_panel", AllBlocks.LUNAR_PANEL, LunarPanelBE::new);


    public static <T extends BlockEntity> BlockEntityEntry<T> simpleBE(String name, BlockEntry<?> entry, BlockEntityBuilder.BlockEntityFactory<T> factory) {
        return REG.blockEntity(name, factory).validBlock(entry).register();
    }

    public static void register() {}
}
