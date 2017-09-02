package eu.metatools.kfunnels


/**
 * A sink that takes values from a [Funneler].
 */
interface Sink {
    /**
     * Begins writing a block.
     * @return Return false if the no further funneling is required and unfunneling will return a [Value]
     * @see Source.begin
     * @see Begin
     * @see Value
     * @see Unfunnel
     */
    fun begin(type: Type, value: Any?): Boolean

    /**
     * Ends writing a block.
     */
    fun end(type: Type, value: Any?)


    /**
     * Indicates nesting of the element [value] for static [type].
     * @return Return false if the no further funneling is required and unfunneling will return a [Item]
     * @see Source.beginNested
     * @see Nested
     * @see Item
     * @see Substitute
     * @see Nest
     */
    fun beginNested(label: String, type: Type, value: Any?): Boolean

    /**
     * Indicates leaving the nested element [value] for static [type].
     */
    fun endNested(label: String, type: Type, value: Any?)


    /**
     * Writes the given element to the given label.
     */
    fun putBoolean(label: String, value: Boolean)

    /**
     * Writes the given element to the given label.
     */
    fun putByte(label: String, value: Byte)

    /**
     * Writes the given element to the given label.
     */
    fun putShort(label: String, value: Short)

    /**
     * Writes the given element to the given label.
     */
    fun putInt(label: String, value: Int)

    /**
     * Writes the given element to the given label.
     */
    fun putLong(label: String, value: Long)

    /**
     * Writes the given element to the given label.
     */
    fun putFloat(label: String, value: Float)

    /**
     * Writes the given element to the given label.
     */
    fun putDouble(label: String, value: Double)

    /**
     * Writes the given element to the given label.
     */
    fun putChar(label: String, value: Char)

    /**
     * Indicates the element of the given label to be null.
     */
    fun putNull(label: String, isNull: Boolean)

    /**
     * Writes the given element to the given label.
     */
    fun putUnit(label: String, value: Unit)

    /**
     * Writes the given element to the given label.
     */
    fun putString(label: String, value: String)
}


/**
 * Writes the given element to the given label.
 */
fun Sink.putNullableBoolean(label: String, value: Boolean?) {
    if (value == null)
        putNull(label, true)
    else {
        putNull(label, false)
        putBoolean(label, value)
    }
}

/**
 * Writes the given element to the given label.
 */
fun Sink.putNullableByte(label: String, value: Byte?) {
    if (value == null)
        putNull(label, true)
    else {
        putNull(label, false)
        putByte(label, value)
    }
}

/**
 * Writes the given element to the given label.
 */
fun Sink.putNullableShort(label: String, value: Short?) {
    if (value == null)
        putNull(label, true)
    else {
        putNull(label, false)
        putShort(label, value)
    }
}

/**
 * Writes the given element to the given label.
 */
fun Sink.putNullableInt(label: String, value: Int?) {
    if (value == null)
        putNull(label, true)
    else {
        putNull(label, false)
        putInt(label, value)
    }
}

/**
 * Writes the given element to the given label.
 */
fun Sink.putNullableLong(label: String, value: Long?) {
    if (value == null)
        putNull(label, true)
    else {
        putNull(label, false)
        putLong(label, value)
    }
}

/**
 * Writes the given element to the given label.
 */
fun Sink.putNullableFloat(label: String, value: Float?) {
    if (value == null)
        putNull(label, true)
    else {
        putNull(label, false)
        putFloat(label, value)
    }
}

/**
 * Writes the given element to the given label.
 */
fun Sink.putNullableDouble(label: String, value: Double?) {
    if (value == null)
        putNull(label, true)
    else {
        putNull(label, false)
        putDouble(label, value)
    }
}

/**
 * Writes the given element to the given label.
 */
fun Sink.putNullableChar(label: String, value: Char?) {
    if (value == null)
        putNull(label, true)
    else {
        putNull(label, false)
        putChar(label, value)
    }
}

/**
 * Writes the given element to the given label.
 */
fun Sink.putNullableUnit(label: String, value: Unit?) {
    if (value == null)
        putNull(label, true)
    else {
        putNull(label, false)
        putUnit(label, value)
    }
}

/**
 * Writes the given element to the given label.
 */
fun Sink.putNullableString(label: String, value: String?) {
    if (value == null)
        putNull(label, true)
    else {
        putNull(label, false)
        putString(label, value)
    }
}

/**
 * A sink that takes values from a [Funneler] where methods are suspended.
 */
interface SuspendSink {
    /**
     * Begins writing a block.
     * @return Return false if the no further funneling is required and unfunneling will return a [Value]
     * @see SuspendSource.begin
     * @see Begin
     * @see Value
     * @see Unfunnel
     */
    suspend fun begin(type: Type, value: Any?): Boolean

