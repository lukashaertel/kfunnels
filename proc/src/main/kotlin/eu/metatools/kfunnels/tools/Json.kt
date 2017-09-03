package eu.metatools.kfunnels.tools

import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.core.base.ParserMinimalBase
import com.google.common.base.Optional
import eu.metatools.kfunnels.*
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import kotlin.NoSuchElementException
import kotlin.reflect.full.isSubclassOf

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
            generator.writeFieldName(type.forInstance(value).toString())
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

class JsonSource(val parser: JsonParser) : Source {
    private val actualParser = ResettableParser(parser)

    private val isListStack = Stack<Boolean>()

    private fun pushList() {
        isListStack.push(true)
    }

    private fun pushObject() {
        isListStack.push(false)
    }

    private fun popIsList() {
        isListStack.pop()
    }

    private val isList get() = isListStack.peek()


    /**
     * The element that was peeked last or absent.
     */
    private var state = Optional.absent<JsonToken>()

    private val encouteredStack = Stack<MutableMap<String, Int>>()

    private fun pushMap() {
        encouteredStack.push(hashMapOf())
    }

    private val encountered get() = encouteredStack.peek()

    private fun popMap() {
        encouteredStack.pop()
    }


    /**
     * Prepare the state, i.e., read an item into [state] if none present.
     */
    private fun prepareNext() {
        if (!state.isPresent)
            state = Optional.of(actualParser.nextToken())
    }

    /**
     * Takes the next token, leaves the state empty.
     */
    private fun takeNext(): JsonToken {
        prepareNext()
        val result = state.get()
        state = Optional.absent()
        return result
    }

    /**
     * Peeks the next token, leaves the state with the item.
     */
    private fun peekNext(): JsonToken {
        prepareNext()
        return state.get()
    }


    override fun begin(type: Type): Begin {
        if (type.kClass.isSubclassOf(Collection::class)) {
            // If collection, use JSON collection
            check(takeNext() == JsonToken.START_ARRAY)
            pushList()
            pushMap()
            return Unfunnel
        } else {
            // Terminal type, just start object
            check(takeNext() == JsonToken.START_OBJECT)
            pushObject()
            pushMap()
            return Unfunnel
        }
    }

    override fun isEnd() =
            when (peekNext()) {
                JsonToken.END_ARRAY,
                JsonToken.END_OBJECT -> true
                else -> false
            }

    override fun end(type: Type) {
        if (type.kClass.isSubclassOf(Collection::class)) {
            popMap()
            popIsList()
            check(takeNext() == JsonToken.END_ARRAY)
        } else {
            popMap()
            popIsList()
            spoolEnd()
        }
    }

    private fun spoolEnd() {
        while (true) {
            val token = takeNext()
            if (token.isStructStart)
                actualParser.skipChildren()
            if (token.isStructEnd) {
                break
            }
        }
    }

    private fun spool(label: String) {
        val r = encountered[label]
        if (r != null) {
            actualParser.reset(r)
            return
        }

        while (true) {
            val token = takeNext()

            // If trying to nest into a structure, skip
            if (token.isStructStart)
                actualParser.skipChildren()

            // If field name, mark location and check if this is the one we are looking for
            if (token == JsonToken.FIELD_NAME) {
                encountered[actualParser.text] = actualParser.current()
                if (actualParser.text == label)
                    break
            }

            // If at end, no label has been found
            if (token.isStructEnd)
                throw NoSuchElementException("No element $label.")
        }
    }

    private fun use(label: String) {
        val r = encountered.remove(label)
        if (r != null)
            actualParser.discard(encouteredStack.flatMap { it.values }.min() ?: 0)
    }

    override fun beginNested(label: String, type: Type): Nested {
        if (!isList) {
            spool(label)
            use(label)
        }

        if (!type.isTerminal()) {
            check(takeNext() == JsonToken.START_OBJECT)
            check(takeNext() == JsonToken.FIELD_NAME)
            pushObject()
            pushMap()
            return Substitute(Type.parse(actualParser.currentName!!))

        }

        return Nest
    }

    override fun endNested(label: String, type: Type) {

        if (!type.isTerminal()) {
            popMap()
            popIsList()
            spoolEnd()
        }
    }

    override fun getBoolean(label: String): Boolean {
        if (!isList) {
            spool(label)
            use(label)
        }
        check(takeNext().isBoolean)
        return actualParser.valueAsBoolean
    }

    override fun getByte(label: String): Byte {
        if (!isList) {
            spool(label)
            use(label)
        }
        check(takeNext() == JsonToken.VALUE_NUMBER_INT)
        return actualParser.valueAsInt.toByte()
    }

    override fun getShort(label: String): Short {
        if (!isList) {
            spool(label)
            use(label)
        }
        check(takeNext() == JsonToken.VALUE_NUMBER_INT)
        return actualParser.valueAsInt.toShort()
    }

    override fun getInt(label: String): Int {
        if (!isList) {
            spool(label)
            use(label)
        }
        check(takeNext() == JsonToken.VALUE_NUMBER_INT)
        return actualParser.valueAsInt
    }

