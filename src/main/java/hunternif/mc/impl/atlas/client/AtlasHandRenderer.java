package hunternif.mc.impl.atlas.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import hunternif.mc.impl.atlas.item.AntiqueAtlasItems;
import hunternif.mc.impl.atlas.mixin.ItemInHandRendererAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderHandEvent;

/**
 * Draws a held atlas in first person like a held map, with the atlas page on it.
 * <p>
 * This used to be a mixin into {@code ItemInHandRenderer.renderArmWithItem} that captured its
 * local variables; Forge's {@link RenderHandEvent} gives the same values and lets us replace
 * the hand's rendering without depending on that method's internals.
 */
@OnlyIn(Dist.CLIENT)
public final class AtlasHandRenderer {
    private static final OverlayRenderer ATLAS_OVERLAY = new OverlayRenderer();

    private AtlasHandRenderer() {}

    public static void onRenderHand(RenderHandEvent event) {
        ItemStack item = event.getItemStack();
        if (item.isEmpty() || item.getItem() != AntiqueAtlasItems.ATLAS.get()) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) {
            return;
        }
        ItemInHandRendererAccessor renderer = (ItemInHandRendererAccessor) mc.getEntityRenderDispatcher().getItemInHandRenderer();
        PoseStack matrices = event.getPoseStack();
        MultiBufferSource buffers = event.getMultiBufferSource();
        int light = event.getPackedLight();
        boolean mainHand = event.getHand() == InteractionHand.MAIN_HAND;
        HumanoidArm arm = mainHand ? player.getMainArm() : player.getMainArm().getOpposite();

        matrices.pushPose();
        if (mainHand && renderer.antiqueatlas_getOffHandItem().isEmpty()) {
            renderAtlasInBothHands(renderer, player, matrices, buffers, light, event.getInterpolatedPitch(),
                    event.getEquipProgress(), event.getSwingProgress(), item);
        } else {
            renderAtlasInOneHand(renderer, player, matrices, buffers, light, event.getEquipProgress(), arm,
                    event.getSwingProgress(), item);
        }
        matrices.popPose();

        event.setCanceled(true);
    }

    private static void renderAtlasInBothHands(ItemInHandRendererAccessor renderer, LocalPlayer player, PoseStack matrices,
                                               MultiBufferSource vertexConsumers, int light, float pitch,
                                               float equipProgress, float swingProgress, ItemStack item) {
        float f = Mth.sqrt(swingProgress);
        float g = -0.2F * Mth.sin(swingProgress * 3.1415927F);
        float h = -0.4F * Mth.sin(f * 3.1415927F);
        matrices.translate(0.0D, -g / 2.0F, h);
        float i = renderer.antiqueatlas_calculateMapTilt(pitch);
        matrices.translate(0.0D, 0.04F + equipProgress * -1.2F + i * -0.5F, -0.7200000286102295D);
        matrices.mulPose(Axis.XP.rotationDegrees(i * -85.0F));

        if (!player.isInvisible()) {
            matrices.pushPose();
            matrices.mulPose(Axis.YP.rotationDegrees(90.0F));
            renderer.antiqueatlas_renderMapHand(matrices, vertexConsumers, light, HumanoidArm.RIGHT);
            renderer.antiqueatlas_renderMapHand(matrices, vertexConsumers, light, HumanoidArm.LEFT);
            matrices.popPose();
        }

        float j = Mth.sin(f * 3.1415927F);
        matrices.mulPose(Axis.XP.rotationDegrees(j * 20.0F));
        matrices.scale(0.5F, 0.5F, 1.0F);

        matrices.mulPose(Axis.YP.rotationDegrees(180.0F));
        matrices.mulPose(Axis.ZP.rotationDegrees(180.0F));
        matrices.scale(0.38F, 0.38F, 0.38F);
        matrices.translate(-1.85D, -0.5D, 0.0D);
        matrices.scale(0.0078125F, 0.0078125F, 0.0078125F);

        ATLAS_OVERLAY.drawOverlay(matrices, vertexConsumers, light, item);
    }

    private static void renderAtlasInOneHand(ItemInHandRendererAccessor renderer, LocalPlayer player, PoseStack matrices,
                                             MultiBufferSource vertexConsumers, int light, float equipProgress,
                                             HumanoidArm arm, float swingProgress, ItemStack item) {
        float f = arm == HumanoidArm.RIGHT ? 1.0F : -1.0F;
        matrices.pushPose();
        matrices.translate(f * 0.125F, -0.125D, 0.0D);
        if (!player.isInvisible()) {
            matrices.pushPose();
            matrices.mulPose(Axis.ZP.rotationDegrees(f * 10.0F));
            renderer.antiqueatlas_renderPlayerArm(matrices, vertexConsumers, light, equipProgress, swingProgress, arm);
            matrices.popPose();
        }

        matrices.translate(f * 0.51F, -0.08F + equipProgress * -1.2F, -0.75D);
        float g = Mth.sqrt(swingProgress);
        float h = Mth.sin(g * 3.1415927F);
        float i = -0.5F * h;
        float j = 0.4F * Mth.sin(g * 6.2831855F);
        float k = -0.3F * Mth.sin(swingProgress * 3.1415927F);
        matrices.translate(f * i, j - 0.3F * h, k);
        matrices.mulPose(Axis.XP.rotationDegrees(h * -45.0F));
        matrices.mulPose(Axis.YP.rotationDegrees(f * h * -30.0F));

        matrices.mulPose(Axis.YP.rotationDegrees(180.0F));
        matrices.mulPose(Axis.ZP.rotationDegrees(180.0F));
        matrices.scale(0.38F, 0.38F, 0.38F);
        matrices.translate(-0.75D, -0.5D, 0.0D);
        matrices.scale(0.0078125F, 0.0078125F, 0.0078125F);

        matrices.scale(0.4f, 0.4F, 0.4F);

        ATLAS_OVERLAY.drawOverlay(matrices, vertexConsumers, light, item);
        matrices.popPose();
    }
}
