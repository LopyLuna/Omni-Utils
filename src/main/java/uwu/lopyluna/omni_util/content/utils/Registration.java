package uwu.lopyluna.omni_util.content.utils;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import uwu.lopyluna.omni_util.content.utils.builders.BlockBuilder;
import uwu.lopyluna.omni_util.content.utils.builders.BlockEntityBuilder;
import uwu.lopyluna.omni_util.content.utils.builders.BlockItemBuilder;
import uwu.lopyluna.omni_util.content.utils.builders.ItemBuilder;
import uwu.lopyluna.omni_util.content.utils.entry.ItemEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static net.minecraft.core.registries.Registries.*;
import static uwu.lopyluna.omni_util.OmniUtils.MOD_ID;

@SuppressWarnings({"unused", "removal"})
public record Registration(String modID) {

    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(FLUID, MOD_ID);
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BLOCK_ENTITY_TYPE, MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);
    public static final DeferredRegister<DamageType> DAMAGES = DeferredRegister.create(DAMAGE_TYPE, MOD_ID);
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(MOB_EFFECT, MOD_ID);
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(PARTICLE_TYPE, MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(RECIPE_SERIALIZER, MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPES = DeferredRegister.create(RECIPE_TYPE, MOD_ID);
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(MENU, MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(SOUND_EVENT, MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(CREATIVE_MODE_TAB, MOD_ID);
    public static final DeferredRegister<Level> DIMENSIONS = DeferredRegister.create(DIMENSION, MOD_ID);

    public DeferredRegister.DataComponents components() {return DATA_COMPONENTS;}
    public DeferredRegister<? extends Fluid> fluids() {return FLUIDS;}
    public DeferredRegister.Blocks blocks() {return BLOCKS;}
    public <T extends Block> BlockBuilder<T> block(String name, Function<BlockBehaviour.Properties, T> factory) {
        return new BlockBuilder<>(name, factory, blocks(), items());
    }
    public DeferredRegister<BlockEntityType<?>> blockEntities() {return BLOCK_ENTITIES;}
    @SuppressWarnings("all")
    public <T extends BlockEntity> BlockEntityBuilder<T> blockEntity(String name, BlockEntityType.BlockEntitySupplier<T> pFactory, Block... pValidBlocks) {
        return new BlockEntityBuilder<T>(blockEntities(), name, pFactory);
    }
    public DeferredRegister.Items items() {return ITEMS;}
    public <T extends Item> ItemBuilder<T> item(String name, Function<Item.Properties, T> factory) {
        return new ItemBuilder<>(name, factory, items());
    }
    public DeferredRegister<CreativeModeTab> creativeTab() {return CREATIVE_MODE_TABS;}
    public DeferredRegister<DamageType> damages() {return DAMAGES;}
    public DeferredRegister<MobEffect> mobEffects() {return MOB_EFFECTS;}
    public DeferredRegister<ParticleType<?>> particles() {return PARTICLES;}
    public DeferredRegister<RecipeSerializer<?>> recipe_ser() {return RECIPE_SERIALIZERS;}
    public DeferredRegister<RecipeType<?>> recipes() {return RECIPES;}
    public DeferredRegister<MenuType<?>> menus() {return MENUS;}
    public DeferredRegister<SoundEvent> sounds() {return SOUNDS;}
    public DeferredRegister<Level> dimensions() {return DIMENSIONS;}

    public void register(IEventBus bus) {
        System.out.println("Registering Data Components...");
        DATA_COMPONENTS.register(bus);
        System.out.println("Registering Fluids...");
        FLUIDS.register(bus);
        System.out.println("Registering Blocks...");
        BLOCKS.register(bus);
        System.out.println("Registering Items...");
        ITEMS.register(bus);
        System.out.println("Registering Block Entities...");
        BLOCK_ENTITIES.register(bus);
        System.out.println("Registering Creative Tabs...");
        CREATIVE_MODE_TABS.register(bus);
        System.out.println("Registering Damage Types...");
        DAMAGES.register(bus);
        System.out.println("Registering Mob Effects...");
        MOB_EFFECTS.register(bus);
        System.out.println("Registering Particles...");
        PARTICLES.register(bus);
        System.out.println("Registering Recipes Serializers...");
        RECIPE_SERIALIZERS.register(bus);
        System.out.println("Registering Recipes...");
        RECIPES.register(bus);
        System.out.println("Registering Menus...");
        MENUS.register(bus);
        System.out.println("Registering Sounds...");
        SOUNDS.register(bus);
        System.out.println("Registering Dimensions...");
        DIMENSIONS.register(bus);

        System.out.println("Registering Completed");
    }

    public CreativeModeTab.DisplayItemsGenerator itemForCreativeTab() {
        return (parameters, output) -> getItemEntries().forEach(entry -> { var item = entry.inTab(); if (item != null) output.accept(item); });
    }

    public static List<ItemEntry<? extends Item>> getItemEntries() {
        List<ItemEntry<? extends Item>> items = new ArrayList<>(List.of());
        if (!BlockItemBuilder.getEntries().isEmpty())
            items.addAll(BlockItemBuilder.getEntries());
        if (!ItemBuilder.getEntries().isEmpty())
            items.addAll(ItemBuilder.getEntries());
        return items;
    }

    private static String convertToLangName(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1).replace('_', ' ');
    }
}
