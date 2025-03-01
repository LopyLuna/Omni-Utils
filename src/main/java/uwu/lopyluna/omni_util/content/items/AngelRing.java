package uwu.lopyluna.omni_util.content.items;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.omni_util.register.AllDataComponents;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

import static net.neoforged.neoforge.common.NeoForgeMod.CREATIVE_FLIGHT;
import static uwu.lopyluna.omni_util.register.AllPowerSources.ANGEL_FLIGHT;

@ParametersAreNonnullByDefault
public class AngelRing extends BasePowerItem implements Equipable {
    Player ownerPlayer;

    public AngelRing(Properties pProperties) {
        super(pProperties.component(AllDataComponents.WINGS_COMPONENTS.value(), "angel"));
    }

    public String getWingType(ItemStack stack) {
        return stack.getOrDefault(AllDataComponents.WINGS_COMPONENTS.get(), "angel");
    }

    @Override
    public @NotNull String getDescriptionId(ItemStack stack) {
        var data = stack.get(AllDataComponents.WINGS_COMPONENTS.get());
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
    public boolean isGenerator() {
        return false;
    }

    @Override
    public int getPower() {
        return ANGEL_FLIGHT.power;
    }

    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.CHEST;
    }

    @Override
    public boolean getFlag(ServerPlayer pPlayer) {
        return pPlayer.getItemBySlot(EquipmentSlot.CHEST).is(this) && super.getFlag(pPlayer);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (level.isClientSide && entity instanceof Player player) ownerPlayer = player;
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    @Override
    public boolean clientFlag(ItemStack stack, Item item) {
        //Big Check
        var chestItem = ownerPlayer.getItemBySlot(EquipmentSlot.CHEST);
        return ownerPlayer != null &&
                chestItem.equals(stack) &&
                chestItem.is(this) &&
                chestItem.is(item) &&
                chestItem.is(stack.getItem());
    }

    @Override
    public void runActive(Level pLevel, ItemStack pStack, ServerPlayer pPlayer) {
        var flight = Objects.requireNonNull(pPlayer.getAttributes().getInstance(CREATIVE_FLIGHT));
        if (!flight.hasModifier(ANGEL_FLIGHT.id)) flight.addTransientModifier(new AttributeModifier(ANGEL_FLIGHT.id, 1, AttributeModifier.Operation.ADD_VALUE));
    }

    @Override
    public void runFailed(Level pLevel, ItemStack pStack, ServerPlayer pPlayer) {
        var flight = Objects.requireNonNull(pPlayer.getAttributes().getInstance(CREATIVE_FLIGHT));
        if (flight.hasModifier(ANGEL_FLIGHT.id)) flight.removeModifier(ANGEL_FLIGHT.id);
    }
}
