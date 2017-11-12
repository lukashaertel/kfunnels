package eu.metatools.kfunnels.tools

import eu.metatools.kfunnels.*
import eu.metatools.kfunnels.utils.None
import eu.metatools.kfunnels.utils.Option
import eu.metatools.kfunnels.utils.Some
import java.io.*
import java.util.*

// TODO: Funnel needsEnd
val endMark = 0x1337fabcaffe420bL

/**
 * A raw data sink.
 */
class RawSink(val target: OutputStream,
              val utf8: Boolean = true) : Sink, AutoCloseable {
    override fun close() {
        prim.close()
    }

    private val prim = DataOutputStream(target)

    override fun begin(type: Type, value: Any?): Boolean {
        return true
    }

    override fun end(type: Type, value: Any?) {
        prim.writeLong(endMark)
    }

    override fun beginNested(label: String, type: Type, value: Any?): Boolean {
        if (!type.isTerminal())
            prim.writeUTF(type.forInstance(value).toString())

        return true
    }

    override fun endNested(label: String, type: Type, value: Any?) {
    }

    override fun putBoolean(label: String, value: Boolean) {
        prim.writeBoolean(value)
    }

    override fun putByte(label: String, value: Byte) {
        prim.writeByte(value.toInt())
    }

    override fun putShort(label: String, value: Short) {
        prim.writeShort(value.toInt())
    }

    override fun putInt(label: String, value: Int) {
        prim.writeInt(value)
    }

    override fun putLong(label: String, value: Long) {
        prim.writeLong(value)
    }

    override fun putFloat(label: String, value: Float) {
        prim.writeFloat(value)
    }

    override fun putDouble(label: String, value: Double) {
        prim.writeDouble(value)
    }

    override fun putChar(label: String, value: Char) {
        prim.writeChar(value.toInt())
    }

    override fun putNull(label: String, isNull: Boolean) {
        prim.writeBoolean(isNull)
    }

    override fun putUnit(label: String, value: Unit) {
    }

    override fun putString(label: String, value: String) {
        if (utf8)
            prim.writeUTF(value)
        else {
            prim.writeShort(value.length)
            prim.writeChars(value)
        }
    }
}

private fun InputStream.allowMark(length: Int) =
        if (markSupported())
            this
        else
            BufferedInputStream(this, length)

/**
 * A raw data source.
 */
class RawSource(val source: InputStream,
                val utf8: Boolean = true) : Source, AutoCloseable {
    override fun close() {
        prim.close()
    }

    private val prim = DataInputStream(source.allowMark(8))

    /**
     * Peeks the next long
     */
    private fun peekLong(): Long {
        prim.mark(8)
        val result = prim.readLong()
        prim.reset()
        return result
    }

    override fun begin(type: Type): Begin {
        return Unfunnel

    }

    override fun isEnd(): Boolean {
        return peekLong() == endMark
    }

    override fun end(type: Type) {
        prim.readLong()
    }

    override fun beginNested(label: String, type: Type): Nested {
        if (type.isTerminal())
            return Nest
        else
            return Substitute(Type.parse(prim.readUTF()))
    }

    override fun endNested(label: String, type: Type) {
    }

    override fun getBoolean(label: String): Boolean {
        return prim.readBoolean()
    }

    override fun getByte(label: String): Byte {
        return prim.readByte()
    }

    override fun getShort(label: String): Short {
        return prim.readShort()
    }

    override fun getInt(label: String): Int {
        return prim.readInt()
    }

    override fun getLong(label: String): Long {
        return prim.readLong()
    }

    override fun getFloat(label: String): Float {
        return prim.readFloat()
    }

    override fun getDouble(label: String): Double {
        return prim.readDouble()
    }

    override fun getChar(label: String): Char {
        return prim.readChar()
    }

    override fun isNull(label: String): Boolean {
        return prim.readBoolean()
    }

    override fun getUnit(label: String) {
    }

    override fun getString(label: String): String {
        if (utf8)
            return prim.readUTF()
        else {
            val length = prim.readShort()
            val builder = StringBuilder(length.toInt())
            for (i in 1..length)
                builder.append(prim.readChar())
            return builder.toString()
        }
    }

}

/**
 * A raw data sink with index maintenance.
 */
class IndexedRawSink(val target: OutputStream,
                     val indexLabel: String,
                     val utf8: Boolean = true) : Sink {
    private val prim = DataOutputStream(target)

    private val index = hashMapOf<Any?, Pair<Int, Int>>()

    private var depth = 0

    private var encounter: Option<Int> = None()

    private var start = Int.MIN_VALUE

    fun index() = index.toMap()


    override fun begin(type: Type, value: Any?): Boolean {
        depth++
        return true
    }

    override fun end(type: Type, value: Any?) {
        prim.writeLong(endMark)
        depth--
    }

    private fun beginMarkIndex(label: String) {
        if (label != indexLabel)
            return

        when (encounter) {
            is None<Int> -> encounter = Some(depth)
            is Some<Int> -> if (depth != encounter.value) return
        }

        if (start == Int.MIN_VALUE)
            start = prim.size()
    }


    private fun endMarkIndex(label: String, value: Any?) {
        if (label != indexLabel)
            return

        when (encounter) {
            is None<Int> -> encounter = Some(depth)
            is Some<Int> -> if (depth != encounter.value) return
        }

        check(start != Int.MIN_VALUE)

        val prev = index.get(value)
        if (prev == null) {
            index.put(value, start to prim.size())
            start = Int.MIN_VALUE
        } else
            error("Index not unique, previously encountered at $prev.")
    }

    private inline fun markIndex(label: String, value: Any?, block: () -> Unit) {
        beginMarkIndex(label)
        block()
        endMarkIndex(label, value)
    }

    override fun beginNested(label: String, type: Type, value: Any?): Boolean {
        beginMarkIndex(label)
        if (!type.isTerminal())
            prim.writeUTF(type.forInstance(value).toString())

        return true
    }

    override fun endNested(label: String, type: Type, value: Any?) {
        endMarkIndex(label, value)
    }

    override fun putBoolean(label: String, value: Boolean) = markIndex(label, value) {
        prim.writeBoolean(value)
    }

    override fun putByte(label: String, value: Byte) = markIndex(label, value) {
        prim.writeByte(value.toInt())
    }

    override fun putShort(label: String, value: Short) = markIndex(label, value) {
        prim.writeShort(value.toInt())
    }

    override fun putInt(label: String, value: Int) = markIndex(label, value) {
        prim.writeInt(value)
    }

    override fun putLong(label: String, value: Long) = markIndex(label, value) {
        prim.writeLong(value)
    }

    override fun putFloat(label: String, value: Float) = markIndex(label, value) {
        prim.writeFloat(value)
    }

    override fun putDouble(label: String, value: Double) = markIndex(label, value) {
        prim.writeDouble(value)
    }

    override fun putChar(label: String, value: Char) = markIndex(label, value) {
        prim.writeChar(value.toInt())
    }

    override fun putNull(label: String, isNull: Boolean) {
        beginMarkIndex(label)
        prim.writeBoolean(isNull)
        if (isNull)
            endMarkIndex(label, null)
    }

    override fun putUnit(label: String, value: Unit) = markIndex(label, value) {
    }

    override fun putString(label: String, value: String) = markIndex(label, value) {
        if (utf8)
            prim.writeUTF(value)
        else {
            prim.writeShort(value.length)
            prim.writeChars(value)
        }
    }
}