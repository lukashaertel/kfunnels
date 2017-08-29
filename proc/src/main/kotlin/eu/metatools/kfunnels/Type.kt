package eu.metatools.kfunnels

import java.util.*
import kotlin.reflect.KClass

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
    val key get() = args[0].apply { check(args.size == 2) }

    /**
     * Convenience method for the second type argument of two.
     */
    val value get() = args[1].apply { check(args.size == 2) }

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
}