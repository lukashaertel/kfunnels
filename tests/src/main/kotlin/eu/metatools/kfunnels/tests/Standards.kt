package eu.metatools.kfunnels.tests

import eu.metatools.kfunnels.base.PrintSink
import eu.metatools.kfunnels.base.StdlibModule
import eu.metatools.kfunnels.write

fun main(args: Array<String>) {
    // Write a standard pair
    StdlibModule.write(PrintSink.labeled, Pair(120, "Hello"))

    // Write a triple, since type detection cannot find nullables in the arguments, the type
    // needs to be mapped
    StdlibModule.write(PrintSink.labeled, Triple(2.34, Pair<Int?, Int>(null, 2), false)) {
        it.mapArg(1) { it.keyNullable() }
    }

    // Print a standard list
    StdlibModule.write(PrintSink.sequence, listOf(2, 3, 46, 5, 43, 2, 0))
}