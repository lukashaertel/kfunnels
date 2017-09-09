package eu.metatools.kfunnels.base

import eu.metatools.kfunnels.*
import eu.metatools.kfunnels.utils.RawEnums

/**
 * The label if only one value is serialized into a labeled sink.
 */
val singularValueLabel = "it"

/**
 * Funneler for primitive types. If a single value is written, [singularValueLabel] is used as a label.
 */
object BooleanFunneler : Funneler<Boolean> {
    override fun read(module: Module, type: Type, source: Source) =
            source.getBoolean(singularValueLabel)

    override suspend fun read(module: Module, type: Type, source: SuspendSource) =
            source.getBoolean(singularValueLabel)

    override fun write(module: Module, type: Type, sink: Sink, item: Boolean) {
        sink.putBoolean(singularValueLabel, item)
    }

    override suspend fun write(module: Module, type: Type, sink: SuspendSink, item: Boolean) {
        sink.putBoolean(singularValueLabel, item)
    }
}

/**
 * Funneler for primitive types. If a single value is written, [singularValueLabel] is used as a label.
 */
object ByteFunneler : Funneler<Byte> {
    override fun read(module: Module, type: Type, source: Source) =
            source.getByte(singularValueLabel)

    override suspend fun read(module: Module, type: Type, source: SuspendSource) =
            source.getByte(singularValueLabel)

    override fun write(module: Module, type: Type, sink: Sink, item: Byte) {
        sink.putByte(singularValueLabel, item)
    }

    override suspend fun write(module: Module, type: Type, sink: SuspendSink, item: Byte) {
        sink.putByte(singularValueLabel, item)
    }
}


/**
 * Funneler for primitive types. If a single value is written, [singularValueLabel] is used as a label.
 */
object ShortFunneler : Funneler<Short> {
    override fun read(module: Module, type: Type, source: Source) =
            source.getShort(singularValueLabel)

    override suspend fun read(module: Module, type: Type, source: SuspendSource) =
            source.getShort(singularValueLabel)

    override fun write(module: Module, type: Type, sink: Sink, item: Short) {
        sink.putShort(singularValueLabel, item)
    }

    override suspend fun write(module: Module, type: Type, sink: SuspendSink, item: Short) {
        sink.putShort(singularValueLabel, item)
    }
}

/**
 * Funneler for primitive types. If a single value is written, [singularValueLabel] is used as a label.
 */
object IntFunneler : Funneler<Int> {
    override fun read(module: Module, type: Type, source: Source) =
            source.getInt(singularValueLabel)

    override suspend fun read(module: Module, type: Type, source: SuspendSource) =
            source.getInt(singularValueLabel)

    override fun write(module: Module, type: Type, sink: Sink, item: Int) {
        sink.putInt(singularValueLabel, item)
    }

    override suspend fun write(module: Module, type: Type, sink: SuspendSink, item: Int) {
        sink.putInt(singularValueLabel, item)
    }
}


/**
 * Funneler for primitive types. If a single value is written, [singularValueLabel] is used as a label.
 */
object LongFunneler : Funneler<Long> {
    override fun read(module: Module, type: Type, source: Source) =
            source.getLong(singularValueLabel)

    override suspend fun read(module: Module, type: Type, source: SuspendSource) =
            source.getLong(singularValueLabel)

    override fun write(module: Module, type: Type, sink: Sink, item: Long) {
        sink.putLong(singularValueLabel, item)
    }

    override suspend fun write(module: Module, type: Type, sink: SuspendSink, item: Long) {
        sink.putLong(singularValueLabel, item)
    }
}


/**
 * Funneler for primitive types. If a single value is written, [singularValueLabel] is used as a label.
 */
object FloatFunneler : Funneler<Float> {
    override fun read(module: Module, type: Type, source: Source) =
            source.getFloat(singularValueLabel)

    override suspend fun read(module: Module, type: Type, source: SuspendSource) =
            source.getFloat(singularValueLabel)

    override fun write(module: Module, type: Type, sink: Sink, item: Float) {
        sink.putFloat(singularValueLabel, item)
    }

    override suspend fun write(module: Module, type: Type, sink: SuspendSink, item: Float) {
        sink.putFloat(singularValueLabel, item)
    }
}


/**
 * Funneler for primitive types. If a single value is written, [singularValueLabel] is used as a label.
 */
object DoubleFunneler : Funneler<Double> {
    override fun read(module: Module, type: Type, source: Source) =
            source.getDouble(singularValueLabel)

    override suspend fun read(module: Module, type: Type, source: SuspendSource) =
            source.getDouble(singularValueLabel)

    override fun write(module: Module, type: Type, sink: Sink, item: Double) {
        sink.putDouble(singularValueLabel, item)
    }

    override suspend fun write(module: Module, type: Type, sink: SuspendSink, item: Double) {
        sink.putDouble(singularValueLabel, item)
    }
}

/**
 * Funneler for primitive types. If a single value is written, [singularValueLabel] is used as a label.
 */
object CharFunneler : Funneler<Char> {
    override fun read(module: Module, type: Type, source: Source) =
            source.getChar(singularValueLabel)

    override suspend fun read(module: Module, type: Type, source: SuspendSource) =
            source.getChar(singularValueLabel)

    override fun write(module: Module, type: Type, sink: Sink, item: Char) {
        sink.putChar(singularValueLabel, item)
    }

    override suspend fun write(module: Module, type: Type, sink: SuspendSink, item: Char) {
        sink.putChar(singularValueLabel, item)
    }
}

/**
 * Funneler for primitive types. If a single value is written, [singularValueLabel] is used as a label.
 */
object UnitFunneler : Funneler<Unit> {
    override fun read(module: Module, type: Type, source: Source) =
            source.getUnit(singularValueLabel)

    override suspend fun read(module: Module, type: Type, source: SuspendSource) =
            source.getUnit(singularValueLabel)

    override fun write(module: Module, type: Type, sink: Sink, item: Unit) {
        sink.putUnit(singularValueLabel, item)
    }

    override suspend fun write(module: Module, type: Type, sink: SuspendSink, item: Unit) {
        sink.putUnit(singularValueLabel, item)
    }
}

/**
 * Funneler for primitive types. If a single value is written, [singularValueLabel] is used as a label.
 */
object StringFunneler : Funneler<String> {
    override fun read(module: Module, type: Type, source: Source) =
            source.getString(singularValueLabel)

    override suspend fun read(module: Module, type: Type, source: SuspendSource) =
            source.getString(singularValueLabel)

    override fun write(module: Module, type: Type, sink: Sink, item: String) {
        sink.putString(singularValueLabel, item)
    }

    override suspend fun write(module: Module, type: Type, sink: SuspendSink, item: String) {
        sink.putString(singularValueLabel, item)
    }
}

/**
 * Funneler for enum type. Uses Java valueOf to determine the value of an enum stored in a strng.  If a single value is
 * written, [singularValueLabel] is used as a label.
 */
object EnumFunneler : Funneler<Enum<*>> {
    override fun read(module: Module, type: Type, source: Source) =
            RawEnums.valueOf(type.kClass, source.getString(singularValueLabel)) as Enum<*>


    override suspend fun read(module: Module, type: Type, source: SuspendSource): Enum<*> {
        return RawEnums.valueOf(type.kClass, source.getString(singularValueLabel)) as Enum<*>
    }

    override fun write(module: Module, type: Type, sink: Sink, item: Enum<*>) {
        sink.putString(singularValueLabel, item.name)
    }

    override suspend fun write(module: Module, type: Type, sink: SuspendSink, item: Enum<*>) {
        sink.putString(singularValueLabel, item.name)
    }
}