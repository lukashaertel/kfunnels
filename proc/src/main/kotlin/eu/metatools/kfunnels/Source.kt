package eu.metatools.kfunnels

/**
 * Result of [Source.begin] and [SuspendSource.begin].
 */
sealed class Begin

/**
 * A value could already be determined, no unfunneling required.
 */
data class Value<T>(val value: T) : Begin()

/**
 * Go ahead with unfunneling as is.
 */
object Unfunnel : Begin()

/**
 * Result of [Source.beginNested] and [SuspendSource.beginNested].
 */
sealed class Nested

/**
 * A value could already be determined, no further unfunneling required.
 */
data class Item<T>(val value: T) : Nested()

/**
 * Further unfunneling required, but for a different type.
 */
data class Substitute(val type: Type) : Nested()

/**
 * Go ahead with unfunneling as is.
 */
object Nest : Nested()

/**
 * A source that provides values for a [Funneler].
 */
interface Source {
    /**
     * Begins reading a block.
     */
    fun begin(type: Type): Begin

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
    fun beginNested(label: String, type: Type): Nested

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
    suspend fun begin(type: Type): Begin

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
    suspend fun beginNested(label: String, type: Type): Nested

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

private fun errorSubstitutionInTerminal(): Nothing {
    error("Substitute is not a valid output for nesting of terminal types.")
}

/**
 * Gets a nested value.
 */
fun <T> Source.getNested(
        module: Module, funneler: Funneler<T>, label: String, type: Type) =
        if (type.isTerminal())
            getTerminalNested(module, funneler, label, type)
        else
            getDynamicNested(module, funneler, label, type)


/**
 * Gets a nested value.
 */
suspend fun <T> SuspendSource.getNested(
        module: Module, funneler: Funneler<T>, label: String, type: Type) =
        if (type.isTerminal())
            getTerminalNested(module, funneler, label, type)
        else
            getDynamicNested(module, funneler, label, type)

/**
 * Gets a nullable nested value.
 */
fun <T> Source.getNullableNested(
        module: Module, funneler: Funneler<T>, label: String, type: Type) =
        if (type.isTerminal())
            getNullableTerminalNested(module, funneler, label, type)
        else
            getNullableDynamicNested(module, funneler, label, type)


/**
 * Gets a nullable nested value.
 */
suspend fun <T> SuspendSource.getNullableNested(
        module: Module, funneler: Funneler<T>, label: String, type: Type) =
        if (type.isTerminal())
            getNullableTerminalNested(module, funneler, label, type)
        else
            getNullableDynamicNested(module, funneler, label, type)

/**
 * Gets a nested value for a terminal type.
 */
fun <T> Source.getTerminalNested(
        module: Module, funneler: Funneler<T>, label: String, type: Type): T {
    val sub = beginNested(label, type)

    @Suppress("unchecked_cast")
    when (sub) {
        is Item<*> -> {
            endNested(label, type)
            return sub.value as T
        }
        is Substitute -> errorSubstitutionInTerminal()
        Nest -> {
            val r = funneler.read(module, type, this)
            endNested(label, type)
            return r
        }
    }
}

/**
 * Gets a nested value for a terminal type. Prefixes a read to the null flag.
 */
fun <T> Source.getNullableTerminalNested(
        module: Module, funneler: Funneler<T>, label: String, type: Type): T? {
    if (isNull(label))
        return null

    val sub = beginNested(label, type)

    @Suppress("unchecked_cast")
    when (sub) {
        is Item<*> -> {
            endNested(label, type)
            return sub.value as T
        }
        is Substitute -> errorSubstitutionInTerminal()
        Nest -> {
            val r = funneler.read(module, type, this)
            endNested(label, type)
            return r
        }
    }
}


/**
 * Gets a nested value for a terminal type.
 */
suspend fun <T> SuspendSource.getTerminalNested(
        module: Module, funneler: Funneler<T>, label: String, type: Type): T {
    val sub = beginNested(label, type)

    @Suppress("unchecked_cast")
    when (sub) {
        is Item<*> -> {
            endNested(label, type)
            return sub.value as T
        }
        is Substitute -> errorSubstitutionInTerminal()
        Nest -> {
            val r = funneler.read(module, type, this)
            endNested(label, type)
            return r
        }
    }
}

/**
 * Gets a nested value for a terminal type. Prefixes a read to the null flag.
 */
suspend fun <T> SuspendSource.getNullableTerminalNested(
        module: Module, funneler: Funneler<T>, label: String, type: Type): T? {
    if (isNull(label))
        return null

    val sub = beginNested(label, type)

    @Suppress("unchecked_cast")
    when (sub) {
        is Item<*> -> {
            endNested(label, type)
            return sub.value as T
        }
        is Substitute -> errorSubstitutionInTerminal()
        Nest -> {
            val r = funneler.read(module, type, this)
            endNested(label, type)
            return r
        }
    }
}


/**
 * Gets a nested value for a terminal type.
 */
fun <T> Source.getDynamicNested(
        module: Module, funneler: Funneler<T>, label: String, type: Type): T {
    val sub = beginNested(label, type)

    @Suppress("unchecked_cast")
    when (sub) {
        is Item<*> -> {
            endNested(label, type)
            return sub.value as T
        }
        is Substitute -> {
            val subFunneler = module.resolve<T>(sub.type)
            val r = subFunneler.read(module, sub.type, this)
            endNested(label, type)
            return r
        }
        Nest -> {
            val r = funneler.read(module, type, this)
            endNested(label, type)
            return r
        }
    }
}

/**
 * Gets a nested value for a terminal type. Prefixes a read to the null flag.
 */
fun <T> Source.getNullableDynamicNested(
        module: Module, funneler: Funneler<T>, label: String, type: Type): T? {
    if (isNull(label))
        return null

    val sub = beginNested(label, type)

    @Suppress("unchecked_cast")
    when (sub) {
        is Item<*> -> {
            endNested(label, type)
            return sub.value as T
        }
        is Substitute -> {
            val subFunneler = module.resolve<T>(sub.type)
            val r = subFunneler.read(module, sub.type, this)
            endNested(label, type)
            return r
        }
        Nest -> {
            val r = funneler.read(module, type, this)
            endNested(label, type)
            return r
        }
    }
}


/**
 * Gets a nested value for a terminal type.
 */
suspend fun <T> SuspendSource.getDynamicNested(
        module: Module, funneler: Funneler<T>, label: String, type: Type): T {
    val sub = beginNested(label, type)

    @Suppress("unchecked_cast")
    when (sub) {
        is Item<*> -> {
            endNested(label, type)
            return sub.value as T
        }
        is Substitute -> {
            val subFunneler = module.resolve<T>(sub.type)
            val r = subFunneler.read(module, sub.type, this)
            endNested(label, type)
            return r
        }
        Nest -> {
            val r = funneler.read(module, type, this)
            endNested(label, type)
            return r
        }
    }
}

/**
 * Gets a nested value for a terminal type. Prefixes a read to the null flag.
 */
suspend fun <T> SuspendSource.getNullableDynamicNested(
        module: Module, funneler: Funneler<T>, label: String, type: Type): T? {
    if (isNull(label))
        return null

    val sub = beginNested(label, type)

    @Suppress("unchecked_cast")
    when (sub) {
        is Item<*> -> {
            endNested(label, type)
            return sub.value as T
        }
        is Substitute -> {
            val subFunneler = module.resolve<T>(sub.type)
            val r = subFunneler.read(module, sub.type, this)
            endNested(label, type)
            return r
        }
        Nest -> {
            val r = funneler.read(module, type, this)
            endNested(label, type)
            return r
        }
    }
}


/**
 * Marks beginning and ending of a [type] around the given [block].
 * @return Returns the block's return value
 */
inline fun <T> Source.markAround(type: Type, block: () -> T): T {
    val sub = begin(type)

    @Suppress("unchecked_cast")
    when (sub) {
        is Value<*> -> {
            end(type)
            return sub.value as T
        }
        is Unfunnel -> {
            val result = block()
            end(type)
            return result
        }
    }
}

/**
 * Marks beginning and ending of a [type] around the given [block].
 * @return Returns the block's return value
 */
suspend inline fun <T> SuspendSource.markAround(type: Type, block: () -> T): T {
    val sub = begin(type)

    @Suppress("unchecked_cast")
    when (sub) {
        is Value<*> -> {
            end(type)
            return sub.value as T
        }
        is Unfunnel -> {
            val result = block()
            end(type)
            return result
        }
    }
}