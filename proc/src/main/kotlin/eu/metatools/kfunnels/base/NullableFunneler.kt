package eu.metatools.kfunnels.base

import eu.metatools.kfunnels.*

// TODO: I really don't want to make a complex type out of a simple nullity issue

// TODO: Maybe use RT check of type for primitive, then use actual primitive methods of source and sink

/**
 * Wraps nullable funneling, uses [itName] when a labeled source or sink is used.
 */
class NullableFunneler<T>(val of: Funneler<T>) : Funneler<T?> {
    override fun read(module: Module, type: Type, source: Source): T? = source.markAround(type) {
        if (source.isNull(singularValueLabel))
            return null

        val t = source.beginNested(singularValueLabel, type)
        val r = of.read(module, +t, source)
        source.endNested(singularValueLabel, type)
        return r
    }

    override suspend fun read(module: Module, type: Type, source: SuspendSource): T? = source.markAround(type) {
        if (source.isNull(singularValueLabel))
            return null

        val t = source.beginNested(singularValueLabel, type)
        val r = of.read(module, +t, source)
        source.endNested(singularValueLabel, type)
        return r
    }

    override fun write(module: Module, type: Type, sink: Sink, item: T?) = sink.markAround(type) {
        if (item == null) {
            sink.putNull(singularValueLabel, true)
            return
        }

        sink.putNull(singularValueLabel, false)
        sink.beginNested(singularValueLabel, type, item)
        of.write(module, type, sink, item)
        sink.endNested(singularValueLabel, type, item)
    }

    override suspend fun write(module: Module, type: Type, sink: SuspendSink, item: T?) = sink.markAround(type) {
        if (item == null) {
            sink.putNull(singularValueLabel, true)
            return
        }

        sink.putNull(singularValueLabel, false)
        sink.beginNested(singularValueLabel, type, item)
        of.write(module, type, sink, item)
        sink.endNested(singularValueLabel, type, item)
    }

}