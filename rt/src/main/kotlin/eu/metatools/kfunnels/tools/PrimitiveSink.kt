package eu.metatools.kfunnels.tools

import com.google.common.hash.PrimitiveSink
import eu.metatools.kfunnels.*
import java.io.PrintStream
import java.util.*
import kotlin.reflect.full.isSubclassOf

/**
 * Wraps the [PrimitiveSink] as a [Sink]. This class requires compiling with Guava.
 */
fun PrimitiveSink.asSeqSink() = object : Sink {
    override fun begin(type: Type, value: Any?): Boolean {
        return true
    }

    override fun end(type: Type, value: Any?) {
    }

    override fun beginNested(label: String, type: Type, value: Any?) = true

    override fun endNested(label: String, type: Type, value: Any?) {
    }

    override fun putBoolean(label: String, value: Boolean) {
        putBoolean(value)
    }

    override fun putByte(label: String, value: Byte) {
        putByte(value)
    }

    override fun putShort(label: String, value: Short) {
        putShort(value)
    }

    override fun putInt(label: String, value: Int) {
        putInt(value)
    }

    override fun putLong(label: String, value: Long) {
        putLong(value)
    }

    override fun putFloat(label: String, value: Float) {
        putFloat(value)
    }

    override fun putDouble(label: String, value: Double) {
        putDouble(value)
    }

    override fun putChar(label: String, value: Char) {
        putChar(value)
    }

    override fun putNull(label: String, isNull: Boolean) {
    }

    override fun putUnit(label: String, value: Unit) {
    }

    override fun putString(label: String, value: String) {
        putUnencodedChars(value)
        putByte(0)
    }
}