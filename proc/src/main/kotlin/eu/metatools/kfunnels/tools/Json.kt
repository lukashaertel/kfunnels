package eu.metatools.kfunnels.tools

import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.core.base.ParserMinimalBase
import com.google.common.base.Optional
import eu.metatools.kfunnels.*
import eu.metatools.kfunnels.utils.peekUnder
import eu.metatools.kfunnels.utils.peekable
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import kotlin.NoSuchElementException
import kotlin.reflect.full.isSubclassOf

private val disambiguateType = "_type"

private val disambiguateIt = "_it"


/**
 * Writes to a [JsonGenerator].
 */
class JsonSink(val generator: JsonGenerator) : Sink {
    private val isListStack = Stack<Boolean>()

    private fun pushList() {
        isListStack.push(true)
    }

    private fun pushObject() {
        isListStack.push(false)
    }

    private fun pop() {
        isListStack.pop()
    }

    private val isList get() = isListStack.peek()

    override fun begin(type: Type, value: Any?): Boolean {
        if (type.kClass.isSubclassOf(Collection::class)) {
            // If collection, start an array
            generator.writeStartArray((value as Collection<*>?)?.size ?: 0)
            pushList()
            return true
        } else {
            // Start object
            generator.writeStartObject(value)
            pushObject()
            return true
        }
    }

    override fun end(type: Type, value: Any?) {
        if (type.kClass.isSubclassOf(Collection::class)) {
            pop()
            generator.writeEndArray()
        } else {
            pop()
            generator.writeEndObject()
        }
    }


    override fun beginNested(label: String, type: Type, value: Any?): Boolean {
        if (!isList)
            generator.writeFieldName(label)

        if (!type.isTerminal()) {
            generator.writeStartObject(value)
            generator.writeStringField(disambiguateType, type.forInstance(value).toString())
            generator.writeFieldName(disambiguateIt)
            pushObject()
        }

        return true
    }

    override fun endNested(label: String, type: Type, value: Any?) {
        if (!type.isTerminal()) {
            pop()
            generator.writeEndObject()
        }
    }

    override fun putBoolean(label: String, value: Boolean) {
        if (isList)
            generator.writeBoolean(value)
        else
            generator.writeBooleanField(label, value)
    }

    override fun putByte(label: String, value: Byte) {
        if (isList)
            generator.writeNumber(value.toInt())
        else
            generator.writeNumberField(label, value.toInt())
    }

    override fun putShort(label: String, value: Short) {
        if (isList)
            generator.writeNumber(value.toInt())
        else
            generator.writeNumberField(label, value.toInt())
    }

    override fun putInt(label: String, value: Int) {
        if (isList)
            generator.writeNumber(value)
        else
            generator.writeNumberField(label, value)
    }

    override fun putLong(label: String, value: Long) {
        if (isList)
            generator.writeNumber(value)
        else
            generator.writeNumberField(label, value)
    }

    override fun putFloat(label: String, value: Float) {
        if (isList)
            generator.writeNumber(value)
        else
            generator.writeNumberField(label, value)
    }

    override fun putDouble(label: String, value: Double) {
        if (isList)
            generator.writeNumber(value)
        else
            generator.writeNumberField(label, value)
    }

    override fun putChar(label: String, value: Char) {
        if (isList)
            generator.writeString(value.toString())
        else
            generator.writeStringField(label, value.toString())
    }

    override fun putNull(label: String, isNull: Boolean) {
        if (!isNull)
            return

        if (isList)
            generator.writeNull()
        else
            generator.writeNullField(label)
    }

    override fun putUnit(label: String, value: Unit) {
    }

    override fun putString(label: String, value: String) {
        if (isList)
            generator.writeString(value)
        else
            generator.writeStringField(label, value)
    }
}

/**
 * Read/Write-stack item. Used to store unprocessed data in the stream, until it is consumed by the funneler reading
 * from [JsonSource].
 */
private sealed class JItem

/**
 * A JSON terminal value.
 */
private data class JTerminal(val value: Any) : JItem()

/**
 * A JSON array.
 */
private data class JArray(val deque: Deque<JItem>, var closed: Boolean = false) : JItem()

/**
 * A JSON object.
 */
