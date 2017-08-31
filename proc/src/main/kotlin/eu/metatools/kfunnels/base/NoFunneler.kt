package eu.metatools.kfunnels.base

import eu.metatools.kfunnels.*

/**
 * Instance of a funneler that cannot funnel or unfunnel. Used as a fallback return if no funneler is found.
 */
object NoFunneler : Funneler<Nothing> {
    private fun error(type: Type): Nothing {
        error("There is no funneler for $type, check if annotations were processed.")
    }

    override fun read(module: Module, type: Type, source: Source): Nothing {
        error(type)
    }

    override suspend fun read(module: Module, type: Type, source: SuspendSource): Nothing {
        error(type)
    }

    override fun write(module: Module, type: Type, sink: Sink, item: Nothing) {
        error(type)
    }

    override suspend fun write(module: Module, type: Type, sink: SuspendSink, item: Nothing) {
        error(type)
    }
}