package uwu.lopyluna.omni_util.content.items;

import com.google.common.collect.Sets;
import net.neoforged.neoforge.common.ItemAbility;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.neoforged.neoforge.common.ItemAbilities.*;

public class AllItemAbilities {

    public static final ItemAbility HAMMER_DIG = ItemAbility.get("hammer_dig");

    public static final ItemAbility SCYTHE_DIG = ItemAbility.get("scythe_dig");
    public static final ItemAbility SCYTHE_TILL = ItemAbility.get("scythe_till");

    public static final Set<ItemAbility> DEFAULT_PAXEL_ACTIONS = of(
            AXE_DIG, AXE_STRIP, AXE_SCRAPE, AXE_WAX_OFF,
            HOE_DIG, HOE_TILL,
            SHOVEL_DIG, SHOVEL_FLATTEN, SHOVEL_DOUSE,
            PICKAXE_DIG,
            SWORD_DIG
    );
    public static final Set<ItemAbility> DEFAULT_HAMMER_ACTIONS = of(HAMMER_DIG);
    public static final Set<ItemAbility> DEFAULT_SCYTHE_ACTIONS = of(SCYTHE_DIG, SCYTHE_TILL);

    private static Set<ItemAbility> of(ItemAbility... actions) {
        return Stream.of(actions).collect(Collectors.toCollection(Sets::newIdentityHashSet));
    }
}
