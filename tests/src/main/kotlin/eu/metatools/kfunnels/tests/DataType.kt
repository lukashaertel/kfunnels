package eu.metatools.kfunnels.tests

import eu.metatools.kfunnels.*
import eu.metatools.kfunnels.base.*

enum class Color {
    Red, Green, Blue
}

//@Funnelable
//data class Thing(val i: Int, val j: Float)
//
//@Funnelable
//data class Container<T, U>(val t: T, val u: U, val c: Color)

fun main(args: Array<String>) {
    val l = listOf(listOf(1, 2, 3), listOf(2, 3, 4))
    val t = Type.list(Type.list(Type.int))
    val f = StdlibModule.resolve<List<List<Int>>>(t)

    f.write(StdlibModule, t, PrintSink.sequence, l)

//    // Make the container object.
//    val c = Container(Thing(10, 2.3f), listOf(1, 2, 3), Color.Red)
//    val s = ListSink()
//
//    // Write to sink
//    ServiceModule.std.write(s, c)
}
