package eu.metatools.kfunnels.tools

import eu.metatools.kfunnels.*

/**
 * Stores the elements in a map using their labels.
 */
class MapSink : Sink {
    private val NULL = Any()

    private val mutableMap = hashMapOf<String, Any?>()

    fun reset(): Map<String, Any?> {
        val result = mutableMap.filterValues { it != NULL }
        mutableMap.clear()
        return result
    }

    override fun begin(type: Type, value: Any?): Boolean {
        return true
    }

    override fun end(type: Type, value: Any?) {
    }


    override fun beginNested(label: String, type: Type, value: Any?): Boolean {
        mutableMap.getOrPut(label, { value })
        return false
    }

    override fun endNested(label: String, type: Type, value: Any?) {
    }

    override fun putBoolean(label: String, value: Boolean) {
        mutableMap.getOrPut(label, { value })
    }

    override fun putByte(label: String, value: Byte) {
        mutableMap.getOrPut(label, { value })
    }

    override fun putShort(label: String, value: Short) {
        mutableMap.getOrPut(label, { value })
    }

    override fun putInt(label: String, value: Int) {
        mutableMap.getOrPut(label, { value })
    }

    override fun putLong(label: String, value: Long) {
        mutableMap.getOrPut(label, { value })
    }

    override fun putFloat(label: String, value: Float) {
        mutableMap.getOrPut(label, { value })
    }

    override fun putDouble(label: String, value: Double) {
        mutableMap.getOrPut(label, { value })
    }

    override fun putChar(label: String, value: Char) {
        mutableMap.getOrPut(label, { value })
    }

    override fun putNull(label: String, isNull: Boolean) {
        if (isNull)
            mutableMap[label] = NULL
    }

    override fun putUnit(label: String, value: Unit) {
        mutableMap.getOrPut(label, { })
    }

    override fun putString(label: String, value: String) {
        mutableMap.getOrPut(label, { value })
    }
}

class MapSource(val map: Map<String, Any?>) : Source {
    override fun begin(type: Type): Begin {
        return Unfunnel
    }

    override fun isEnd(): Boolean {
        throw UnsupportedOperationException()
    }

    override fun end(type: Type) {
    }

    override fun beginNested(label: String, type: Type): Nested {
        return Item(map[label])
    }

    override fun endNested(label: String, type: Type) {
    }

    override fun getBoolean(label: String): Boolean {
        return map[label] as Boolean
    }

    override fun getByte(label: String): Byte {
        return map[label] as Byte
    }

    override fun getShort(label: String): Short {
        return map[label] as Short
    }

    override fun getInt(label: String): Int {
        return map[label] as Int
    }

    override fun getLong(label: String): Long {
        return map[label] as Long
    }

    override fun getFloat(label: String): Float {
        return map[label] as Float
    }

    override fun getDouble(label: String): Double {
        return map[label] as Double
    }

    override fun getChar(label: String): Char {
        return map[label] as Char
    }

    override fun isNull(label: String): Boolean {
        return label !in map
    }

    override fun getUnit(label: String) {
        return map[label] as Unit
    }

    override fun getString(label: String): String {
        return map[label] as String
    }

}