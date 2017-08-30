package eu.metatools.kfunnels.base.lists

import eu.metatools.kfunnels.*

class MutableListNullableFunneler(val create: () -> MutableList<Any?>) : Funneler<MutableList<Any?>> {
    /**
     * Computes the positional label for an item.
     */
    private fun Int.toLabel() = "item$this"

    override fun read(module: Module, type: Type, source: SeqSource): MutableList<Any?> = source.markAround(type) {

        // Length is always needed
        val length = source.getInt()

        // Try to find a faster resolution for primitive types
        @Suppress("unchecked_cast")
        when (type.arg.primitiveCode) {
            Type.primitiveBoolean -> {
                val r = create() as MutableList<Boolean?>
                repeat(length) { r += if (source.isNull()) null else source.getBoolean() }
                return r as MutableList<Any?>
            }

            Type.primitiveShort -> {
                val r = create() as MutableList<Short?>
                repeat(length) { r += if (source.isNull()) null else source.getShort() }
                return r as MutableList<Any?>
            }

            Type.primitiveInt -> {
                val r = create() as MutableList<Int?>
                repeat(length) { r += if (source.isNull()) null else source.getInt() }
                return r as MutableList<Any?>
            }

            Type.primitiveLong -> {
                val r = create() as MutableList<Long?>
                repeat(length) { r += if (source.isNull()) null else source.getLong() }
                return r as MutableList<Any?>
            }

            Type.primitiveFloat -> {
                val r = create() as MutableList<Float?>
                repeat(length) { r += if (source.isNull()) null else source.getFloat() }
                return r as MutableList<Any?>
            }

            Type.primitiveDouble -> {
                val r = create() as MutableList<Double?>
                repeat(length) { r += if (source.isNull()) null else source.getDouble() }
                return r as MutableList<Any?>
            }

            Type.primitiveChar -> {
                val r = create() as MutableList<Char?>
                repeat(length) { r += if (source.isNull()) null else source.getChar() }
                return r as MutableList<Any?>
            }

            Type.primitiveUnit -> {
                val r = create() as MutableList<Unit?>
                repeat(length) { r += if (source.isNull()) null else source.getUnit() }
                return r as MutableList<Any?>
            }

            Type.primitiveString -> {
                val r = create() as MutableList<String?>
                repeat(length) { r += if (source.isNull()) null else source.getString() }
                return r as MutableList<Any?>
            }

            else -> {
                // Resolve element funneler
                val sub = module.resolve<Any>(type.arg)

                // Read as nested elements into mutable list
                val r = create()
                repeat(length) {
                    if (source.isNull())
                        r.add(null)
                    else {
                        source.beginNested()
                        r += sub.read(module, type.arg, source)
                        source.endNested()
                    }
                }
                return r
            }
        }
    }

