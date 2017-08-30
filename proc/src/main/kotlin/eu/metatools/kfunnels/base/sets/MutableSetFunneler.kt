package eu.metatools.kfunnels.base.sets

import eu.metatools.kfunnels.*

class MutableSetFunneler(val create: () -> MutableSet<Any>) : Funneler<MutableSet<Any>> {
    /**
     * Computes the positional label for an item.
     */
    private fun Int.toLabel() = "item$this"

    override fun read(module: Module, type: Type, source: SeqSource): MutableSet<Any> = source.markAround(type) {
        // Length is always needed
        val length = source.getInt()

        // Try to find a faster resolution for primitive types
        @Suppress("unchecked_cast")
        when (type.arg.primitiveCode) {
            Type.primitiveBoolean -> {
                val r = create() as MutableSet<Boolean>
                repeat(length) { r += source.getBoolean() }
                return r as MutableSet<Any>
            }

            Type.primitiveShort -> {
                val r = create() as MutableSet<Short>
                repeat(length) { r += source.getShort() }
                return r as MutableSet<Any>
            }

            Type.primitiveInt -> {
                val r = create() as MutableSet<Int>
                repeat(length) { r += source.getInt() }
                return r as MutableSet<Any>
            }

            Type.primitiveLong -> {
                val r = create() as MutableSet<Long>
                repeat(length) { r += source.getLong() }
                return r as MutableSet<Any>
            }

            Type.primitiveFloat -> {
                val r = create() as MutableSet<Float>
                repeat(length) { r += source.getFloat() }
                return r as MutableSet<Any>
            }

            Type.primitiveDouble -> {
                val r = create() as MutableSet<Double>
                repeat(length) { r += source.getDouble() }
                return r as MutableSet<Any>
            }

            Type.primitiveChar -> {
                val r = create() as MutableSet<Char>
                repeat(length) { r += source.getChar() }
                return r as MutableSet<Any>
            }

            Type.primitiveUnit -> {
                val r = create() as MutableSet<Unit>
                repeat(length) { r += source.getUnit() }
                return r as MutableSet<Any>
            }

            Type.primitiveString -> {
                val r = create() as MutableSet<String>
                repeat(length) { r += source.getString() }
                return r as MutableSet<Any>
            }

            else -> {
                // Resolve element funneler
                val sub = module.resolve<Any>(type.arg)

                // Read as nested elements into mutable Set
                val r = create()
                repeat(length) {
                    source.beginNested()
                    r += sub.read(module, type.arg, source)
                    source.endNested()
                }
                return r
            }
        }
    }

    override fun read(module: Module, type: Type, source: LabelSource): MutableSet<Any> = source.markAround(type) {
        // Length is always needed
        val length = source.getInt("length")

        // Try to find a faster resolution for primitive types
        @Suppress("unchecked_cast")
        when (type.arg.primitiveCode) {
            Type.primitiveBoolean -> {
                val r = create() as MutableSet<Boolean>
                repeat(length) { r += source.getBoolean(it.toLabel()) }
                return r as MutableSet<Any>
            }

            Type.primitiveShort -> {
                val r = create() as MutableSet<Short>
                repeat(length) { r += source.getShort(it.toLabel()) }
                return r as MutableSet<Any>
            }

            Type.primitiveInt -> {
                val r = create() as MutableSet<Int>
                repeat(length) { r += source.getInt(it.toLabel()) }
                return r as MutableSet<Any>
            }

            Type.primitiveLong -> {
                val r = create() as MutableSet<Long>
                repeat(length) { r += source.getLong(it.toLabel()) }
                return r as MutableSet<Any>
            }

            Type.primitiveFloat -> {
                val r = create() as MutableSet<Float>
                repeat(length) { r += source.getFloat(it.toLabel()) }
                return r as MutableSet<Any>
            }

            Type.primitiveDouble -> {
                val r = create() as MutableSet<Double>
                repeat(length) { r += source.getDouble(it.toLabel()) }
                return r as MutableSet<Any>
            }

            Type.primitiveChar -> {
                val r = create() as MutableSet<Char>
                repeat(length) { r += source.getChar(it.toLabel()) }
                return r as MutableSet<Any>
            }

            Type.primitiveUnit -> {
                val r = create() as MutableSet<Unit>
                repeat(length) { r += source.getUnit(it.toLabel()) }
                return r as MutableSet<Any>
            }

            Type.primitiveString -> {
                val r = create() as MutableSet<String>
                repeat(length) { r += source.getString(it.toLabel()) }
                return r as MutableSet<Any>
            }

            else -> {
                // Resolve element funneler
                val sub = module.resolve<Any>(type.arg)

                // Read as nested elements into mutable Set
                val r = create()
                repeat(length) {
                    source.beginNested(it.toLabel())
                    r += sub.read(module, type.arg, source)
                    source.endNested(it.toLabel())
                }
                return r
            }
        }
    }

    override fun write(module: Module, type: Type, sink: SeqSink, item: MutableSet<Any>) = sink.markAround(type) {

        sink.putInt(item.size)

        @Suppress("unchecked_cast")
        when (type.arg.primitiveCode) {
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
                val sub = module.resolve<Any>(type.arg)

                // Write nested
                for (it in item) {
                    sink.beginNested()
                    sub.write(module, type.arg, sink, it)
                    sink.endNested()
                }
            }
        }
    }

    override fun write(module: Module, type: Type, sink: LabelSink, item: MutableSet<Any>) = sink.markAround(type) {

        sink.putInt("length", item.size)

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
                // Resolve element funneler
                val sub = module.resolve<Any>(type.arg)

                // Write nested
                for ((i, it) in item.withIndex()) {
                    sink.beginNested(i.toLabel())
                    sub.write(module, type.arg, sink, it)
                    sink.endNested(i.toLabel())
                }
            }
        }
    }
}