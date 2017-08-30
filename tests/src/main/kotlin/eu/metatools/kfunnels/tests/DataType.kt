package eu.metatools.kfunnels.tests

import eu.metatools.kfunnels.*
import eu.metatools.kfunnels.base.*

interface Show {
    fun show()
}

@Funnelable
data class Thing(val i: Int, val j: Float) : Show {
    override fun show() {
        println("Hello, my int is $i, my float is $j")
    }
}

@Funnelable
data class Container<T : Show, U>(val t: T, val u: U)

fun main(args: Array<String>) {

    // Make the container object.
    val c = Container(Thing(10, 2.3f), listOf(1, 2, 3))
    val s = JsonSink(System.out)

    // Write to sink
    ServiceModule.std.write(s, c)
}