    override fun read(module: Module, type: Type, source: LabelSource): MutableList<Any?> = source.markAround(type) {

        // Length is always needed
        val length = source.getInt("length")

        // Try to find a faster resolution for primitive types
        @Suppress("unchecked_cast")
        when (type.arg.primitiveCode) {
            Type.primitiveBoolean -> {
                val r = create() as MutableList<Boolean?>
                repeat(length) { r += if (source.isNull(it.toLabel())) null else source.getBoolean(it.toLabel()) }
                return r as MutableList<Any?>
            }

            Type.primitiveShort -> {
                val r = create() as MutableList<Short?>
                repeat(length) { r += if (source.isNull(it.toLabel())) null else source.getShort(it.toLabel()) }
                return r as MutableList<Any?>
            }

            Type.primitiveInt -> {
                val r = create() as MutableList<Int?>
                repeat(length) { r += if (source.isNull(it.toLabel())) null else source.getInt(it.toLabel()) }
                return r as MutableList<Any?>
            }

            Type.primitiveLong -> {
                val r = create() as MutableList<Long?>
                repeat(length) { r += if (source.isNull(it.toLabel())) null else source.getLong(it.toLabel()) }
                return r as MutableList<Any?>
            }

            Type.primitiveFloat -> {
                val r = create() as MutableList<Float?>
                repeat(length) { r += if (source.isNull(it.toLabel())) null else source.getFloat(it.toLabel()) }
                return r as MutableList<Any?>
            }

            Type.primitiveDouble -> {
                val r = create() as MutableList<Double?>
                repeat(length) { r += if (source.isNull(it.toLabel())) null else source.getDouble(it.toLabel()) }
                return r as MutableList<Any?>
            }

            Type.primitiveChar -> {
                val r = create() as MutableList<Char?>
                repeat(length) { r += if (source.isNull(it.toLabel())) null else source.getChar(it.toLabel()) }
                return r as MutableList<Any?>
            }

            Type.primitiveUnit -> {
                val r = create() as MutableList<Unit?>
                repeat(length) { r += if (source.isNull(it.toLabel())) null else source.getUnit(it.toLabel()) }
                return r as MutableList<Any?>
            }

            Type.primitiveString -> {
                val r = create() as MutableList<String?>
                repeat(length) { r += if (source.isNull(it.toLabel())) null else source.getString(it.toLabel()) }
                return r as MutableList<Any?>
            }

            else -> {
                // Resolve element funneler
                val sub = module.resolve<Any>(type.arg)

                // Read as nested elements into mutable list
                val r = create()
                repeat(length) {
                    if (source.isNull(it.toLabel()))
                        r.add(null)
                    else {
                        source.beginNested(it.toLabel())
                        r += sub.read(module, type.arg, source)
                        source.endNested(it.toLabel())
                    }
                }
                return r
            }
        }
    }

    override fun write(module: Module, type: Type, sink: SeqSink, item: MutableList<Any?>) = sink.markAround(type) {


        sink.putInt(item.size)

        @Suppress("unchecked_cast")
        when (type.arg.primitiveCode) {
            Type.primitiveBoolean ->
                for (it in item as List<Boolean?>)
                    if (it == null)
                        sink.putNull(true)
                    else {
                        sink.putNull(false)
                        sink.putBoolean(it)
                    }

            Type.primitiveByte ->
                for (it in item as List<Byte?>)
                    if (it == null)
                        sink.putNull(true)
                    else {
                        sink.putNull(false)
                        sink.putByte(it)
                    }

            Type.primitiveShort ->
                for (it in item as List<Short?>)
                    if (it == null)
                        sink.putNull(true)
                    else {
                        sink.putNull(false)
                        sink.putShort(it)
                    }

            Type.primitiveInt ->
                for (it in item as List<Int?>)
                    if (it == null)
                        sink.putNull(true)
                    else {
                        sink.putNull(false)
                        sink.putInt(it)
                    }

            Type.primitiveLong ->
                for (it in item as List<Long?>)
                    if (it == null)
                        sink.putNull(true)
                    else {
                        sink.putNull(false)
                        sink.putLong(it)
                    }

            Type.primitiveFloat ->
                for (it in item as List<Float?>)
                    if (it == null)
                        sink.putNull(true)
                    else {
                        sink.putNull(false)
                        sink.putFloat(it)
                    }

            Type.primitiveDouble ->
                for (it in item as List<Double?>)
                    if (it == null)
                        sink.putNull(true)
                    else {
                        sink.putNull(false)
                        sink.putDouble(it)
                    }

            Type.primitiveChar ->
                for (it in item as List<Char?>)
                    if (it == null)
                        sink.putNull(true)
                    else {
                        sink.putNull(false)
                        sink.putChar(it)
                    }

            Type.primitiveUnit ->
                for (it in item as List<Unit?>)
                    if (it == null)
                        sink.putNull(true)
                    else {
                        sink.putNull(false)
                        sink.putUnit(it)
                    }

            Type.primitiveString ->
                for (it in item as List<String?>)
                    if (it == null)
                        sink.putNull(true)
                    else {
                        sink.putNull(false)
                        sink.putString(it)
                    }

            else -> {
                // Resolve element funneler
                val sub = module.resolve<Any>(type.arg)

                // Write nested
                for (it in item)
                    if (it == null)
                        sink.putNull(true)
                    else {
                        sink.putNull(false)
                        sink.beginNested()
                        sub.write(module, type.arg, sink, it)
                        sink.endNested()
                    }
            }
        }
    }

