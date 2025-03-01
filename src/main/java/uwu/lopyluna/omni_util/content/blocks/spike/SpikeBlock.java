package uwu.lopyluna.omni_util.content.blocks.spike;

import com.mojang.authlib.GameProfile;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class SpikeBlock extends DirectionalBlock {
    float damage;
    boolean dontKill;
    boolean playerDrops;
    boolean expDrops;
    boolean fire;
    FakePlayer player;

    private SpikeBlock(Properties p) {
        super(p);
        this.damage = 0;
        this.dontKill = true;
        this.playerDrops = false;
        this.expDrops = false;
        this.fire = false;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
    }

    public SpikeBlock(Properties p, float damage, boolean dontKill, boolean playerDrops, boolean expDrops, boolean fire) {
        super(p);
        this.damage = damage;
        this.dontKill = dontKill;
        this.playerDrops = playerDrops;
        this.expDrops = expDrops;
        this.fire = fire;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return getShape(state);
    }

    @Override
    protected boolean isPathfindable(BlockState p_221547_, PathComputationType p_221550_) {
        return false;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity target) {
        if (player == null && level instanceof ServerLevel serverLevel) {
            this.player = new FakePlayer(serverLevel, new GameProfile(UUID.randomUUID(), "a Spike"));
            player.setPos(pos.getCenter());
        }
        if (!target.isSteppingCarefully() && target instanceof LivingEntity entity) {
            var diff = entity.getHealth()-damage;
            var isDead = diff <= 0;
            var dmg = dontKill ? isDead ? (damage-0.5f)+diff : damage : damage;
            var bool = playerDrops && player != null;
            if (dmg > 0f) {
                if (fire && !entity.fireImmune()) {
                    dmg = dmg * 0.5f;
                    entity.setRemainingFireTicks(entity.getRemainingFireTicks() + 1);
                    if (entity.getRemainingFireTicks() == 0) entity.igniteForSeconds(8.0F);
                    entity.hurt(level.damageSources().campfire(), dmg);
                }
                var vec = entity.position();
                if (!(bool || expDrops) && isDead) entity.skipDropExperience();
                if (bool) entity.setLastHurtByPlayer(player);
                if (isDead && player != null) entity.hurt(level.damageSources().trident(player, player), dmg);
                else entity.hurt(level.damageSources().stalagmite(), dmg);
                entity.setPos(vec);


            }
        }
        super.entityInside(state, level, pos, target);
    }

    public VoxelShape getShape(BlockState state) {
        return switch (state.getValue(BlockStateProperties.FACING)) {
            case SOUTH -> Block.box(0, 0, 0, 16, 16, 4);
            case NORTH -> Block.box(0, 0, 12, 16, 16, 16);
            case WEST -> Block.box(12, 0, 0, 16, 16, 16);
            case EAST -> Block.box(0, 0, 0, 4, 16, 16);
            case DOWN -> Block.box(0, 12, 0, 16, 16, 16);
            case UP -> Block.box(0, 0, 0, 16, 4, 16);
        };
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (player != null) player.discard();
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player realPlayer, boolean willHarvest, FluidState fluid) {
        if (player != null) player.discard();
        return super.onDestroyedByPlayer(state, level, pos, realPlayer, willHarvest, fluid);
    }

    public static final MapCodec<SpikeBlock> CODEC = simpleCodec(SpikeBlock::new);
    @Override protected @NotNull MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
    @Override public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getClickedFace();
        BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos().relative(direction.getOpposite()));
        return blockstate.is(this) && blockstate.getValue(FACING) == direction
                ? this.defaultBlockState().setValue(FACING, direction.getOpposite())
                : this.defaultBlockState().setValue(FACING, direction);
    }
}
