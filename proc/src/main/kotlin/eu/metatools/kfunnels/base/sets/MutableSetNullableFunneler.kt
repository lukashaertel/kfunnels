package eu.metatools.kfunnels.base.sets

import eu.metatools.kfunnels.*

class MutableSetNullableFunneler(val type: Type, val create: () -> MutableSet<Any?>) : Funneler<MutableSet<Any?>> {
    /**
     * Computes the positional label for an item.
     */
    private fun Int.toLabel() = "item$this"

    override fun read(module: Module, source: SeqSource): MutableSet<Any?> {
        // Length is always needed
        val length = source.getInt()

        // Try to find a faster resolution for primitive types
        @Suppress("unchecked_cast")
        when (type.primitiveCode) {
            Type.primitiveBoolean -> {
                val r = create() as MutableSet<Boolean?>
                repeat(length) { r += if (source.isNull()) null else source.getBoolean() }
                return r as MutableSet<Any?>
            }

            Type.primitiveShort -> {
                val r = create() as MutableSet<Short?>
                repeat(length) { r += if (source.isNull()) null else source.getShort() }
                return r as MutableSet<Any?>
            }

            Type.primitiveInt -> {
                val r = create() as MutableSet<Int?>
                repeat(length) { r += if (source.isNull()) null else source.getInt() }
                return r as MutableSet<Any?>
            }

            Type.primitiveLong -> {
                val r = create() as MutableSet<Long?>
                repeat(length) { r += if (source.isNull()) null else source.getLong() }
                return r as MutableSet<Any?>
            }

            Type.primitiveFloat -> {
                val r = create() as MutableSet<Float?>
                repeat(length) { r += if (source.isNull()) null else source.getFloat() }
                return r as MutableSet<Any?>
            }

            Type.primitiveDouble -> {
                val r = create() as MutableSet<Double?>
                repeat(length) { r += if (source.isNull()) null else source.getDouble() }
                return r as MutableSet<Any?>
            }

            Type.primitiveChar -> {
                val r = create() as MutableSet<Char?>
                repeat(length) { r += if (source.isNull()) null else source.getChar() }
                return r as MutableSet<Any?>
            }

            Type.primitiveUnit -> {
                val r = create() as MutableSet<Unit?>
                repeat(length) { r += if (source.isNull()) null else source.getUnit() }
                return r as MutableSet<Any?>
            }

            Type.primitiveString -> {
                val r = create() as MutableSet<String?>
                repeat(length) { r += if (source.isNull()) null else source.getString() }
                return r as MutableSet<Any?>
            }

            else -> {
                // Resolve element funneler
                val sub = module.resolve<Any>(type)

                // Read as nested elements into mutable Set
                val r = create()
                repeat(length) {
                    if (source.isNull())
                        r.add(null)
                    else {
                        source.beginNested()
                        r += sub.read(module, source)
                        source.endNested()
                    }
                }
                return r
            }
        }
    }

