package uwu.lopyluna.omni_util.content.items.base;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.omni_util.content.managers.PowerManager;
import uwu.lopyluna.omni_util.register.AllPowerSources;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static uwu.lopyluna.omni_util.client.ClientRPData.getCachedRP;

@SuppressWarnings("unused")
public class PowerItem extends OmniItem {
    public final AllPowerSources.PowerSource source;
    public PowerItem(Properties properties, AllPowerSources.PowerSource source) {
        super(properties);
        this.source = source;
    }

    public void onPlayerTick(Level pLevel, ItemStack pStack, Player pPlayer, int pSlotId, boolean pInArmorSlot) {
        if (isActive(pLevel, pStack, pPlayer, pInArmorSlot)) onActive(pLevel, pStack, pPlayer);
        else onFailed(pLevel, pStack, pPlayer);
    }

    public boolean isActive(Level pLevel, ItemStack pStack, Player pPlayer, boolean pInArmorSlot) {
        if (pLevel == null || pPlayer == null) return false;
        boolean generator = isGenerator(pLevel, pStack, pPlayer);
        boolean generating = isGenerating(pLevel, pStack, pPlayer);
        if (generator) return generating;
        int netRP = pLevel.isClientSide ? getCachedRP() : PowerManager.getNetRPOmni(pPlayer);
        return netRP >= 0;
    }

    protected boolean isActivated(Level pLevel, ItemStack pStack, Player pPlayer) {
        if (isGenerator(pLevel, pStack, pPlayer)) return isGenerating(pLevel, pStack, pPlayer);
        return isEquipped(pLevel, pStack, pPlayer);
    }

    public void onActive(Level pLevel, ItemStack pStack, Player pPlayer) {
    }

    public void onFailed(Level pLevel, ItemStack pStack, Player pPlayer) {
    }

    public int getImpact(Level pLevel, ItemStack pStack, Player pPlayer) {
        return isActivated(pLevel, pStack, pPlayer) ? source.impact : 0;
    }

    public boolean isGenerator(Level pLevel, ItemStack pStack, Player pPlayer) {
        return source.genertor;
    }

    public boolean isGenerating(Level pLevel, ItemStack pStack, Player pPlayer) {
        return true;
    }

    public boolean isEquipped(Level pLevel, ItemStack pStack, Player pPlayer) {
        return true;
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return false;
    }

    public void onProcessPower(ServerPlayer owner, ServerLevel level, ItemStack stack) {
        int impact = getImpact(level, stack, owner);
        if (source.genertor) PowerManager.adjustGeneratedRP(owner, impact);
        else PowerManager.adjustConsumedRP(owner, impact);
    }

    @Override
    @ParametersAreNonnullByDefault
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
            var level = context.level();
            var player = Minecraft.getInstance().player;

            int cachedRP = getCachedRP();
            boolean bool = level != null && player != null && cachedRP >= 0 && isActivated(level, stack, player);

            if (!source.genertor) tooltipComponents.add(Component.literal("⚠ Require: " + source.impact + " RP").withStyle(bool ? ChatFormatting.GREEN : ChatFormatting.RED));
            else tooltipComponents.add(Component.literal("+ Generating: " + (bool ? source.impact : 0) + " RP").withStyle(ChatFormatting.AQUA));
            tooltipComponents.add(Component.literal("⚡ Impact: " + (bool ? source.impact : 0) + " RP").withStyle(bool ? ChatFormatting.YELLOW : ChatFormatting.GOLD));

            tooltipComponents.add(Component.literal("🔋 Current: " + cachedRP + " RP").withStyle(ChatFormatting.BLUE));
        }
    }
}