    /**
     * Ends writing a block.
     */
    suspend fun end(type: Type, value: Any?)


    /**
     * Indicates nesting of the element [value] for static [type].
     * @return Return false if the no further funneling is required and unfunneling will return a [Item]
     * @see SuspendSource.beginNested
     * @see Nested
     * @see Item
     * @see Substitute
     * @see Nest
     */
    suspend fun beginNested(label: String, type: Type, value: Any?): Boolean

    /**
     * Indicates leaving the nested element [value] for static [type].
     */
    suspend fun endNested(label: String, type: Type, value: Any?)


    /**
     * Writes the given element to the given label.
     */
    suspend fun putBoolean(label: String, value: Boolean)

    /**
     * Writes the given element to the given label.
     */
    suspend fun putByte(label: String, value: Byte)

    /**
     * Writes the given element to the given label.
     */
    suspend fun putShort(label: String, value: Short)

    /**
     * Writes the given element to the given label.
     */
    suspend fun putInt(label: String, value: Int)

    /**
     * Writes the given element to the given label.
     */
    suspend fun putLong(label: String, value: Long)

    /**
     * Writes the given element to the given label.
     */
    suspend fun putFloat(label: String, value: Float)

    /**
     * Writes the given element to the given label.
     */
    suspend fun putDouble(label: String, value: Double)

    /**
     * Writes the given element to the given label.
     */
    suspend fun putChar(label: String, value: Char)

    /**
     * Indicates the element of the given label to be null.
     */
    suspend fun putNull(label: String, isNull: Boolean)

    /**
     * Writes the given element to the given label.
     */
    suspend fun putUnit(label: String, value: Unit)

    /**
     * Writes the given element to the given label.
     */
    suspend fun putString(label: String, value: String)
}


/**
 * Writes the given element to the given label.
 */
suspend fun SuspendSink.putNullableBoolean(label: String, value: Boolean?) {
    if (value == null)
        putNull(label, true)
    else {
        putNull(label, false)
        putBoolean(label, value)
    }
}

/**
 * Writes the given element to the given label.
 */
suspend fun SuspendSink.putNullableByte(label: String, value: Byte?) {
    if (value == null)
        putNull(label, true)
    else {
        putNull(label, false)
        putByte(label, value)
    }
}

/**
 * Writes the given element to the given label.
 */
suspend fun SuspendSink.putNullableShort(label: String, value: Short?) {
    if (value == null)
        putNull(label, true)
    else {
        putNull(label, false)
        putShort(label, value)
    }
}

/**
 * Writes the given element to the given label.
 */
suspend fun SuspendSink.putNullableInt(label: String, value: Int?) {
    if (value == null)
        putNull(label, true)
    else {
        putNull(label, false)
        putInt(label, value)
    }
}

/**
 * Writes the given element to the given label.
 */
suspend fun SuspendSink.putNullableLong(label: String, value: Long?) {
    if (value == null)
        putNull(label, true)
    else {
        putNull(label, false)
        putLong(label, value)
    }
}

/**
 * Writes the given element to the given label.
 */
suspend fun SuspendSink.putNullableFloat(label: String, value: Float?) {
    if (value == null)
        putNull(label, true)
    else {
        putNull(label, false)
        putFloat(label, value)
    }
}

/**
 * Writes the given element to the given label.
 */
suspend fun SuspendSink.putNullableDouble(label: String, value: Double?) {
    if (value == null)
        putNull(label, true)
    else {
        putNull(label, false)
        putDouble(label, value)
    }
}

/**
 * Writes the given element to the given label.
 */
suspend fun SuspendSink.putNullableChar(label: String, value: Char?) {
    if (value == null)
        putNull(label, true)
    else {
        putNull(label, false)
        putChar(label, value)
    }
}

/**
 * Writes the given element to the given label.
 */
suspend fun SuspendSink.putNullableUnit(label: String, value: Unit?) {
    if (value == null)
        putNull(label, true)
    else {
        putNull(label, false)
        putUnit(label, value)
    }
}

/**
 * Writes the given element to the given label.
 */
suspend fun SuspendSink.putNullableString(label: String, value: String?) {
    if (value == null)
        putNull(label, true)
    else {
        putNull(label, false)
        putString(label, value)
    }
}

/**
 * Puts a nested value for a type.
 */
fun <T> Sink.putNested(
        module: Module, funneler: Funneler<T>, label: String, type: Type, value: T) =
        if (type.isTerminal())
            putTerminalNested(module, funneler, label, type, value)
        else
            putDynamicNested(module, label, type, value)

