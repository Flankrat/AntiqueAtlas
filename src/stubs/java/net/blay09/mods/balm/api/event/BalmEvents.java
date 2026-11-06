package net.blay09.mods.balm.api.event;

import java.util.function.Consumer;

/** Compile-only stub of Balm's API; the real interface comes from the Balm mod. */
public interface BalmEvents {
    default <T> void onEvent(Class<T> eventClass, Consumer<T> handler) {
        throw new UnsupportedOperationException("stub");
    }
}
