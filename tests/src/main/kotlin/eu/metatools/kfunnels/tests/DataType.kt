package eu.metatools.kfunnels.tests

import eu.metatools.kfunnels.*
import java.io.PrintStream
import java.util.*
import kotlin.reflect.full.isSubclassOf

@Funnelable
data class Thing(val i: Int, val j: Float)

@Funnelable
data class Container<T, U>(val t: T, val u: U)

fun main(args: Array<String>) {

    // Make the container object.
    val c = Container(Thing(10, 2.3f), listOf(1, 2, 3))
    val s = JsonSink(System.out)

    // Write to sink
    TestsModule.std.write(s, c)

}

/**
 * I basically guessed that class, please don't hurt me.
 */
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