package eu.metatools.kfunnels

import com.google.common.reflect.TypeToken
import eu.metatools.kfunnels.utils.kClassFor
import java.lang.reflect.GenericArrayType
import java.lang.reflect.ParameterizedType
import java.lang.reflect.WildcardType
import java.util.*
import java.util.regex.Pattern
import kotlin.reflect.KClass
import kotlin.*

/**
 * Represents a type with actual arguments.
 */
data class Type(val kClass: KClass<*>, val nullable: Boolean, val args: List<Type>) {
    companion object {
        /**
         * Primitive code.
         */
        val primitiveBoolean: Byte = 0

        /**
         * Primitive code.
         */
        val primitiveByte: Byte = 1

        /**
         * Primitive code.
         */
        val primitiveShort: Byte = 2

        /**
         * Primitive code.
         */
        val primitiveInt: Byte = 3

        /**
         * Primitive code.
         */
        val primitiveLong: Byte = 4

        /**
         * Primitive code.
         */
        val primitiveFloat: Byte = 5

        /**
         * Primitive code.
         */
        val primitiveDouble: Byte = 6

        /**
         * Primitive code.
         */
        val primitiveChar: Byte = 7

        /**
         * Primitive code.
         */
        val primitiveUnit: Byte = 8

        /**
         * Primitive code.
         */
        val primitiveString: Byte = 9

        /**
         * Primitive instance.
         */
        val boolean = Type(Boolean::class, false, emptyList())

        /**
         * Primitive instance.
         */
        val byte = Type(Byte::class, false, emptyList())

        /**
         * Primitive instance.
         */
        val short = Type(Short::class, false, emptyList())

        /**
         * Primitive instance.
         */
        val int = Type(Int::class, false, emptyList())

        /**
         * Primitive instance.
         */
        val long = Type(Long::class, false, emptyList())

        /**
         * Primitive instance.
         */
        val float = Type(Float::class, false, emptyList())

        /**
         * Primitive instance.
         */
        val double = Type(Double::class, false, emptyList())

        /**
         * Primitive instance.
         */
        val char = Type(Char::class, false, emptyList())

        /**
         * Primitive instance.
         */
        val unit = Type(Unit::class, false, emptyList())

        /**
         * Primitive instance.
         */
        val string = Type(String::class, false, emptyList())

        /**
         * Constructs an array list type.
         */
        fun arrayList(type: Type) = Type(ArrayList::class, false, listOf(type))

        /**
         * Constructs an linked list type.
         */
        fun linkedList(type: Type) = Type(LinkedList::class, false, listOf(type))

        /**
         * Constructs an mutable list type.
         */
        fun mutableList(type: Type) = Type(MutableList::class, false, listOf(type))

        /**
         * Constructs an immutable list type.
         */
        fun list(type: Type) = Type(List::class, false, listOf(type))

        /**
         * Constructs an pair type.
         */
        fun pair(first: Type, second: Type) = Type(Pair::class, false, listOf(first, second))

        /**
         * Translates a Java reflection type.
         */
        fun from(type: java.lang.reflect.Type): Type {
            // For parameters, map the arguments
            if (type is ParameterizedType) {
                val base = type.rawType as Class<*>
                val args = type.actualTypeArguments.map { from(it) }
                return Type(base.kotlin, false, args)
            }

            // For generic arrays, use the compound type as an argument
            if (type is GenericArrayType) {
                val arg = from(type.genericComponentType)
                return Type(Array<Any?>::class, false, listOf(arg))
            }

            //  For wildcard type, assume that it has one single upper bound, otherwise it cannot be easily converted
            if (type is WildcardType)
                return from(type.upperBounds.single())

            // Cast to class and return as type
            return Type((type as Class<*>).kotlin, false, listOf())
        }

        /**
         * Analyzes the type and passes it to the Java reflection translator. This method requires compiling with Guava.
         * **WARNING: Nullable marks are not supported by this method.**
         */
        inline fun <reified T> from() =
                from(object : TypeToken<T>() {}.type)

        /**
         * Parses the type from the string.
         */
        fun parse(string: String): Type {
            /**
             * Position of the parser.
             */
            var pos = 0

            /**
             * Reads skips whitespaces.
             */
            fun ws() {
                while (pos in string.indices)
                    if (string[pos].isWhitespace())
                        pos++
                    else
                        break
            }

            /**
             * Returns true and advances if currently at the given character, otherwise remains at the current position.
             */
            fun chr(char: Char): Boolean {
                if (pos !in string.indices)
                    return false

                if (string[pos] != char)
                    return false

                pos++
                return true
            }

            /**
             * Parses a name fragment.
             */
            fun name(): String {
                var name = ""
                while (pos in string.indices) {
                    val c = string[pos]
                    if (!c.isLetter())
                        return name
                    name += c
                    pos++
                }
                return name
            }

            /**
             * Parses a full name (with '.').
             */
            fun fullName(): String {
                val name = name()

                ws()
                if (chr('.'))
                    return name + '.' + fullName()
                else
                    return name
            }

            /**
             * Parses a type.
             */
            fun type(): Type {
                // Get name and map it to a KClass
                val kClass = kClassFor(fullName())

                // Make argument list
                val args = arrayListOf<Type>()

                // Try to read type arguments
                ws()
                if (chr('<')) {
                    ws()

                    while (true) {
                        ws()

                        // Recursively parse
                        args += type()

                        ws()

                        // Check if there are more arguments
                        if (!chr(','))
                            break
                    }

                    ws()
                    check(chr('>'))
                    ws()
                }
                ws()

                // Returns with determined nullability
                if (chr('?'))
                    return Type(kClass, true, args.toList())
                else
                    return Type(kClass, false, args.toList())
            }

            return type()
        }
    }

