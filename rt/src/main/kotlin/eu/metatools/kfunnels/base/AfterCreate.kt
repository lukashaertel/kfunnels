package eu.metatools.kfunnels.base

import eu.metatools.kfunnels.*

/**
 * Intercepts the [Source.afterCreate] invocation, checks and casts to [T].
 */
inline fun <reified T> Source.onAfterCreate(crossinline block: (T) -> Unit) = object : Source by this {
    override fun afterCreate(item: Any?) {
        this@onAfterCreate.afterCreate(item)
        if (item is T)
            block(item)
    }
}

/**
 * Intercepts the [SuspendSource.afterCreate] invocation, checks and casts to [T].
 */
inline fun <reified T> SuspendSource.onAfterCreate(crossinline block: (T) -> Unit) = object : SuspendSource by this {
    override suspend fun afterCreate(item: Any?) {
        this@onAfterCreate.afterCreate(item)
        if (item is T)
            block(item)
    }
}