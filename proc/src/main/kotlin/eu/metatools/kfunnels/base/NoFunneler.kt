package eu.metatools.kfunnels.base

import eu.metatools.kfunnels.*

/**
 * Instance of a funneler that cannot funnel or unfunnel. Used as a fallback return if no funneler is found.
 */
object NoFunneler : Funneler<Nothing> {
    override fun read(module: Module, type: Type, source: SeqSource): Nothing {
        throw NoSuchElementException()
    }

    override fun read(module: Module, type: Type, source: LabelSource): Nothing {
        throw NoSuchElementException()
    }

    override fun write(module: Module, type: Type, sink: SeqSink, item: Nothing) {
    }

    override fun write(module: Module, type: Type, sink: LabelSink, item: Nothing) {
    }
}