package eu.metatools.kfunnels.tests

import eu.metatools.kfunnels.Funnelable
import eu.metatools.kfunnels.base.ServiceModule
import eu.metatools.kfunnels.base.std
import eu.metatools.kfunnels.read
import eu.metatools.kfunnels.tools.*
import eu.metatools.kfunnels.write
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

fun main(args: Array<String>) {
    // Make a variant of the class with both Left and Right as an argument
    val itemLeft = Another(Thing(1, 3.4f), Left(3))
    val itemRight = Another(Thing(1, 3.4f), Right(4.5f))

    // Print the items for comparison
    println(itemLeft)
    println(itemRight)

    // Sequence both elements to a list
    val rawLeft = ByteArrayOutputStream().use {
        // Use the service registry, the .std suffix registers primtive types
        ServiceModule.std.write(RawSink(it), itemLeft)
        it.toByteArray()
    }
    val rawRight = ByteArrayOutputStream().use {
        ServiceModule.std.write(RawSink(it), itemRight)
        it.toByteArray()
    }

    // Print the lists
    println(rawLeft.toList())
    println(rawRight.toList())

    // Read both items back from the list
    val cloneLeft = ServiceModule.std.read<Another>(RawSource(ByteArrayInputStream(rawLeft)))
    val cloneRight = ServiceModule.std.read<Another>(RawSource(ByteArrayInputStream(rawRight)))

    // Print the clones as well
    println(cloneLeft)
    println(cloneRight)

    // Make a class that uses lists
    val container = Container(listOf(Left(1), Right(2.3f), Left(4), null))

    // Print the original item
    println(container)

    // Sequence into list
    val rawContainer = ByteArrayOutputStream().use {
        ServiceModule.std.write(RawSink(it), container)
        it.toByteArray()
    }

    // Print the output list
    println(rawContainer.toList())

    // Read the clone form the list
    val cloneContainer = ServiceModule.std.read<Container>(RawSource(ByteArrayInputStream(rawContainer)))

    // Print the clone
    println(cloneContainer)


    // Make a class that uses generics
    val generic = Generic(100)

    // Print that original item
    println(generic)

    // Sequence into list
    val rawGeneric = ByteArrayOutputStream().use {
        ServiceModule.std.write(RawSink(it), generic)
        it.toByteArray()
    }

    // Print the output list
    println(rawGeneric.toList())

    // Read the clone form the list
    val cloneGeneric = ServiceModule.std.read<Generic<Int>>(RawSource(ByteArrayInputStream(rawGeneric)))

    // Print the clone
    println(cloneGeneric)

    val indexed = listOf("a" to 20, "b" to 34, "c" to 50)

    println(indexed)

    val (rawIndexed, index) = ByteArrayOutputStream().use {
        val sink = IndexedRawSink(it, "first")
        ServiceModule.std.write(sink, indexed)
        it.toByteArray() to sink.index()
    }

    println(rawIndexed.toList())
    println(index)

    val item = ByteArrayInputStream(rawIndexed).use {
        it.skip(index.getValue("b").first.toLong())
        ServiceModule.std.read<Pair<String, Int>>(RawSource(it))
    }

    println(item)
}