package eu.metatools.kfunnels

/**
 * A token matching events in [Source] and [Sink].
 */
enum class Event {
    /**
     * Matches [Source.begin] and [Sink.begin].
     */
    BEGIN,

    /**
     * Matches [Source.end] and [Sink.end].
     */
    END,

    /**
     * Matches [Source.isNull] returning true and [Sink.putNull] with true.
     */
    IS_NULL,

    /**
     * Matches [Source.isNull] returning false and [Sink.putNull] with false.
     */
    IS_NOT_NULL,

    /**
     * Matches [Source.beginNested] and [Sink.beginNested].
     */
    BEGIN_NESTED,

    /**
     * Matches [Source.endNested] and [Sink.endNested].
     */
    END_NESTED
}