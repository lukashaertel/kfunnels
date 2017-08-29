package eu.metatools.kfunnels.base.lists

import eu.metatools.kfunnels.*

class ListFunneler(val type: Type) : Funneler<List<Any>> {
    /**
     * Computes the positional label for an item.
     */
    private fun Int.toLabel() = "item$this"

    override fun read(module: Module, source: SeqSource): List<Any> {
        // Length is always needed
        val length = source.getInt()

        // Try to find a faster resolution for primitive types
        @Suppress("unchecked_cast")
        when (type.primitiveCode) {
            Type.primitiveBoolean ->
                return (1..length).map { source.getBoolean() }

            Type.primitiveByte ->
                return (1..length).map { source.getByte() }

            Type.primitiveShort ->
                return (1..length).map { source.getShort() }

            Type.primitiveInt ->
                return (1..length).map { source.getInt() }

            Type.primitiveLong ->
                return (1..length).map { source.getLong() }

            Type.primitiveFloat ->
                return (1..length).map { source.getFloat() }

            Type.primitiveDouble ->
                return (1..length).map { source.getDouble() }

            Type.primitiveChar ->
                return (1..length).map { source.getChar() }

            Type.primitiveUnit ->
                return (1..length).map { source.getUnit() }

            Type.primitiveString ->
                return (1..length).map { source.getString() }

            else -> {
                // Resolve element funneler
                val sub = module.resolve<Any>(type)

                // Read as nested elements
                return (1..length).map {
                    source.beginNested()
                    val result = sub.read(module, source)
                    source.endNested()
                    result
                }
            }
        }

    }

    override fun read(module: Module, source: LabelSource): List<Any> {
        // Length is always needed
        val length = source.getInt("length")

        // Try to find a faster resolution for primitive types
        @Suppress("unchecked_cast")
        when (type.primitiveCode) {
            Type.primitiveBoolean ->
                return (1..length).map { source.getBoolean(it.toLabel()) }

            Type.primitiveByte ->
                return (1..length).map { source.getByte(it.toLabel()) }

            Type.primitiveShort ->
                return (1..length).map { source.getShort(it.toLabel()) }

            Type.primitiveInt ->
                return (1..length).map { source.getInt(it.toLabel()) }

            Type.primitiveLong ->
                return (1..length).map { source.getLong(it.toLabel()) }

            Type.primitiveFloat ->
                return (1..length).map { source.getFloat(it.toLabel()) }

            Type.primitiveDouble ->
                return (1..length).map { source.getDouble(it.toLabel()) }

            Type.primitiveChar ->
                return (1..length).map { source.getChar(it.toLabel()) }

            Type.primitiveUnit ->
                return (1..length).map { source.getUnit(it.toLabel()) }

            Type.primitiveString ->
                return (1..length).map { source.getString(it.toLabel()) }

            else -> {
                // Resolve element funneler
                val sub = module.resolve<Any>(type)

                // Read as nested elements
                return (1..length).map {
                    source.beginNested(it.toLabel())
                    val result = sub.read(module, source)
                    source.endNested()
                    result
                }
            }
        }
    }

    override fun write(module: Module, sink: SeqSink, item: List<Any>) {

        sink.putInt(item.size)

        @Suppress("unchecked_cast")
        when (type.primitiveCode) {
            Type.primitiveBoolean ->
                for (it in item as List<Boolean>)
                    sink.putBoolean(it)

            Type.primitiveByte ->
                for (it in item as List<Byte>)
                    sink.putByte(it)

            Type.primitiveShort ->
                for (it in item as List<Short>)
                    sink.putShort(it)

            Type.primitiveInt ->
                for (it in item as List<Int>)
                    sink.putInt(it)

            Type.primitiveLong ->
                for (it in item as List<Long>)
                    sink.putLong(it)

            Type.primitiveFloat ->
                for (it in item as List<Float>)
                    sink.putFloat(it)

            Type.primitiveDouble ->
                for (it in item as List<Double>)
                    sink.putDouble(it)

            Type.primitiveChar ->
                for (it in item as List<Char>)
                    sink.putChar(it)

            Type.primitiveUnit ->
                for (it in item as List<Unit>)
                    sink.putUnit(it)

            Type.primitiveString ->
                for (it in item as List<String>)
                    sink.putString(it)

            else -> {
                // Resolve element funneler
                val sub = module.resolve<Any>(type)

                // Write nested
                for (it in item) {
                    sink.beginNested()
                    sub.write(module, sink, it)
                    sink.endNested()
                }
            }
        }
    }

    override fun write(module: Module, sink: LabelSink, item: List<Any>) {

        sink.putInt("length", item.size)

        @Suppress("unchecked_cast")
        when (type.primitiveCode) {
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
                val sub = module.resolve<Any>(type)

                // Write nested
                for ((i, it) in item.withIndex()) {
                    sink.beginNested(i.toLabel())
                    sub.write(module, sink, it)
                    sink.endNested()
                }
            }
        }
    }

}