private data class JObject(val map: MutableMap<String, JItem>, var closed: Boolean = false) : JItem()

/**
 * Reads from a [JsonParser]. To align labels that are present in the stream with labels that are requested by a
 * [Funneler], date might need to be preemptively pulled from the stream. The data will however be removed once
 * consumed.
 */
class JsonSource(val parser: JsonParser) : Source, AutoCloseable {
    override fun close() {
        parser.close()
    }

    /**
     * True if the read and write stacks have been prepared for the first read.
     */
    private var initialized = false

    /**
     * Special object marking a null value.
     */
    private val NULL = Any()

    /**
     * Peekable stream of tokens.
     */
    private val tokens = peekable { parser.nextToken() }

    /**
     * The next name to use in the write stack.
     */
    private var writeStackName: String? = null

    /**
     * The current write stack.
     */
    private val writeStack = Stack<JItem>()

    /**
     * The current read stack.
     */
    private val readStack = Stack<JItem>()

    /**
     * Kind of advancement performed by [advanceOne].
     */
    private enum class Advancement {
        START, NAME, VALUE, END
    }

    /**
     * Advances reading from the parser by one.
     */
    private fun advanceOne(): Advancement {
        // Get the next token in the stream
        val token = tokens.takeNext()

        /**
         * Pushes [item] into the write stack.
         */
        fun push(item: JItem) {
            val top = writeStack.peek()

            // Add item to top if possible
            when (top) {
                is JTerminal ->
                    error("Trying to push to a terminal")

                is JArray ->
                    top.deque.offerLast(item)

                is JObject -> {
                    top.map[writeStackName ?: error("No name read")] = item
                    writeStackName = null
                }
            }

            // If item is a container, push it as the new write stack
            when (item) {
                is JArray,
                is JObject ->
                    writeStack.push(item)
            }
        }

        // Decide on token type.
        when (token) {
            JsonToken.FIELD_NAME -> {
                check(writeStackName == null)
                writeStackName = parser.currentName
                return Advancement.NAME
            }

            JsonToken.START_OBJECT -> {
                push(JObject(hashMapOf()))
                return Advancement.START
            }

            JsonToken.END_OBJECT -> {
                val top = writeStack.pop()
                if (top !is JObject)
                    error("Write stack mismatching, should be an object.")
                top.closed = true
                return Advancement.END
            }

            JsonToken.VALUE_NUMBER_INT -> {
                push(JTerminal(parser.longValue))
                return Advancement.VALUE
            }

            JsonToken.VALUE_NUMBER_FLOAT -> {
                push(JTerminal(parser.doubleValue))
                return Advancement.VALUE
            }

            JsonToken.VALUE_FALSE -> {
                push(JTerminal(false))
                return Advancement.VALUE
            }

            JsonToken.VALUE_TRUE -> {
                push(JTerminal(true))
                return Advancement.VALUE
            }

            JsonToken.VALUE_STRING -> {
                push(JTerminal(parser.text))
                return Advancement.VALUE
            }

            JsonToken.VALUE_NULL -> {
                push(JTerminal(NULL))
                return Advancement.VALUE
            }

            JsonToken.START_ARRAY -> {
                push(JArray(LinkedList()))
                return Advancement.START
            }

            JsonToken.END_ARRAY -> {
                val top = writeStack.pop()
                if (top !is JArray)
                    error("Write stack mismatching, should be an array.")
                top.closed = true
                return Advancement.END
            }

            else -> error("Unknown token: $token")
        }
    }

    /**
     * Advances out of the current container.
     */
    private fun advanceOut() {
        var entered = 0
        while (tokens.peekNext() != null && entered >= 0)
            when (advanceOne()) {
                JsonSource.Advancement.START -> entered++
                JsonSource.Advancement.END -> entered--
            }
    }

    /**
     * Advances the parser until the object in the read stack contains the [label].
     */
    private fun advanceObject(label: String) {
        val current = readStack.peek()
        when (current) {
            is JTerminal,
            is JArray ->
                error("Trying to advance for non-object.")
            is JObject -> {
                // Advance if the label is not yet present.
                while (label !in current.map && writeStack.size >= readStack.size && tokens.peekNext() != null)
                    advanceOne()

                if (label !in current.map)
                    error("Object was closed without initializing $label: ${current.map}")
            }
        }
    }

