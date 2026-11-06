package hunternif.mc.impl.atlas.mixin;

import hunternif.mc.impl.atlas.mixinhooks.NewServerConnectionCallback;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(ClientPacketListener.class)
public class MixinClientPlayNetworkHandler {
    @Final
    @Shadow
    private Minecraft minecraft;

    @Inject(at = @At("RETURN"), method = "handleLogin")
    public void afterGameJoin(ClientboundLoginPacket packet, CallbackInfo info) {
        NewServerConnectionCallback.EVENT.invoker().onNewConnection(!minecraft.hasSingleplayerServer());
    }
}