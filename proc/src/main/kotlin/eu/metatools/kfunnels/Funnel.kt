package eu.metatools.kfunnels

import kotlin.reflect.KClass

/**
 * Funnels and unfunnels elements of type [T] into and out of sequence and label sinks.
 */
interface Funneler<T> {
    /**
     * Uses the given module to read the item from the source.
     * @param module The module used to resolve nested funnelers
     * @param type The actual type that the funneler is to read
     * @param source The source to read from
     */
    fun read(module: Module, type: Type, source: Source): T

    /**
     * Uses the given module to read the item from the suspendable source.
     * @param module The module used to resolve nested funnelers
     * @param type The actual type that the funneler is to read
     * @param source The source to read from
     */
    suspend fun read(module: Module, type: Type, source: SuspendSource): T

    /**
     * Uses the given module to write the item to the sink.
     * @param module The module used to resolve nested funnelers
     * @param type The actual type that the funneler is to read
     * @param sink The sink to write to
     * @param item The item to write
     */
    fun write(module: Module, type: Type, sink: Sink, item: T)

    /**
     * Uses the given module to write the item to the suspendable sink.
     * @param module The module used to resolve nested funnelers
     * @param type The actual type that the funneler is to read
     * @param sink The sink to write to
     * @param item The item to write
     */
    suspend fun write(module: Module, type: Type, sink: SuspendSink, item: T)

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