    override fun read(module: Module, source: LabelSource): MutableSet<Any?> {
        // Length is always needed
        val length = source.getInt("length")

        // Try to find a faster resolution for primitive types
        @Suppress("unchecked_cast")
        when (type.primitiveCode) {
            Type.primitiveBoolean -> {
                val r = create() as MutableSet<Boolean?>
                repeat(length) { r += if (source.isNull(it.toLabel())) null else source.getBoolean(it.toLabel()) }
                return r as MutableSet<Any?>
            }

            Type.primitiveShort -> {
                val r = create() as MutableSet<Short?>
                repeat(length) { r += if (source.isNull(it.toLabel())) null else source.getShort(it.toLabel()) }
                return r as MutableSet<Any?>
            }

            Type.primitiveInt -> {
                val r = create() as MutableSet<Int?>
                repeat(length) { r += if (source.isNull(it.toLabel())) null else source.getInt(it.toLabel()) }
                return r as MutableSet<Any?>
            }

            Type.primitiveLong -> {
                val r = create() as MutableSet<Long?>
                repeat(length) { r += if (source.isNull(it.toLabel())) null else source.getLong(it.toLabel()) }
                return r as MutableSet<Any?>
            }

            Type.primitiveFloat -> {
                val r = create() as MutableSet<Float?>
                repeat(length) { r += if (source.isNull(it.toLabel())) null else source.getFloat(it.toLabel()) }
                return r as MutableSet<Any?>
            }

            Type.primitiveDouble -> {
                val r = create() as MutableSet<Double?>
                repeat(length) { r += if (source.isNull(it.toLabel())) null else source.getDouble(it.toLabel()) }
                return r as MutableSet<Any?>
            }

            Type.primitiveChar -> {
                val r = create() as MutableSet<Char?>
                repeat(length) { r += if (source.isNull(it.toLabel())) null else source.getChar(it.toLabel()) }
                return r as MutableSet<Any?>
            }

            Type.primitiveUnit -> {
                val r = create() as MutableSet<Unit?>
                repeat(length) { r += if (source.isNull(it.toLabel())) null else source.getUnit(it.toLabel()) }
                return r as MutableSet<Any?>
            }

            Type.primitiveString -> {
                val r = create() as MutableSet<String?>
                repeat(length) { r += if (source.isNull(it.toLabel())) null else source.getString(it.toLabel()) }
                return r as MutableSet<Any?>
            }

            else -> {
                // Resolve element funneler
                val sub = module.resolve<Any>(type)

                // Read as nested elements into mutable Set
                val r = create()
                repeat(length) {
                    if (source.isNull(it.toLabel()))
                        r.add(null)
                    else {
                        source.beginNested(it.toLabel())
                        r += sub.read(module, source)
                        source.endNested()
                    }
                }
                return r
            }
        }
    }

    override fun write(module: Module, sink: SeqSink, item: MutableSet<Any?>) {

        sink.putInt(item.size)

        @Suppress("unchecked_cast")
        when (type.primitiveCode) {
            Type.primitiveBoolean ->
                for (it in item as Set<Boolean?>)
                    if (it == null)
                        sink.putNull(true)
                    else {
                        sink.putNull(false)
                        sink.putBoolean(it)
                    }

            Type.primitiveByte ->
                for (it in item as Set<Byte?>)
                    if (it == null)
                        sink.putNull(true)
                    else {
                        sink.putNull(false)
                        sink.putByte(it)
                    }

            Type.primitiveShort ->
                for (it in item as Set<Short?>)
                    if (it == null)
                        sink.putNull(true)
                    else {
                        sink.putNull(false)
                        sink.putShort(it)
                    }

            Type.primitiveInt ->
                for (it in item as Set<Int?>)
                    if (it == null)
                        sink.putNull(true)
                    else {
                        sink.putNull(false)
                        sink.putInt(it)
                    }

            Type.primitiveLong ->
                for (it in item as Set<Long?>)
                    if (it == null)
                        sink.putNull(true)
                    else {
                        sink.putNull(false)
                        sink.putLong(it)
                    }

            Type.primitiveFloat ->
                for (it in item as Set<Float?>)
                    if (it == null)
                        sink.putNull(true)
                    else {
                        sink.putNull(false)
                        sink.putFloat(it)
                    }

            Type.primitiveDouble ->
                for (it in item as Set<Double?>)
                    if (it == null)
                        sink.putNull(true)
                    else {
                        sink.putNull(false)
                        sink.putDouble(it)
                    }

            Type.primitiveChar ->
                for (it in item as Set<Char?>)
                    if (it == null)
                        sink.putNull(true)
                    else {
                        sink.putNull(false)
                        sink.putChar(it)
                    }

            Type.primitiveUnit ->
                for (it in item as Set<Unit?>)
                    if (it == null)
                        sink.putNull(true)
                    else {
                        sink.putNull(false)
                        sink.putUnit(it)
                    }

            Type.primitiveString ->
                for (it in item as Set<String?>)
                    if (it == null)
                        sink.putNull(true)
                    else {
                        sink.putNull(false)
                        sink.putString(it)
                    }

            else -> {
                // Resolve element funneler
                val sub = module.resolve<Any>(type)

                // Write nested
                for (it in item)
                    if (it == null)
                        sink.putNull(true)
                    else {
                        sink.putNull(false)
                        sink.beginNested()
                        sub.write(module, sink, it)
                        sink.endNested()
                    }
            }
        }
    }

