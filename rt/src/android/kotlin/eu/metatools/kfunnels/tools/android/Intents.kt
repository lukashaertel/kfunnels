package eu.metatools.kfunnels.tools.android

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import eu.metatools.kfunnels.*
import eu.metatools.kfunnels.utils.*
import java.io.Serializable
import java.util.*
import kotlin.reflect.full.isSubclassOf

/**
 * The Intent serializable type, has primitives in [Intent].
 */
private enum class Variant {
    /**
     * No special type for serialization.
     */
    NONE,

    /**
     * A collection, size will be marked.
     */
    COLLECTION,


    BOOLEAN_ARRAY,
    BYTE_ARRAY,
    SHORT_ARRAY,
    INT_ARRAY,
    LONG_ARRAY,
    FLOAT_ARRAY,
    DOUBLE_ARRAY,
    CHAR_ARRAY,

    BUNDLE,
    SERIALIZABLE,
    PARCELABLE,

    STRING_ARRAY,
    PARCELABLE_ARRAY,
    CHAR_SEQUENCE_ARRAY,

    INT_ARRAY_LIST,
    STRING_ARRAY_LIST,
    PARCELABLE_ARRAY_LIST,
    CHAR_SEQUENCE_ARRAY_LIST
}

/**
 * Detects the [Variant] type.
 */
private val Type.variant
    get() =
        when (kClass) {
            BooleanArray::class -> Variant.BOOLEAN_ARRAY
            ByteArray::class -> Variant.BYTE_ARRAY
            ShortArray::class -> Variant.SHORT_ARRAY
            IntArray::class -> Variant.INT_ARRAY
            LongArray::class -> Variant.LONG_ARRAY
            FloatArray::class -> Variant.FLOAT_ARRAY
            DoubleArray::class -> Variant.DOUBLE_ARRAY
            CharArray::class -> Variant.CHAR_ARRAY

            Bundle::class -> Variant.BUNDLE

            Array<String>::class -> Variant.STRING_ARRAY
            Array<Parcelable>::class -> Variant.PARCELABLE_ARRAY
            Array<CharSequence>::class -> Variant.CHAR_SEQUENCE_ARRAY

            ArrayList::class -> when (arg.kClass) {
                Int::class -> Variant.INT_ARRAY_LIST
                String::class -> Variant.STRING_ARRAY_LIST
                Parcelable::class -> Variant.PARCELABLE_ARRAY_LIST
                CharSequence::class -> Variant.CHAR_SEQUENCE_ARRAY_LIST
                else -> Variant.NONE
            }

            else ->
                when {
                    kClass.isSubclassOf(Serializable::class) -> Variant.SERIALIZABLE
                    kClass.isSubclassOf(Parcelable::class) -> Variant.PARCELABLE
                    kClass.isSubclassOf(Collection::class) -> Variant.COLLECTION
                    else -> Variant.NONE
                }
        }

/**
 * A class that uses a label stack.
 */
interface LabelStack {
    val labels: Stack<String>
}


/**
 * Gets the qualified name currently on the stack.
 */
private val LabelStack.qn
    get() =
        labels.joinToString(".")

/**
 * Prefixed qualified name.
 */
private fun LabelStack.joinPre(prefix: String) =
        (listOf(prefix) + labels).joinToString(".")

/**
 * Surrounded qualified name.
 */
private fun LabelStack.joinSur(prefix: String, suffix: String) =
        (listOf(prefix) + labels + listOf(suffix)).joinToString(".")

/**
 * Suffixed qualified name.
 */
private fun LabelStack.joinSuf(suffix: String) =
        (labels + listOf(suffix)).joinToString(".")

/**
 * Writes to the intent, nesting will be joined by concatenation.
 */
class IntentSink(val intent: Intent) : Sink, LabelStack {
    override val labels = Stack<String>()

