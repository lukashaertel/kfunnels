package eu.metatools.kfunnels.base.listlike

import eu.metatools.kfunnels.*
import java.util.*

abstract class ListlikeFunneler<T, U> : Funneler<U> {
    protected open fun combineContinue(source: T) = true

    protected abstract fun create(): T
    protected abstract fun begin(source: T): U
    protected abstract fun end(source: T): U
    protected abstract fun combineBoolean(target: T, value: Boolean)
    protected abstract fun combineByte(target: T, value: Byte)
    protected abstract fun combineShort(target: T, value: Short)
    protected abstract fun combineInt(target: T, value: Int)
    protected abstract fun combineLong(target: T, value: Long)
    protected abstract fun combineFloat(target: T, value: Float)
    protected abstract fun combineDouble(target: T, value: Double)
    protected abstract fun combineChar(target: T, value: Char)
    protected abstract fun combineUnit(target: T, value: Unit)
    protected abstract fun combineString(target: T, value: String)
    protected abstract fun combineElement(target: T, value: Any)

    // TODO: Suspend
    protected abstract fun uncombineBooleans(source: U): Sequence<Boolean>

    protected abstract fun uncombineBytes(source: U): Sequence<Byte>
    protected abstract fun uncombineShorts(source: U): Sequence<Short>
    protected abstract fun uncombineInts(source: U): Sequence<Int>
    protected abstract fun uncombineLongs(source: U): Sequence<Long>
    protected abstract fun uncombineFloats(source: U): Sequence<Float>
    protected abstract fun uncombineDoubles(source: U): Sequence<Double>
    protected abstract fun uncombineChars(source: U): Sequence<Char>
    protected abstract fun uncombineUnits(source: U): Sequence<Unit>
    protected abstract fun uncombineStrings(source: U): Sequence<String>
    protected abstract fun uncombineElements(source: U): Sequence<Any>

    /**
     * Computes the positional label for an item.
     */
    private fun Int.toLabel() = "item$this"

    override fun read(module: Module, type: Type, source: Source): U
            = source.markAround(type) {

        // Try to find a faster resolution for primitive types
        @Suppress("unchecked_cast")
        when (type.arg.primitiveCode) {
            Type.primitiveBoolean -> {
                val r = create()
                source.afterCreate(begin(r))
                var p = 0
                while (!source.isEnd() && combineContinue(r))
                    combineBoolean(r, source.getBoolean((p++).toLabel()))
                return@markAround end(r)
            }

            Type.primitiveByte -> {
                val r = create()
                source.afterCreate(begin(r))
                var p = 0
                while (!source.isEnd() && combineContinue(r))
                    combineByte(r, source.getByte((p++).toLabel()))
                return@markAround end(r)
            }


            Type.primitiveShort -> {
                val r = create()
                source.afterCreate(begin(r))
                var p = 0
                while (!source.isEnd() && combineContinue(r))
                    combineShort(r, source.getShort((p++).toLabel()))
                return@markAround end(r)
            }

            Type.primitiveInt -> {
                val r = create()
                source.afterCreate(begin(r))
                var p = 0
                while (!source.isEnd() && combineContinue(r))
                    combineInt(r, source.getInt((p++).toLabel()))
                return@markAround end(r)
            }

            Type.primitiveLong -> {
                val r = create()
                source.afterCreate(begin(r))
                var p = 0
                while (!source.isEnd() && combineContinue(r))
                    combineLong(r, source.getLong((p++).toLabel()))
                return@markAround end(r)
            }

            Type.primitiveFloat -> {
                val r = create()
                source.afterCreate(begin(r))
                var p = 0
                while (!source.isEnd() && combineContinue(r))
                    combineFloat(r, source.getFloat((p++).toLabel()))
                return@markAround end(r)
            }

            Type.primitiveDouble -> {
                val r = create()
                source.afterCreate(begin(r))
                var p = 0
                while (!source.isEnd() && combineContinue(r))
                    combineDouble(r, source.getDouble((p++).toLabel()))
                return@markAround end(r)
            }

            Type.primitiveChar -> {
                val r = create()
                source.afterCreate(begin(r))
                var p = 0
                while (!source.isEnd() && combineContinue(r))
                    combineChar(r, source.getChar((p++).toLabel()))
                return@markAround end(r)
            }

            Type.primitiveUnit -> {
                val r = create()
                source.afterCreate(begin(r))
                var p = 0
                while (!source.isEnd() && combineContinue(r))
                    combineUnit(r, source.getUnit((p++).toLabel()))
                return@markAround end(r)
            }

            Type.primitiveString -> {
                val r = create()
                source.afterCreate(begin(r))
                var p = 0
                while (!source.isEnd() && combineContinue(r))
                    combineString(r, source.getString((p++).toLabel()))
                return@markAround end(r)
            }

            else -> {
                if (type.arg.isTerminal()) {
                    // Resolve element funneler for terminal type
                    val sub = module.resolve<Any>(type.arg)

                    val r = create()
                    source.afterCreate(begin(r))
                    var p = 0
                    while (!source.isEnd() && combineContinue(r))
                        combineElement(r, source.getTerminalNested(module, sub, (p++).toLabel(), type.arg))
                    return@markAround end(r)
                } else {
                    val sub = module.resolve<Any>(type.arg)

                    val r = create()
                    source.afterCreate(begin(r))
                    var p = 0
                    while (!source.isEnd() && combineContinue(r))
                        combineElement(r, source.getDynamicNested<Any>(module, sub, (p++).toLabel(), type.arg))
                    return@markAround end(r)
                }
            }
        }
    }


