package eu.metatools.kfunnels.base.sets

import eu.metatools.kfunnels.*

object SetFunneler : Funneler<Set<Any>> {
    /**
     * Computes the positional label for an item.
     */
    private fun Int.toLabel() = "item$this"

    override fun read(module: Module, type: Type, source: Source): Set<Any>
            = source.markAround(type) {

        // Try to find a faster resolution for primitive types
        @Suppress("unchecked_cast")
        when (type.arg.primitiveCode) {
            Type.primitiveBoolean -> {
                val r = HashSet<Boolean>()
                var p = 0
                while (!source.isEnd())
                    r += source.getBoolean((p++).toLabel())
                return@markAround r
            }

            Type.primitiveByte -> {
                val r = HashSet<Byte>()
                var p = 0
                while (!source.isEnd())
                    r += source.getByte((p++).toLabel())
                return@markAround r
            }


            Type.primitiveShort -> {
                val r = HashSet<Short>()
                var p = 0
                while (!source.isEnd())
                    r += source.getShort((p++).toLabel())
                return@markAround r
            }

            Type.primitiveInt -> {
                val r = HashSet<Int>()
                var p = 0
                while (!source.isEnd())
                    r += source.getInt((p++).toLabel())
                return@markAround r
            }

            Type.primitiveLong -> {
                val r = HashSet<Long>()
                var p = 0
                while (!source.isEnd())
                    r += source.getLong((p++).toLabel())
                return@markAround r
            }

            Type.primitiveFloat -> {
                val r = HashSet<Float>()
                var p = 0
                while (!source.isEnd())
                    r += source.getFloat((p++).toLabel())
                return@markAround r
            }

            Type.primitiveDouble -> {
                val r = HashSet<Double>()
                var p = 0
                while (!source.isEnd())
                    r += source.getDouble((p++).toLabel())
                return@markAround r
            }

            Type.primitiveChar -> {
                val r = HashSet<Char>()
                var p = 0
                while (!source.isEnd())
                    r += source.getChar((p++).toLabel())
                return@markAround r
            }

            Type.primitiveUnit -> {
                val r = HashSet<Unit>()
                var p = 0
                while (!source.isEnd())
                    r += source.getUnit((p++).toLabel())
                return@markAround r
            }

            Type.primitiveString -> {
                val r = HashSet<String>()
                var p = 0
                while (!source.isEnd())
                    r += source.getString((p++).toLabel())
                return@markAround r
            }

            else -> {
                if (type.arg.isTerminal()) {
                    // Resolve element funneler for terminal type
                    val sub = module.resolve<Any>(type.arg)

                    val r = HashSet<Any>()
                    var p = 0
                    while (!source.isEnd())
                        r += source.getTerminalNested(module, sub, (p++).toLabel(), type.arg)
                    return@markAround r
                } else {
                    val sub = module.resolve<Any>(type.arg)

                    val r = HashSet<Any>()
                    var p = 0
                    while (!source.isEnd())
                        r += source.getDynamicNested<Any>(module, sub, (p++).toLabel(), type.arg)
                    return@markAround r
                }
            }
        }
    }


    override suspend fun read(module: Module, type: Type, source: SuspendSource): Set<Any>
            = source.markAround(type) {

        // Try to find a faster resolution for primitive types
        @Suppress("unchecked_cast")
        when (type.arg.primitiveCode) {
            Type.primitiveBoolean -> {
                val r = HashSet<Boolean>()
                var p = 0
                while (!source.isEnd())
                    r += source.getBoolean((p++).toLabel())
                return@markAround r
            }

            Type.primitiveByte -> {
                val r = HashSet<Byte>()
                var p = 0
                while (!source.isEnd())
                    r += source.getByte((p++).toLabel())
                return@markAround r
            }


            Type.primitiveShort -> {
                val r = HashSet<Short>()
                var p = 0
                while (!source.isEnd())
                    r += source.getShort((p++).toLabel())
                return@markAround r
            }

            Type.primitiveInt -> {
                val r = HashSet<Int>()
                var p = 0
                while (!source.isEnd())
                    r += source.getInt((p++).toLabel())
                return@markAround r
            }

            Type.primitiveLong -> {
                val r = HashSet<Long>()
                var p = 0
                while (!source.isEnd())
                    r += source.getLong((p++).toLabel())
                return@markAround r
            }

            Type.primitiveFloat -> {
                val r = HashSet<Float>()
                var p = 0
                while (!source.isEnd())
                    r += source.getFloat((p++).toLabel())
                return@markAround r
            }

            Type.primitiveDouble -> {
                val r = HashSet<Double>()
                var p = 0
                while (!source.isEnd())
                    r += source.getDouble((p++).toLabel())
                return@markAround r
            }

            Type.primitiveChar -> {
                val r = HashSet<Char>()
                var p = 0
                while (!source.isEnd())
                    r += source.getChar((p++).toLabel())
                return@markAround r
            }

            Type.primitiveUnit -> {
                val r = HashSet<Unit>()
                var p = 0
                while (!source.isEnd())
                    r += source.getUnit((p++).toLabel())
                return@markAround r
            }

            Type.primitiveString -> {
                val r = HashSet<String>()
                var p = 0
                while (!source.isEnd())
                    r += source.getString((p++).toLabel())
                return@markAround r
            }

            else -> {
                if (type.arg.isTerminal()) {
                    // Resolve element funneler for terminal type
                    val sub = module.resolve<Any>(type.arg)

                    val r = HashSet<Any>()
                    var p = 0
                    while (!source.isEnd())
                        r += source.getTerminalNested(module, sub, (p++).toLabel(), type.arg)
                    return@markAround r
                } else {
                    val sub = module.resolve<Any>(type.arg)

                    val r = HashSet<Any>()
                    var p = 0
                    while (!source.isEnd())
                        r += source.getDynamicNested<Any>(module, sub, (p++).toLabel(), type.arg)
                    return@markAround r
                }
            }
        }
    }

