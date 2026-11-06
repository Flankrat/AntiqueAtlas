package hunternif.mc.impl.atlas.util.event;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Creates {@link Event}s for single-method callback interfaces. Replaces Architectury's
 * EventFactory with the two kinds this mod uses:
 * <ul>
 *   <li>{@link #createLoop()}: the invoker calls every listener in order.</li>
 *   <li>{@link #createEventResult()}: the invoker calls listeners until one returns an
 *       {@link EventResult} that interrupts, and returns that (or {@link EventResult#pass()}).</li>
 * </ul>
 */
public final class EventFactory {
    private EventFactory() {}

    /** The callback interface is taken from the declared type of the field, like Architectury does. */
    @SafeVarargs
    public static <T> Event<T> createLoop(T... typeGetter) {
        if (typeGetter.length != 0) throw new IllegalStateException("Don't pass anything to createLoop()");
        return createLoop(typeOf(typeGetter));
    }

    public static <T> Event<T> createLoop(Class<T> type) {
        return new ProxyEvent<>(type, (listeners, method, args) -> {
            for (Object listener : listeners) {
                invoke(listener, method, args);
            }
            return null;
        });
    }

    @SafeVarargs
    public static <T> Event<T> createEventResult(T... typeGetter) {
        if (typeGetter.length != 0) throw new IllegalStateException("Don't pass anything to createEventResult()");
        return createEventResult(typeOf(typeGetter));
    }

    public static <T> Event<T> createEventResult(Class<T> type) {
        return new ProxyEvent<>(type, (listeners, method, args) -> {
            for (Object listener : listeners) {
                EventResult result = (EventResult) Objects.requireNonNull(invoke(listener, method, args),
                        "A listener returned null instead of an EventResult");
                if (result.interruptsFurtherEvaluation()) {
                    return result;
                }
            }
            return EventResult.pass();
        });
    }

    @SuppressWarnings("unchecked")
    private static <T> Class<T> typeOf(T[] typeGetter) {
        return (Class<T>) typeGetter.getClass().getComponentType();
    }

    private static Object invoke(Object listener, Method method, Object[] args) throws Throwable {
        try {
            return method.invoke(listener, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @FunctionalInterface
    private interface Dispatcher {
        Object dispatch(List<Object> listeners, Method method, Object[] args) throws Throwable;
    }

    private static final class ProxyEvent<T> implements Event<T> {
        private final List<Object> listeners = new CopyOnWriteArrayList<>();
        private final T invoker;

        @SuppressWarnings("unchecked")
        ProxyEvent(Class<T> type, Dispatcher dispatcher) {
            InvocationHandler handler = (proxy, method, args) -> {
                if (method.getDeclaringClass() == Object.class) {
                    return switch (method.getName()) {
                        case "equals" -> proxy == args[0];
                        case "hashCode" -> System.identityHashCode(proxy);
                        default -> "Event<" + type.getSimpleName() + ">";
                    };
                }
                if (method.isDefault()) {
                    return InvocationHandler.invokeDefault(proxy, method, args);
                }
                return dispatcher.dispatch(listeners, method, args);
            };
            this.invoker = (T) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, handler);
        }

        @Override public T invoker() { return invoker; }
        @Override public void register(T listener) { listeners.add(Objects.requireNonNull(listener)); }
        @Override public void unregister(T listener) { listeners.remove(listener); }
        @Override public boolean isRegistered(T listener) { return listeners.contains(listener); }
        @Override public void clearListeners() { listeners.clear(); }
    }
}
