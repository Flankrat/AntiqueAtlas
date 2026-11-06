package hunternif.mc.impl.atlas.mixinhooks;

import hunternif.mc.impl.atlas.util.event.Event;
import hunternif.mc.impl.atlas.util.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;

@FunctionalInterface
public interface NewPlayerConnectionCallback {
    Event<NewPlayerConnectionCallback> EVENT = EventFactory.createLoop();

    void onNewConnection(ServerPlayer player);
}
