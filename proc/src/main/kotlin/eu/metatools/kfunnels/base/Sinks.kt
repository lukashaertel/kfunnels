package eu.metatools.kfunnels.base

import com.google.common.hash.PrimitiveSink
import eu.metatools.kfunnels.SeqSink

/**
 * Wraps the [PrimitiveSink] as a [SeqSink].
 */
fun PrimitiveSink.asSeqSink() = object : SeqSink {
    override fun putBoolean(b: Boolean) {
        this@asSeqSink.putBoolean(b)
    }

    override fun putByte(b: Byte) {
        this@asSeqSink.putByte(b)
    }

    override fun putShort(s: Short) {
        this@asSeqSink.putShort(s)
    }

    override fun putInt(i: Int) {
        this@asSeqSink.putInt(i)
    }

    override fun putLong(l: Long) {
        this@asSeqSink.putLong(l)
    }

    override fun putFloat(f: Float) {
        this@asSeqSink.putFloat(f)
    }

    override fun putDouble(d: Double) {
        this@asSeqSink.putDouble(d)
    }

    override fun putChar(c: Char) {
        this@asSeqSink.putChar(c)
    }

    override fun putNull(isNull: Boolean) {
    }

    override fun putUnit(unit: Unit) {
    }

    override fun putString(string: String) {
        this@asSeqSink.putUnencodedChars(string)
        this@asSeqSink.putByte(0)
    }

    override fun beginNested() {
    }

    override fun endNested() {
    }
}