package uwu.lopyluna.omni_util.content.blocks;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.ItemStack;
import uwu.lopyluna.omni_util.content.items.SoulLassoItem;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class WitheredSoulLassoItem extends SoulLassoItem {
    public WitheredSoulLassoItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean flagMob(LivingEntity pTarget) {
        return !(pTarget instanceof Enemy) || pTarget.getType().getCategory().isFriendly();
    }

    @Override
    public boolean flagHealth(LivingEntity pTarget) {
        return pTarget.getHealth() > (pTarget.getMaxHealth() > 10f ? pTarget.getMaxHealth() * 0.1f :
                pTarget.getMaxHealth() > 5f ?  pTarget.getMaxHealth() * 0.5f :
                        pTarget.getMaxHealth() * 0.75f
        );
    }

    @Override
    public Component invalidMob(ItemStack pStack, LivingEntity pTarget) {
        return Component.empty().append(getTargetName(pStack, pTarget)).append(" is too Friendly").withStyle(ChatFormatting.RED);
    }
}
