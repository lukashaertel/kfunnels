package eu.metatools.kfunnels.base

import eu.metatools.kfunnels.*

/**
 * Wraps nullable funneling, uses [itName] when a labeled source or sink is used.
 */
class NullableFunneler<T>(val of: Funneler<T>, val itName: String = "it") : Funneler<T?> {
    override fun read(module: Module, source: SeqSource): T? {
        if (source.isNull())
            return null

        source.beginNested()
        val r = of.read(module, source)
        source.endNested()
        return r
    }

    override fun read(module: Module, source: LabelSource): T? {
        if (source.isNull(itName))
            return null

        source.beginNested(itName)
        val r = of.read(module, source)
        source.endNested()
        return r
    }

    override fun write(module: Module, sink: SeqSink, item: T?) {
        if (item == null) {
            sink.putNull(true)
            return
        }

        sink.putNull(false)
        sink.beginNested()
        val r = of.write(module, sink, item)
        sink.endNested()
        return r
    }

    override fun write(module: Module, sink: LabelSink, item: T?) {
        if (item == null) {
            sink.putNull(itName, true)
            return
        }

        sink.putNull(itName, false)
        sink.beginNested(itName)
        val r = of.write(module, sink, item)
        sink.endNested()
        return r
    }

}