package eu.metatools.kfunnels.base.sets

import eu.metatools.kfunnels.*

class SetFunneler(val type: Type) : Funneler<Set<Any>> {
    /**
     * Computes the positional label for an item.
     */
    private fun Int.toLabel() = "item$this"

    override fun read(module: Module, source: SeqSource): Set<Any> {
        // Length is always needed
        val length = source.getInt()

        // Try to find a faster resolution for primitive types
        @Suppress("unchecked_cast")
        when (type.primitiveCode) {
            Type.primitiveBoolean ->
                return (1..length).map { source.getBoolean() }.toSet()

            Type.primitiveByte ->
                return (1..length).map { source.getByte() }.toSet()

            Type.primitiveShort ->
                return (1..length).map { source.getShort() }.toSet()

            Type.primitiveInt ->
                return (1..length).map { source.getInt() }.toSet()

            Type.primitiveLong ->
                return (1..length).map { source.getLong() }.toSet()

            Type.primitiveFloat ->
                return (1..length).map { source.getFloat() }.toSet()

            Type.primitiveDouble ->
                return (1..length).map { source.getDouble() }.toSet()

            Type.primitiveChar ->
                return (1..length).map { source.getChar() }.toSet()

            Type.primitiveUnit ->
                return (1..length).map { source.getUnit() }.toSet()

            Type.primitiveString ->
                return (1..length).map { source.getString() }.toSet()

            else -> {
                // Resolve element funneler
                val sub = module.resolve<Any>(type)

                // Read as nested elements
                return (1..length).map {
                    source.beginNested()
                    val result = sub.read(module, source)
                    source.endNested()
                    result
                }.toSet()
            }
        }

    }

    override fun read(module: Module, source: LabelSource): Set<Any> {
        // Length is always needed
        val length = source.getInt("length")

        // Try to find a faster resolution for primitive types
        @Suppress("unchecked_cast")
        when (type.primitiveCode) {
            Type.primitiveBoolean ->
                return (1..length).map { source.getBoolean(it.toLabel()) }.toSet()

            Type.primitiveByte ->
                return (1..length).map { source.getByte(it.toLabel()) }.toSet()

            Type.primitiveShort ->
                return (1..length).map { source.getShort(it.toLabel()) }.toSet()

            Type.primitiveInt ->
                return (1..length).map { source.getInt(it.toLabel()) }.toSet()

            Type.primitiveLong ->
                return (1..length).map { source.getLong(it.toLabel()) }.toSet()

            Type.primitiveFloat ->
                return (1..length).map { source.getFloat(it.toLabel()) }.toSet()

            Type.primitiveDouble ->
                return (1..length).map { source.getDouble(it.toLabel()) }.toSet()

            Type.primitiveChar ->
                return (1..length).map { source.getChar(it.toLabel()) }.toSet()

            Type.primitiveUnit ->
                return (1..length).map { source.getUnit(it.toLabel()) }.toSet()

            Type.primitiveString ->
                return (1..length).map { source.getString(it.toLabel()) }.toSet()

            else -> {
                // Resolve element funneler
                val sub = module.resolve<Any>(type)

                // Read as nested elements
                return (1..length).map {
                    source.beginNested(it.toLabel())
                    val result = sub.read(module, source)
                    source.endNested()
                    result
                }.toSet()
            }
        }
    }

    override fun write(module: Module, sink: SeqSink, item: Set<Any>) {

        sink.putInt(item.size)

        @Suppress("unchecked_cast")
        when (type.primitiveCode) {
            Type.primitiveBoolean ->
                for (it in item as Set<Boolean>)
                    sink.putBoolean(it)

            Type.primitiveByte ->
                for (it in item as Set<Byte>)
                    sink.putByte(it)

            Type.primitiveShort ->
                for (it in item as Set<Short>)
                    sink.putShort(it)

            Type.primitiveInt ->
                for (it in item as Set<Int>)
                    sink.putInt(it)

            Type.primitiveLong ->
                for (it in item as Set<Long>)
                    sink.putLong(it)

            Type.primitiveFloat ->
                for (it in item as Set<Float>)
                    sink.putFloat(it)

            Type.primitiveDouble ->
                for (it in item as Set<Double>)
                    sink.putDouble(it)

            Type.primitiveChar ->
                for (it in item as Set<Char>)
                    sink.putChar(it)

            Type.primitiveUnit ->
                for (it in item as Set<Unit>)
                    sink.putUnit(it)

            Type.primitiveString ->
                for (it in item as Set<String>)
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

    override fun write(module: Module, sink: LabelSink, item: Set<Any>) {

        sink.putInt("length", item.size)

        @Suppress("unchecked_cast")
        when (type.primitiveCode) {
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