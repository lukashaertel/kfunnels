package eu.metatools.kfunnels


/**
 * Base interface for sources and sinks.
 */
interface Pivot

/**
 * Marker interface for sequential sources and sinks.
 */
interface Sequential


/**
 * Marker interface for labeled sources and sinks.
 */
interface Labeled

/**
 * Marker interface for sources.
 */
interface Source : Pivot

/**
 * A source that provides values by sequence.
 */
interface SeqSource : Sequential, Source {
    /**
     * Gets the next element, will throw an exception if not at the correct position.
     */
    fun getBoolean(): Boolean

    /**
     * Gets the next element, will throw an exception if not at the correct position.
     */
    fun getByte(): Byte

    /**
     * Gets the next element, will throw an exception if not at the correct position.
     */
    fun getShort(): Short

    /**
     * Gets the next element, will throw an exception if not at the correct position.
     */
    fun getInt(): Int

    /**
     * Gets the next element, will throw an exception if not at the correct position.
     */
    fun getLong(): Long

    /**
     * Gets the next element, will throw an exception if not at the correct position.
     */
    fun getFloat(): Float

    /**
     * Gets the next element, will throw an exception if not at the correct position.
     */
    fun getDouble(): Double

    /**
     * Gets the next element, will throw an exception if not at the correct position.
     */
    fun getChar(): Char

    /**
     * True if the next element in the sequence is null.
     */
    fun isNull(): Boolean

    /**
     * Gets the next element, will throw an exception if not at the correct position.
     */
    fun getUnit()

    /**
     * Gets the next element, will throw an exception if not at the correct position.
     */
    fun getString(): String

    /**
     * Enters the nesting of a new element.
     */
    fun beginNested()

    /**
     * Leaves the nesting.
     */
    fun endNested()
}


/**
 * A source that provides values by label.
 */
interface LabelSource : Labeled, Source {
    /**
     * Gets the value for the label, will throw an exception if element is not present.
     */
    fun getBoolean(label: String): Boolean

    /**
     * Gets the value for the label, will throw an exception if element is not present.
     */
    fun getByte(label: String): Byte

    /**
     * Gets the value for the label, will throw an exception if element is not present.
     */
    fun getShort(label: String): Short

    /**
     * Gets the value for the label, will throw an exception if element is not present.
     */
    fun getInt(label: String): Int

    /**
     * Gets the value for the label, will throw an exception if element is not present.
     */
    fun getLong(label: String): Long

    /**
     * Gets the value for the label, will throw an exception if element is not present.
     */
    fun getFloat(label: String): Float

    /**
     * Gets the value for the label, will throw an exception if element is not present.
     */
    fun getDouble(label: String): Double

    /**
     * Gets the value for the label, will throw an exception if element is not present.
     */
    fun getChar(label: String): Char

    /**
     * True if the given label is for a null value.
     */
    fun isNull(label: String): Boolean

    /**
     * Gets the value for the label, will throw an exception if element is not present.
     */
    fun getUnit(label: String)

    /**
     * Gets the value for the label, will throw an exception if element is not present.
     */
    fun getString(label: String): String

    /**
     * Enters the nesting of a new element.
     */
    fun beginNested(label: String)

    /**
     * Leaves the nesting.
     */
    fun endNested(label: String)
}

/**
 * Marker interface for sinks.
 */
interface Sink : Pivot

/**
 * A sink that uses sequence to match up values.
 */
interface SeqSink : Sequential, Sink {
    /**
     * Writes the given element to the sequence.
     */
    fun putBoolean(b: Boolean)

    /**
     * Writes the given element to the sequence.
     */
    fun putByte(b: Byte)

    /**
     * Writes the given element to the sequence.
     */
    fun putShort(s: Short)

    /**
     * Writes the given element to the sequence.
     */
    fun putInt(i: Int)

    /**
     * Writes the given element to the sequence.
     */
    fun putLong(l: Long)

    /**
     * Writes the given element to the sequence.
     */
    fun putFloat(f: Float)

    /**
     * Writes the given element to the sequence.
     */
    fun putDouble(d: Double)

    /**
     * Writes the given element to the sequence.
     */
    fun putChar(c: Char)

    /**
     * Indicates the next element in the sequence to be null.
     */
    fun putNull(isNull: Boolean)

    /**
     * Writes the given element to the sequence.
     */
    fun putUnit(unit: Unit)

    /**
     * Writes the given element to the sequence.
     */
    fun putString(string: String)

    /**
     * Indicates nesting of an element.
     */
    fun beginNested()

    /**
     * Indicates leaving a nested element.
     */
    fun endNested()
}

/**
 * A sink that uses labels match up values.
 */
interface LabelSink : Labeled, Sink {
    /**
     * Writes the given element to the given label.
     */
    fun putBoolean(label: String, b: Boolean)

    /**
     * Writes the given element to the given label.
     */
    fun putByte(label: String, b: Byte)

    /**
     * Writes the given element to the given label.
     */
    fun putShort(label: String, s: Short)

    /**
     * Writes the given element to the given label.
     */
    fun putInt(label: String, i: Int)

    /**
     * Writes the given element to the given label.
     */
    fun putLong(label: String, l: Long)

    /**
     * Writes the given element to the given label.
     */
    fun putFloat(label: String, f: Float)

    /**
     * Writes the given element to the given label.
     */
    fun putDouble(label: String, d: Double)

    /**
     * Writes the given element to the given label.
     */
    fun putChar(label: String, c: Char)

    /**
     * Indicates the element of the given label to be null.
     */
    fun putNull(label: String, isNull: Boolean)

    /**
     * Writes the given element to the given label.
     */
    fun putUnit(label: String, unit: Unit)

    /**
     * Writes the given element to the given label.
     */
    fun putString(label: String, string: String)

    /**
     * Indicates nesting of an element.
     */
    fun beginNested(label: String)

    /**
     * Indicates leaving the last nested element.
     */
    fun endNested(label: String)
}

/**
 * Boundary marker. TODO: Pull boundaries into the [Pivot]s?
 */
interface Boundaries {
    /**
     * Called on the start of a complex object.
     * @param type The type that is started
     */
    fun startEntire(type: Type)

    /**
     * Called on the end of a complex object.
     */
    fun endEntire(type: Type)
}

/**
 * Marks the start if the receiver supports boundaries.
 * @param type The type that is started
 */
fun Pivot.markStart(type: Type) {
    if (this is Boundaries)
        startEntire(type)
}

/**
 * Marks the end if the receiver supports boundaries.
 * @param type The type that is ended
 */
fun Pivot.markEnd(type: Type) {
    if (this is Boundaries)
        endEntire(type)
}

/**
 * Marks start and end around the [block] using [markStart] and [markEnd].
 */
inline fun <T> Pivot.markAround(type: Type, block: () -> T): T {
    markStart(type)
    val r = block()
    markEnd(type)
    return r
}


/**
 * Reads from the source but only if not marked null.
 */
fun <T> SeqSource.readIfNotNull(block: () -> T): T? {
    if (isNull())
        return null
    else
        return block()
}

/**
 * Reads from the source but only if not marked null.
 */
fun <T> LabelSource.readIfNotNull(label: String, block: () -> T): T? {
    if (isNull(label))
        return null
    else
        return block()
}
