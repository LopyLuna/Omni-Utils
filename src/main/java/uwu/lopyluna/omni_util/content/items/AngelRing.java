package uwu.lopyluna.omni_util.content.items;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.omni_util.content.items.base.PowerItem;
import uwu.lopyluna.omni_util.register.AllDataComponents;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

import static net.neoforged.neoforge.common.NeoForgeMod.CREATIVE_FLIGHT;
import static uwu.lopyluna.omni_util.register.AllPowerSources.ANGEL_FLIGHT;

@ParametersAreNonnullByDefault
public class AngelRing extends PowerItem implements Equipable {

    public AngelRing(Properties pProperties) {
        super(pProperties.component(AllDataComponents.WING_TYPE.value(), "angel"), ANGEL_FLIGHT);
    }

    public String getWingType(ItemStack stack) {
        return stack.getOrDefault(AllDataComponents.WING_TYPE.get(), "angel");
    }

    @Override
    public @NotNull String getDescriptionId(ItemStack stack) {
        var data = stack.get(AllDataComponents.WING_TYPE.get());
        var path = "item.omni_util.";
        var pathEnd = "_angel_ring";
        var def = super.getDescriptionId(stack);
        return data != null ? switch (data) {
            case "feathered" -> path + "feathered" + pathEnd;
            case "demon" -> path + "demon" + pathEnd;
            case "gilded" -> path + "gilded" + pathEnd;
            case "bat" -> path + "bat" + pathEnd;
            default -> def;
        } : def;
    }

    @Override
    public boolean isActive(Level pLevel, ItemStack pStack, Player pPlayer, boolean pInArmorSlot) {
        return pInArmorSlot && super.isActive(pLevel, pStack, pPlayer, true);
    }

    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.CHEST;
    }

    @Override
    public boolean isEquipped(Level pLevel, ItemStack pStack, Player pPlayer) {
        return pStack.equals(pPlayer.getItemBySlot(EquipmentSlot.CHEST));
    }

    @Override
    public void onActive(Level pLevel, ItemStack pStack, Player pPlayer) {
        super.onActive(pLevel, pStack, pPlayer);
        var flight = Objects.requireNonNull(pPlayer.getAttributes().getInstance(CREATIVE_FLIGHT));
        if (!flight.hasModifier(ANGEL_FLIGHT.id)) flight.addTransientModifier(new AttributeModifier(ANGEL_FLIGHT.id, 1, AttributeModifier.Operation.ADD_VALUE));
    }

    @Override
    public void onFailed(Level pLevel, ItemStack pStack, Player pPlayer) {
        super.onFailed(pLevel, pStack, pPlayer);
        var flight = Objects.requireNonNull(pPlayer.getAttributes().getInstance(CREATIVE_FLIGHT));
        if (flight.hasModifier(ANGEL_FLIGHT.id)) flight.removeModifier(ANGEL_FLIGHT.id);
    }
}
