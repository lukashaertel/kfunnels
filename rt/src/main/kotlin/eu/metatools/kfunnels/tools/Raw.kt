package eu.metatools.kfunnels.tools

import eu.metatools.kfunnels.*
import eu.metatools.kfunnels.utils.None
import eu.metatools.kfunnels.utils.Option
import eu.metatools.kfunnels.utils.Some
import java.io.*
import java.util.*
import kotlin.reflect.KProperty1

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
                     val indexType: Type,
                     val indexLabel: String,
                     val utf8: Boolean = true) : Sink {
    private val prim = DataOutputStream(target)

    private val index = hashMapOf<Any?, Pair<Int, Int>>()

    private var starts = Stack<Int>()
    private var ids = Stack<Option<Any?>>()


    fun index() = index.toMap()


    override fun begin(type: Type, value: Any?): Boolean {
        if (type == indexType) {
            starts.push(prim.size())
            ids.push(None())
        }

        return true
    }

    override fun end(type: Type, value: Any?) {
        if (type == indexType) {
            val start = starts.pop()
            val id = ids.pop()
            if (id.isSome)
                index.put(id.value, start to prim.size())
        }

        prim.writeLong(endMark)
    }


    private fun handleId(label: String, value: Any?) {
        if (label == indexLabel) {
            ids.pop()
            ids.push(Some(value))
        }
    }

    override fun beginNested(label: String, type: Type, value: Any?): Boolean {
        handleId(label, value)

        if (!type.isTerminal())
            prim.writeUTF(type.forInstance(value).toString())

        return true
    }

    override fun endNested(label: String, type: Type, value: Any?) {
    }

    override fun putBoolean(label: String, value: Boolean) {
        handleId(label, value)
        prim.writeBoolean(value)
    }

    override fun putByte(label: String, value: Byte) {
        handleId(label, value)
        prim.writeByte(value.toInt())
    }

    override fun putShort(label: String, value: Short) {
        handleId(label, value)
        prim.writeShort(value.toInt())
    }

    override fun putInt(label: String, value: Int) {
        handleId(label, value)
        prim.writeInt(value)
    }

    override fun putLong(label: String, value: Long) {
        handleId(label, value)
        prim.writeLong(value)
    }

    override fun putFloat(label: String, value: Float) {
        handleId(label, value)
        prim.writeFloat(value)
    }

    override fun putDouble(label: String, value: Double) {
        handleId(label, value)
        prim.writeDouble(value)
    }

    override fun putChar(label: String, value: Char) {
        handleId(label, value)
        prim.writeChar(value.toInt())
    }

    override fun putNull(label: String, isNull: Boolean) {
        if (isNull)
            handleId(label, null)
        prim.writeBoolean(isNull)
    }

    override fun putUnit(label: String, value: Unit) {
        handleId(label, value)
    }

    override fun putString(label: String, value: String) {
        handleId(label, value)
        if (utf8)
            prim.writeUTF(value)
        else {
            prim.writeShort(value.length)
            prim.writeChars(value)
        }
    }
}

inline fun <reified R> indexedRawSink(target: OutputStream, property: KProperty1<R, *>, utf8: Boolean = true) =
        IndexedRawSink(target, Type.from<R>(), property.name, utf8)