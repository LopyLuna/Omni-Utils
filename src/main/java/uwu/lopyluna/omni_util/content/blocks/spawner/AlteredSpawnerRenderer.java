package uwu.lopyluna.omni_util.content.blocks.spawner;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.client.renderer.blockentity.SpawnerRenderer.renderEntityInSpawner;

@OnlyIn(Dist.CLIENT)
public class AlteredSpawnerRenderer implements BlockEntityRenderer<AlteredSpawnerBE> {
    private final EntityRenderDispatcher entityRenderer;

    public AlteredSpawnerRenderer(BlockEntityRendererProvider.Context context) {
        entityRenderer = context.getEntityRenderer();
    }

    @Override
    public void render(AlteredSpawnerBE blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Level level = blockEntity.getLevel();
        if (level != null) {
            var basespawner = blockEntity.getSpawner();
            var entity = basespawner.getOrCreateDisplayEntity(level, blockEntity.getBlockPos());
            if (entity != null) renderEntityInSpawner(partialTick, poseStack, bufferSource, packedLight, entity, entityRenderer, basespawner.getoSpin(), basespawner.getSpin());
        }
    }

    @Override
    public @NotNull AABB getRenderBoundingBox(AlteredSpawnerBE blockEntity) {
        var pos = blockEntity.getBlockPos();
        var x = pos.getX();
        var y = pos.getY();
        var z = pos.getZ();
        return new AABB(x - 1.0, y - 1.0, z - 1.0, x + 2.0, y + 2.0, z + 2.0);
    }
}