    override fun write(module: Module, sink: LabelSink, item: MutableSet<Any?>) {

        sink.putInt("length", item.size)

        @Suppress("unchecked_cast")
        when (type.primitiveCode) {
            Type.primitiveBoolean ->
                for ((i, it) in (item as Set<Boolean?>).withIndex())
                    if (it == null)
                        sink.putNull(i.toLabel(), true)
                    else {
                        sink.putNull(i.toLabel(), false)
                        sink.putBoolean(i.toLabel(), it)
                    }

            Type.primitiveByte ->
                for ((i, it) in (item as Set<Byte?>).withIndex())
                    if (it == null)
                        sink.putNull(i.toLabel(), true)
                    else {
                        sink.putNull(i.toLabel(), false)
                        sink.putByte(i.toLabel(), it)
                    }

            Type.primitiveShort ->
                for ((i, it) in (item as Set<Short?>).withIndex())
                    if (it == null)
                        sink.putNull(i.toLabel(), true)
                    else {
                        sink.putNull(i.toLabel(), false)
                        sink.putShort(i.toLabel(), it)
                    }

            Type.primitiveInt ->
                for ((i, it) in (item as Set<Int?>).withIndex())
                    if (it == null)
                        sink.putNull(i.toLabel(), true)
                    else {
                        sink.putNull(i.toLabel(), false)
                        sink.putInt(i.toLabel(), it)
                    }

            Type.primitiveLong ->
                for ((i, it) in (item as Set<Long?>).withIndex())
                    if (it == null)
                        sink.putNull(i.toLabel(), true)
                    else {
                        sink.putNull(i.toLabel(), false)
                        sink.putLong(i.toLabel(), it)
                    }

            Type.primitiveFloat ->
                for ((i, it) in (item as Set<Float?>).withIndex())
                    if (it == null)
                        sink.putNull(i.toLabel(), true)
                    else {
                        sink.putNull(i.toLabel(), false)
                        sink.putFloat(i.toLabel(), it)
                    }

            Type.primitiveDouble ->
                for ((i, it) in (item as Set<Double?>).withIndex())
                    if (it == null)
                        sink.putNull(i.toLabel(), true)
                    else {
                        sink.putNull(i.toLabel(), false)
                        sink.putDouble(i.toLabel(), it)
                    }

            Type.primitiveChar ->
                for ((i, it) in (item as Set<Char?>).withIndex())
                    if (it == null)
                        sink.putNull(i.toLabel(), true)
                    else {
                        sink.putNull(i.toLabel(), false)
                        sink.putChar(i.toLabel(), it)
                    }

            Type.primitiveUnit ->
                for ((i, it) in (item as Set<Unit?>).withIndex())
                    if (it == null)
                        sink.putNull(i.toLabel(), true)
                    else {
                        sink.putNull(i.toLabel(), false)
                        sink.putUnit(i.toLabel(), it)
                    }

            Type.primitiveString ->
                for ((i, it) in (item as Set<String?>).withIndex())
                    if (it == null)
                        sink.putNull(i.toLabel(), true)
                    else {
                        sink.putNull(i.toLabel(), false)
                        sink.putString(i.toLabel(), it)
                    }

            else -> {
                // Resolve element funneler
                val sub = module.resolve<Any>(type)

                // Write nested
                for ((i, it) in item.withIndex())
                    if (it == null)
                        sink.putNull(i.toLabel(), true)
                    else {
                        sink.putNull(i.toLabel(), false)
                        sink.beginNested(i.toLabel())
                        sub.write(module, sink, it)
                        sink.endNested()
                    }
            }
        }
    }
}