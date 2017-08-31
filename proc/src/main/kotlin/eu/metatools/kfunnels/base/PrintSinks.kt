package eu.metatools.kfunnels.base

import eu.metatools.kfunnels.Sink
import eu.metatools.kfunnels.Type
import java.io.PrintStream

/**
 * Pipes the calls into the standard output.
 */
class PrintSink(val printLables: Boolean, val printStream: PrintStream = System.out) : Sink {
    companion object {
        /**
         * A default instance where labels are used and the standard output is the target.
         */
        val labeled = PrintSink(true)

        /**
         * A default instance where labels are *not* used and the standard output is the target.
         */
        val sequence = PrintSink(true)
    }

    private inline fun withOut(block: PrintStream.() -> Unit) {
        printStream.block()
    }

    private inline fun withOut(label: String, block: PrintStream.() -> Unit) {
        if (printLables)
            printStream.print("$label: ")
        printStream.block()
    }

    override fun begin(type: Type) {
        withOut { println("Begin $type") }
    }

    override fun end(type: Type) {
        withOut { println("End $type") }
    }

    override fun beginNested(label: String, type: Type, value: Any?) {
        withOut(label) { println("Begin-nested $type") }
    }

    override fun endNested(label: String, type: Type, value: Any?) {
        withOut(label) { println("End-nested $type") }
    }

    override fun putBoolean(label: String, value: Boolean) {
        withOut(label) { println("Boolean $value") }
    }

    override fun putByte(label: String, value: Byte) {
        withOut(label) { println("Byte $value") }
    }

    override fun putShort(label: String, value: Short) {
        withOut(label) { println("Short $value") }
    }

    override fun putInt(label: String, value: Int) {
        withOut(label) { println("Int $value") }
    }

    override fun putLong(label: String, value: Long) {
        withOut(label) { println("Long $value") }
    }

    override fun putFloat(label: String, value: Float) {
        withOut(label) { println("Float $value") }
    }

    override fun putDouble(label: String, value: Double) {
        withOut(label) { println("Double $value") }
    }

    override fun putChar(label: String, value: Char) {
        withOut(label) { println("Char $value") }
    }

    override fun putNull(label: String, isNull: Boolean) {
        withOut(label) { println("Is-null $isNull") }
    }

    override fun putUnit(label: String, value: Unit) {
        withOut(label) { println("Unit $value") }
    }

    override fun putString(label: String, value: String) {
        withOut(label) { println("String $value") }
    }
}