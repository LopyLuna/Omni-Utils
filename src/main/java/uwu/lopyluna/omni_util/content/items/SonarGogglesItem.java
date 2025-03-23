package uwu.lopyluna.omni_util.content.items;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

import static uwu.lopyluna.omni_util.register.AllArmorMaterials.SONAR_GOGGLES;

@ParametersAreNonnullByDefault
public class SonarGogglesItem extends ArmorItem {
    public SonarGogglesItem(Properties properties) {
        super(SONAR_GOGGLES, Type.HELMET, properties.durability(Type.HELMET.getDurability(25)));
    }


    @Override
    public @Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return layer.texture(true);
    }
}
