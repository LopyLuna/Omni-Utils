package uwu.lopyluna.omni_util.content.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import uwu.lopyluna.omni_util.client.ClientRPData;
import uwu.lopyluna.omni_util.content.power.PowerManager;
import uwu.lopyluna.omni_util.content.power.PowerTickHandler;
import uwu.lopyluna.omni_util.network.SyncActivationToClientPacket;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.UUID;

@ParametersAreNonnullByDefault
public abstract class BasePowerItem extends Item {
    private UUID owner;
    private boolean registered = false;
    public BasePowerItem(Properties properties) {
        super(properties);
    }

    public abstract boolean isGenerator();

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        boolean shift = tooltipFlag.hasShiftDown();
        tooltipComponents.add(Component.empty()
                .append(Component.literal("Hold [").withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal("Shift").withStyle(shift ? ChatFormatting.WHITE : ChatFormatting.GRAY))
                .append(Component.literal("] for ").withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal("Radiant Power").withStyle(ChatFormatting.GRAY))
                .append(Component.literal(" Info").withStyle(ChatFormatting.DARK_GRAY)));
        if (shift) {
            boolean bool = ClientRPData.getCachedActivation() && clientFlag(stack, this);
            if (!isGenerator()) tooltipComponents.add(Component.literal("⚠ Require: " + getPower() + " RP").withStyle(bool ? ChatFormatting.GREEN : ChatFormatting.RED));
            else tooltipComponents.add(Component.literal("+ Generating: " + getPower() + " RP").withStyle(ChatFormatting.AQUA));
            if (bool) tooltipComponents.add(Component.literal("⚡ Usage: " + getPower() + " RP").withStyle(ChatFormatting.YELLOW));
            else tooltipComponents.add(Component.literal("⚡ Usage: 0 RP").withStyle(ChatFormatting.GOLD));

            int cachedRP = ClientRPData.getCachedRP();
            tooltipComponents.add(Component.literal("🔋 Current: " + cachedRP + " RP").withStyle(ChatFormatting.BLUE));
        }
    }

    public abstract int getPower();

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (!level.isClientSide && entity instanceof ServerPlayer player) {
            if (owner == null) owner = player.getUUID();

            if (!registered) {
                PowerTickHandler.registerPowerItem(this);
                registered = true;
            }
        }
    }

    public void processPower() {
        if (owner != null) {
            ServerPlayer player = getPlayerByUUID(owner);
            if (player != null) {
                var bool = getFlag(player);
                PacketDistributor.sendToPlayer(player, new SyncActivationToClientPacket(bool));
                if (bool) {
                    PowerManager.addConsumedRP(player, getPower());
                    runActive(player.level(), new ItemStack(this), player);
                } else runFailed(player.level(), new ItemStack(this), player);
            }
        }
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player entity) {
        super.onCraftedBy(stack, level, entity);
        if (!level.isClientSide && entity instanceof ServerPlayer player) {
            owner = player.getUUID();
            PowerTickHandler.registerPowerItem(this);
        }
    }

    @Override
    public void onDestroyed(ItemEntity itemEntity, DamageSource damageSource) {
        super.onDestroyed(itemEntity, damageSource);
        PowerTickHandler.unregisterPowerItem(this);
    }

    public boolean getFlag(ServerPlayer pPlayer) {
        return PowerManager.getNetRP(pPlayer) >= getPower();
    }

    public boolean clientFlag(ItemStack stack, Item item) {
        return false;
    }

    public void runActive(Level pLevel, ItemStack pStack, ServerPlayer pPlayer) {
    }

    public void runFailed(Level pLevel, ItemStack pStack, ServerPlayer pPlayer) {
    }

    private static ServerPlayer getPlayerByUUID(UUID uuid) {
        assert ServerLifecycleHooks.getCurrentServer() != null;
        return ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(uuid);
    }
}
