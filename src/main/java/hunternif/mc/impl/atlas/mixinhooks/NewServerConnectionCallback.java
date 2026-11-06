package hunternif.mc.impl.atlas.mixinhooks;

import hunternif.mc.impl.atlas.util.event.Event;
import hunternif.mc.impl.atlas.util.event.EventFactory;

@FunctionalInterface
public interface NewServerConnectionCallback {
    Event<NewServerConnectionCallback> EVENT = EventFactory.createLoop();

    void onNewConnection(boolean isRemote);
}
