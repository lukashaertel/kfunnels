package eu.metatools.kfunnels.base

import com.google.common.hash.PrimitiveSink
import eu.metatools.kfunnels.Boundaries
import eu.metatools.kfunnels.LabelSink
import eu.metatools.kfunnels.SeqSink
import eu.metatools.kfunnels.Type
import java.io.PrintStream
import java.util.*
import kotlin.reflect.full.isSubclassOf

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


@Deprecated("This is a rudimentary implementation and is not guaranteed to work for everything.")
class JsonSink(val printStream: PrintStream) : LabelSink, Boundaries {
    private val isSkipLabelStack = Stack<Boolean>()

    private val isSkipLabel get() = isSkipLabelStack.peek()

    private var skipInt = false

    override fun startEntire(type: Type) {
        if (type.kClass.isSubclassOf(List::class)) {
            printStream.append("[")
            isSkipLabelStack.push(true)
            skipInt = true
        } else {
            printStream.append("{")
            isSkipLabelStack.push(false)
        }
    }

    override fun endEntire(type: Type) {
        if (type.kClass.isSubclassOf(List::class)) {
            printStream.append("]")
            isSkipLabelStack.pop()
        } else {
            printStream.append("}")
            isSkipLabelStack.pop()
        }
    }

    private var sep = false

    private fun doSep() {
        if (sep)
            printStream.append(", ")
        sep = true
    }

    override fun putBoolean(label: String, b: Boolean) {
        doSep()
        if (!isSkipLabel)
            printStream.append("\"$label\":")
        printStream.append("$b")
    }

    override fun putByte(label: String, b: Byte) {
        doSep()
        if (!isSkipLabel)
            printStream.append("\"$label\":")
        printStream.append("$b")
    }

    override fun putShort(label: String, s: Short) {
        doSep()
        if (!isSkipLabel)
            printStream.append("\"$label\":")
        printStream.append("$s")
    }

    override fun putInt(label: String, i: Int) {
        if (skipInt) {
            // TODO: List funneler is using an extra field for the length, maybe this is not necessary
            skipInt = false
            return
        }

        doSep()
        if (!isSkipLabel)
            printStream.append("\"$label\":")
        printStream.append("$i")
    }

    override fun putLong(label: String, l: Long) {
        doSep()
        if (!isSkipLabel)
            printStream.append("\"$label\":")
        printStream.append("$l")
    }

    override fun putFloat(label: String, f: Float) {
        doSep()
        if (!isSkipLabel)
            printStream.append("\"$label\":")
        printStream.append("$f")
    }

    override fun putDouble(label: String, d: Double) {
        doSep()
        if (!isSkipLabel)
            printStream.append("\"$label\":")
        printStream.append("$d")
    }

    override fun putChar(label: String, c: Char) {
        doSep()
        if (!isSkipLabel)
            printStream.append("\"$label\":")
        printStream.append("\"$c\"")
    }

    override fun putNull(label: String, isNull: Boolean) {
        if (isNull) {
            doSep()
            if (!isSkipLabel)
                printStream.append("\"$label\":")
            printStream.append("null")
        }
    }

    override fun putUnit(label: String, unit: Unit) {
    }

    override fun putString(label: String, string: String) {
        doSep()
        if (!isSkipLabel)
            printStream.append("\"$label\":")
        printStream.append("\"$string\"")
    }

    override fun beginNested(label: String) {
        doSep()
        if (!isSkipLabel)
            printStream.append("\"$label\":")

        isSkipLabelStack.push(true)
        sep = false
    }

    override fun endNested(label: String) {
        isSkipLabelStack.pop()
        sep = true
    }
}