package eu.metatools.kfunnels.base

import eu.metatools.kfunnels.Event
import eu.metatools.kfunnels.Sink
import eu.metatools.kfunnels.Source
import eu.metatools.kfunnels.Type

/**
 * Sink that receives items into a list. Use [reset] to get the list and reset the buffer.
 */
class ListSink : Sink {
    private val list = arrayListOf<Any?>()

    override fun begin(type: Type) {
        list += Event.BEGIN
    }

    override fun end(type: Type) {
        list += Event.END
    }

    override fun beginNested(label: String, type: Type, value: Any?) {
        list += Event.BEGIN_NESTED
        list += type
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

    private inline fun <reified T> next(): T {
        check(pos in list.indices)
        return list[pos++] as T
    }

    private inline fun <reified T> peek(): T {
        check(pos + 1 in list.indices)
        return list[pos + 1] as T
    }

    private val at: Any?
        get() {
            check(pos - 1 in list.indices)
            return list[pos - 1]
        }

    override fun begin(type: Type) {
        check(next<Event>() == Event.BEGIN)
    }

    override fun isEnd() =
            peek<Event>() == Event.END

    override fun end(type: Type) {
        check(next<Event>() == Event.END)
    }

    override fun beginNested(label: String, type: Type): Type {
        check(next<Event>() == Event.BEGIN_NESTED)
        return next()
    }

    override fun endNested(label: String, type: Type) {
        check(next<Event>() == Event.END_NESTED)
    }

    override fun getBoolean(label: String): Boolean =
            next()

    override fun getByte(label: String): Byte =
            next()

    override fun getShort(label: String): Short =
            next()

    override fun getInt(label: String): Int =
            next()

    override fun getLong(label: String): Long =
            next()

    override fun getFloat(label: String): Float =
            next()

    override fun getDouble(label: String): Double =
            next()

    override fun getChar(label: String): Char =
            next()

    override fun isNull(label: String): Boolean =
            when (next<Event>()) {
                Event.IS_NULL -> true
                Event.IS_NOT_NULL -> false
                else -> error("Not at a valid list token: $at")
            }

    override fun getUnit(label: String) =
            next<Unit>()

    override fun getString(label: String): String =
            next()
}