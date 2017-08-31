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
    override fun read(module: Module, type: Type, source: SeqSource) =
            source.getBoolean()

    override fun read(module: Module, type: Type, source: LabelSource) =
            source.getBoolean(singularValueLabel)

    override fun write(module: Module, type: Type, sink: SeqSink, item: Boolean) {
        sink.putBoolean(item)
    }

    override fun write(module: Module, type: Type, sink: LabelSink, item: Boolean) {
        sink.putBoolean(singularValueLabel, item)
    }
}

/**
 * Funneler for primitive types. If a single value is written, [singularValueLabel] is used as a label.
 */
object ByteFunneler : Funneler<Byte> {
    override fun read(module: Module, type: Type, source: SeqSource) =
            source.getByte()

    override fun read(module: Module, type: Type, source: LabelSource) =
            source.getByte(singularValueLabel)

    override fun write(module: Module, type: Type, sink: SeqSink, item: Byte) {
        sink.putByte(item)
    }

    override fun write(module: Module, type: Type, sink: LabelSink, item: Byte) {
        sink.putByte(singularValueLabel, item)
    }
}


/**
 * Funneler for primitive types. If a single value is written, [singularValueLabel] is used as a label.
 */
object ShortFunneler : Funneler<Short> {
    override fun read(module: Module, type: Type, source: SeqSource) =
            source.getShort()

    override fun read(module: Module, type: Type, source: LabelSource) =
            source.getShort(singularValueLabel)

    override fun write(module: Module, type: Type, sink: SeqSink, item: Short) {
        sink.putShort(item)
    }

    override fun write(module: Module, type: Type, sink: LabelSink, item: Short) {
        sink.putShort(singularValueLabel, item)
    }
}

/**
 * Funneler for primitive types. If a single value is written, [singularValueLabel] is used as a label.
 */
object IntFunneler : Funneler<Int> {
    override fun read(module: Module, type: Type, source: SeqSource) =
            source.getInt()

    override fun read(module: Module, type: Type, source: LabelSource) =
            source.getInt(singularValueLabel)

    override fun write(module: Module, type: Type, sink: SeqSink, item: Int) {
        sink.putInt(item)
    }

    override fun write(module: Module, type: Type, sink: LabelSink, item: Int) {
        sink.putInt(singularValueLabel, item)
    }
}


/**
 * Funneler for primitive types. If a single value is written, [singularValueLabel] is used as a label.
 */
object LongFunneler : Funneler<Long> {
    override fun read(module: Module, type: Type, source: SeqSource) =
            source.getLong()

    override fun read(module: Module, type: Type, source: LabelSource) =
            source.getLong(singularValueLabel)

    override fun write(module: Module, type: Type, sink: SeqSink, item: Long) {
        sink.putLong(item)
    }

    override fun write(module: Module, type: Type, sink: LabelSink, item: Long) {
        sink.putLong(singularValueLabel, item)
    }
}


/**
 * Funneler for primitive types. If a single value is written, [singularValueLabel] is used as a label.
 */
object FloatFunneler : Funneler<Float> {
    override fun read(module: Module, type: Type, source: SeqSource) =
            source.getFloat()

    override fun read(module: Module, type: Type, source: LabelSource) =
            source.getFloat(singularValueLabel)

    override fun write(module: Module, type: Type, sink: SeqSink, item: Float) {
        sink.putFloat(item)
    }

    override fun write(module: Module, type: Type, sink: LabelSink, item: Float) {
        sink.putFloat(singularValueLabel, item)
    }
}


/**
 * Funneler for primitive types. If a single value is written, [singularValueLabel] is used as a label.
 */
object DoubleFunneler : Funneler<Double> {
    override fun read(module: Module, type: Type, source: SeqSource) =
            source.getDouble()

    override fun read(module: Module, type: Type, source: LabelSource) =
            source.getDouble(singularValueLabel)

    override fun write(module: Module, type: Type, sink: SeqSink, item: Double) {
        sink.putDouble(item)
    }

    override fun write(module: Module, type: Type, sink: LabelSink, item: Double) {
        sink.putDouble(singularValueLabel, item)
    }
}

/**
 * Funneler for primitive types. If a single value is written, [singularValueLabel] is used as a label.
 */
object CharFunneler : Funneler<Char> {
    override fun read(module: Module, type: Type, source: SeqSource) =
            source.getChar()

    override fun read(module: Module, type: Type, source: LabelSource) =
            source.getChar(singularValueLabel)

    override fun write(module: Module, type: Type, sink: SeqSink, item: Char) {
        sink.putChar(item)
    }

    override fun write(module: Module, type: Type, sink: LabelSink, item: Char) {
        sink.putChar(singularValueLabel, item)
    }
}

/**
 * Funneler for primitive types. If a single value is written, [singularValueLabel] is used as a label.
 */
object UnitFunneler : Funneler<Unit> {
    override fun read(module: Module, type: Type, source: SeqSource) =
            source.getUnit()

    override fun read(module: Module, type: Type, source: LabelSource) =
            source.getUnit(singularValueLabel)

    override fun write(module: Module, type: Type, sink: SeqSink, item: Unit) {
        sink.putUnit(item)
    }

    override fun write(module: Module, type: Type, sink: LabelSink, item: Unit) {
        sink.putUnit(singularValueLabel, item)
    }
}

/**
 * Funneler for primitive types. If a single value is written, [singularValueLabel] is used as a label.
 */
object StringFunneler : Funneler<String> {
    override fun read(module: Module, type: Type, source: SeqSource) =
            source.getString()

    override fun read(module: Module, type: Type, source: LabelSource) =
            source.getString(singularValueLabel)

    override fun write(module: Module, type: Type, sink: SeqSink, item: String) {
        sink.putString(item)
    }

    override fun write(module: Module, type: Type, sink: LabelSink, item: String) {
        sink.putString(singularValueLabel, item)
    }
}

/**
 * Funneler for enum type. Uses Java valueOf to determine the value of an enum stored in a strng.  If a single value is
 * written, [singularValueLabel] is used as a label.
 */
object EnumFunneler : Funneler<Enum<*>> {
    override fun read(module: Module, type: Type, source: SeqSource) =
            RawEnums.valueOf(type.kClass, source.getString()) as Enum<*>


    override fun read(module: Module, type: Type, source: LabelSource): Enum<*> {
        return RawEnums.valueOf(type.kClass, source.getString(singularValueLabel)) as Enum<*>
    }

    override fun write(module: Module, type: Type, sink: SeqSink, item: Enum<*>) {
        sink.putString(item.name)
    }

    override fun write(module: Module, type: Type, sink: LabelSink, item: Enum<*>) {
        sink.putString(singularValueLabel, item.name)
    }
}