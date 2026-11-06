package hunternif.mc.impl.atlas.mixin;

import hunternif.mc.api.AtlasAPI;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** The cartography table's second input slot also accepts an atlas, so a map can be added to it. */
@Mixin(targets = "net.minecraft.world.inventory.CartographyTableMenu$4")
class MixinCartographyTableScreenHandlerSlot {
    @Inject(method = "mayPlace", at = @At("RETURN"), cancellable = true)
    void antiqueatlas_canInsert(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
        info.setReturnValue(stack.getItem() == AtlasAPI.getAtlasItem() || info.getReturnValueZ());
    }
}
