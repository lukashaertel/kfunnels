package eu.metatools.kfunnels.tests

import eu.metatools.kfunnels.*
import eu.metatools.kfunnels.base.*

@Funnelable
data class Container<T, U>(val t: T?, val u: U)

fun main(args: Array<String>) {

    // Make the container object.
    val c = Container(100, "Hello")

    // Write to sink, also set the key (first type argument) to be nullable.
    TestsModule.std.write(PrintLabelSink, c) { it.keyNullable() }

}