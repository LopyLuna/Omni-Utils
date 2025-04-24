package uwu.lopyluna.omni_util.register;

import com.tterrag.registrate.builders.BlockEntityBuilder;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import uwu.lopyluna.omni_util.content.blocks.colored_block.ColoredBE;
import uwu.lopyluna.omni_util.content.blocks.colored_block.ColoredBlockRenderer;
import uwu.lopyluna.omni_util.content.blocks.generator.ConsumorBE;
import uwu.lopyluna.omni_util.content.blocks.generator.GeneratorBE;
import uwu.lopyluna.omni_util.content.blocks.grim_devour.GrimDevourBE;
import uwu.lopyluna.omni_util.content.blocks.panels.LunarPanelBE;
import uwu.lopyluna.omni_util.content.blocks.panels.SolarPanelBE;
import uwu.lopyluna.omni_util.content.blocks.power_crank.PowerCrankBE;
import uwu.lopyluna.omni_util.content.blocks.spawner.AlteredSpawnerBE;
import uwu.lopyluna.omni_util.content.blocks.spawner.AlteredSpawnerRenderer;
import uwu.lopyluna.omni_util.content.blocks.trash_can.TrashCanBE;

import static uwu.lopyluna.omni_util.OmniUtils.REG;

@SuppressWarnings("unused")
public class AllBlockEntities {

    public static final BlockEntityEntry<PowerCrankBE> POWER_CRANK = simpleBE("power_crank", AllBlocks.POWER_CRANK, PowerCrankBE::new);
    public static final BlockEntityEntry<TrashCanBE> TRASH_CAN = simpleBE("trash_can", AllBlocks.TRASH_CAN, TrashCanBE::new);
    public static final BlockEntityEntry<ColoredBE> COLORED_BLOCK = simpleBE("colored_block", AllBlocks.COLORED_BLOCK, ColoredBlockRenderer::new, ColoredBE::new);
    public static final BlockEntityEntry<GrimDevourBE> GRIM_DEVOUR = simpleBE("grim_devour", AllBlocks.GRIM_DEVOUR, GrimDevourBE::new);
    public static final BlockEntityEntry<AlteredSpawnerBE> SPAWNER = simpleBE("altered_spawner", AllBlocks.SPAWNER, AlteredSpawnerRenderer::new, AlteredSpawnerBE::new);
    public static final BlockEntityEntry<GeneratorBE> GENERATOR = simpleBE("generator", AllBlocks.GENERATOR, GeneratorBE::new);
    public static final BlockEntityEntry<ConsumorBE> CONSUMOR = simpleBE("consumor", AllBlocks.CONSUMOR, ConsumorBE::new);
    public static final BlockEntityEntry<SolarPanelBE> SOLAR_PANEL = simpleBE("solar_panel", AllBlocks.SOLAR_PANEL, SolarPanelBE::new);
    public static final BlockEntityEntry<LunarPanelBE> LUNAR_PANEL = simpleBE("lunar_panel", AllBlocks.LUNAR_PANEL, LunarPanelBE::new);



    public static <T extends BlockEntity> BlockEntityEntry<T> simpleBE(String name, BlockEntry<?> entry, BlockEntityBuilder.BlockEntityFactory<T> factory) {
        return REG.blockEntity(name, factory).validBlock(entry).register();
    }
    public static <T extends BlockEntity> BlockEntityEntry<T> simpleBE(String name, BlockEntry<?> entry, NonNullFunction<BlockEntityRendererProvider.Context, BlockEntityRenderer<? super T>> renderer, BlockEntityBuilder.BlockEntityFactory<T> factory) {
        return REG.blockEntity(name, factory).renderer(() -> renderer).validBlock(entry).register();
    }


    public static void register() {}
}
