package eu.metatools.kfunnels.tests

import eu.metatools.kfunnels.base.ChannelSink
import eu.metatools.kfunnels.base.ChannelSource
import eu.metatools.kfunnels.base.ServiceModule
import eu.metatools.kfunnels.read
import eu.metatools.kfunnels.std
import eu.metatools.kfunnels.write
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking

fun main(args: Array<String>) = runBlocking {
    // Make an item to send
    val item = Another(Thing(1, 3.4f), Right(4.5f))

    // Make a channel to read from and write to
    val channel = Channel<Any?>()
    val source = ChannelSource(channel)
    val sink = ChannelSink(channel)

    // Start a job that writes the item
    val first = launch(CommonPool) {
        println("Writing $item from first job")
        ServiceModule.std.write(sink, item)
    }

    // Start a job that reads the item
    val second = launch(CommonPool) {
        val received = ServiceModule.std.read<Another>(source)
        println("Received $received in second job")
    }

    // Await both jobs
    first.join()
    second.join()
}