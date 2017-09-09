package eu.metatools.kfunnels.base

import eu.metatools.kfunnels.Funneler
import eu.metatools.kfunnels.Module
import eu.metatools.kfunnels.ModuleProvider
import eu.metatools.kfunnels.Type
import java.util.*

/**
 * Loads all [ModuleProvider] that are defined as services as per [ServiceLoader] definition. These will be instantiated
 * and then used to resolve given [Type].
 */
object ServiceModule : Module {
    /**
     * Delegates as loaded by the service loader.
     */
    val delegates = ServiceLoader
            .load(ModuleProvider::class.java)
            .map { it.provide() }

    /**
     * Extra modules that are registered by hand.
     */
    val extra = hashSetOf<Module>()

    /**
     * Gets all available modules.
     */
    val available get() = (delegates + extra).distinct()

    /**
     * The cache, storing already resolved funnelers.
     */
    private var cache = hashMapOf<Type, Funneler<Any?>>()

    override fun <T> resolve(type: Type): Funneler<T> {
        val r = cache.getOrPut(type) {
            val r = available.map {
                it.resolve<T>(type)
            }.firstOrNull {
                it != NoFunneler
            } ?: NoFunneler

            // Cast to arbitrary type, will be cast back
            @Suppress("unchecked_cast")
            r as Funneler<Any?>
        }

        // Cast back from arbitrary type
        @Suppress("unchecked_cast")
        return r as Funneler<T>
    }

}