    override fun write(module: Module, type: Type, sink: Sink, item: Set<Any>)
            = sink.markAround(type, item) {

        @Suppress("unchecked_cast")
        when (type.arg.primitiveCode) {
            Type.primitiveBoolean ->
                for ((i, it) in (item as Set<Boolean>).withIndex())
                    sink.putBoolean(i.toLabel(), it)

            Type.primitiveByte ->
                for ((i, it) in (item as Set<Byte>).withIndex())
                    sink.putByte(i.toLabel(), it)

            Type.primitiveShort ->
                for ((i, it) in (item as Set<Short>).withIndex())
                    sink.putShort(i.toLabel(), it)

            Type.primitiveInt ->
                for ((i, it) in (item as Set<Int>).withIndex())
                    sink.putInt(i.toLabel(), it)

            Type.primitiveLong ->
                for ((i, it) in (item as Set<Long>).withIndex())
                    sink.putLong(i.toLabel(), it)

            Type.primitiveFloat ->
                for ((i, it) in (item as Set<Float>).withIndex())
                    sink.putFloat(i.toLabel(), it)

            Type.primitiveDouble ->
                for ((i, it) in (item as Set<Double>).withIndex())
                    sink.putDouble(i.toLabel(), it)

            Type.primitiveChar ->
                for ((i, it) in (item as Set<Char>).withIndex())
                    sink.putChar(i.toLabel(), it)

            Type.primitiveUnit ->
                for ((i, it) in (item as Set<Unit>).withIndex())
                    sink.putUnit(i.toLabel(), it)

            Type.primitiveString ->
                for ((i, it) in (item as Set<String>).withIndex())
                    sink.putString(i.toLabel(), it)

            else -> {
                if (type.arg.isTerminal()) {
                    // Resolve element funneler for terminal type
                    val sub = module.resolve<Any>(type.arg)

                    // Write nested
                    for ((i, it) in item.withIndex())
                        sink.putTerminalNested(module, sub, i.toLabel(), type.arg, it)
                } else {
                    // Write nested
                    for ((i, it) in item.withIndex())
                        sink.putDynamicNested(module, i.toLabel(), type.arg, it)
                }
            }
        }
    }

    override suspend fun write(module: Module, type: Type, sink: SuspendSink, item: Set<Any>)
            = sink.markAround(type, item) {

        @Suppress("unchecked_cast")
        when (type.arg.primitiveCode) {
            Type.primitiveBoolean ->
                for ((i, it) in (item as Set<Boolean>).withIndex())
                    sink.putBoolean(i.toLabel(), it)

            Type.primitiveByte ->
                for ((i, it) in (item as Set<Byte>).withIndex())
                    sink.putByte(i.toLabel(), it)

            Type.primitiveShort ->
                for ((i, it) in (item as Set<Short>).withIndex())
                    sink.putShort(i.toLabel(), it)

            Type.primitiveInt ->
                for ((i, it) in (item as Set<Int>).withIndex())
                    sink.putInt(i.toLabel(), it)

            Type.primitiveLong ->
                for ((i, it) in (item as Set<Long>).withIndex())
                    sink.putLong(i.toLabel(), it)

            Type.primitiveFloat ->
                for ((i, it) in (item as Set<Float>).withIndex())
                    sink.putFloat(i.toLabel(), it)

            Type.primitiveDouble ->
                for ((i, it) in (item as Set<Double>).withIndex())
                    sink.putDouble(i.toLabel(), it)

            Type.primitiveChar ->
                for ((i, it) in (item as Set<Char>).withIndex())
                    sink.putChar(i.toLabel(), it)

            Type.primitiveUnit ->
                for ((i, it) in (item as Set<Unit>).withIndex())
                    sink.putUnit(i.toLabel(), it)

            Type.primitiveString ->
                for ((i, it) in (item as Set<String>).withIndex())
                    sink.putString(i.toLabel(), it)

            else -> {
                if (type.arg.isTerminal()) {
                    // Resolve element funneler for terminal type
                    val sub = module.resolve<Any>(type.arg)

                    // Write nested
                    for ((i, it) in item.withIndex())
                        sink.putTerminalNested(module, sub, i.toLabel(), type.arg, it)
                } else {
                    // Write nested
                    for ((i, it) in item.withIndex())
                        sink.putDynamicNested(module, i.toLabel(), type.arg, it)
                }
            }
        }
    }
}