package hunternif.mc.impl.atlas.util.event;

/** Result of a listener in an {@link EventFactory#createEventResult()} event. Replaces Architectury's EventResult. */
public final class EventResult {
    private static final EventResult PASS = new EventResult(false, null);
    private static final EventResult TRUE = new EventResult(true, true);
    private static final EventResult FALSE = new EventResult(true, false);
    private static final EventResult STOP = new EventResult(true, null);

    private final boolean interruptsFurtherEvaluation;
    private final Boolean value;

    private EventResult(boolean interruptsFurtherEvaluation, Boolean value) {
        this.interruptsFurtherEvaluation = interruptsFurtherEvaluation;
        this.value = value;
    }

    /** Let the next listener (and then the default behaviour) run. */
    public static EventResult pass() { return PASS; }

    /** Stop here; the outcome is "allowed". */
    public static EventResult interruptTrue() { return TRUE; }

    /** Stop here; the outcome is "denied". */
    public static EventResult interruptFalse() { return FALSE; }

    /** Stop here without an outcome. */
    public static EventResult interruptDefault() { return STOP; }

    public static EventResult interrupt(Boolean value) {
        return value == null ? STOP : value ? TRUE : FALSE;
    }

    public boolean interruptsFurtherEvaluation() { return interruptsFurtherEvaluation; }

    public Boolean value() { return value; }

    public boolean isEmpty() { return value == null; }

    public boolean isTrue() { return Boolean.TRUE.equals(value); }

    public boolean isFalse() { return Boolean.FALSE.equals(value); }
}