    /**
     * Advances the parser until the array in the read stack contains another element.
     */
    private fun advanceArray() {
        val current = readStack.peek()
        when (current) {
            is JTerminal,
            is JObject ->
                error("Trying to advance for non-array.")
            is JArray -> {
                // If there are elements left to read, do not advance.
                if (current.deque.isNotEmpty())
                    return

                // Advance if the list has no element yet
                while (current.deque.isEmpty() && writeStack.size >= readStack.size && tokens.peekNext() != null)
                    advanceOne()

                if (current.deque.isEmpty())
                    error("Array was closed without providing another element")
            }
        }
    }


    /**
     * Checks if the read stack currently has an item for [label].
     */
    private fun hasLabel(label: String): Boolean {
        val current = readStack.peek()
        when (current) {
            is JTerminal,
            is JArray ->
                error("Trying to check if $label is present for non-object.")
            is JObject -> {
                return label in current.map
            }
        }
    }

    /**
     * Checks if the read stack currently has a next item.
     */
    private fun hasNext(): Boolean {
        val current = readStack.peek()
        when (current) {
            is JTerminal,
            is JObject ->
                error("Trying to check if next is present for non-array.")
            is JArray -> {
                return current.deque.isNotEmpty()
            }
        }
    }

    /**
     * Enters the label in the read stack, given that the current position is a [JObject].
     */
    private fun enter(label: String) {
        if (!hasLabel(label))
            advanceObject(label)

        val current = readStack.peek()
        when (current) {
            is JTerminal,
            is JArray ->
                error("Trying to enter for non-object.")
            is JObject -> {
                readStack.push(current.map[label])
            }
        }
    }

    /**
     * Enters the next item in the read stack, given that the current position is a [JArray].
     */
    private fun enter() {
        if (!hasNext())
            advanceArray()

        val current = readStack.peek()
        when (current) {
            is JTerminal,
            is JObject ->
                error("Trying to enter for non-array.")
            is JArray -> {
                readStack.push(current.deque.peekFirst())
            }
        }
    }

    /**
     * Leaves the label in the read stack, given that the current position is a [JObject].
     */
    private fun leave(label: String) {
        val current = readStack.peekUnder()
        when (current) {
            is JTerminal,
            is JArray ->
                error("Trying to leave for non-object.")
            is JObject -> {
                val target = current.map.remove(label)
                when (target) {
                    is JObject -> check(target.map.isEmpty()) { "Unconsumed values in the source: ${target.map}" }
                    is JArray -> check(target.deque.isEmpty()) { "Unconsumed values in the source: ${target.deque}" }
                }
                readStack.pop()
            }
        }
    }

    /**
     * Leaves the item in the read stack, given that the current position is a [JArray].
     */
    private fun leave() {
        val current = readStack.peekUnder()
        when (current) {
            is JTerminal,
            is JObject ->
                error("Trying to leave for non-array.")
            is JArray -> {
                val target = current.deque.pollFirst()
                when (target) {
                    is JObject -> check(target.map.isEmpty()) { "Unconsumed values in the source: ${target.map}" }
                    is JArray -> check(target.deque.isEmpty()) { "Unconsumed values in the source: ${target.deque}" }
                }
                readStack.pop()
            }
        }
    }

    /**
     * Initializes the read and the write stack.
     */
    private fun initialize() {
        // Take the first token
        val token = tokens.takeNext()

        // Compute first item
        val item = when (token) {
        // Container items
            JsonToken.START_OBJECT -> JObject(hashMapOf());
            JsonToken.START_ARRAY -> JArray(LinkedList())

        // Value items
            JsonToken.VALUE_STRING -> JTerminal(parser.text)
            JsonToken.VALUE_NUMBER_INT -> JTerminal(parser.longValue)
            JsonToken.VALUE_NUMBER_FLOAT -> JTerminal(parser.doubleValue)
            JsonToken.VALUE_TRUE -> JTerminal(true)
            JsonToken.VALUE_FALSE -> JTerminal(false)
            JsonToken.VALUE_NULL -> JTerminal(NULL)

        // Erroneous tokens at initial position
            JsonToken.FIELD_NAME -> error("Field name without containing object.")

            JsonToken.END_OBJECT -> error("Ending object before it started.")
            JsonToken.END_ARRAY -> error("Ending array before it started.")

        // Unknown types
            else -> error("Unknown token: $token")
        }

        // Push to both read and write stack
        readStack.push(item)
        writeStack.push(item)
    }

