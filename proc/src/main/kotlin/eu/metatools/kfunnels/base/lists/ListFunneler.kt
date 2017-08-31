package eu.metatools.kfunnels.base.lists

import eu.metatools.kfunnels.*

object ListFunneler : Funneler<List<Any>> {
    /**
     * Computes the positional label for an item.
     */
    private fun Int.toLabel() = "item$this"

    override fun read(module: Module, type: Type, source: Source): List<Any>
            = source.markAround(type) {

        // Try to find a faster resolution for primitive types
        @Suppress("unchecked_cast")
        when (type.arg.primitiveCode) {
            Type.primitiveBoolean -> {
                val r = ArrayList<Boolean>()
                var p = 0
                while (!source.isEnd())
                    r += source.getBoolean((p++).toLabel())
                return r
            }

            Type.primitiveByte -> {
                val r = ArrayList<Byte>()
                var p = 0
                while (!source.isEnd())
                    r += source.getByte((p++).toLabel())
                return r
            }


            Type.primitiveShort -> {
                val r = ArrayList<Short>()
                var p = 0
                while (!source.isEnd())
                    r += source.getShort((p++).toLabel())
                return r
            }

            Type.primitiveInt -> {
                val r = ArrayList<Int>()
                var p = 0
                while (!source.isEnd())
                    r += source.getInt((p++).toLabel())
                return r
            }

            Type.primitiveLong -> {
                val r = ArrayList<Long>()
                var p = 0
                while (!source.isEnd())
                    r += source.getLong((p++).toLabel())
                return r
            }

            Type.primitiveFloat -> {
                val r = ArrayList<Float>()
                var p = 0
                while (!source.isEnd())
                    r += source.getFloat((p++).toLabel())
                return r
            }

            Type.primitiveDouble -> {
                val r = ArrayList<Double>()
                var p = 0
                while (!source.isEnd())
                    r += source.getDouble((p++).toLabel())
                return r
            }

            Type.primitiveChar -> {
                val r = ArrayList<Char>()
                var p = 0
                while (!source.isEnd())
                    r += source.getChar((p++).toLabel())
                return r
            }

            Type.primitiveUnit -> {
                val r = ArrayList<Unit>()
                var p = 0
                while (!source.isEnd())
                    r += source.getUnit((p++).toLabel())
                return r
            }

            Type.primitiveString -> {
                val r = ArrayList<String>()
                var p = 0
                while (!source.isEnd())
                    r += source.getString((p++).toLabel())
                return r
            }

            else -> {
                if (type.arg.isTerminal()) {
                    // Resolve element funneler for terminal type
                    val sub = module.resolve<Any>(type.arg)

                    val r = ArrayList<Any>()
                    var p = 0
                    while (!source.isEnd()) {
                        source.beginNested(p.toLabel(), type.arg)
                        r += sub.read(module, type.arg, source)
                        source.endNested((p++).toLabel(), type.arg)
                    }
                    return r
                } else {
                    val r = ArrayList<Any>()
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


    override suspend fun read(module: Module, type: Type, source: SuspendSource): List<Any>
            = source.markAround(type) {

        // Try to find a faster resolution for primitive types
        @Suppress("unchecked_cast")
        when (type.arg.primitiveCode) {
            Type.primitiveBoolean -> {
                val r = ArrayList<Boolean>()
                var p = 0
                while (!source.isEnd())
                    r += source.getBoolean((p++).toLabel())
                return r
            }

            Type.primitiveByte -> {
                val r = ArrayList<Byte>()
                var p = 0
                while (!source.isEnd())
                    r += source.getByte((p++).toLabel())
                return r
            }


            Type.primitiveShort -> {
                val r = ArrayList<Short>()
                var p = 0
                while (!source.isEnd())
                    r += source.getShort((p++).toLabel())
                return r
            }

            Type.primitiveInt -> {
                val r = ArrayList<Int>()
                var p = 0
                while (!source.isEnd())
                    r += source.getInt((p++).toLabel())
                return r
            }

            Type.primitiveLong -> {
                val r = ArrayList<Long>()
                var p = 0
                while (!source.isEnd())
                    r += source.getLong((p++).toLabel())
                return r
            }

            Type.primitiveFloat -> {
                val r = ArrayList<Float>()
                var p = 0
                while (!source.isEnd())
                    r += source.getFloat((p++).toLabel())
                return r
            }

            Type.primitiveDouble -> {
                val r = ArrayList<Double>()
                var p = 0
                while (!source.isEnd())
                    r += source.getDouble((p++).toLabel())
                return r
            }

            Type.primitiveChar -> {
                val r = ArrayList<Char>()
                var p = 0
                while (!source.isEnd())
                    r += source.getChar((p++).toLabel())
                return r
            }

            Type.primitiveUnit -> {
                val r = ArrayList<Unit>()
                var p = 0
                while (!source.isEnd())
                    r += source.getUnit((p++).toLabel())
                return r
            }

            Type.primitiveString -> {
                val r = ArrayList<String>()
                var p = 0
                while (!source.isEnd())
                    r += source.getString((p++).toLabel())
                return r
            }

            else -> {
                if (type.arg.isTerminal()) {
                    // Resolve element funneler for terminal type
                    val sub = module.resolve<Any>(type.arg)

                    val r = ArrayList<Any>()
                    var p = 0
                    while (!source.isEnd()) {
                        source.beginNested(p.toLabel(), type.arg)
                        r += sub.read(module, type.arg, source)
                        source.endNested((p++).toLabel(), type.arg)
                    }
                    return r
                } else {
                    val r = ArrayList<Any>()
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

    override fun write(module: Module, type: Type, sink: Sink, item: List<Any>)
            = sink.markAround(type) {

        @Suppress("unchecked_cast")
        when (type.arg.primitiveCode) {
            Type.primitiveBoolean ->
                for ((i, it) in (item as List<Boolean>).withIndex())
                    sink.putBoolean(i.toLabel(), it)

            Type.primitiveByte ->
                for ((i, it) in (item as List<Byte>).withIndex())
                    sink.putByte(i.toLabel(), it)

            Type.primitiveShort ->
                for ((i, it) in (item as List<Short>).withIndex())
                    sink.putShort(i.toLabel(), it)

            Type.primitiveInt ->
                for ((i, it) in (item as List<Int>).withIndex())
                    sink.putInt(i.toLabel(), it)

            Type.primitiveLong ->
                for ((i, it) in (item as List<Long>).withIndex())
                    sink.putLong(i.toLabel(), it)

            Type.primitiveFloat ->
                for ((i, it) in (item as List<Float>).withIndex())
                    sink.putFloat(i.toLabel(), it)

            Type.primitiveDouble ->
                for ((i, it) in (item as List<Double>).withIndex())
                    sink.putDouble(i.toLabel(), it)

            Type.primitiveChar ->
                for ((i, it) in (item as List<Char>).withIndex())
                    sink.putChar(i.toLabel(), it)

            Type.primitiveUnit ->
                for ((i, it) in (item as List<Unit>).withIndex())
                    sink.putUnit(i.toLabel(), it)

            Type.primitiveString ->
                for ((i, it) in (item as List<String>).withIndex())
                    sink.putString(i.toLabel(), it)

            else -> {
                // Resolve element funneler
                val sub = module.resolve<Any>(type.arg)

                // Write nested
                for ((i, it) in item.withIndex()) {
                    sink.beginNested(i.toLabel(), type.arg, it)
                    sub.write(module, type.arg, sink, it)
                    sink.endNested(i.toLabel(), type.arg, it)
                }
            }
        }
    }

    override suspend fun write(module: Module, type: Type, sink: SuspendSink, item: List<Any>)
            = sink.markAround(type) {

        @Suppress("unchecked_cast")
        when (type.arg.primitiveCode) {
            Type.primitiveBoolean ->
                for ((i, it) in (item as List<Boolean>).withIndex())
                    sink.putBoolean(i.toLabel(), it)

            Type.primitiveByte ->
                for ((i, it) in (item as List<Byte>).withIndex())
                    sink.putByte(i.toLabel(), it)

            Type.primitiveShort ->
                for ((i, it) in (item as List<Short>).withIndex())
                    sink.putShort(i.toLabel(), it)

            Type.primitiveInt ->
                for ((i, it) in (item as List<Int>).withIndex())
                    sink.putInt(i.toLabel(), it)

            Type.primitiveLong ->
                for ((i, it) in (item as List<Long>).withIndex())
                    sink.putLong(i.toLabel(), it)

            Type.primitiveFloat ->
                for ((i, it) in (item as List<Float>).withIndex())
                    sink.putFloat(i.toLabel(), it)

            Type.primitiveDouble ->
                for ((i, it) in (item as List<Double>).withIndex())
                    sink.putDouble(i.toLabel(), it)

            Type.primitiveChar ->
                for ((i, it) in (item as List<Char>).withIndex())
                    sink.putChar(i.toLabel(), it)

            Type.primitiveUnit ->
                for ((i, it) in (item as List<Unit>).withIndex())
                    sink.putUnit(i.toLabel(), it)

            Type.primitiveString ->
                for ((i, it) in (item as List<String>).withIndex())
                    sink.putString(i.toLabel(), it)

            else -> {
                // Resolve element funneler
                val sub = module.resolve<Any>(type.arg)

                // Write nested
                for ((i, it) in item.withIndex()) {
                    sink.beginNested(i.toLabel(), type.arg, it)
                    sub.write(module, type.arg, sink, it)
                    sink.endNested(i.toLabel(), type.arg, it)
                }
            }
        }
    }
}