package eu.metatools.kfunnels.base

import eu.metatools.kfunnels.*

/**
 * Instance of a funneler that cannot funnel or unfunnel. Used as a fallback return if no funneler is found.
 */
object NoFunneler : Funneler<Nothing> {
    private fun error(type: Type): Nothing {
        error("There is no funneler for $type, check if annotations were processed.")
    }

    override fun read(module: Module, type: Type, source: SeqSource): Nothing {
        error(type)
    }

    override fun read(module: Module, type: Type, source: LabelSource): Nothing {
        error(type)
    }

    override fun write(module: Module, type: Type, sink: SeqSink, item: Nothing) {
        error(type)
    }

    override fun write(module: Module, type: Type, sink: LabelSink, item: Nothing) {
        error(type)
    }
}