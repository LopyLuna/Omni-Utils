package uwu.lopyluna.omni_util.register;

import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import uwu.lopyluna.omni_util.content.container.TrashCanMenu;

import java.util.function.Supplier;

import static uwu.lopyluna.omni_util.OmniUtils.REGISTER;

public class AllMenuTypes {

    public static final Supplier<MenuType<TrashCanMenu>> TRASH_CAN = REGISTER.menus()
            .register("trash_can", () -> IMenuTypeExtension.create(TrashCanMenu::new));


    public static void register() {}
}
