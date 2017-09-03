package eu.metatools.kfunnels.tests

import eu.metatools.kfunnels.*
import eu.metatools.kfunnels.base.*
import eu.metatools.kfunnels.tools.*

/**
 * A simple thing, it is completely terminal
 */
@Funnelable
data class Thing(val i: Int, val j: Float)

/**
 * Define an interface
 */
interface Some {}

/**
 * Define an instance of an interface.
 */
@Funnelable
data class Left(val i: Int) : Some

/**
 * Define another instance of an interface.
 */
@Funnelable
data class Right(val j: Float) : Some

/**
 * A thing that has a "non-terminal" variable type [Some] can be [Left] or [Right].
 */
@Funnelable
data class Another(val i: Thing, val s: Some)

/**
 * A container of some items.
 */
@Funnelable
data class Container(val items: List<Some?>)

/**
 * A type that has a generic argument.
 */
@Funnelable
data class Generic<T>(val item: T)

fun main(args: Array<String>) {
    // Make a variant of the class with both Left and Right as an argument
    val itemLeft = Another(Thing(1, 3.4f), Left(3))
    val itemRight = Another(Thing(1, 3.4f), Right(4.5f))

    // Print the items for comparison
    println(itemLeft)
    println(itemRight)

    // Sequence both elements to a list
    val listLeft = ListSink().let {
        // Use the service registry, the .std suffix registers primtive types
        ServiceModule.std.write(it, itemLeft)
        it.reset()
    }
    val listRight = ListSink().let {
        ServiceModule.std.write(it, itemRight)
        it.reset()
    }

    // Print the lists
    println(listLeft)
    println(listRight)

    // Read both items back from the list
    val cloneLeft = ServiceModule.std.read<Another>(ListSource(listLeft))
    val cloneRight = ServiceModule.std.read<Another>(ListSource(listRight))

    // Print the clones as well
    println(cloneLeft)
    println(cloneRight)

    // Make a class that uses lists
    val container = Container(listOf(Left(1), Right(2.3f), Left(4), null))

    // Print the original item
    println(container)

    // Sequence into list
    val listContainer = ListSink().let {
        ServiceModule.std.write(it, container)
        it.reset()
    }

    // Print the output list
    println(listContainer)

    // Read the clone form the list
    val cloneContainer = ServiceModule.std.read<Container>(ListSource(listContainer))

    // Print the clone
    println(cloneContainer)


    // Make a class that uses generics
    val generic = Generic(100)

    // Print that original item
    println(generic)

    // Sequence into list
    val listGeneric = ListSink().let {
        ServiceModule.std.write(it, generic)
        it.reset()
    }

    // Print the output list
    println(listGeneric)

    // Read the clone form the list
    val cloneGeneric = ServiceModule.std.read<Generic<Int>>(ListSource(listGeneric))

    // Print the clone
    println(cloneGeneric)

    // Get a map of the left item
    val mapLeft = MapSink().let {
        ServiceModule.std.write(it, itemLeft)
        it.reset()
    }

    // Print the map
    println(mapLeft)

    // Read clone from map
    val cloneLeftByMap = ServiceModule.std.read<Another>(MapSource(mapLeft))

    // Print the clone
    println(cloneLeftByMap)

    // Print the hash of the container
    println(container.hash())

}
