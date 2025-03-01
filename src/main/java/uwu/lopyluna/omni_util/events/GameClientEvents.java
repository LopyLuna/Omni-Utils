package uwu.lopyluna.omni_util.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import uwu.lopyluna.omni_util.OmniUtils;
import uwu.lopyluna.omni_util.register.AllBlocks;

import static net.neoforged.neoforge.client.event.RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS;

@EventBusSubscriber(modid = OmniUtils.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class GameClientEvents {
    private static final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public static void onRenderStage(RenderLevelStageEvent event) {
        if (mc.getConnection() == null || mc.player == null || mc.level == null || event.getStage() != AFTER_TRIPWIRE_BLOCKS) return;
        var player = mc.player;
        if (!(AllBlocks.ANGEL_BLOCK.is(player.getMainHandItem()) || AllBlocks.ANGEL_BLOCK.is(player.getOffhandItem()))) return;

        Vec3 vec3 = player.getEyePosition();
        Vec3 vec31 = player.getViewVector(1f);
        var dist = player.isShiftKeyDown() ? 2.5 : 4.5;
        Vec3 vec32 = vec3.add(vec31.x * dist, vec31.y * dist, vec31.z * dist);
        var rayTrace = mc.level.clip(new ClipContext(player.getEyePosition(), vec32, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
        var pos = rayTrace.getBlockPos();
        if (rayTrace.getType() == HitResult.Type.MISS && mc.hitResult != null && mc.level.getBlockState(((BlockHitResult)mc.hitResult).getBlockPos()).isAir() && mc.level.getBlockState(pos).isAir()) {
            var buffer = mc.renderBuffers().bufferSource().getBuffer(RenderType.lines());
            var camPos = event.getCamera().getPosition();

            var x = (double) pos.getX() - camPos.x();
            var y = (double) pos.getY() - camPos.y();
            var z = (double) pos.getZ() - camPos.z();
            var posStack = event.getPoseStack().last();
            Shapes.block().forAllEdges((x1, y1, z1, x2, y2, z2) -> {
                float f = (float)(x2 - x1);
                float f1 = (float)(y2 - y1);
                float f2 = (float)(z2 - z1);
                float f3 = Mth.sqrt(f * f + f1 * f1 + f2 * f2);
                f /= f3;
                f1 /= f3;
                f2 /= f3;
                buffer.addVertex(posStack, (float) (x1 + x), (float) (y1 + y), (float) (z1 + z))
                        .setColor(0, 0, 0, 0.4f)
                        .setNormal(posStack, f, f1, f2);
                buffer.addVertex(posStack, (float) (x2 + x), (float) (y2 + y), (float) (z2 + z))
                        .setColor(0, 0, 0, 0.4f)
                        .setNormal(posStack, f, f1, f2);
            });
        }
    }
}
