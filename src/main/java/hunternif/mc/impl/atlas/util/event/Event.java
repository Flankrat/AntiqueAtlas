package hunternif.mc.impl.atlas.util.event;

/**
 * A list of listeners for one callback interface. Replaces Architectury's {@code Event}
 * (the mod was Architectury-based before it became Forge-only); same method names, so the
 * callback interfaces only needed their imports changed.
 */
public interface Event<T> {
    /** A T that calls every registered listener. */
    T invoker();

    void register(T listener);

    void unregister(T listener);

    boolean isRegistered(T listener);

    void clearListeners();
}
