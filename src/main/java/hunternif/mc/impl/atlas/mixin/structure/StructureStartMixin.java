package hunternif.mc.impl.atlas.mixin.structure;

import hunternif.mc.impl.atlas.structure.StructureAddedCallback;
import hunternif.mc.impl.atlas.structure.StructurePieceAddedCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(StructureStart.class)
public class StructureStartMixin {

    @Final
    @Shadow
    private PiecesContainer pieceContainer;


    @Redirect(method = "placeInChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/StructurePiece;postProcess(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/core/BlockPos;)V"))
    private void structurePieceGenerated(StructurePiece structurePiece, WorldGenLevel structureWorldAccess, StructureManager structureAccessor, ChunkGenerator chunkGenerator, RandomSource random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos blockPos) {
        ServerLevel world;

        if (structureWorldAccess instanceof ServerLevel) {
            world = (ServerLevel) structureWorldAccess;
        } else {
            world = ((WorldGenRegion) structureWorldAccess).getLevel();
        }

        StructurePieceAddedCallback.EVENT.invoker().onStructurePieceAdded(structurePiece, world);
        structurePiece.postProcess(structureWorldAccess, structureAccessor, chunkGenerator, random, boundingBox, chunkPos, blockPos);
    }

    @Inject(method = "placeInChunk", at = @At("RETURN"))
    private void structureGenerated(WorldGenLevel serverWorldAccess, StructureManager structureAccessor, ChunkGenerator chunkGenerator, RandomSource random, BoundingBox chunkBox, ChunkPos chunkPos, CallbackInfo ci) {
        ServerLevel world;

        if (serverWorldAccess instanceof ServerLevel) {
            world = (ServerLevel) serverWorldAccess;
        } else {
            world = ((WorldGenRegion) serverWorldAccess).getLevel();
        }

        synchronized (this.pieceContainer) {
            if(this.pieceContainer.isEmpty()) return;

            StructureAddedCallback.EVENT.invoker().onStructureAdded((StructureStart) (Object) this, world);
        }
    }
}
