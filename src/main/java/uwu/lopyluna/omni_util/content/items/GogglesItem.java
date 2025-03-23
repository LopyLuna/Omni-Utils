package uwu.lopyluna.omni_util.content.items;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

import static uwu.lopyluna.omni_util.register.AllArmorMaterials.GOGGLES;

@ParametersAreNonnullByDefault
public class GogglesItem extends ArmorItem {
    public GogglesItem(Properties properties) {
        super(GOGGLES, ArmorItem.Type.HELMET, properties.durability(ArmorItem.Type.HELMET.getDurability(25)));
    }

    @Override
    public @Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return layer.texture(true);
    }
}
