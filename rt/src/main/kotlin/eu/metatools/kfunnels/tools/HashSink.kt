package eu.metatools.kfunnels.tools

import eu.metatools.kfunnels.Sink
import eu.metatools.kfunnels.Type
import eu.metatools.kfunnels.base.ServiceModule
import eu.metatools.kfunnels.base.std
import eu.metatools.kfunnels.write

/**
 * A sink that computes a hash value.
 */
class HashSink(val prime: Int = 37) : Sink {
    private var carry = 1;

    private var nextNull = false

    /**
     * Resets the hash sink and returns the current value.
     */
    fun reset(): Int {
        val result = carry
        carry = 1
        nextNull = false

        return result
    }

    override fun begin(type: Type, value: Any?): Boolean {
        return true
    }

    override fun end(type: Type, value: Any?) {
    }

    override fun beginNested(label: String, type: Type, value: Any?): Boolean {
        carry = prime * carry + if (value == null) 0 else value.hashCode()
        nextNull = false
        return false
    }

    override fun endNested(label: String, type: Type, value: Any?) {
    }

    private fun put(value: Any) {
        carry = prime * carry + if (nextNull) 0 else value.hashCode()
        nextNull = false
    }

    override fun putBoolean(label: String, value: Boolean) {
        put(value)
    }

    override fun putByte(label: String, value: Byte) {
        put(value)
    }

    override fun putShort(label: String, value: Short) {
        put(value)
    }

    override fun putInt(label: String, value: Int) {
        put(value)
    }

    override fun putLong(label: String, value: Long) {
        put(value)
    }

    override fun putFloat(label: String, value: Float) {
        put(value)
    }

    override fun putDouble(label: String, value: Double) {
        put(value)
    }

    override fun putChar(label: String, value: Char) {
        put(value)
    }

    override fun putNull(label: String, isNull: Boolean) {
        nextNull = isNull
    }

    override fun putUnit(label: String, value: Unit) {
        put(value)
    }

    override fun putString(label: String, value: String) {
        put(value)
    }
}

/**
 * Computes the hash  of the receiver by using [ServiceModule] with [Module.std], writing to a [HashSink].
 */
inline fun <reified T> T.hash() = HashSink().let {
    ServiceModule.std.write(it, this)
    it.reset()
}

/**
 * Computes the hash  of the receiver by using [ServiceModule] with [Module.std], writing to a [HashSink]. Specifies
 * a prime to use for combination.
 */
inline fun <reified T> T.hash(prime: Int) = HashSink(prime).let {
    ServiceModule.std.write(it, this)
    it.reset()
}