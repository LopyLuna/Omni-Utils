package uwu.lopyluna.omni_util.events;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import uwu.lopyluna.omni_util.content.datagen.*;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static uwu.lopyluna.omni_util.OmniUtils.MOD_ID;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataEvents {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existing = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new BlockTagProvider(output, provider, existing));
        generator.addProvider(event.includeServer(), new RegisterDataProvider(output, provider));
        generator.addProvider(event.includeServer(), new LootTableProvider(output, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(BlockLootTablesProvider::new, LootContextParamSets.BLOCK)), provider));

        generator.addProvider(event.includeClient(), new LangProvider(output));
        generator.addProvider(event.includeClient(), new ItemModelProvider(output, existing));
        generator.addProvider(event.includeClient(), new BlockStateProvider(output, existing));
    }
}
