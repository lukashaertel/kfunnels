package eu.metatools.kfunnels

import eu.metatools.kfunnels.base.ListSink

/**
 * A source that provides values for a [Funneler].
 */
interface Source {
    /**
     * Begins reading a block.
     */
    fun begin(type: Type)

    /**
     * Return true if block is ending now.
     */
    fun isEnd(): Boolean

    /**
     * Ends reading a block.
     */
    fun end(type: Type)


    /**
     * Enters the nesting of a new element for static [type]. May return a substitute type.
     */
    fun beginNested(label: String, type: Type): Type

    /**
     * Leaves the nesting of the element for static [type].
     */
    fun endNested(label: String, type: Type)


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
}

/**
 * Gets the value for the label, will throw an exception if element is not present. Prefixes a read to the null flag.
 */
fun Source.getNullableBoolean(label: String): Boolean? =
        if (isNull(label))
            null
        else
            getBoolean(label)

/**
 * Gets the value for the label, will throw an exception if element is not present. Prefixes a read to the null flag.
 */
fun Source.getNullableByte(label: String): Byte? =
        if (isNull(label))
            null
        else
            getByte(label)

/**
 * Gets the value for the label, will throw an exception if element is not present. Prefixes a read to the null flag.
 */
fun Source.getNullableShort(label: String): Short? =
        if (isNull(label))
            null
        else
            getShort(label)

/**
 * Gets the value for the label, will throw an exception if element is not present. Prefixes a read to the null flag.
 */
fun Source.getNullableInt(label: String): Int? =
        if (isNull(label))
            null
        else
            getInt(label)

/**
 * Gets the value for the label, will throw an exception if element is not present. Prefixes a read to the null flag.
 */
fun Source.getNullableLong(label: String): Long? =
        if (isNull(label))
            null
        else
            getLong(label)

/**
 * Gets the value for the label, will throw an exception if element is not present. Prefixes a read to the null flag.
 */
fun Source.getNullableFloat(label: String): Float? =
        if (isNull(label))
            null
        else
            getFloat(label)

/**
 * Gets the value for the label, will throw an exception if element is not present. Prefixes a read to the null flag.
 */
fun Source.getNullableDouble(label: String): Double? =
        if (isNull(label))
            null
        else
            getDouble(label)

/**
 * Gets the value for the label, will throw an exception if element is not present. Prefixes a read to the null flag.
 */
fun Source.getNullableChar(label: String): Char? =
        if (isNull(label))
            null
        else
            getChar(label)

/**
 * Gets the value for the label, will throw an exception if element is not present. Prefixes a read to the null flag.
 */
fun Source.getNullableUnit(label: String): Unit? =
        if (isNull(label))
            null
        else
            getUnit(label)

/**
 * Gets the value for the label, will throw an exception if element is not present. Prefixes a read to the null flag.
 */
fun Source.getNullableString(label: String): String? =
        if (isNull(label))
            null
        else
            getString(label)


/**
 * A source that provides values for a [Funneler] where methods are suspended.
 */
interface SuspendSource {
    /**
     * Begins reading a block.
     */
    suspend fun begin(type: Type)

    /**
     * Return true if block is ending now.
     */
    suspend fun isEnd(): Boolean

    /**
     * Ends reading a block.
     */
    suspend fun end(type: Type)


    /**
     * Enters the nesting of a new element for static [type]. May return a substitute type.
     */
    suspend fun beginNested(label: String, type: Type): Type

    /**
     * Leaves the nesting of the element for static [type].
     */
    suspend fun endNested(label: String, type: Type)


    /**
     * Gets the value for the label, will throw an exception if element is not present.
     */
    suspend fun getBoolean(label: String): Boolean

    /**
     * Gets the value for the label, will throw an exception if element is not present.
     */
    suspend fun getByte(label: String): Byte

    /**
     * Gets the value for the label, will throw an exception if element is not present.
     */
    suspend fun getShort(label: String): Short

