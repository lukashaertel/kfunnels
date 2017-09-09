package eu.metatools.kfunnels.base.lists

import eu.metatools.kfunnels.*
import java.util.*

// TODO This can probably be merged with Listlike Nullable Funneler
class ListNullableFunneler(val create: () -> MutableList<Any?>) : Funneler<List<Any?>> {
    companion object {
        /**
         * List funneler for array lists.
         */
        val forArrayList = ListNullableFunneler(::ArrayList)

        /**
         * List funneler for linked lists.
         */
        val forLinkedList = ListNullableFunneler(::LinkedList)

        /**
         * List funneler for stacks.
         */
        val forStack = ListNullableFunneler(::Stack)

        /**
         * List funneler for vectors.
         */
        val forVector = ListNullableFunneler(::Vector)
    }

    /**
     * Computes the positional label for an item.
     */
    private fun Int.toLabel() = "item$this"

    override fun read(module: Module, type: Type, source: Source): List<Any?>
            = source.markAround(type) {

        // Try to find a faster resolution for primitive types
        @Suppress("unchecked_cast")
        when (type.arg.primitiveCode) {
            Type.primitiveBoolean -> {
                val r = create()
                source.afterCreate(r)
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableBoolean((p++).toLabel())
                return@markAround r
            }

            Type.primitiveByte -> {
                val r = create()
                source.afterCreate(r)
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableByte((p++).toLabel())
                return@markAround r
            }


            Type.primitiveShort -> {
                val r = create()
                source.afterCreate(r)
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableShort((p++).toLabel())
                return@markAround r
            }

            Type.primitiveInt -> {
                val r = create()
                source.afterCreate(r)
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableInt((p++).toLabel())
                return@markAround r
            }

            Type.primitiveLong -> {
                val r = create()
                source.afterCreate(r)
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableLong((p++).toLabel())
                return@markAround r
            }

            Type.primitiveFloat -> {
                val r = create()
                source.afterCreate(r)
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableFloat((p++).toLabel())
                return@markAround r
            }

            Type.primitiveDouble -> {
                val r = create()
                source.afterCreate(r)
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableDouble((p++).toLabel())
                return@markAround r
            }

            Type.primitiveChar -> {
                val r = create()
                source.afterCreate(r)
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableChar((p++).toLabel())
                return@markAround r
            }

            Type.primitiveUnit -> {
                val r = create()
                source.afterCreate(r)
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableUnit((p++).toLabel())
                return@markAround r
            }

            Type.primitiveString -> {
                val r = create()
                source.afterCreate(r)
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableString((p++).toLabel())
                return@markAround r
            }

            else -> {
                if (type.arg.isTerminal()) {
                    // Resolve element funneler for terminal type
                    val sub = module.resolve<Any>(type.arg)

                    val r = create()
                    source.afterCreate(r)
                    var p = 0
                    while (!source.isEnd())
                        r += source.getNullableTerminalNested(module, sub, (p++).toLabel(), type.arg)
                    return@markAround r
                } else {
                    val sub = module.resolve<Any>(type.arg)

                    val r = create()
                    source.afterCreate(r)
                    var p = 0
                    while (!source.isEnd())
                        r += source.getNullableDynamicNested<Any>(module, sub, (p++).toLabel(), type.arg)
                    return@markAround r
                }
            }
        }
    }


    override suspend fun read(module: Module, type: Type, source: SuspendSource): List<Any?>
            = source.markAround(type) {

        // Try to find a faster resolution for primitive types
        @Suppress("unchecked_cast")
        when (type.arg.primitiveCode) {
            Type.primitiveBoolean -> {
                val r = create()
                source.afterCreate(r)
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableBoolean((p++).toLabel())
                return@markAround r
            }

            Type.primitiveByte -> {
                val r = create()
                source.afterCreate(r)
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableByte((p++).toLabel())
                return@markAround r
            }


            Type.primitiveShort -> {
                val r = create()
                source.afterCreate(r)
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableShort((p++).toLabel())
                return@markAround r
            }

            Type.primitiveInt -> {
                val r = create()
                source.afterCreate(r)
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableInt((p++).toLabel())
                return@markAround r
            }

            Type.primitiveLong -> {
                val r = create()
                source.afterCreate(r)
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableLong((p++).toLabel())
                return@markAround r
            }

            Type.primitiveFloat -> {
                val r = create()
                source.afterCreate(r)
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableFloat((p++).toLabel())
                return@markAround r
            }

            Type.primitiveDouble -> {
                val r = create()
                source.afterCreate(r)
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableDouble((p++).toLabel())
                return@markAround r
            }

            Type.primitiveChar -> {
                val r = create()
                source.afterCreate(r)
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableChar((p++).toLabel())
                return@markAround r
            }

            Type.primitiveUnit -> {
                val r = create()
                source.afterCreate(r)
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableUnit((p++).toLabel())
                return@markAround r
            }

            Type.primitiveString -> {
                val r = create()
                source.afterCreate(r)
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableString((p++).toLabel())
                return@markAround r
            }

            else -> {
                if (type.arg.isTerminal()) {
                    // Resolve element funneler for terminal type
                    val sub = module.resolve<Any>(type.arg)

                    val r = create()
                    source.afterCreate(r)
                    var p = 0
                    while (!source.isEnd())
                        r += source.getNullableTerminalNested(module, sub, (p++).toLabel(), type.arg)
                    return@markAround r
                } else {
                    val sub = module.resolve<Any>(type.arg)

                    val r = create()
                    source.afterCreate(r)
                    var p = 0
                    while (!source.isEnd())
                        r += source.getNullableDynamicNested<Any>(module, sub, (p++).toLabel(), type.arg)
                    return@markAround r
                }
            }
        }
    }

