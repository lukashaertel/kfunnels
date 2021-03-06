package eu.metatools.kfunnels

import eu.metatools.kfunnels.base.NoFunneler
import eu.metatools.kfunnels.base.NullableFunneler
import eu.metatools.kfunnels.base.StdlibModule
import kotlin.reflect.KClass

/**
 * A module provides resolution of funneler from class.
 */
interface Module {
    /**
     * Resolves the funneler for the given type.
     */
    fun <T> resolve(type: Type): Funneler<T>
}


/**
 * Interface implemented by generated modules.
 */
interface GeneratedModule : Module {
    /**
     * List of supported types.
     */
    val types: List<GeneratedFunneler<*>>
}

/**
 * Module provider for the service registry.
 */
interface ModuleProvider {
    fun provide(): Module
}


/**
 * Adds support for first level nullable inputs to [Module.resolve]. Nested nullable values are ususally handled within
 * the responsible funneler itself. If the first call is to a nullable type, this method should be used.
 */
fun Module.withNullableSupport() = object : Module {
    override fun <T> resolve(type: Type): Funneler<T> {
        // Catch nullable types
        if (type.nullable)
            @Suppress("unchecked_cast")
            return NullableFunneler<T>(this@withNullableSupport.resolve(type)) as Funneler<T>

        // Otherwise use base
        return this@withNullableSupport.resolve(type)
    }
}

infix fun Module.then(nextModule: Module) = object : Module {
    override fun <T> resolve(type: Type): Funneler<T> {
        val p = this@then.resolve<T>(type)
        if (p == NoFunneler)
            return nextModule.resolve(type)
        else
            return p
    }
}

/**
 * Resolves the funneler for the type (non-nullable, no arguments), and reads from the source.
 */
fun <T> Module.read(source: Source, type: Type): T {
    return resolve<T>(type).read(this, type, source)
}

/**
 * Resolves the funneler for the type (non-nullable, no arguments), and reads from the source.
 */
suspend fun <T> Module.read(source: SuspendSource, type: Type): T {
    return resolve<T>(type).read(this, type, source)
}


/**
 * Resolves the funneler for the type (non-nullable, no arguments), and writes to the sink.
 */
fun <T> Module.write(sink: Sink, type: Type, item: T) {
    return resolve<T>(type).write(this, type, sink, item)
}

/**
 * Resolves the funneler for the type (non-nullable, no arguments), and writes to the sink.
 */
suspend fun <T> Module.write(sink: SuspendSink, type: Type, item: T) {
    return resolve<T>(type).write(this, type, sink, item)
}


/**
 * Resolves the funneler for the type (non-nullable, no arguments), and reads from the source. See [Type.from] for
 * limitations.
 */
inline fun <reified T> Module.read(source: Source): T {
    val t = Type.from<T>()
    return resolve<T>(t).read(this, t, source)
}

/**
 * Resolves the funneler for the type (non-nullable, no arguments), and reads from the source. See [Type.from] for
 * limitations.
 */
suspend inline fun <reified T> Module.read(source: SuspendSource): T {
    val t = Type.from<T>()
    return resolve<T>(t).read(this, t, source)
}

/**
 * Resolves the funneler for the type (non-nullable, no arguments), and writes to the sink. See [Type.from] for
 * limitations.
 */
inline fun <reified T> Module.write(sink: Sink, item: T) {
    val t = Type.from<T>()
    return resolve<T>(t).write(this, t, sink, item)
}

/**
 * Resolves the funneler for the type (non-nullable, no arguments), and writes to the sink. See [Type.from] for
 * limitations.
 */
suspend inline fun <reified T> Module.write(sink: SuspendSink, item: T) {
    val t = Type.from<T>()
    return resolve<T>(t).write(this, t, sink, item)
}


/**
 * Resolves the funneler for the type (non-nullable, no arguments), and reads from the source. See [Type.from] for
 * limitations. Applies a configurator function to the inferred type.
 */
inline fun <reified T> Module.read(source: Source, configureType: (Type) -> Type): T {
    val t = configureType(Type.from<T>())
    return resolve<T>(t).read(this, t, source)
}

/**
 * Resolves the funneler for the type (non-nullable, no arguments), and reads from the source. See [Type.from] for
 * limitations. Applies a configurator function to the inferred type.
 */
suspend inline fun <reified T> Module.read(source: SuspendSource, configureType: (Type) -> Type): T {
    val t = configureType(Type.from<T>())
    return resolve<T>(t).read(this, t, source)
}

/**
 * Resolves the funneler for the type (non-nullable, no arguments), and writes to the sink. See [Type.from] for
 * limitations. Applies a configurator function to the inferred type.
 */
inline fun <reified T> Module.write(sink: Sink, item: T, configureType: (Type) -> Type) {
    val t = configureType(Type.from<T>())
    return resolve<T>(t).write(this, t, sink, item)
}

/**
 * Resolves the funneler for the type (non-nullable, no arguments), and writes to the sink. See [Type.from] for
 * limitations. Applies a configurator function to the inferred type.
 */
suspend inline fun <reified T> Module.write(sink: SuspendSink, item: T, configureType: (Type) -> Type) {
    val t = configureType(Type.from<T>())
    return resolve<T>(t).write(this, t, sink, item)
}


