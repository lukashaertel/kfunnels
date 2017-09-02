package eu.metatools.kfunnels.base

import eu.metatools.kfunnels.*
import kotlinx.coroutines.experimental.channels.Channel

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

class ChannelSource(val channel: Channel<Any?>) : SuspendSource {

    override suspend fun begin(type: Type): Begin {
        check(channel.receive() == Event.BEGIN)
        return Unfunnel
    }

    override suspend fun isEnd() =
            channel.receive() == Event.END

    override suspend fun end(type: Type) {
        check(channel.receive() == Event.END)
    }

    override suspend fun beginNested(label: String, type: Type): Nested {
        check(channel.receive() == Event.BEGIN_NESTED)
        if (type.isTerminal())
            return Nest
        else
            return Substitute(channel.receive() as Type)
    }

    override suspend fun endNested(label: String, type: Type) {
        check(channel.receive() == Event.END_NESTED)
    }

    override suspend fun getBoolean(label: String): Boolean =
            channel.receive() as Boolean

    override suspend fun getByte(label: String): Byte =
            channel.receive() as Byte

    override suspend fun getShort(label: String): Short =
            channel.receive() as Short

    override suspend fun getInt(label: String): Int =
            channel.receive() as Int

    override suspend fun getLong(label: String): Long =
            channel.receive() as Long

    override suspend fun getFloat(label: String): Float =
            channel.receive() as Float

    override suspend fun getDouble(label: String): Double =
            channel.receive() as Double

    override suspend fun getChar(label: String): Char =
            channel.receive() as Char

    override suspend fun isNull(label: String): Boolean =
            when (channel.receive()) {
                Event.IS_NULL -> true
                Event.IS_NOT_NULL -> false
                else -> error("Not at a valid token.")
            }

    override suspend fun getUnit(label: String) =
            channel.receive() as Unit

    override suspend fun getString(label: String): String =
            channel.receive() as String
}