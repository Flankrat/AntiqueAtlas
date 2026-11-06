package hunternif.mc.impl.atlas.structure;

import hunternif.mc.impl.atlas.util.event.Event;
import hunternif.mc.impl.atlas.util.event.EventFactory;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.StructurePiece;

public interface StructurePieceAddedCallback {
	Event<StructurePieceAddedCallback> EVENT = EventFactory.createLoop();

	void onStructurePieceAdded(StructurePiece structurePiece, ServerLevel world);
}