/**
 * Puts a nested value for a type.
 */
suspend fun <T> SuspendSink.putNested(
        module: Module, funneler: Funneler<T>, label: String, type: Type, value: T) =
        if (type.isTerminal())
            putTerminalNested(module, funneler, label, type, value)
        else
            putDynamicNested(module, label, type, value)

/**
 * Puts a nested value for a type.
 */
fun <T> Sink.putNullableNested(
        module: Module, funneler: Funneler<T>, label: String, type: Type, value: T?) =
        if (type.isTerminal())
            putNullableTerminalNested(module, funneler, label, type, value)
        else
            putNullableDynamicNested(module, label, type, value)

/**
 * Puts a nested value for a type.
 */
suspend fun <T> SuspendSink.putNullableNested(
        module: Module, funneler: Funneler<T>, label: String, type: Type, value: T?) =
        if (type.isTerminal())
            putNullableTerminalNested(module, funneler, label, type, value)
        else
            putNullableDynamicNested(module, label, type, value)


/**
 * Puts a nested value for a type.
 */
fun <T> Sink.putTerminalNested(
        module: Module, funneler: Funneler<T>, label: String, type: Type, value: T) {
    if (beginNested(label, type, value))
        funneler.write(module, type, this, value)
    endNested(label, type, value)
}


/**
 * Puts a nested value for a type. Prefixes a read to the null flag.
 */
fun <T> Sink.putNullableTerminalNested(
        module: Module, funneler: Funneler<T>, label: String, type: Type, value: T?) {
    if (value == null) {
        putNull(label, true)
        return
    }
    putNull(label, false)
    if (beginNested(label, type, value))
        funneler.write(module, type, this, value)
    endNested(label, type, value)
}

/**
 * Puts a nested value for a type.
 */
suspend fun <T> SuspendSink.putTerminalNested(
        module: Module, funneler: Funneler<T>, label: String, type: Type, value: T) {
    if (beginNested(label, type, value))
        funneler.write(module, type, this, value)
    endNested(label, type, value)
}


/**
 * Puts a nested value for a type. Prefixes a read to the null flag.
 */
suspend fun <T> SuspendSink.putNullableTerminalNested(
        module: Module, funneler: Funneler<T>, label: String, type: Type, value: T?) {
    if (value == null) {
        putNull(label, true)
        return
    }
    putNull(label, false)
    if (beginNested(label, type, value))
        funneler.write(module, type, this, value)
    endNested(label, type, value)
}


/**
 * Puts a nested value for a type.
 */
fun <T> Sink.putDynamicNested(
        module: Module, label: String, type: Type, value: T) {
    if (beginNested(label, type, value)) {
        val funneler = module.resolve<T>(type.forInstance(value))
        funneler.write(module, type, this, value)
    }
    endNested(label, type, value)
}


/**
 * Puts a nested value for a type. Prefixes a read to the null flag.
 */
fun <T> Sink.putNullableDynamicNested(
        module: Module, label: String, type: Type, value: T?) {
    if (value == null) {
        putNull(label, true)
        return
    }
    putNull(label, false)
    if (beginNested(label, type, value)) {
        val funneler = module.resolve<T>(type.forInstance(value))
        funneler.write(module, type, this, value)
    }
    endNested(label, type, value)
}

/**
 * Puts a nested value for a type.
 */
suspend fun <T> SuspendSink.putDynamicNested(
        module: Module, label: String, type: Type, value: T) {
    if (beginNested(label, type, value)) {
        val funneler = module.resolve<T>(type.forInstance(value))
        funneler.write(module, type, this, value)
    }
    endNested(label, type, value)
}


/**
 * Puts a nested value for a type. Prefixes a read to the null flag.
 */
suspend fun <T> SuspendSink.putNullableDynamicNested(
        module: Module, label: String, type: Type, value: T?) {
    if (value == null) {
        putNull(label, true)
        return
    }
    putNull(label, false)
    if (beginNested(label, type, value)) {
        val funneler = module.resolve<T>(type.forInstance(value))
        funneler.write(module, type, this, value)
    }
    endNested(label, type, value)
}


/**
 * Marks beginning and ending of a [type] around the given [block].
 */
inline fun Sink.markAround(type: Type, value: Any?, block: () -> Unit) {
    if (begin(type, value))
        block()
    end(type, value)
}

/**
 * Marks beginning and ending of a [type] around the given [block].
 */
suspend inline fun SuspendSink.markAround(type: Type, value: Any?, block: () -> Unit) {
    if (begin(type, value))
        block()
    end(type, value)
}