    override fun getLong(label: String): Long {
        if (!isList) {
            spool(label)
            use(label)
        }
        check(takeNext() == JsonToken.VALUE_NUMBER_INT)
        return actualParser.valueAsLong
    }

    override fun getFloat(label: String): Float {
        if (!isList) {
            spool(label)
            use(label)
        }
        check(takeNext() == JsonToken.VALUE_NUMBER_FLOAT)
        return actualParser.valueAsDouble.toFloat()
    }

    override fun getDouble(label: String): Double {
        if (!isList) {
            spool(label)
            use(label)
        }
        check(takeNext() == JsonToken.VALUE_NUMBER_FLOAT)
        return actualParser.valueAsDouble
    }

    override fun getChar(label: String): Char {
        if (!isList) {
            spool(label)
            use(label)
        }
        check(takeNext() == JsonToken.VALUE_STRING)
        return actualParser.valueAsString[0]
    }

    override fun isNull(label: String): Boolean {
        if (!isList) {
            spool(label)
            use(label)
        }

        if (peekNext() == JsonToken.VALUE_NULL) {
            takeNext()
            return true
        }

        return false
    }

    override fun getUnit(label: String) {
    }

    override fun getString(label: String): String {
        if (!isList) {
            spool(label)
            use(label)
        }
        takeNext()
        return actualParser.valueAsString
    }

}

/**
 * A JSON parser that is resettable. (Hacky implementation, pls gib better)
 */
private class ResettableParser(val source: JsonParser) : ParserMinimalBase(source.featureMask) {
    private val currentStoredToken get() = store[position - storedOffset]

    override fun getIntValue(): Int {
        return currentStoredToken.text.toInt()
    }

    override fun close() {
        source.close()
    }

    override fun getNumberValue(): Number {
        if (currentStoredToken.token == JsonToken.VALUE_NUMBER_FLOAT)
            return currentStoredToken.text.toFloat()
        else if (currentStoredToken.token == JsonToken.VALUE_NUMBER_INT)
            return currentStoredToken.text.toInt()
        else
            error("not a number")
    }

    override fun getBinaryValue(b64variant: Base64Variant?): ByteArray {
        error("unused")
    }

    override fun _handleEOF() {
    }

    override fun getDoubleValue(): Double {
        return currentStoredToken.text.toDouble()
    }

    override fun getNumberType(): NumberType {
        if (currentStoredToken.token == JsonToken.VALUE_NUMBER_FLOAT)
            return NumberType.FLOAT
        else if (currentStoredToken.token == JsonToken.VALUE_NUMBER_INT)
            return NumberType.INT
        else
            error("not a number")
    }

    override fun overrideCurrentName(name: String?) {
    }

    override fun hasTextCharacters(): Boolean {
        return false
    }

    override fun getFloatValue(): Float {
        return currentStoredToken.text.toFloat()
    }

    override fun getBigIntegerValue(): BigInteger {
        return BigInteger(currentStoredToken.text)
    }

    override fun version() =
            source.version()

    override fun isClosed() =
            source.isClosed

    override fun getParsingContext() =
            source.parsingContext

    override fun getCurrentLocation() =
            currentStoredToken.currentLocation

    override fun getDecimalValue(): BigDecimal {
        return BigDecimal(currentStoredToken.text)
    }

    override fun getLongValue(): Long {
        return currentStoredToken.text.toLong()
    }

    override fun getCurrentName(): String? {
        if (currentStoredToken.token != JsonToken.FIELD_NAME)
            return null
        else
            return currentStoredToken.text
    }

    override fun getTokenLocation() =
            currentStoredToken.tokenLocation


    private data class StoredToken(
            val text: String,
            val token: JsonToken,
            val currentLocation: JsonLocation,
            val tokenLocation: JsonLocation)

    private var storedOffset = 0

    private val store = arrayListOf<StoredToken>()

    private var position = -1

    fun reset(position: Int) {
        if (position < storedOffset)
            error("Trying to reset to a position that has been already discarded.")

        this.position = position
    }

    fun discard(position: Int) {
        while (storedOffset < position) {
            storedOffset++
            store.removeAt(0)
        }
    }

    fun current(): Int {
        return position
    }

    override fun setCodec(c: ObjectCodec?) {
        source.codec = c
    }

    override fun getCodec(): ObjectCodec {
        return source.codec
    }

    override fun getText() =
            currentStoredToken.text

    override fun getTextLength() =
            currentStoredToken.text.length

    override fun getTextOffset() = 0

    override fun getTextCharacters() =
            currentStoredToken.text.toCharArray()

    override fun nextToken(): JsonToken {
        position++
        while (store.size < 1 + position - storedOffset) {
            val token = source.nextToken()
            store += StoredToken(source.text, token, source.currentLocation, source.tokenLocation)
        }

        val result = currentStoredToken.token
        _currToken = result
        return result
    }
}