    /**
     * Reads a terminal in the proper manner.
     */
    private inline fun <reified T> readTerminal(label: String, block: (Any) -> T): T {
        val current = readStack.peek()
        when (current) {
            is JTerminal -> return block(current.value)
            is JArray -> {
                enter()
                val actual = readStack.peek()
                val result = when (actual) {
                    is JTerminal -> block(actual.value)
                    is JArray,
                    is JObject ->
                        error("Trying to get terminal for object or array.")
                }
                leave()
                return result
            }
            is JObject -> {
                enter(label)
                val actual = readStack.peek()
                val result = when (actual) {
                    is JTerminal -> block(actual.value)
                    is JArray,
                    is JObject ->
                        error("Trying to get terminal for object or array.")
                }
                leave(label)
                return result
            }
        }
    }

    override fun begin(type: Type): Begin {
        if (!initialized) {
            initialize()
            initialized = true
        }

        return Unfunnel

    }

    override fun isEnd(): Boolean {
        val current = readStack.peek()
        when (current) {
            is JTerminal -> return true
            is JObject -> {
                // If there are labels that are not consumed, not at an end
                if (current.map.isNotEmpty())
                    return false

                // If the container is not closed, advance until out
                if (!current.closed)
                    advanceOut()

                return current.map.isEmpty()
            }
            is JArray -> {
                // If there are items in the array that are not consumed, not at end
                if (current.deque.isNotEmpty())
                    return false

                // If the container is not closed, advance until out
                if (!current.closed)
                    advanceOut()

                return current.deque.isEmpty()
            }
        }
    }

    override fun end(type: Type) {
    }

    override fun beginNested(label: String, type: Type): Nested {
        val current = readStack.peek()
        when (current) {
            is JTerminal -> error("Trying nest from terminal.")
            is JArray ->
                enter()
            is JObject ->
                enter(label)
        }

        if (type.isTerminal())
            return Nest
        else {
            val sub = readTerminal(disambiguateType) { Type.parse(it as String) }
            enter(disambiguateIt)
            return Substitute(sub)
        }
    }

    override fun endNested(label: String, type: Type) {
        if (!type.isTerminal())
            leave(disambiguateIt)

        val current = readStack.peekUnder()
        when (current) {
            is JTerminal -> error("Trying un-nest from terminal.")
            is JArray ->
                leave()
            is JObject ->
                leave(label)
        }
    }

    override fun getBoolean(label: String): Boolean
            = readTerminal(label) { it as Boolean }

    override fun getByte(label: String): Byte
            = readTerminal(label) { (it as Long).toByte() }

    override fun getShort(label: String): Short
            = readTerminal(label) { (it as Long).toShort() }

    override fun getInt(label: String): Int
            = readTerminal(label) { (it as Long).toInt() }

    override fun getLong(label: String): Long
            = readTerminal(label) { it as Long }

    override fun getFloat(label: String): Float
            = readTerminal(label) { (it as Double).toFloat() }

    override fun getDouble(label: String): Double
            = readTerminal(label) { it as Double }

    override fun getChar(label: String): Char
            = readTerminal(label) { (it as String).single() }

    override fun isNull(label: String): Boolean {
        val current = readStack.peek()
        when (current) {
            is JTerminal -> return current.value == NULL
            is JArray -> {
                val first = current.deque.peekFirst()
                if (first is JTerminal && first.value == NULL) {
                    current.deque.pollFirst()
                    return true
                }

                return false
            }
            is JObject -> {
                val value = current.map[label]
                if (value is JTerminal && value.value == NULL) {
                    current.map.remove(label)
                    return true
                }

                return false
            }
        }
    }

    override fun getUnit(label: String) {
    }

    override fun getString(label: String): String
            = readTerminal(label) { it as String }
}