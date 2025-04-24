package uwu.lopyluna.omni_util.content.utils;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import static net.minecraft.core.registries.Registries.*;
import static uwu.lopyluna.omni_util.OmniUtils.MOD_ID;

@SuppressWarnings({"unused", "removal"})
public record OmniRegistration(String modID) {

    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(MOD_ID);
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
        System.out.println("Registering Omni Utils Data Components...");
        DATA_COMPONENTS.register(bus);
        System.out.println("Registering Omni Utils Creative Tabs...");
        CREATIVE_MODE_TABS.register(bus);
        System.out.println("Registering Omni Utils Damage Types...");
        DAMAGES.register(bus);
        System.out.println("Registering Omni Utils Mob Effects...");
        MOB_EFFECTS.register(bus);
        System.out.println("Registering Omni Utils Particles...");
        PARTICLES.register(bus);
        System.out.println("Registering Omni Utils Recipes Serializers...");
        RECIPE_SERIALIZERS.register(bus);
        System.out.println("Registering Omni Utils Recipes...");
        RECIPES.register(bus);
        System.out.println("Registering Omni Utils Menus...");
        MENUS.register(bus);
        System.out.println("Registering Omni Utils Sounds...");
        SOUNDS.register(bus);
        System.out.println("Registering Omni Utils Dimensions...");
        DIMENSIONS.register(bus);

        System.out.println("Registering Omni Utils Done");
    }
}
