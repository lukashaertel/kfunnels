package eu.metatools.kfunnels

import eu.metatools.kfunnels.base.NoFunneler
import eu.metatools.kfunnels.base.NullableFunneler
import eu.metatools.kfunnels.base.StdlibModule

/**
 * A module provides resolution of funneler from class.
 */
interface Module {
    /**
     * Resolves the funneler for the given type.
     */
    fun <T> resolve(type: Type): Funneler<T>
}


/**
 * Interface implemented by generated modules.
 */
interface GeneratedModule : Module {
    /**
     * List of supported types.
     */
    val types: List<Type>
}

/**
 * Module provider for the service registry.
 */
interface ModuleProvider {
    fun provide(): Module
}

/**
 * Returns a composite module of this module and the [StdlibModule]. This should normally be used to support primitives.
 */
val Module.std get() = this then StdlibModule

/**
 * Adds support for first level nullable inputs to [Module.resolve]. Nested nullable values are ususally handled within
 * the responsible funneler itself. If the first call is to a nullable type, this method should be used.
 */
fun Module.withNullableSupport() = object : Module {
    override fun <T> resolve(type: Type): Funneler<T> {
        // Catch nullable types
        if (type.nullable)
            @Suppress("unchecked_cast")
            return NullableFunneler<T>(this@withNullableSupport.resolve(type)) as Funneler<T>

        // Otherwise use base
        return this@withNullableSupport.resolve(type)
    }
}

infix fun Module.then(nextModule: Module) = object : Module {
    override fun <T> resolve(type: Type): Funneler<T> {
        val p = this@then.resolve<T>(type)
        if (p == NoFunneler)
            return nextModule.resolve(type)
        else
            return p
    }
}

/**
 * Resolves the funneler for the given type (non-nullable, no arguments).
 */
inline fun <reified T> Module.resolve(): Funneler<T> =
        resolve(Type(T::class, false, listOf()))

/**
 * Resolves the funneler for the type (non-nullable, no arguments), and reads from the source.
 */
inline fun <reified T> Module.read(source: SeqSource): T =
        resolve<T>().read(this, source)

/**
 * Resolves the funneler for the type (non-nullable, no arguments), and reads from the source.
 */
inline fun <reified T> Module.read(source: LabelSource): T =
        resolve<T>().read(this, source)

/**
 * Resolves the funneler for the type (non-nullable, no arguments), and writes to the sink.
 */
inline fun <reified T> Module.write(sink: SeqSink, item: T) {
    resolve<T>().write(this, sink, item)
}

/**
 * Resolves the funneler for the type (non-nullable, no arguments), and writes to the sink.
 */
inline fun <reified T> Module.write(sink: LabelSink, item: T) {
    resolve<T>().write(this, sink, item)
}

/**
 * Funnels and unfunnels elements of type [T] into and out of sequence and label sinks.
 */
interface Funneler<T> {
    /**
     * Uses the given module to read the item from the sequence source.
     */
    fun read(module: Module, source: SeqSource): T

    /**
     * Uses the given module to read the item from the label source.
     */
    fun read(module: Module, source: LabelSource): T

    /**
     * Uses the given module to write the item to the sequence sink.
     */
    fun write(module: Module, sink: SeqSink, item: T)

    /**
     * Uses the given module to write the item to the label sink.
     */
    fun write(module: Module, sink: LabelSink, item: T)
}

/**
 * Interface implemented by funnelers generated for classes.
 */
interface GeneratedFunneler<T> : Funneler<T> {
    /**
     * Gets the module in which the funneler is registered.
     */
    val module: Module

    /**
     * Type of this funneler.
     * TODO: Type arguments maybe?
     */
    val type: Type
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

/**
 * A source that provides values by sequence.
 */
interface SeqSource {
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
interface LabelSource {
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
    fun endNested()
}

/**
 * A sink that uses sequence to match up values.
 */
interface SeqSink {
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
interface LabelSink {
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
    fun endNested()
}