    override fun write(module: Module, type: Type, sink: Sink, item: List<Any?>)
            = sink.markAround(type, item) {

        @Suppress("unchecked_cast")
        when (type.arg.primitiveCode) {
            Type.primitiveBoolean ->
                for ((i, it) in (item as List<Boolean>).withIndex())
                    sink.putNullableBoolean(i.toLabel(), it)

            Type.primitiveByte ->
                for ((i, it) in (item as List<Byte>).withIndex())
                    sink.putNullableByte(i.toLabel(), it)

            Type.primitiveShort ->
                for ((i, it) in (item as List<Short>).withIndex())
                    sink.putNullableShort(i.toLabel(), it)

            Type.primitiveInt ->
                for ((i, it) in (item as List<Int>).withIndex())
                    sink.putNullableInt(i.toLabel(), it)

            Type.primitiveLong ->
                for ((i, it) in (item as List<Long>).withIndex())
                    sink.putNullableLong(i.toLabel(), it)

            Type.primitiveFloat ->
                for ((i, it) in (item as List<Float>).withIndex())
                    sink.putNullableFloat(i.toLabel(), it)

            Type.primitiveDouble ->
                for ((i, it) in (item as List<Double>).withIndex())
                    sink.putNullableDouble(i.toLabel(), it)

            Type.primitiveChar ->
                for ((i, it) in (item as List<Char>).withIndex())
                    sink.putNullableChar(i.toLabel(), it)

            Type.primitiveUnit ->
                for ((i, it) in (item as List<Unit>).withIndex())
                    sink.putNullableUnit(i.toLabel(), it)

            Type.primitiveString ->
                for ((i, it) in (item as List<String>).withIndex())
                    sink.putNullableString(i.toLabel(), it)

            else -> {
                if (type.arg.isTerminal()) {
                    // Resolve element funneler for terminal type
                    val sub = module.resolve<Any>(type.arg)

                    // Write nested
                    for ((i, it) in item.withIndex())
                        sink.putNullableTerminalNested(module, sub, i.toLabel(), type.arg, it)
                } else {
                    // Write nested
                    for ((i, it) in item.withIndex())
                        sink.putNullableDynamicNested(module, i.toLabel(), type.arg, it)
                }
            }
        }
    }

    override suspend fun write(module: Module, type: Type, sink: SuspendSink, item: List<Any?>)
            = sink.markAround(type, item) {

        @Suppress("unchecked_cast")
        when (type.arg.primitiveCode) {
            Type.primitiveBoolean ->
                for ((i, it) in (item as List<Boolean>).withIndex())
                    sink.putNullableBoolean(i.toLabel(), it)

            Type.primitiveByte ->
                for ((i, it) in (item as List<Byte>).withIndex())
                    sink.putNullableByte(i.toLabel(), it)

            Type.primitiveShort ->
                for ((i, it) in (item as List<Short>).withIndex())
                    sink.putNullableShort(i.toLabel(), it)

            Type.primitiveInt ->
                for ((i, it) in (item as List<Int>).withIndex())
                    sink.putNullableInt(i.toLabel(), it)

            Type.primitiveLong ->
                for ((i, it) in (item as List<Long>).withIndex())
                    sink.putNullableLong(i.toLabel(), it)

            Type.primitiveFloat ->
                for ((i, it) in (item as List<Float>).withIndex())
                    sink.putNullableFloat(i.toLabel(), it)

            Type.primitiveDouble ->
                for ((i, it) in (item as List<Double>).withIndex())
                    sink.putNullableDouble(i.toLabel(), it)

            Type.primitiveChar ->
                for ((i, it) in (item as List<Char>).withIndex())
                    sink.putNullableChar(i.toLabel(), it)

            Type.primitiveUnit ->
                for ((i, it) in (item as List<Unit>).withIndex())
                    sink.putNullableUnit(i.toLabel(), it)

            Type.primitiveString ->
                for ((i, it) in (item as List<String>).withIndex())
                    sink.putNullableString(i.toLabel(), it)

            else -> {
                if (type.arg.isTerminal()) {
                    // Resolve element funneler for terminal type
                    val sub = module.resolve<Any>(type.arg)

                    // Write nested
                    for ((i, it) in item.withIndex())
                        sink.putNullableTerminalNested(module, sub, i.toLabel(), type.arg, it)
                } else {
                    // Write nested
                    for ((i, it) in item.withIndex())
                        sink.putNullableDynamicNested(module, i.toLabel(), type.arg, it)
                }
            }
        }
    }
}