    override fun write(module: Module, type: Type, sink: LabelSink, item: MutableList<Any?>) = sink.markAround(type) {


        sink.putInt("length", item.size)

        @Suppress("unchecked_cast")
        when (type.arg.primitiveCode) {
            Type.primitiveBoolean ->
                for ((i, it) in (item as List<Boolean?>).withIndex())
                    if (it == null)
                        sink.putNull(i.toLabel(), true)
                    else {
                        sink.putNull(i.toLabel(), false)
                        sink.putBoolean(i.toLabel(), it)
                    }

            Type.primitiveByte ->
                for ((i, it) in (item as List<Byte?>).withIndex())
                    if (it == null)
                        sink.putNull(i.toLabel(), true)
                    else {
                        sink.putNull(i.toLabel(), false)
                        sink.putByte(i.toLabel(), it)
                    }

            Type.primitiveShort ->
                for ((i, it) in (item as List<Short?>).withIndex())
                    if (it == null)
                        sink.putNull(i.toLabel(), true)
                    else {
                        sink.putNull(i.toLabel(), false)
                        sink.putShort(i.toLabel(), it)
                    }

            Type.primitiveInt ->
                for ((i, it) in (item as List<Int?>).withIndex())
                    if (it == null)
                        sink.putNull(i.toLabel(), true)
                    else {
                        sink.putNull(i.toLabel(), false)
                        sink.putInt(i.toLabel(), it)
                    }

            Type.primitiveLong ->
                for ((i, it) in (item as List<Long?>).withIndex())
                    if (it == null)
                        sink.putNull(i.toLabel(), true)
                    else {
                        sink.putNull(i.toLabel(), false)
                        sink.putLong(i.toLabel(), it)
                    }

            Type.primitiveFloat ->
                for ((i, it) in (item as List<Float?>).withIndex())
                    if (it == null)
                        sink.putNull(i.toLabel(), true)
                    else {
                        sink.putNull(i.toLabel(), false)
                        sink.putFloat(i.toLabel(), it)
                    }

            Type.primitiveDouble ->
                for ((i, it) in (item as List<Double?>).withIndex())
                    if (it == null)
                        sink.putNull(i.toLabel(), true)
                    else {
                        sink.putNull(i.toLabel(), false)
                        sink.putDouble(i.toLabel(), it)
                    }

            Type.primitiveChar ->
                for ((i, it) in (item as List<Char?>).withIndex())
                    if (it == null)
                        sink.putNull(i.toLabel(), true)
                    else {
                        sink.putNull(i.toLabel(), false)
                        sink.putChar(i.toLabel(), it)
                    }

            Type.primitiveUnit ->
                for ((i, it) in (item as List<Unit?>).withIndex())
                    if (it == null)
                        sink.putNull(i.toLabel(), true)
                    else {
                        sink.putNull(i.toLabel(), false)
                        sink.putUnit(i.toLabel(), it)
                    }

            Type.primitiveString ->
                for ((i, it) in (item as List<String?>).withIndex())
                    if (it == null)
                        sink.putNull(i.toLabel(), true)
                    else {
                        sink.putNull(i.toLabel(), false)
                        sink.putString(i.toLabel(), it)
                    }

            else -> {
                // Resolve element funneler
                val sub = module.resolve<Any>(type.arg)

                // Write nested
                for ((i, it) in item.withIndex())
                    if (it == null)
                        sink.putNull(i.toLabel(), true)
                    else {
                        sink.putNull(i.toLabel(), false)
                        sink.beginNested(i.toLabel())
                        sub.write(module, type.arg, sink, it)
                        sink.endNested(i.toLabel())
                    }
            }
        }
    }
}