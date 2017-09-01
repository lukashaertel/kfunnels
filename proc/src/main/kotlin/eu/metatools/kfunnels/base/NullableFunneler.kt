package eu.metatools.kfunnels.base

import eu.metatools.kfunnels.*

// TODO: I really don't want to make a complex type out of a simple nullity issue

// TODO: Maybe use RT check of type for primitive, then use actual primitive methods of source and sink

/**
 * Wraps nullable funneling, uses [itName] when a labeled source or sink is used.
 */
class NullableFunneler<T>(val of: Funneler<T>) : Funneler<T?> {
    // TODO: Check this, it's not used very often but I have the feeling it is wrong.
    override fun read(module: Module, type: Type, source: Source): T? = source.markAround(type) {
        if (source.isNull(singularValueLabel))
            return@markAround null

        val sub = source.beginNested(singularValueLabel, type)

        @Suppress("unchecked_cast")
        when (sub) {
            is Value<*> -> {
                source.endNested(singularValueLabel, type)
                (sub.item as T)
            }
            is Substitute -> {
                val subFunneler = module.resolve<T>(+sub.type)
                val r = subFunneler.read(module, +sub.type, source)
                source.endNested(singularValueLabel, type)
                r
            }
            Continue -> {
                val r = of.read(module, +type, source)
                source.endNested(singularValueLabel, type)
                r
            }
        }
    }

    override suspend fun read(module: Module, type: Type, source: SuspendSource): T? = source.markAround(type) {
        if (source.isNull(singularValueLabel))
            return@markAround null

        val sub = source.beginNested(singularValueLabel, type)

        @Suppress("unchecked_cast")
        when (sub) {
            is Value<*> -> {
                source.endNested(singularValueLabel, type)
                (sub.item as T)
            }
            is Substitute -> {
                val subFunneler = module.resolve<T>(+sub.type)
                val r = subFunneler.read(module, +sub.type, source)
                source.endNested(singularValueLabel, type)
                r
            }
            Continue -> {
                val r = of.read(module, +type, source)
                source.endNested(singularValueLabel, type)
                r
            }
        }
    }

    override fun write(module: Module, type: Type, sink: Sink, item: T?) = sink.markAround(type) {
        if (item == null) {
            sink.putNull(singularValueLabel, true)
            return@markAround
        }

        sink.putNull(singularValueLabel, false)
        if (sink.beginNested(singularValueLabel, type, item))
            of.write(module, +type, sink, item)
        sink.endNested(singularValueLabel, type, item)
    }

    override suspend fun write(module: Module, type: Type, sink: SuspendSink, item: T?) = sink.markAround(type) {
        if (item == null) {
            sink.putNull(singularValueLabel, true)
            return@markAround
        }

        sink.putNull(singularValueLabel, false)
        sink.beginNested(singularValueLabel, type, item)
        of.write(module, +type, sink, item)
        sink.endNested(singularValueLabel, type, item)
    }

}