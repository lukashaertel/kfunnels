![kfunnels](https://raw.githubusercontent.com/lukashaertel/kfunnels/resource/kfunnels_logo_small_text.png) [![Release](https://jitpack.io/v/lukashaertel/kfunnels.svg)](https://jitpack.io/#lukashaertel/kfunnels)


Generates serialization and unserialization for Kotlin classes via annotation processing. Check out the [wiki](https://github.com/lukashaertel/kfunnels/wiki) for more details.

## Include in your project
The artifacts are available via [JitPack](https://jitpack.io/). The runtime (funnelers, sources and sinks) should be linked as a `compile` dependency, the processor as a `kapt` dependency. You'll probably want to include Guava, as it's type tokens help kfunnels to automatically infer the [type](https://github.com/lukashaertel/kfunnels/wiki/Type) of generic classes like lists.

```groovy
apply plugin: "kotlin-kapt"

dependencies {
    compile 'com.github.lukashaertel.kfunnels:rt:0.1.0'
    kapt 'com.github.lukashaertel.kfunnels:proc:0.1.0'
    
    // Include this dependency to support inferring the Type from Guava's TypeToken
    compile 'com.google.guava:guava:23.0'
    
    // Include this for RxKotlin tools
    compile 'io.reactivex.rxjava2:rxkotlin:2.1.0'
    // Include this for JSON (de)serialization tools.
    compile 'com.fasterxml.jackson.core:jackson-core:2.9.0.pr4'
}

kapt {
    correctErrorTypes = true
}
```

Dependencies to Guava and other libraries are kept optional to publish everything in one artifact without automatically cluttering the build with potentially unwanted depdendencies. Keep in mind that the used versions should be compatible.

## Define the data

*kfunnels* supports primitives, enums, some collection types, variables that are themselves `@Funnelable` and variables where instances are funnelable at runtime.

```kotlin
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
```

## Use

Read and write using the generated module (with standard type support) or use the Java Service
registry to serialize and deserialize the type.

```kotlin
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
```

### Android
Check out the [android co-repository](https://github.com/lukashaertel/kfunnels-android) for a demo app using [RxKotlin](https://github.com/ReactiveX/RxKotlin) observables, read from the web, deserialized from JSON, and fed into a nice list.

## Output

```text
Another(i=Thing(i=1, j=3.4), s=Left(i=3))
Another(i=Thing(i=1, j=3.4), s=Right(j=4.5))

[BEGIN, BEGIN_NESTED, BEGIN, 1, 3.4, END, END_NESTED, BEGIN_NESTED, eu.metatools.kfunnels.tests.Left, BEGIN, 3, END, END_NESTED, END]
[BEGIN, BEGIN_NESTED, BEGIN, 1, 3.4, END, END_NESTED, BEGIN_NESTED, eu.metatools.kfunnels.tests.Right, BEGIN, 4.5, END, END_NESTED, END]

Another(i=Thing(i=1, j=3.4), s=Left(i=3))
Another(i=Thing(i=1, j=3.4), s=Right(j=4.5))

Container(items=[Left(i=1), Right(j=2.3), Left(i=4), null])

[BEGIN, BEGIN_NESTED, BEGIN, IS_NOT_NULL, BEGIN_NESTED, eu.metatools.kfunnels.tests.Left?, BEGIN, 1, END, END_NESTED, IS_NOT_NULL, BEGIN_NESTED, eu.metatools.kfunnels.tests.Right?, BEGIN, 2.3, END, END_NESTED, IS_NOT_NULL, BEGIN_NESTED, eu.metatools.kfunnels.tests.Left?, BEGIN, 4, END, END_NESTED, IS_NULL, END, END_NESTED, END]

Container(items=[Left(i=1), Right(j=2.3), Left(i=4), null])

Generic(item=100)

[BEGIN, BEGIN_NESTED, 100, END_NESTED, END]

Generic(item=100)

{s=Left(i=3), i=Thing(i=1, j=3.4)}

Another(i=Thing(i=1, j=3.4), s=Left(i=3))

-2011054348
```

## Define output and input
Outputs are defined as `Sink`s. A simple example is the list sink, where all items are stored in a list (with some additional tokens to mark special events). There is also a class `SuspendSink` that provides the exact same signatures but suspended. The input class `Source` has matching methods. For nested elements it allows for type substitution, so that an implementation type may be unfunneled instead of an interface.

```kotlin
/**
 * Sink that receives items into a list. Use [reset] to get the list and reset the buffer.
 */
class ListSink : Sink {
    private val list = arrayListOf<Any?>()

    fun reset(): List<Any?> {
        val result = list.toList()
        list.clear()
        return result
    }

    override fun begin(type: Type) {
        list += Event.BEGIN
    }

    override fun end(type: Type) {
        list += Event.END
    }

    override fun beginNested(label: String, type: Type, value: Any?): Boolean {
        list += Event.BEGIN_NESTED
        if (!type.isTerminal())
            list += type.forInstance(value)
        return true
    }

    override fun endNested(label: String, type: Type, value: Any?) {
        list += Event.END_NESTED
    }

    override fun putBoolean(label: String, value: Boolean) {
        list += value
    }

    override fun putByte(label: String, value: Byte) {
        list += value
    }

    override fun putShort(label: String, value: Short) {
        list += value
    }

    override fun putInt(label: String, value: Int) {
        list += value
    }

    override fun putLong(label: String, value: Long) {
        list += value
    }

    override fun putFloat(label: String, value: Float) {
        list += value
    }

    override fun putDouble(label: String, value: Double) {
        list += value
    }

    override fun putChar(label: String, value: Char) {
        list += value
    }

    override fun putNull(label: String, isNull: Boolean) {
        if (isNull)
            list += Event.IS_NULL
        else
            list += Event.IS_NOT_NULL
    }

    override fun putUnit(label: String, value: Unit) {
        list += value
    }

    override fun putString(label: String, value: String) {
        list += value
    }
}
```
