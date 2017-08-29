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