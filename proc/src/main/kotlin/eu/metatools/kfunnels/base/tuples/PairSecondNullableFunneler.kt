package eu.metatools.kfunnels.base.tuples

import eu.metatools.kfunnels.*

object PairSecondNullableFunneler : Funneler<Pair<*, *>> {
    override fun read(module: Module, type: Type, source: Source): Pair<*, *>
            = source.markAround(type) {
        val first = source.getNested(module, module.resolve<Any>(type.key), "first", type.key)
        val second = source.getNullableNested(module, module.resolve<Any>(type.value), "second", type.value)

        Pair(first, second)
    }

    suspend override fun read(module: Module, type: Type, source: SuspendSource): Pair<*, *>
            = source.markAround(type) {
        val first = source.getNested(module, module.resolve<Any>(type.key), "first", type.key)
        val second = source.getNullableNested(module, module.resolve<Any>(type.value), "second", type.value)

        Pair(first, second)
    }

    override fun write(module: Module, type: Type, sink: Sink, item: Pair<*, *>)
            = sink.markAround(type, item) {
        sink.putNested(module, module.resolve<Any>(type.key), "first", type.key, item.first as Any)
        sink.putNullableNested(module, module.resolve<Any>(type.value), "second", type.value, item.second)
    }

    suspend override fun write(module: Module, type: Type, sink: SuspendSink, item: Pair<*, *>)
            = sink.markAround(type, item) {
        sink.putNested(module, module.resolve<Any>(type.key), "first", type.key, item.first as Any)
        sink.putNullableNested(module, module.resolve<Any>(type.value), "second", type.value, item.second)
    }
}