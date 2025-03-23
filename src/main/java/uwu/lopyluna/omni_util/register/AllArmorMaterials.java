package uwu.lopyluna.omni_util.register;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import uwu.lopyluna.omni_util.OmniUtils;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("SameParameterValue")
public class AllArmorMaterials {
    private static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, OmniUtils.MOD_ID);

    public static final Holder<ArmorMaterial> GOGGLES = register(
            "goggles",
            new int[] { 1, 1, 1, 1, 1 },
            25,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            0.0F,
            0.0F,
            () -> Ingredient.of(Items.GOLD_INGOT)
    );
    public static final Holder<ArmorMaterial> SONAR_GOGGLES = register(
            "sonar_goggles",
            new int[] { 1, 1, 1, 1, 1 },
            12,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            0.0F,
            0.0F,
            () -> Ingredient.of(Items.IRON_INGOT)
    );

    private static Holder<ArmorMaterial> register(String name, int[] defense, int enchantmentValue, Holder<SoundEvent> equipSound,
        float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        List<ArmorMaterial.Layer> list = List.of(new ArmorMaterial.Layer(OmniUtils.loc(name)));
        return register(name, defense, enchantmentValue, equipSound, toughness, knockbackResistance, repairIngredient, list);
    }

    private static Holder<ArmorMaterial> register(String name, int[] defense, int enchantmentValue, Holder<SoundEvent> equipSound,
        float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient, List<ArmorMaterial.Layer> layers) {
        EnumMap<ArmorItem.Type, Integer> enummap = new EnumMap<>(ArmorItem.Type.class);
        for (ArmorItem.Type armoritem$type : ArmorItem.Type.values()) enummap.put(armoritem$type, defense[armoritem$type.ordinal()]);
        return ARMOR_MATERIALS.register(name, () -> new ArmorMaterial(enummap, enchantmentValue, equipSound, repairIngredient, layers, toughness, knockbackResistance));
    }

    public static void register(IEventBus eventBus) {
        ARMOR_MATERIALS.register(eventBus);
    }
}
