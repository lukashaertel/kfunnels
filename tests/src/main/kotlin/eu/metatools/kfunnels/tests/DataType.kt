package eu.metatools.kfunnels.tests

import eu.metatools.kfunnels.*
import eu.metatools.kfunnels.base.*

enum class Color {
    Red, Green, Blue
}

@Funnelable
data class Thing(val i: Int, val j: Float)

@Funnelable
data class Container<T, U>(val t: T, val u: U, val c: Color)

fun main(args: Array<String>) {

    // Make the container object.
    val c = Container(Thing(10, 2.3f), listOf(1, 2, 3), Color.Red)
    val s = JsonSink(System.out)

    // Write to sink
    ServiceModule.std.write(s, c)
}