    /**
     * Converts the type to a nullable type.
     */
    operator fun unaryMinus() = Type(kClass, true, args)

    /**
     * Converts the type to a non-nullable type.
     */
    operator fun unaryPlus() = Type(kClass, false, args)

    /**
     * Convenience method for the only type argument.
     */
    val arg get() = args.single()

    /**
     * Convenience method for the first type argument of two.
     */
    val key: Type
        get() {
            check(args.size == 2)
            return args[0]
        }

    /**
     * Convenience method for the second type argument of two.
     */
    val value: Type
        get() {
            check(args.size == 2)
            return args[1]
        }

    /**
     * Makes a dynamic instance where the [kClass] is substituted for the actual type.
     */
    fun forInstance(any: Any?) =
            if (any == null)
                this
            else
                Type(any.javaClass.kotlin, nullable, args)

    /**
     * Transforms the argument at the given position
     */
    inline fun mapArg(arg: Int, block: (Type) -> Type) =
            Type(kClass, nullable,
                    args.subList(0, arg) + block(args[arg]) + args.subList(arg + 1, args.size))

    /**
     * Recursively substitutes a type.
     */
    fun sub(original: KClass<*>, with: KClass<*>): Type =
            Type(if (kClass == original) with else kClass, nullable, args.map { it.sub(original, with) })

    /**
     * Maps to a new type where the [arg] is nullable.
     */
    fun argNullable() = Type(kClass, nullable, listOf(-arg))

    /**
     * Maps to a new type where the [arg] is non-nullable.
     */
    fun argNonNullable() = Type(kClass, nullable, listOf(+arg))


    /**
     * Maps to a new type where the [key] is nullable.
     */
    fun keyNullable() = Type(kClass, nullable, listOf(-key, value))

    /**
     * Maps to a new type where the [key] is non-nullable.
     */
    fun keyNonNullable() = Type(kClass, nullable, listOf(+key, value))


    /**
     * Maps to a new type where the [value] is nullable.
     */
    fun valueNullable() = Type(kClass, nullable, listOf(key, -value))

    /**
     * Maps to a new type where the [value] is non-nullable.
     */
    fun valueNonNullable() = Type(kClass, nullable, listOf(key, +value))


    /**
     * The primitive code or null if not a primitive.
     */
    val primitiveCode by lazy {
        if (args.isEmpty())
            when (kClass) {
                Boolean::class -> primitiveBoolean
                Byte::class -> primitiveByte
                Short::class -> primitiveShort
                Int::class -> primitiveInt
                Long::class -> primitiveLong
                Float::class -> primitiveFloat
                Double::class -> primitiveDouble
                Char::class -> primitiveChar
                Unit::class -> primitiveUnit
                String::class -> primitiveString
                else -> null
            }
        else
            null
    }

    /**
     * Returns true if this type is primitive.
     */
    fun isPrimitive() = primitiveCode != null

    /**
     * Returns true if the type can not be substituted by another type.
     */
    fun isTerminal() =
            when (kClass) {
            // Aligned with the funnelable processor
                MutableCollection::class,
                kotlin.collections.MutableList::class,
                kotlin.collections.ArrayList::class,
                kotlin.collections.Collection::class,
                kotlin.collections.List::class,
                kotlin.collections.MutableSet::class,
                kotlin.collections.HashSet::class,
                kotlin.collections.Set::class,
                kotlin.Pair::class,
                kotlin.Triple::class -> true
            // Default detection
                else -> !(kClass.isAbstract || kClass.isOpen || kClass.isSealed)
            }

    private fun isDefaultInclude(): Boolean =
            kClass.qualifiedName?.let {
                when (it.substringBeforeLast('.')) {
                    "kotlin",
                    "kotlin.collections" -> true
                    else -> false
                }
            } ?: false


    override fun toString() =
            buildString {
                // Append name, simplify if possible
                if (isDefaultInclude())
                    append(kClass.simpleName)
                else
                    append(kClass.qualifiedName)

                // Append arguments
                if (args.isNotEmpty()) {
                    append('<')
                    var sep = false
                    for (arg in args) {
                        if (sep)
                            append(", ")
                        append(arg)
                        sep = true
                    }
                    append('>')
                }

                // Append nullable suffix
                if (nullable)
                    append('?')
            }
}