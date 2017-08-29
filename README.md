# kfunnels

Generates serialization and unserialization for kotlin classes via annotation processing.


## Define the data

Primitive, collection types, and @Funnelable types are supported.

```kotlin
package eu.metatools.kfunnels.tests

import eu.metatools.kfunnels.*
import eu.metatools.kfunnels.base.*
import java.util.*

@Funnelable
data class DataType(
        val x1: Boolean,
        val x2: Byte,
        val x3: Short,
        val x4: Int,
        val x5: Long,
        val x6: Float?,
        val x7: Double,
        val x8: Char,
        val x9: Unit,
        val x10: String,
        val x11: List<Int>?) {
    var x12: Int? = 0
}

@Funnelable
data class Rec(
        val a: Int,
        val b: Rec?)
```

# Use

Read and write using the generated module (with standard type support) or use the Java Service
registry to serialize and deserialize the type.

```kotlin
fun main(args: Array<String>) {

    // Make data item
    val original = DataType(true, 23, 43, 124, 512313L, null, 2.0, 'a', Unit, "Hallo", listOf(1, 2, 3))
    original.x12 = 5555

    // Write using direct module reference
    TestsModule.std.write(PrintSeqSink, original)
    println()

    // Write using services
    ServiceModule.std.write(PrintSeqSink, original)
    println()

    // Clone using forward/backward path
    val fwdBwd = ListSink().let {
        TestsModule.std.write(it, original)
        TestsModule.std.read<DataType>(ListSource(it.reset()))
    }

    println(fwdBwd)
    println()

    // Make a recursive data item
    val rec = Rec(1, Rec(2, Rec(3, null)))

    // Write to console
    TestsModule.std.write(PrintLabelSink, rec)
    println()

    // Clone using forward/backward path
    val recFwdBwd = ListSink().let {
        TestsModule.std.write(it, rec)
        TestsModule.std.read<Rec>(ListSource(it.reset()))
    }

    println(recFwdBwd)
    println()
}
```

# Output

```text
Boolean true
Byte 23
Short 43
Int 124
Long 512313
Null true
Double 2.0
Char a
Unit
String Hallo
Null false
Begin nested
Int 3
Int 1
Int 2
Int 3
End nested
Null false
Int 5555

Boolean true
Byte 23
Short 43
Int 124
Long 512313
Null true
Double 2.0
Char a
Unit
String Hallo
Null false
Begin nested
Int 3
Int 1
Int 2
Int 3
End nested
Null false
Int 5555

DataType(x1=true, x2=23, x3=43, x4=124, x5=512313, x6=null, x7=2.0, x8=a, x9=kotlin.Unit, x10=Hallo, x11=[1, 2, 3])

Int a=1
Null b: false
Begin nested b
Int a=2
Null b: false
Begin nested b
Int a=3
Null b: true
End nested
End nested

Rec(a=1, b=Rec(a=2, b=Rec(a=3, b=null)))
```