    /**
     * Gets the value for the label, will throw an exception if element is not present.
     */
    suspend fun getInt(label: String): Int

    /**
     * Gets the value for the label, will throw an exception if element is not present.
     */
    suspend fun getLong(label: String): Long

    /**
     * Gets the value for the label, will throw an exception if element is not present.
     */
    suspend fun getFloat(label: String): Float

    /**
     * Gets the value for the label, will throw an exception if element is not present.
     */
    suspend fun getDouble(label: String): Double

    /**
     * Gets the value for the label, will throw an exception if element is not present.
     */
    suspend fun getChar(label: String): Char

    /**
     * True if the given label is for a null value.
     */
    suspend fun isNull(label: String): Boolean

    /**
     * Gets the value for the label, will throw an exception if element is not present.
     */
    suspend fun getUnit(label: String)

    /**
     * Gets the value for the label, will throw an exception if element is not present.
     */
    suspend fun getString(label: String): String
}

/**
 * Gets the value for the label, will throw an exception if element is not present. Prefixes a read to the null flag.
 */
suspend fun SuspendSource.getNullableBoolean(label: String): Boolean? =
        if (isNull(label))
            null
        else
            getBoolean(label)

/**
 * Gets the value for the label, will throw an exception if element is not present. Prefixes a read to the null flag.
 */
suspend fun SuspendSource.getNullableByte(label: String): Byte? =
        if (isNull(label))
            null
        else
            getByte(label)

/**
 * Gets the value for the label, will throw an exception if element is not present. Prefixes a read to the null flag.
 */
suspend fun SuspendSource.getNullableShort(label: String): Short? =
        if (isNull(label))
            null
        else
            getShort(label)

/**
 * Gets the value for the label, will throw an exception if element is not present. Prefixes a read to the null flag.
 */
suspend fun SuspendSource.getNullableInt(label: String): Int? =
        if (isNull(label))
            null
        else
            getInt(label)

/**
 * Gets the value for the label, will throw an exception if element is not present. Prefixes a read to the null flag.
 */
suspend fun SuspendSource.getNullableLong(label: String): Long? =
        if (isNull(label))
            null
        else
            getLong(label)

/**
 * Gets the value for the label, will throw an exception if element is not present. Prefixes a read to the null flag.
 */
suspend fun SuspendSource.getNullableFloat(label: String): Float? =
        if (isNull(label))
            null
        else
            getFloat(label)

/**
 * Gets the value for the label, will throw an exception if element is not present. Prefixes a read to the null flag.
 */
suspend fun SuspendSource.getNullableDouble(label: String): Double? =
        if (isNull(label))
            null
        else
            getDouble(label)

/**
 * Gets the value for the label, will throw an exception if element is not present. Prefixes a read to the null flag.
 */
suspend fun SuspendSource.getNullableChar(label: String): Char? =
        if (isNull(label))
            null
        else
            getChar(label)

/**
 * Gets the value for the label, will throw an exception if element is not present. Prefixes a read to the null flag.
 */
suspend fun SuspendSource.getNullableUnit(label: String): Unit? =
        if (isNull(label))
            null
        else
            getUnit(label)

/**
 * Gets the value for the label, will throw an exception if element is not present. Prefixes a read to the null flag.
 */
suspend fun SuspendSource.getNullableString(label: String): String? =
        if (isNull(label))
            null
        else
            getString(label)



/**
 * Marks beginning and ending of a [type] around the given [block].
 * @return Returns the block's return value
 */
inline fun <T> Source.markAround(type: Type, block: () -> T): T {
    begin(type)
    val result = block()
    end(type)
    return result
}
/**
 * Marks beginning and ending of a [type] around the given [block].
 * @return Returns the block's return value
 */
suspend inline fun <T> SuspendSource.markAround(type: Type, block: () -> T): T {
    begin(type)
    val result = block()
    end(type)
    return result
}