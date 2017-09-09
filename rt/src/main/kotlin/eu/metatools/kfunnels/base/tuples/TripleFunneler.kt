package eu.metatools.kfunnels.base.tuples

import eu.metatools.kfunnels.*

object TripleFunneler : Funneler<Triple<*, *, *>> {
    override fun read(module: Module, type: Type, source: Source): Triple<*, *, *>
            = source.markAround(type) {
        val (t1, t2, t3) = type.args

        val first = if (t1.nullable)
            source.getNullableNested(module, module.resolve<Any>(t1), "first", t1)
        else
            source.getNested(module, module.resolve<Any>(t1), "first", t1)

        val second = if (t2.nullable)
            source.getNullableNested(module, module.resolve<Any>(t2), "second", t2)
        else
            source.getNested(module, module.resolve<Any>(t2), "second", t2)

        val third = if (t3.nullable)
            source.getNullableNested(module, module.resolve<Any>(t1), "third", t1)
        else
            source.getNested(module, module.resolve<Any>(t1), "third", t1)

        Triple(first, second, third)
    }

    suspend override fun read(module: Module, type: Type, source: SuspendSource): Triple<*, *, *>
            = source.markAround(type) {
        val (t1, t2, t3) = type.args

        val first = if (t1.nullable)
            source.getNullableNested(module, module.resolve<Any>(t1), "first", t1)
        else
            source.getNested(module, module.resolve<Any>(t1), "first", t1)

        val second = if (t2.nullable)
            source.getNullableNested(module, module.resolve<Any>(t2), "second", t2)
        else
            source.getNested(module, module.resolve<Any>(t2), "second", t2)

        val third = if (t3.nullable)
            source.getNullableNested(module, module.resolve<Any>(t1), "third", t1)
        else
            source.getNested(module, module.resolve<Any>(t1), "third", t1)

        Triple(first, second, third)
    }

    override fun write(module: Module, type: Type, sink: Sink, item: Triple<*, *, *>)
            = sink.markAround(type, item) {
        val (t1, t2, t3) = type.args

        if (t1.nullable)
            sink.putNullableNested(module, module.resolve<Any>(t1), "first", t1, item.first as Any)
        else
            sink.putNested(module, module.resolve<Any>(t1), "first", t1, item.first as Any)


        if (t2.nullable)
            sink.putNullableNested(module, module.resolve<Any>(t2), "second", t2, item.second as Any)
        else
            sink.putNested(module, module.resolve<Any>(t2), "second", t2, item.second as Any)

        if (t3.nullable)
            sink.putNullableNested(module, module.resolve<Any>(t3), "third", t3, item.third as Any)
        else
            sink.putNested(module, module.resolve<Any>(t3), "third", t3, item.third as Any)
    }

    suspend override fun write(module: Module, type: Type, sink: SuspendSink, item: Triple<*, *, *>)
            = sink.markAround(type, item) {
        val (t1, t2, t3) = type.args

        if (t1.nullable)
            sink.putNullableNested(module, module.resolve<Any>(t1), "first", t1, item.first as Any)
        else
            sink.putNested(module, module.resolve<Any>(t1), "first", t1, item.first as Any)


        if (t2.nullable)
            sink.putNullableNested(module, module.resolve<Any>(t2), "second", t2, item.second as Any)
        else
            sink.putNested(module, module.resolve<Any>(t2), "second", t2, item.second as Any)

        if (t3.nullable)
            sink.putNullableNested(module, module.resolve<Any>(t3), "third", t3, item.third as Any)
        else
            sink.putNested(module, module.resolve<Any>(t3), "third", t3, item.third as Any)
    }
}