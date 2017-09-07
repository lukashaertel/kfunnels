package eu.metatools.kfunnels.tests

import com.fasterxml.jackson.core.JsonFactory
import eu.metatools.kfunnels.Funnelable
import eu.metatools.kfunnels.base.ServiceModule
import eu.metatools.kfunnels.read
import eu.metatools.kfunnels.std
import eu.metatools.kfunnels.tools.json.JsonSink
import eu.metatools.kfunnels.tools.json.JsonSource
import eu.metatools.kfunnels.write
import java.io.StringWriter

@Funnelable
open class Element(val v: Int) {
    override fun toString() = "Element(v=$v)"
}

@Funnelable
data class Structure(val i: Int, val j: Int, val k: Element)

fun main(args: Array<String>) {
    // Creat ea simple structural element
    val structure = Structure(2, 3, Element(100))

    // Print the original item
    println(structure)

    // Write to a string writer, then get its content
    val structureJson = StringWriter().use {
        JsonFactory().createGenerator(it).use {
            JsonSink(it).let {
                ServiceModule.std.write(it, structure)
            }
        }
        it.toString()
    }

    // Print the JSON string.
    println(structureJson)

    // Parse a clone from the JSON string
    val clone = JsonFactory().createParser(structureJson).use {
        JsonSource(it).let {
            ServiceModule.std.read<Structure>(it)
        }
    }

    // Print that clone
    println(clone)

    // A static string that is the same element but with differing label order
    val scrambledJson = """{"k":{"_type":"eu.metatools.kfunnels.tests.Element","_it":{"v":100}},"j":3,"i":2}"""

    // Parse a clone from the scrambled string
    val scrambledClone = JsonFactory().createParser(scrambledJson).use {
        JsonSource(it).let {
            ServiceModule.std.read<Structure>(it)
        }
    }

    // Print that one as well
    println(scrambledClone)


    // Make some data that uses lists
    val container = Container(listOf(Left(1), Right(2.3f), Left(4), null))

    // Print the original item
    println(container)

    // Convert to JSON
    val containerJson = StringWriter().use {
        JsonFactory().createGenerator(it).use {
            JsonSink(it).let {
                ServiceModule.std.write(it, container)
            }
        }
        it.toString()
    }

    // Print converted JSON
    println(containerJson)

    // Clone from the string
    val containerClone = JsonFactory().createParser(containerJson).use {
        JsonSource(it).let {
            ServiceModule.std.read<Container>(it)
        }
    }

    // Print again
    println(containerClone)

    // Make a simple list
    val list = listOf(1, 2, 4, 5, 6)

    // Print the original item
    println(container)

    // Convert into list
    val listJson = StringWriter().use {
        JsonFactory().createGenerator(it).use {
            JsonSink(it).let {
                ServiceModule.std.write(it, list)
            }
        }
        it.toString()
    }

    // Print list string
    println(listJson)

    // Parse list
    val listClone = JsonFactory().createParser(listJson).use {
        JsonSource(it).let {
            ServiceModule.std.read<List<Int>>(it)
        }
    }

    // Print parsed list
    println(listClone)
}