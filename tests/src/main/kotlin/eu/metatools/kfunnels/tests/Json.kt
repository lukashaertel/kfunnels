package eu.metatools.kfunnels.tests

import com.fasterxml.jackson.core.JsonFactory
import eu.metatools.kfunnels.Funnelable
import eu.metatools.kfunnels.base.ServiceModule
import eu.metatools.kfunnels.read
import eu.metatools.kfunnels.std
import eu.metatools.kfunnels.tools.JsonSink
import eu.metatools.kfunnels.tools.JsonSource
import eu.metatools.kfunnels.tools.ListSink
import eu.metatools.kfunnels.write
import java.io.StringWriter

@Funnelable
data class Element(val v: Int)

@Funnelable
data class Structure(val i: Int, val j: Int, val k: Element)

fun main(args: Array<String>) {
//    // Make a class that uses lists
//    val container = Container(listOf(Left(1), Right(2.3f), Left(4), null))
//
//    // Print the original item
//    println(container)
//
//    JsonFactory().createGenerator(System.out).use {
//        JsonSink(it).let {
//            ServiceModule.std.write(it, container)
//        }
//    }

    val structure = JsonFactory().createParser("""{"k":{"v":100}, "i":2, "j":3}""").use {
        ServiceModule.std.read<Structure>(JsonSource(it))
    }

    println(structure)

}