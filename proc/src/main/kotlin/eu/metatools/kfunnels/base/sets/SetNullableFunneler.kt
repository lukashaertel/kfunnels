package eu.metatools.kfunnels.base.sets

import eu.metatools.kfunnels.*

object SetNullableFunneler : Funneler<Set<Any?>> {
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
                val r = HashSet<Boolean?>()
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableBoolean((p++).toLabel())
                return r
            }

            Type.primitiveByte -> {
                val r = HashSet<Byte?>()
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableByte((p++).toLabel())
                return r
            }


            Type.primitiveShort -> {
                val r = HashSet<Short?>()
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableShort((p++).toLabel())
                return r
            }

            Type.primitiveInt -> {
                val r = HashSet<Int?>()
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableInt((p++).toLabel())
                return r
            }

            Type.primitiveLong -> {
                val r = HashSet<Long?>()
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableLong((p++).toLabel())
                return r
            }

            Type.primitiveFloat -> {
                val r = HashSet<Float?>()
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableFloat((p++).toLabel())
                return r
            }

            Type.primitiveDouble -> {
                val r = HashSet<Double?>()
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableDouble((p++).toLabel())
                return r
            }

            Type.primitiveChar -> {
                val r = HashSet<Char?>()
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableChar((p++).toLabel())
                return r
            }

            Type.primitiveUnit -> {
                val r = HashSet<Unit?>()
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableUnit((p++).toLabel())
                return r
            }

            Type.primitiveString -> {
                val r = HashSet<String?>()
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableString((p++).toLabel())
                return r
            }

            else -> {
                if (type.arg.isTerminal()) {
                    // Resolve element funneler for terminal type
                    val sub = module.resolve<Any>(type.arg)

                    val r = HashSet<Any?>()
                    var p = 0
                    while (!source.isEnd()) {
                        source.beginNested(p.toLabel(), type.arg)
                        r += sub.read(module, type.arg, source)
                        source.endNested((p++).toLabel(), type.arg)
                    }
                    return r
                } else {
                    val r = HashSet<Any?>()
                    var p = 0
                    while (!source.isEnd()) {
                        // Resolve element funneler for dynamic type
                        val t = source.beginNested(p.toLabel(), type.arg)
                        val sub = module.resolve<Any>(t)
                        r += sub.read(module, type.arg, source)
                        source.endNested((p++).toLabel(), type.arg)
                    }
                    return r
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
                val r = HashSet<Boolean?>()
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableBoolean((p++).toLabel())
                return r
            }

            Type.primitiveByte -> {
                val r = HashSet<Byte?>()
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableByte((p++).toLabel())
                return r
            }


            Type.primitiveShort -> {
                val r = HashSet<Short?>()
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableShort((p++).toLabel())
                return r
            }

            Type.primitiveInt -> {
                val r = HashSet<Int?>()
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableInt((p++).toLabel())
                return r
            }

            Type.primitiveLong -> {
                val r = HashSet<Long?>()
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableLong((p++).toLabel())
                return r
            }

            Type.primitiveFloat -> {
                val r = HashSet<Float?>()
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableFloat((p++).toLabel())
                return r
            }

            Type.primitiveDouble -> {
                val r = HashSet<Double?>()
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableDouble((p++).toLabel())
                return r
            }

            Type.primitiveChar -> {
                val r = HashSet<Char?>()
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableChar((p++).toLabel())
                return r
            }

            Type.primitiveUnit -> {
                val r = HashSet<Unit?>()
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableUnit((p++).toLabel())
                return r
            }

            Type.primitiveString -> {
                val r = HashSet<String?>()
                var p = 0
                while (!source.isEnd())
                    r += source.getNullableString((p++).toLabel())
                return r
            }

            else -> {
                if (type.arg.isTerminal()) {
                    // Resolve element funneler for terminal type
                    val sub = module.resolve<Any>(type.arg)

                    val r = HashSet<Any?>()
                    var p = 0
                    while (!source.isEnd()) {
                        if (source.isNull(p.toLabel())) {
                            r.add(null)
                            p++
                        } else {
                            source.beginNested(p.toLabel(), type.arg)
                            r += sub.read(module, type.arg, source)
                            source.endNested((p++).toLabel(), type.arg)
                        }
                    }
                    return r
                } else {
                    val r = HashSet<Any?>()
                    var p = 0
                    while (!source.isEnd()) {
                        if (source.isNull(p.toLabel())) {
                            r.add(null)
                            p++
                        } else {
                            // Resolve element funneler for dynamic type
                            val t = source.beginNested(p.toLabel(), type.arg)
                            val sub = module.resolve<Any>(t)
                            r += sub.read(module, type.arg, source)
                            source.endNested((p++).toLabel(), type.arg)
                        }
                    }
                    return r
                }
            }
        }
    }

    override fun write(module: Module, type: Type, sink: Sink, item: Set<Any?>)
            = sink.markAround(type) {

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
                // Resolve element funneler
                val sub = module.resolve<Any>(type.arg)

                // Write nested
                for ((i, it) in item.withIndex())
                    if (it == null)
                        sink.putNull(i.toLabel(), true)
                    else {
                        sink.putNull(i.toLabel(), false)
                        sink.beginNested(i.toLabel(), type.arg, it)
                        sub.write(module, type.arg, sink, it)
                        sink.endNested(i.toLabel(), type.arg, it)
                    }
            }
        }
    }

    override suspend fun write(module: Module, type: Type, sink: SuspendSink, item: Set<Any?>)
            = sink.markAround(type) {

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
                // Resolve element funneler
                val sub = module.resolve<Any>(type.arg)

                // Write nested
                for ((i, it) in item.withIndex())
                    if (it == null)
                        sink.putNull(i.toLabel(), true)
                    else {
                        sink.putNull(i.toLabel(), false)
                        sink.beginNested(i.toLabel(), type.arg, it)
                        sub.write(module, type.arg, sink, it)
                        sink.endNested(i.toLabel(), type.arg, it)
                    }
            }
        }
    }
}