package eu.metatools.kfunnels

import kotlin.reflect.KClass


/**
 * Funnels and unfunnels elements of type [T] into and out of sequence and label sinks.
 */
interface Funneler<T> {
    /**
     * Uses the given module to read the item from the sequence source.
     * @param module The module used to resolve nested funnelers
     * @param type The actual type that the funneler is to read
     * @param source The source to read from
     */
    fun read(module: Module, type: Type, source: SeqSource): T

    /**
     * Uses the given module to read the item from the label source.
     * @param module The module used to resolve nested funnelers
     * @param type The actual type that the funneler is to read
     * @param source The source to read from
     */
    fun read(module: Module, type: Type, source: LabelSource): T

    /**
     * Uses the given module to write the item to the sequence sink.
     * @param module The module used to resolve nested funnelers
     * @param type The actual type that the funneler is to read
     * @param sink The sink to write to
     * @param item The item to write
     */
    fun write(module: Module, type: Type, sink: SeqSink, item: T)

    /**
     * Uses the given module to write the item to the label sink.
     * @param module The module used to resolve nested funnelers
     * @param type The actual type that the funneler is to read
     * @param sink The sink to write to
     * @param item The item to write
     */
    fun write(module: Module, type: Type, sink: LabelSink, item: T)
}

/**
 * Interface implemented by funnelers generated for classes.
 */
interface GeneratedFunneler<T : Any> : Funneler<T> {
    /**
     * Gets the module in which the funneler is registered.
     */
    val module: Module

    /**
     * Gets the raw type that the funneler serves.
     */
    val type: KClass<T>
}