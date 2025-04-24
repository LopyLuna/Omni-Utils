package uwu.lopyluna.omni_util.content.blocks.spawner;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uwu.lopyluna.omni_util.content.blocks.base.OmniBlock;
import uwu.lopyluna.omni_util.content.blocks.base.OmniBlockEntity;
import uwu.lopyluna.omni_util.register.AllBlockEntities;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static net.minecraft.world.level.Spawner.getSpawnEntityDisplayName;

@ParametersAreNonnullByDefault
public class AlteredSpawnerBlock extends OmniBlock {
    public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;
    public AlteredSpawnerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ENABLED, false));
    }

    @Override
    public @NotNull BlockEntityType<? extends OmniBlockEntity> getBlockEntityType() {
        return AllBlockEntities.SPAWNER.get();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ENABLED);
    }

    @Override
    public int getExpDrop(BlockState state, LevelAccessor level, BlockPos pos, @Nullable BlockEntity be, @Nullable Entity breaker, ItemStack tool) {
        var hasSilk = !tool.isEmpty() && EnchantmentHelper.getTagEnchantmentLevel(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH), tool) > 0;
        return be != null && getSpawnerEntityType(level, state, be) != null && !hasSilk ?
                15 + level.getRandom().nextInt(15) + level.getRandom().nextInt(15) :
                super.getExpDrop(state, level, pos, be, breaker, tool);
    }


    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity be, ItemStack tool) {
        if (level instanceof ServerLevel && be != null && !tool.isEmpty() &&
                EnchantmentHelper.getTagEnchantmentLevel(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH), tool) > 0 && getSpawnerEntityType(level, state, be) != null) {
            var stack = state.getBlock().asItem().getDefaultInstance();
            addCustomNbtData(stack, be, level.registryAccess());
            Block.popResource(level, pos, stack);
        } else super.playerDestroy(level, player, pos, state, be, tool);
    }

    private void addCustomNbtData(ItemStack stack, BlockEntity blockEntity, RegistryAccess registryAccess) {
        BlockItem.setBlockEntityData(stack, blockEntity.getType(), blockEntity.saveCustomAndMetadata(registryAccess));
        stack.applyComponents(blockEntity.collectComponents());
    }

    @SuppressWarnings("deprecation")
    public EntityType<? extends Entity> getSpawnerEntityType(LevelAccessor level, BlockState state, BlockEntity be) {
        var stack = state.getBlock().asItem().getDefaultInstance();
        addCustomNbtData(stack, be, level.registryAccess());
        var data = stack.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY).getUnsafe();
        EntityType<? extends Entity> type = null;
        if (data.contains("SpawnData", 10)) type = BuiltInRegistries.ENTITY_TYPE.getOptional(ResourceLocation.tryParse(data.getCompound("SpawnData").getCompound("entity").getString("id"))).orElse(null);
        return type;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, AllBlockEntities.SPAWNER.get(), level.isClientSide ? AlteredSpawnerBE::onClientTick : AlteredSpawnerBE::onServerTick);
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltip, TooltipFlag pFlag) {
        super.appendHoverText(pStack, pContext, pTooltip, pFlag);
        //Spawner.appendHoverText(pStack, pTooltip, "SpawnData");
        Component component = getSpawnEntityDisplayName(pStack, "SpawnData");
        if (component != null) {
            pTooltip.add(component);
        } else {
            pTooltip.add(CommonComponents.EMPTY);
            pTooltip.add(Component.empty().append(Component.translatable("block.minecraft.spawner.desc1").getString().replace(":", " or Lasso:")).withStyle(ChatFormatting.GRAY));
            pTooltip.add(CommonComponents.space().append(Component.translatable("block.minecraft.spawner.desc2").withStyle(ChatFormatting.BLUE)));
        }
    }
}
