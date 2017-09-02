package eu.metatools.kfunnels.base.tuples

import eu.metatools.kfunnels.*

object PairNullableFunneler : Funneler<Pair<*, *>> {
    override fun read(module: Module, type: Type, source: Source): Pair<*, *>
            = source.markAround(type) {
        val first = source.getNullableNested(module, module.resolve<Any>(type.key), "first", type.key)
        val second = source.getNullableNested(module, module.resolve<Any>(type.value), "second", type.value)

        Pair(first, second)
    }

    suspend override fun read(module: Module, type: Type, source: SuspendSource): Pair<*, *>
            = source.markAround(type) {
        val first = source.getNullableNested(module, module.resolve<Any>(type.key), "first", type.key)
        val second = source.getNullableNested(module, module.resolve<Any>(type.value), "second", type.value)

        Pair(first, second)
    }

    override fun write(module: Module, type: Type, sink: Sink, item: Pair<*, *>)
            = sink.markAround(type, item) {
        sink.putNullableNested(module, module.resolve<Any>(type.key), "first", type.key, item.first)
        sink.putNullableNested(module, module.resolve<Any>(type.value), "second", type.value, item.second)
    }

    suspend override fun write(module: Module, type: Type, sink: SuspendSink, item: Pair<*, *>)
            = sink.markAround(type, item) {
        sink.putNullableNested(module, module.resolve<Any>(type.key), "first", type.key, item.first)
        sink.putNullableNested(module, module.resolve<Any>(type.value), "second", type.value, item.second)
    }
}