    override suspend fun read(module: Module, type: Type, source: SuspendSource): U
            = source.markAround(type) {

        // Try to find a faster resolution for primitive types
        @Suppress("unchecked_cast")
        when (type.arg.primitiveCode) {
            Type.primitiveBoolean -> {
                val r = create()
                source.afterCreate(begin(r))
                var p = 0
                while (!source.isEnd() && combineContinue(r))
                    combineBoolean(r, source.getBoolean((p++).toLabel()))
                return@markAround end(r)
            }

            Type.primitiveByte -> {
                val r = create()
                source.afterCreate(begin(r))
                var p = 0
                while (!source.isEnd() && combineContinue(r))
                    combineByte(r, source.getByte((p++).toLabel()))
                return@markAround end(r)
            }


            Type.primitiveShort -> {
                val r = create()
                source.afterCreate(begin(r))
                var p = 0
                while (!source.isEnd() && combineContinue(r))
                    combineShort(r, source.getShort((p++).toLabel()))
                return@markAround end(r)
            }

            Type.primitiveInt -> {
                val r = create()
                source.afterCreate(begin(r))
                var p = 0
                while (!source.isEnd() && combineContinue(r))
                    combineInt(r, source.getInt((p++).toLabel()))
                return@markAround end(r)
            }

            Type.primitiveLong -> {
                val r = create()
                source.afterCreate(begin(r))
                var p = 0
                while (!source.isEnd() && combineContinue(r))
                    combineLong(r, source.getLong((p++).toLabel()))
                return@markAround end(r)
            }

            Type.primitiveFloat -> {
                val r = create()
                source.afterCreate(begin(r))
                var p = 0
                while (!source.isEnd() && combineContinue(r))
                    combineFloat(r, source.getFloat((p++).toLabel()))
                return@markAround end(r)
            }

            Type.primitiveDouble -> {
                val r = create()
                source.afterCreate(begin(r))
                var p = 0
                while (!source.isEnd() && combineContinue(r))
                    combineDouble(r, source.getDouble((p++).toLabel()))
                return@markAround end(r)
            }

            Type.primitiveChar -> {
                val r = create()
                source.afterCreate(begin(r))
                var p = 0
                while (!source.isEnd() && combineContinue(r))
                    combineChar(r, source.getChar((p++).toLabel()))
                return@markAround end(r)
            }

            Type.primitiveUnit -> {
                val r = create()
                source.afterCreate(begin(r))
                var p = 0
                while (!source.isEnd() && combineContinue(r))
                    combineUnit(r, source.getUnit((p++).toLabel()))
                return@markAround end(r)
            }

            Type.primitiveString -> {
                val r = create()
                source.afterCreate(begin(r))
                var p = 0
                while (!source.isEnd() && combineContinue(r))
                    combineString(r, source.getString((p++).toLabel()))
                return@markAround end(r)
            }

            else -> {
                if (type.arg.isTerminal()) {
                    // Resolve element funneler for terminal type
                    val sub = module.resolve<Any>(type.arg)

                    val r = create()
                    source.afterCreate(begin(r))
                    var p = 0
                    while (!source.isEnd() && combineContinue(r))
                        combineElement(r, source.getTerminalNested(module, sub, (p++).toLabel(), type.arg))
                    return@markAround end(r)
                } else {
                    val sub = module.resolve<Any>(type.arg)

                    val r = create()
                    source.afterCreate(begin(r))
                    var p = 0
                    while (!source.isEnd() && combineContinue(r))
                        combineElement(r, source.getDynamicNested<Any>(module, sub, (p++).toLabel(), type.arg))
                    return@markAround end(r)
                }
            }
        }
    }