    private fun doPut(type: Type, value: Any?): Boolean {
        @Suppress("unchecked_cast")
        when (type.variant) {
        // Fallback case
            Variant.NONE -> {
                return true
            }

            Variant.COLLECTION -> {
                if (value is Collection<*>)
                    intent.putExtra(joinPre("sizes"), value.size)
                return true
            }

        // Primitive arrays
            Variant.BOOLEAN_ARRAY -> intent.getBooleanArrayExtra(qn)
            Variant.BYTE_ARRAY -> intent.getByteArrayExtra(qn)
            Variant.SHORT_ARRAY -> intent.getShortArrayExtra(qn)
            Variant.INT_ARRAY -> intent.getIntArrayExtra(qn)
            Variant.LONG_ARRAY -> intent.getLongArrayExtra(qn)
            Variant.FLOAT_ARRAY -> intent.getFloatArrayExtra(qn)
            Variant.DOUBLE_ARRAY -> intent.getDoubleArrayExtra(qn)
            Variant.CHAR_ARRAY -> intent.getCharArrayExtra(qn)

        // Serializables
            Variant.BUNDLE -> intent.getBundleExtra(qn)
            Variant.SERIALIZABLE -> intent.getSerializableExtra(qn)
            Variant.PARCELABLE -> intent.getParcelableExtra(qn)

        // More array types
            Variant.STRING_ARRAY -> intent.putExtra(qn, value as Array<String>?)
            Variant.PARCELABLE_ARRAY -> intent.putExtra(qn, value as Array<Parcelable>?)
            Variant.CHAR_SEQUENCE_ARRAY -> intent.putExtra(qn, value as Array<CharSequence>?)

        // Array lists
            Variant.INT_ARRAY_LIST ->
                intent.putIntegerArrayListExtra(qn, value as ArrayList<Int>)
            Variant.STRING_ARRAY_LIST ->
                intent.putStringArrayListExtra(qn, value as ArrayList<String>)
            Variant.PARCELABLE_ARRAY_LIST ->
                intent.putParcelableArrayListExtra(qn, value as ArrayList<Parcelable>)
            Variant.CHAR_SEQUENCE_ARRAY_LIST ->
                intent.putCharSequenceArrayListExtra(qn, value as ArrayList<CharSequence>)
        }

        return false
    }

    override fun begin(type: Type, value: Any?): Boolean {
        labels.push(type.kClass.qualifiedName ?: "anonymous")
        return doPut(type, value)
    }

    override fun end(type: Type, value: Any?) {
        labels.pop()
    }

    override fun beginNested(label: String, type: Type, value: Any?): Boolean {
        labels.push(label)
        return doPut(type, value)
    }

    override fun endNested(label: String, type: Type, value: Any?) {
        labels.pop()
    }

    override fun putBoolean(label: String, value: Boolean) {
        intent.putExtra(joinSuf(label), value)
    }

    override fun putByte(label: String, value: Byte) {
        intent.putExtra(joinSuf(label), value)
    }

    override fun putShort(label: String, value: Short) {
        intent.putExtra(joinSuf(label), value)
    }

    override fun putInt(label: String, value: Int) {
        intent.putExtra(joinSuf(label), value)
    }

    override fun putLong(label: String, value: Long) {
        intent.putExtra(joinSuf(label), value)
    }

    override fun putFloat(label: String, value: Float) {
        intent.putExtra(joinSuf(label), value)
    }

    override fun putDouble(label: String, value: Double) {
        intent.putExtra(joinSuf(label), value)
    }

    override fun putChar(label: String, value: Char) {
        intent.putExtra(joinSuf(label), value)
    }

    override fun putNull(label: String, isNull: Boolean) {
        intent.putExtra(joinSur("nulls", label), isNull)
    }

    override fun putUnit(label: String, value: Unit) {
    }

    override fun putString(label: String, value: String) {
        intent.putExtra(joinSuf(label), value)
    }
}

/**
 * Reads from the intent, nesting will be joined by concatenation.
 */
class IntentSource(val intent: Intent) : Source, LabelStack {
    private val counts = Stack<Int>()

    override val labels = Stack<String>()

