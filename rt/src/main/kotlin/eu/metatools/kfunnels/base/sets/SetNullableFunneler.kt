package eu.metatools.kfunnels.base.sets

import eu.metatools.kfunnels.*

class SetNullableFunneler(val create: () -> MutableSet<Any?>) : Funneler<Set<Any?>> {
    companion object {
        /**
         * Set funneler for hash sets.
         */
        val forHashSet = SetFunneler(::HashSet)

        /**
         * Set funneler for linked hash sets.
         */
        val forLinkedHashSet = SetFunneler(::LinkedHashSet)
    }

    /**
     * Computes the positional label for an item.
     */
    private fun Int.toLabel() = "item$this"

    override fun read(module: Module, type: Type, source: Source): Set<Any?>
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


    override suspend fun read(module: Module, type: Type, source: SuspendSource): Set<Any?>
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

    override fun write(module: Module, type: Type, sink: Sink, item: Set<Any?>)
            = sink.markAround(type, item) {

        @Suppress("unchecked_cast")
        when (type.arg.primitiveCode) {
            Type.primitiveBoolean ->
                for ((i, it) in (item as Set<Boolean>).withIndex())
                    sink.putNullableBoolean(i.toLabel(), it)

            Type.primitiveByte ->
                for ((i, it) in (item as Set<Byte>).withIndex())
                    sink.putNullableByte(i.toLabel(), it)

            Type.primitiveShort ->
                for ((i, it) in (item as Set<Short>).withIndex())
                    sink.putNullableShort(i.toLabel(), it)

            Type.primitiveInt ->
                for ((i, it) in (item as Set<Int>).withIndex())
                    sink.putNullableInt(i.toLabel(), it)

            Type.primitiveLong ->
                for ((i, it) in (item as Set<Long>).withIndex())
                    sink.putNullableLong(i.toLabel(), it)

            Type.primitiveFloat ->
                for ((i, it) in (item as Set<Float>).withIndex())
                    sink.putNullableFloat(i.toLabel(), it)

            Type.primitiveDouble ->
                for ((i, it) in (item as Set<Double>).withIndex())
                    sink.putNullableDouble(i.toLabel(), it)

            Type.primitiveChar ->
                for ((i, it) in (item as Set<Char>).withIndex())
                    sink.putNullableChar(i.toLabel(), it)

            Type.primitiveUnit ->
                for ((i, it) in (item as Set<Unit>).withIndex())
                    sink.putNullableUnit(i.toLabel(), it)

            Type.primitiveString ->
                for ((i, it) in (item as Set<String>).withIndex())
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

    override suspend fun write(module: Module, type: Type, sink: SuspendSink, item: Set<Any?>)
            = sink.markAround(type, item) {

        @Suppress("unchecked_cast")
        when (type.arg.primitiveCode) {
            Type.primitiveBoolean ->
                for ((i, it) in (item as Set<Boolean>).withIndex())
                    sink.putNullableBoolean(i.toLabel(), it)

            Type.primitiveByte ->
                for ((i, it) in (item as Set<Byte>).withIndex())
                    sink.putNullableByte(i.toLabel(), it)

            Type.primitiveShort ->
                for ((i, it) in (item as Set<Short>).withIndex())
                    sink.putNullableShort(i.toLabel(), it)

            Type.primitiveInt ->
                for ((i, it) in (item as Set<Int>).withIndex())
                    sink.putNullableInt(i.toLabel(), it)

            Type.primitiveLong ->
                for ((i, it) in (item as Set<Long>).withIndex())
                    sink.putNullableLong(i.toLabel(), it)

            Type.primitiveFloat ->
                for ((i, it) in (item as Set<Float>).withIndex())
                    sink.putNullableFloat(i.toLabel(), it)

            Type.primitiveDouble ->
                for ((i, it) in (item as Set<Double>).withIndex())
                    sink.putNullableDouble(i.toLabel(), it)

            Type.primitiveChar ->
                for ((i, it) in (item as Set<Char>).withIndex())
                    sink.putNullableChar(i.toLabel(), it)

            Type.primitiveUnit ->
                for ((i, it) in (item as Set<Unit>).withIndex())
                    sink.putNullableUnit(i.toLabel(), it)

            Type.primitiveString ->
                for ((i, it) in (item as Set<String>).withIndex())
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