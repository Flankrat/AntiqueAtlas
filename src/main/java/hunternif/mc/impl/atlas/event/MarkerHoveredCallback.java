package hunternif.mc.impl.atlas.event;

import hunternif.mc.impl.atlas.util.event.Event;
import hunternif.mc.impl.atlas.util.event.EventFactory;
import hunternif.mc.impl.atlas.marker.Marker;
import net.minecraft.world.entity.player.Player;

@FunctionalInterface
public interface MarkerHoveredCallback {
    Event<MarkerHoveredCallback> EVENT = EventFactory.createLoop();

    void onHovered(Player player, Marker marker);
}