    private fun doBegin(type: Type): Option<Any?> {
        @Suppress("unchecked_cast")
        when (type.variant) {
        // Fallback case
            Variant.NONE -> {
                counts.push(-1)
                return None()
            }

            Variant.COLLECTION -> {
                joinPre("sizes").let {
                    check(intent.hasExtra(it))
                    counts.push(intent.getIntExtra(it, 0))
                }

                return None()
            }

        // Primitive arrays
            Variant.BOOLEAN_ARRAY -> return Some(intent.getBooleanArrayExtra(qn))
            Variant.BYTE_ARRAY -> return Some(intent.getByteArrayExtra(qn))
            Variant.SHORT_ARRAY -> return Some(intent.getShortArrayExtra(qn))
            Variant.INT_ARRAY -> return Some(intent.getIntArrayExtra(qn))
            Variant.LONG_ARRAY -> return Some(intent.getLongArrayExtra(qn))
            Variant.FLOAT_ARRAY -> return Some(intent.getFloatArrayExtra(qn))
            Variant.DOUBLE_ARRAY -> return Some(intent.getDoubleArrayExtra(qn))
            Variant.CHAR_ARRAY -> return Some(intent.getCharArrayExtra(qn))

        // Serializables
            Variant.BUNDLE -> return Some(intent.getBundleExtra(qn))
            Variant.SERIALIZABLE -> return Some(intent.getSerializableExtra(qn))
            Variant.PARCELABLE -> return Some(intent.getParcelableExtra<Parcelable?>(qn))

        // More array types
            Variant.STRING_ARRAY -> return Some(intent.getStringExtra(qn))
            Variant.PARCELABLE_ARRAY -> return Some(intent.getParcelableArrayExtra(qn))
            Variant.CHAR_SEQUENCE_ARRAY -> return Some(intent.getCharSequenceArrayExtra(qn))

        // Array lists
            Variant.INT_ARRAY_LIST ->
                return Some(intent.getIntegerArrayListExtra(qn))
            Variant.STRING_ARRAY_LIST ->
                return Some(intent.getStringArrayListExtra(qn))
            Variant.PARCELABLE_ARRAY_LIST ->
                return Some(intent.getParcelableArrayListExtra<Parcelable?>(qn))
            Variant.CHAR_SEQUENCE_ARRAY_LIST ->
                return Some(intent.getCharSequenceArrayListExtra(qn))
        }
    }

    private fun doEnd() {
        counts.pop()
    }

    override fun begin(type: Type): Begin {
        labels.push(type.kClass.qualifiedName ?: "anonymous")
        return doBegin(type).fold({ Value(it) }, { Unfunnel })
    }

    override fun end(type: Type) {
        doEnd()
        labels.pop()
    }

    override fun isEnd(): Boolean {
        return counts.topZero()
    }


    override fun beginNested(label: String, type: Type): Nested {
        labels.push(label)
        return doBegin(type).fold({ Item(it) }, { Nest })
    }

    override fun endNested(label: String, type: Type) {
        doEnd()
        labels.pop()
    }

    override fun getBoolean(label: String): Boolean {
        joinSuf(label).let {
            check(intent.hasExtra(it))
            return intent.getBooleanExtra(it, false)
        }
    }

    override fun getByte(label: String): Byte {
        joinSuf(label).let {
            check(intent.hasExtra(it))
            return intent.getByteExtra(it, 0)
        }
    }

    override fun getShort(label: String): Short {
        joinSuf(label).let {
            check(intent.hasExtra(it))
            return intent.getShortExtra(it, 0)
        }
    }

    override fun getInt(label: String): Int {
        joinSuf(label).let {
            check(intent.hasExtra(it))
            return intent.getIntExtra(it, 0)
        }
    }

    override fun getLong(label: String): Long {
        joinSuf(label).let {
            check(intent.hasExtra(it))
            return intent.getLongExtra(it, 0L)
        }
    }

    override fun getFloat(label: String): Float {
        joinSuf(label).let {
            check(intent.hasExtra(it))
            return intent.getFloatExtra(it, 0.0f)
        }
    }

    override fun getDouble(label: String): Double {
        joinSuf(label).let {
            check(intent.hasExtra(it))
            return intent.getDoubleExtra(it, 0.0)
        }
    }

    override fun getChar(label: String): Char {
        joinSuf(label).let {
            check(intent.hasExtra(it))
            return intent.getCharExtra(it, '\u0000')
        }
    }

    override fun isNull(label: String): Boolean {
        joinSur("nulls", label).let {
            return intent.getBooleanExtra(it, false)
        }
    }

    override fun getUnit(label: String) {
    }

    override fun getString(label: String): String {
        joinSuf(label).let {
            check(intent.hasExtra(it))
            return intent.getStringExtra(it)
        }
    }
}