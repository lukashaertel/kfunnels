package eu.metatools.kfunnels.tests

import eu.metatools.kfunnels.*
import eu.metatools.kfunnels.base.ServiceModule
import javax.imageio.spi.ServiceRegistry

/**
 * An item that has a variable that is assigned later.
 */
@Funnelable
data class Item(val language: String) {
    var value: Double = Double.NaN

    override fun toString() = "Item(language=$language, value=$value)"
}

/**
 * Language source provides language and a language dependent value as a string.
 */
class LanguageSource(val language: String, val value: String) : Source {
    private var item: Item? = null
    override fun begin(type: Type) = Unfunnel

    override fun isEnd(): Boolean {
        error("Should not be used.")
    }

    override fun end(type: Type) {
    }

    override fun afterCreate(item: Any?) {
        this.item = item as? Item
    }

    override fun beginNested(label: String, type: Type) = Nest

    override fun endNested(label: String, type: Type) {
    }

    override fun getBoolean(label: String): Boolean {
        error("Should not be used.")
    }

    override fun getByte(label: String): Byte {
        error("Should not be used.")
    }

    override fun getShort(label: String): Short {
        error("Should not be used.")
    }

    override fun getInt(label: String): Int {
        error("Should not be used.")
    }

    override fun getLong(label: String): Long {
        error("Should not be used.")
    }

    override fun getFloat(label: String): Float {
        error("Should not be used.")
    }

    /**
     * Get double, for label="value", uses the already unfunneled language to properly deserialize tha language.
     */
    override fun getDouble(label: String): Double {
        check(label == "value")
        check(item != null)

        return when (item!!.language) {
            "de" -> value.replace(',', '.').toDouble()
            "en" -> value.toDouble()
            else -> error("Unknown language ${item!!.language}")
        }
    }

    override fun getChar(label: String): Char {
        error("Should not be used.")
    }

    override fun isNull(label: String): Boolean {
        error("Should not be used.")
    }

    override fun getUnit(label: String) {
        error("Should not be used.")
    }

    override fun getString(label: String): String {
        check(label == "language")
        return language
    }

}

fun main(args: Array<String>) {
    // Make language sources of different languages
    val sourceDe = LanguageSource("de", "1234,5")
    val sourceEn = LanguageSource("en", "1234.5")

    // Deserialize the items
    val de = ServiceModule.read<Item>(sourceDe)
    val en = ServiceModule.read<Item>(sourceEn)

    // Print them
    println(de)
    println(en)

}