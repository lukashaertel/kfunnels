package eu.metatools.kfunnels.base

import eu.metatools.kfunnels.*

/**
 * Intercepts the [Source.afterCreate] invocation, checks and casts to [T].
 */
inline fun <reified T> Source.onAfterCreate(crossinline block: (T) -> Unit) = object : Source {
    override fun begin(type: Type) = this@onAfterCreate.begin(type)

    override fun afterCreate(item: Any?) {
        this@onAfterCreate.afterCreate(item)
        if (item is T)
            block(item)
    }

    override fun isEnd() =
            this@onAfterCreate.isEnd()

    override fun end(type: Type) =
            this@onAfterCreate.end(type)

    override fun beginNested(label: String, type: Type) =
            this@onAfterCreate.beginNested(label, type)

    override fun endNested(label: String, type: Type) =
            this@onAfterCreate.endNested(label, type)

    override fun getBoolean(label: String) =
            this@onAfterCreate.getBoolean(label)

    override fun getByte(label: String) =
            this@onAfterCreate.getByte(label)

    override fun getShort(label: String) =
            this@onAfterCreate.getShort(label)

    override fun getInt(label: String) =
            this@onAfterCreate.getInt(label)

    override fun getLong(label: String) =
            this@onAfterCreate.getLong(label)

    override fun getFloat(label: String) =
            this@onAfterCreate.getFloat(label)

    override fun getDouble(label: String) =
            this@onAfterCreate.getDouble(label)

    override fun getChar(label: String) =
            this@onAfterCreate.getChar(label)

    override fun isNull(label: String) =
            this@onAfterCreate.isNull(label)

    override fun getUnit(label: String) =
            this@onAfterCreate.getUnit(label)

    override fun getString(label: String) =
            this@onAfterCreate.getString(label)
}

/**
 * Intercepts the [SuspendSource.afterCreate] invocation, checks and casts to [T].
 */
inline fun <reified T> SuspendSource.onAfterCreate(crossinline block: (T) -> Unit) = object : SuspendSource {
    override suspend fun begin(type: Type) = this@onAfterCreate.begin(type)

    override suspend fun afterCreate(item: Any?) {
        this@onAfterCreate.afterCreate(item)
        if (item is T)
            block(item)
    }

    override suspend fun isEnd() =
            this@onAfterCreate.isEnd()

    override suspend fun end(type: Type) =
            this@onAfterCreate.end(type)

    override suspend fun beginNested(label: String, type: Type) =
            this@onAfterCreate.beginNested(label, type)

    override suspend fun endNested(label: String, type: Type) =
            this@onAfterCreate.endNested(label, type)

    override suspend fun getBoolean(label: String) =
            this@onAfterCreate.getBoolean(label)

    override suspend fun getByte(label: String) =
            this@onAfterCreate.getByte(label)

    override suspend fun getShort(label: String) =
            this@onAfterCreate.getShort(label)

    override suspend fun getInt(label: String) =
            this@onAfterCreate.getInt(label)

    override suspend fun getLong(label: String) =
            this@onAfterCreate.getLong(label)

    override suspend fun getFloat(label: String) =
            this@onAfterCreate.getFloat(label)

    override suspend fun getDouble(label: String) =
            this@onAfterCreate.getDouble(label)

    override suspend fun getChar(label: String) =
            this@onAfterCreate.getChar(label)

    override suspend fun isNull(label: String) =
            this@onAfterCreate.isNull(label)

    override suspend fun getUnit(label: String) =
            this@onAfterCreate.getUnit(label)

    override suspend fun getString(label: String) =
            this@onAfterCreate.getString(label)
}