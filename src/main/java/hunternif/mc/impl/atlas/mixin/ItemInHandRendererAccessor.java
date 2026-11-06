package hunternif.mc.impl.atlas.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

/** Lets {@code AtlasHandRenderer} reuse the vanilla map-holding arm rendering. */
@OnlyIn(Dist.CLIENT)
@Mixin(ItemInHandRenderer.class)
public interface ItemInHandRendererAccessor {
    @Accessor("offHandItem")
    ItemStack antiqueatlas_getOffHandItem();

    @Invoker("renderMapHand")
    void antiqueatlas_renderMapHand(PoseStack poseStack, MultiBufferSource buffer, int light, HumanoidArm arm);

    @Invoker("renderPlayerArm")
    void antiqueatlas_renderPlayerArm(PoseStack poseStack, MultiBufferSource buffer, int light, float equipProgress, float swingProgress, HumanoidArm arm);

    @Invoker("calculateMapTilt")
    float antiqueatlas_calculateMapTilt(float pitch);
}