    override fun write(module: Module, type: Type, sink: Sink, item: U)
            = sink.markAround(type, item) {

        @Suppress("unchecked_cast")
        when (type.arg.primitiveCode) {
            Type.primitiveBoolean ->
                for ((i, it) in (uncombineBooleans(item)).withIndex())
                    sink.putBoolean(i.toLabel(), it)

            Type.primitiveByte ->
                for ((i, it) in (uncombineBytes(item)).withIndex())
                    sink.putByte(i.toLabel(), it)

            Type.primitiveShort ->
                for ((i, it) in (uncombineShorts(item)).withIndex())
                    sink.putShort(i.toLabel(), it)

            Type.primitiveInt ->
                for ((i, it) in (uncombineInts(item)).withIndex())
                    sink.putInt(i.toLabel(), it)

            Type.primitiveLong ->
                for ((i, it) in (uncombineLongs(item)).withIndex())
                    sink.putLong(i.toLabel(), it)

            Type.primitiveFloat ->
                for ((i, it) in (uncombineFloats(item)).withIndex())
                    sink.putFloat(i.toLabel(), it)

            Type.primitiveDouble ->
                for ((i, it) in (uncombineDoubles(item)).withIndex())
                    sink.putDouble(i.toLabel(), it)

            Type.primitiveChar ->
                for ((i, it) in (uncombineChars(item)).withIndex())
                    sink.putChar(i.toLabel(), it)

            Type.primitiveUnit ->
                for ((i, it) in (uncombineUnits(item)).withIndex())
                    sink.putUnit(i.toLabel(), it)

            Type.primitiveString ->
                for ((i, it) in (uncombineStrings(item)).withIndex())
                    sink.putString(i.toLabel(), it)

            else -> {
                if (type.arg.isTerminal()) {
                    // Resolve element funneler for terminal type
                    val sub = module.resolve<Any>(type.arg)

                    // Write nested
                    for ((i, it) in uncombineElements(item).withIndex())
                        sink.putTerminalNested(module, sub, i.toLabel(), type.arg, it)
                } else {
                    // Write nested
                    for ((i, it) in uncombineElements(item).withIndex())
                        sink.putDynamicNested(module, i.toLabel(), type.arg, it)
                }
            }
        }
    }

    override suspend fun write(module: Module, type: Type, sink: SuspendSink, item: U)
            = sink.markAround(type, item) {

        @Suppress("unchecked_cast")
        when (type.arg.primitiveCode) {
            Type.primitiveBoolean ->
                for ((i, it) in (uncombineBooleans(item)).withIndex())
                    sink.putBoolean(i.toLabel(), it)

            Type.primitiveByte ->
                for ((i, it) in (uncombineBytes(item)).withIndex())
                    sink.putByte(i.toLabel(), it)

            Type.primitiveShort ->
                for ((i, it) in (uncombineShorts(item)).withIndex())
                    sink.putShort(i.toLabel(), it)

            Type.primitiveInt ->
                for ((i, it) in (uncombineInts(item)).withIndex())
                    sink.putInt(i.toLabel(), it)

            Type.primitiveLong ->
                for ((i, it) in (uncombineLongs(item)).withIndex())
                    sink.putLong(i.toLabel(), it)

            Type.primitiveFloat ->
                for ((i, it) in (uncombineFloats(item)).withIndex())
                    sink.putFloat(i.toLabel(), it)

            Type.primitiveDouble ->
                for ((i, it) in (uncombineDoubles(item)).withIndex())
                    sink.putDouble(i.toLabel(), it)

            Type.primitiveChar ->
                for ((i, it) in (uncombineChars(item)).withIndex())
                    sink.putChar(i.toLabel(), it)

            Type.primitiveUnit ->
                for ((i, it) in (uncombineUnits(item)).withIndex())
                    sink.putUnit(i.toLabel(), it)

            Type.primitiveString ->
                for ((i, it) in (uncombineStrings(item)).withIndex())
                    sink.putString(i.toLabel(), it)

            else -> {
                if (type.arg.isTerminal()) {
                    // Resolve element funneler for terminal type
                    val sub = module.resolve<Any>(type.arg)

                    // Write nested
                    for ((i, it) in uncombineElements(item).withIndex())
                        sink.putTerminalNested(module, sub, i.toLabel(), type.arg, it)
                } else {
                    // Write nested
                    for ((i, it) in uncombineElements(item).withIndex())
                        sink.putDynamicNested(module, i.toLabel(), type.arg, it)
                }
            }
        }
    }
}