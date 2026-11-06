package hunternif.mc.impl.atlas.mixin.structure;

import hunternif.mc.impl.atlas.structure.AutoMarkers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Lets {@link AutoMarkers} mark world generation features (like Ice and Fire's dragon roosts
 * and caves) after they place successfully.
 */
@Mixin(ConfiguredFeature.class)
public abstract class ConfiguredFeatureMixin {
    @Inject(method = "place", at = @At("RETURN"))
    private void antiqueatlas_featurePlaced(WorldGenLevel level, ChunkGenerator generator, RandomSource random, BlockPos origin, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ()) {
            ServerLevel serverLevel = level.getLevel();
            if (serverLevel != null) {
                AutoMarkers.onFeaturePlaced(((ConfiguredFeature<?, ?>) (Object) this).feature(), serverLevel, origin);
            }
        }
    }
}
