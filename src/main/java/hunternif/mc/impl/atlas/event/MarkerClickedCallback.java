package hunternif.mc.impl.atlas.event;

import hunternif.mc.impl.atlas.util.event.Event;
import hunternif.mc.impl.atlas.util.event.EventFactory;
import hunternif.mc.impl.atlas.util.event.EventResult;
import hunternif.mc.impl.atlas.marker.Marker;
import net.minecraft.world.entity.player.Player;

@FunctionalInterface
public interface MarkerClickedCallback {
    Event<MarkerClickedCallback> EVENT = EventFactory.createEventResult();

    EventResult onClicked(Player player, Marker marker, int mouseState);
}
