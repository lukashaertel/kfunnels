package eu.metatools.kfunnels.base

import com.google.common.hash.PrimitiveSink
import eu.metatools.kfunnels.*
import java.io.PrintStream
import java.util.*
import kotlin.reflect.full.isSubclassOf

/**
 * Wraps the [PrimitiveSink] as a [Sink].
 */
fun PrimitiveSink.asSeqSink() = object : Sink {
    override fun begin(type: Type) {
    }

    override fun end(type: Type) {
    }

    override fun beginNested(label: String, type: Type, value: Any?) {
    }

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

//
//@Deprecated("This is a rudimentary implementation and is not guaranteed to work for everything.")
//class JsonSink(val printStream: PrintStream) : LabelSink, Boundaries {
//    private val isSkipLabelStack = Stack<Boolean>()
//
//    private val isSkipLabel get() = isSkipLabelStack.peek()
//
//    private var skipInt = false
//
//    override fun startEntire(type: Type) {
//        if (type.kClass.isSubclassOf(List::class)) {
//            printStream.append("[")
//            isSkipLabelStack.push(true)
//            skipInt = true
//        } else {
//            printStream.append("{")
//            isSkipLabelStack.push(false)
//        }
//    }
//
//    override fun endEntire(type: Type) {
//        if (type.kClass.isSubclassOf(List::class)) {
//            printStream.append("]")
//            isSkipLabelStack.pop()
//        } else {
//            printStream.append("}")
//            isSkipLabelStack.pop()
//        }
//    }
//
//    private var sep = false
//
//    private fun doSep() {
//        if (sep)
//            printStream.append(", ")
//        sep = true
//    }
//
//    override fun putBoolean(label: String, b: Boolean) {
//        doSep()
//        if (!isSkipLabel)
//            printStream.append("\"$label\":")
//        printStream.append("$b")
//    }
//
//    override fun putByte(label: String, b: Byte) {
//        doSep()
//        if (!isSkipLabel)
//            printStream.append("\"$label\":")
//        printStream.append("$b")
//    }
//
//    override fun putShort(label: String, s: Short) {
//        doSep()
//        if (!isSkipLabel)
//            printStream.append("\"$label\":")
//        printStream.append("$s")
//    }
//
//    override fun putInt(label: String, i: Int) {
//        if (skipInt) {
//            // TODO: List funneler is using an extra field for the length, maybe this is not necessary
//            skipInt = false
//            return
//        }
//
//        doSep()
//        if (!isSkipLabel)
//            printStream.append("\"$label\":")
//        printStream.append("$i")
//    }
//
//    override fun putLong(label: String, l: Long) {
//        doSep()
//        if (!isSkipLabel)
//            printStream.append("\"$label\":")
//        printStream.append("$l")
//    }
//
//    override fun putFloat(label: String, f: Float) {
//        doSep()
//        if (!isSkipLabel)
//            printStream.append("\"$label\":")
//        printStream.append("$f")
//    }
//
//    override fun putDouble(label: String, d: Double) {
//        doSep()
//        if (!isSkipLabel)
//            printStream.append("\"$label\":")
//        printStream.append("$d")
//    }
//
//    override fun putChar(label: String, c: Char) {
//        doSep()
//        if (!isSkipLabel)
//            printStream.append("\"$label\":")
//        printStream.append("\"$c\"")
//    }
//
//    override fun putNull(label: String, isNull: Boolean) {
//        if (isNull) {
//            doSep()
//            if (!isSkipLabel)
//                printStream.append("\"$label\":")
//            printStream.append("null")
//        }
//    }
//
//    override fun putUnit(label: String, unit: Unit) {
//    }
//
//    override fun putString(label: String, string: String) {
//        doSep()
//        if (!isSkipLabel)
//            printStream.append("\"$label\":")
//        printStream.append("\"$string\"")
//    }
//
//    override fun beginNested(label: String) {
//        doSep()
//        if (!isSkipLabel)
//            printStream.append("\"$label\":")
//
//        isSkipLabelStack.push(true)
//        sep = false
//    }
//
//    override fun endNested(label: String) {
//        isSkipLabelStack.pop()
//        sep = true
//    }
//}