package eu.metatools.kfunnels.tools

import eu.metatools.kfunnels.*
import eu.metatools.kfunnels.utils.SuspendPeekable
import kotlinx.coroutines.experimental.channels.Channel

/**
 * Streams the items into the channel using [Channel.send].
 */
class ChannelSink(val channel: Channel<Any?>) : SuspendSink {

    override suspend fun begin(type: Type, value: Any?): Boolean {
        channel.send(Event.BEGIN)
        return true
    }

    override suspend fun end(type: Type, value: Any?) {
        channel.send(Event.END)
    }

    override suspend fun beginNested(label: String, type: Type, value: Any?): Boolean {
        channel.send(Event.BEGIN_NESTED)
        if (!type.isTerminal())
            channel.send(type.forInstance(value))
        return true
    }

    override suspend fun endNested(label: String, type: Type, value: Any?) {
        channel.send(Event.END_NESTED)
    }

    override suspend fun putBoolean(label: String, value: Boolean) {
        channel.send(value)
    }

    override suspend fun putByte(label: String, value: Byte) {
        channel.send(value)
    }

    override suspend fun putShort(label: String, value: Short) {
        channel.send(value)
    }

    override suspend fun putInt(label: String, value: Int) {
        channel.send(value)
    }

    override suspend fun putLong(label: String, value: Long) {
        channel.send(value)
    }

    override suspend fun putFloat(label: String, value: Float) {
        channel.send(value)
    }

    override suspend fun putDouble(label: String, value: Double) {
        channel.send(value)
    }

    override suspend fun putChar(label: String, value: Char) {
        channel.send(value)
    }

    override suspend fun putNull(label: String, isNull: Boolean) {
        if (isNull)
            channel.send(Event.IS_NULL)
        else
            channel.send(Event.IS_NOT_NULL)
    }

    override suspend fun putUnit(label: String, value: Unit) {
        channel.send(value)
    }

    override suspend fun putString(label: String, value: String) {
        channel.send(value)
    }
}

/**
 * Receives items from the channel using [Channel.receive].
 */
class ChannelSource(val channel: Channel<Any?>) : SuspendSource {
    private val items = object : SuspendPeekable<Any?>() {
        suspend override fun provide() =
                channel.receive()
    }


    override suspend fun begin(type: Type): Begin {
        check(items.takeNext() == Event.BEGIN)
        return Unfunnel
    }

    override suspend fun isEnd() =
            items.peekNext() == Event.END

    override suspend fun end(type: Type) {
        check(items.takeNext() == Event.END)
    }

    override suspend fun beginNested(label: String, type: Type): Nested {
        check(items.takeNext() == Event.BEGIN_NESTED)
        if (type.isTerminal())
            return Nest
        else
            return Substitute(items.takeNext() as Type)
    }

    override suspend fun endNested(label: String, type: Type) {
        check(items.takeNext() == Event.END_NESTED)
    }

    override suspend fun getBoolean(label: String): Boolean =
            items.takeNext() as Boolean

    override suspend fun getByte(label: String): Byte =
            items.takeNext() as Byte

    override suspend fun getShort(label: String): Short =
            items.takeNext() as Short

    override suspend fun getInt(label: String): Int =
            items.takeNext() as Int

    override suspend fun getLong(label: String): Long =
            items.takeNext() as Long

    override suspend fun getFloat(label: String): Float =
            items.takeNext() as Float

    override suspend fun getDouble(label: String): Double =
            items.takeNext() as Double

    override suspend fun getChar(label: String): Char =
            items.takeNext() as Char

    override suspend fun isNull(label: String): Boolean =
            when (items.takeNext()) {
                Event.IS_NULL -> true
                Event.IS_NOT_NULL -> false
                else -> error("Not at a valid token.")
            }

    override suspend fun getUnit(label: String) =
            items.takeNext() as Unit

    override suspend fun getString(label: String): String =
            items.takeNext() as String
}