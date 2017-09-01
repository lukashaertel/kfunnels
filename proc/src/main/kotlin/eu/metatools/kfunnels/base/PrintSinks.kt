package eu.metatools.kfunnels.base

import eu.metatools.kfunnels.Sink
import eu.metatools.kfunnels.Type
import java.io.PrintStream

/**
 * Pipes the calls into the standard output.
 */
class PrintSink(val printLables: Boolean, val printStream: PrintStream = System.out) : Sink {
    private var indent = 0

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

    private inline fun withOut(block: () -> Any?) {
        val prefix = "\t".repeat(indent)
        val text = block().toString()
        printStream.println(prefix + text.replace(Regex("\r?\n")) { it.value + prefix })
    }

    private inline fun withOut(label: String, block: () -> Any?) {
        val prefix = "\t".repeat(indent)
        val text = block().toString()
        printStream.println(prefix + label + ": " + text.replace(Regex("\r?\n")) { it.value + prefix })
    }

    override fun begin(type: Type) {
        withOut { "Begin $type" }
    }

    override fun end(type: Type) {
        withOut { "End" }
    }

    override fun beginNested(label: String, type: Type, value: Any?): Boolean {
        indent++

        val actual = type.forInstance(value)
        if (actual != type)
            withOut(label) { "Begin-nested $type ($actual)" }
        else
            withOut(label) { "Begin-nested $type" }

        return true
    }

    override fun endNested(label: String, type: Type, value: Any?) {
        withOut(label) { "End-nested" }
        indent--
    }

    override fun putBoolean(label: String, value: Boolean) {
        withOut(label) { "Boolean $value" }
    }

    override fun putByte(label: String, value: Byte) {
        withOut(label) { "Byte $value" }
    }

    override fun putShort(label: String, value: Short) {
        withOut(label) { "Short $value" }
    }

    override fun putInt(label: String, value: Int) {
        withOut(label) { "Int $value" }
    }

    override fun putLong(label: String, value: Long) {
        withOut(label) { "Long $value" }
    }

    override fun putFloat(label: String, value: Float) {
        withOut(label) { "Float $value" }
    }

    override fun putDouble(label: String, value: Double) {
        withOut(label) { "Double $value" }
    }

    override fun putChar(label: String, value: Char) {
        withOut(label) { "Char $value" }
    }

    override fun putNull(label: String, isNull: Boolean) {
        withOut(label) { "Is-null $isNull" }
    }

    override fun putUnit(label: String, value: Unit) {
        withOut(label) { "Unit $value" }
    }

    override fun putString(label: String, value: String) {
        withOut(label) { "String $value" }
    }
}