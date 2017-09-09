package eu.metatools.kfunnels.tools

import eu.metatools.kfunnels.*

/**
 * Sink that receives items into a list. Use [reset] to get the list and reset the buffer.
 */
class ListSink : Sink {
    private val list = arrayListOf<Any?>()

    fun reset(): List<Any?> {
        val result = list.toList()
        list.clear()
        return result
    }

    override fun begin(type: Type, value: Any?): Boolean {
        list += Event.BEGIN
        return true
    }

    override fun end(type: Type, value: Any?) {
        list += Event.END
    }

    override fun beginNested(label: String, type: Type, value: Any?): Boolean {
        list += Event.BEGIN_NESTED
        if (!type.isTerminal())
            list += type.forInstance(value)
        return true
    }

    override fun endNested(label: String, type: Type, value: Any?) {
        list += Event.END_NESTED
    }

    override fun putBoolean(label: String, value: Boolean) {
        list += value
    }

    override fun putByte(label: String, value: Byte) {
        list += value
    }

    override fun putShort(label: String, value: Short) {
        list += value
    }

    override fun putInt(label: String, value: Int) {
        list += value
    }

    override fun putLong(label: String, value: Long) {
        list += value
    }

    override fun putFloat(label: String, value: Float) {
        list += value
    }

    override fun putDouble(label: String, value: Double) {
        list += value
    }

    override fun putChar(label: String, value: Char) {
        list += value
    }

    override fun putNull(label: String, isNull: Boolean) {
        if (isNull)
            list += Event.IS_NULL
        else
            list += Event.IS_NOT_NULL
    }

    override fun putUnit(label: String, value: Unit) {
        list += value
    }

    override fun putString(label: String, value: String) {
        list += value
    }
}

/**
 * Source that provides elements from the [list], use [ListSink] to create it.
 */
class ListSource(val list: List<Any?>) : Source {

    private var pos = 0

    private inline fun <reified T> advance(): T {
        check(pos in list.indices)
        return list[pos++] as T
    }

    private val current: Any?
        get() {
            check(pos in list.indices)
            return list[pos]
        }

    private val before: Any?
        get() {
            check(pos - 1 in list.indices)
            return list[pos - 1]
        }

    override fun begin(type: Type): Begin {
        check(advance<Event>() == Event.BEGIN)
        return Unfunnel
    }

    override fun isEnd() =
            current == Event.END

    override fun end(type: Type) {
        check(advance<Event>() == Event.END)
    }

    override fun beginNested(label: String, type: Type): Nested {
        check(advance<Event>() == Event.BEGIN_NESTED)
        if (type.isTerminal())
            return Nest
        else
            return Substitute(advance())
    }

    override fun endNested(label: String, type: Type) {
        check(advance<Event>() == Event.END_NESTED)
    }

    override fun getBoolean(label: String): Boolean =
            advance()

    override fun getByte(label: String): Byte =
            advance()

    override fun getShort(label: String): Short =
            advance()

    override fun getInt(label: String): Int =
            advance()

    override fun getLong(label: String): Long =
            advance()

    override fun getFloat(label: String): Float =
            advance()

    override fun getDouble(label: String): Double =
            advance()

    override fun getChar(label: String): Char =
            advance()

    override fun isNull(label: String): Boolean =
            when (advance<Event>()) {
                Event.IS_NULL -> true
                Event.IS_NOT_NULL -> false
                else -> error("Not at a valid list token: $before")
            }

    override fun getUnit(label: String) =
            advance<Unit>()

    override fun getString(label: String): String =
            advance()
}