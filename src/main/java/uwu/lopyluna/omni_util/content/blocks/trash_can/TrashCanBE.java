package uwu.lopyluna.omni_util.content.blocks.trash_can;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import uwu.lopyluna.omni_util.content.AllUtils;
import uwu.lopyluna.omni_util.content.blocks.base.ContainerOmniBlockEntity;
import uwu.lopyluna.omni_util.content.container.TrashCanMenu;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class TrashCanBE extends ContainerOmniBlockEntity {
    private final ContainerOpenersCounter openersCounter;

    public TrashCanBE(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState, NonNullList.withSize(5, ItemStack.EMPTY));
        this.openersCounter = new ContainerOpenersCounter() {
            protected void onOpen(Level level, BlockPos pos, BlockState state) {
                //playSound(SoundEvents.VAULT_INSERT_ITEM);
                updateState(state, true);
            }
            protected void onClose(Level level, BlockPos pos, BlockState state) {
                //playSound(SoundEvents.VAULT_INSERT_ITEM_FAIL);
                updateState(state, false);
            }
            protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int count, int current) {}
            protected boolean isOwnContainer(Player player) { return player.containerMenu instanceof TrashCanMenu menu && menu.trashCan == TrashCanBE.this; }
        };
    }

    int t = 0;
    @Override
    public void onTick(boolean pClient) {
        assert level != null;
        super.onTick(pClient);
        if (pClient) return;
        t = ++t % 2;
        if (t == 0 && hasItemInAllSlot()) {
            deleteItem();
            offsetRow();
            AllUtils.playSound(level, getBlockPos(), SoundEvents.WOLF_ARMOR_CRACK, SoundSource.BLOCKS, 0.25f, 1f);
        }
    }

    public void deleteItem() {
        var stack = getItem(0);
        if (stack.isEmpty()) return;
        this.unpackLootTable(null);
        ContainerHelper.removeItem(this.getItems(), 0, stack.getCount());
        this.setChanged();
    }

    public void offsetRow() {
        this.unpackLootTable(null);
        for(int i = 1; i < 5; ++i) {
            var stack = getItem(i);
            this.getItems().set(i-1, stack);
            stack.limitSize(this.getMaxStackSize(stack));
        }
        this.getItems().set(4, ItemStack.EMPTY);
        this.setChanged();
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new TrashCanMenu(id, inventory, this);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatableWithFallback("container.trash_can", "Trash Can");
    }

    @Override
    public void startOpen(Player player) {
        assert level != null;
        if (!remove && !player.isSpectator()) openersCounter.incrementOpeners(player, level, getBlockPos(), getBlockState());
    }

    @Override
    public void stopOpen(Player player) {
        assert level != null;
        if (!remove && !player.isSpectator()) openersCounter.decrementOpeners(player, level, getBlockPos(), getBlockState());
    }

    public void recheckOpen() {
        assert level != null;
        if (!remove) openersCounter.recheckOpeners(level, getBlockPos(), getBlockState());
    }

    void updateState(BlockState state, boolean open) {
        assert level != null;
        level.setBlockAndUpdate(getBlockPos(), state.setValue(BarrelBlock.OPEN, open));
    }

    //*void playSound(SoundEvent sound) {
    //*    assert level != null;
    //*    AllUtils.playSound(level, getBlockPos(), sound, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 1.1F);
    //*}
}
