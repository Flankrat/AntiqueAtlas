package hunternif.mc.impl.atlas.structure;

import hunternif.mc.impl.atlas.util.event.Event;
import hunternif.mc.impl.atlas.util.event.EventFactory;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.StructureStart;

public interface StructureAddedCallback {
	Event<StructureAddedCallback> EVENT = EventFactory.createLoop();

	void onStructureAdded(StructureStart structureStart, ServerLevel world);
}
