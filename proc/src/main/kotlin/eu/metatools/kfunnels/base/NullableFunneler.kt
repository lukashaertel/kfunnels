package eu.metatools.kfunnels.base

import eu.metatools.kfunnels.*

// TODO: I really don't want to make a complex type out of a simple nullity issue

// TODO: Maybe use RT check of type for primitive, then use actual primitive methods of source and sink

/**
 * Wraps nullable funneling, uses [itName] when a labeled source or sink is used.
 */
class NullableFunneler<T>(val of: Funneler<T>, val itName: String = "it") : Funneler<T?> {
    override fun read(module: Module, type: Type, source: SeqSource): T? = source.markAround(type) {
        if (source.isNull())
            return null

        source.beginNested()
        val r = of.read(module, +type, source)
        source.endNested()
        return r
    }

    override fun read(module: Module, type: Type, source: LabelSource): T? = source.markAround(type) {
        if (source.isNull(itName))
            return null

        source.beginNested(itName)
        val r = of.read(module, +type, source)
        source.endNested(itName)
        return r
    }

    override fun write(module: Module, type: Type, sink: SeqSink, item: T?) = sink.markAround(type) {
        if (item == null) {
            sink.putNull(true)
            return
        }

        sink.putNull(false)
        sink.beginNested()
        of.write(module, +type, sink, item)
        sink.endNested()
    }

    override fun write(module: Module, type: Type, sink: LabelSink, item: T?) = sink.markAround(type) {
        if (item == null) {
            sink.putNull(itName, true)
            return
        }

        sink.putNull(itName, false)
        sink.beginNested(itName)
        of.write(module, +type, sink, item)
        sink.endNested(itName)
    }

}