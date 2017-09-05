package eu.metatools.kfunnels.tests

import com.fasterxml.jackson.core.JsonFactory
import eu.metatools.kfunnels.base.ServiceModule
import eu.metatools.kfunnels.std
import eu.metatools.kfunnels.tests.rx.ReceiverModule
import eu.metatools.kfunnels.tests.rx.stream
import eu.metatools.kfunnels.then
import eu.metatools.kfunnels.tools.JsonSource
import java.net.URL

fun main(args: Array<String>) {

    // Compose modules to allow resolving receiver
    val module = (ReceiverModule then ServiceModule).std

    // Create an observable of events
    val observable = module.stream<Event> {
        // A new source is a JSON source for a parser on a URL
        JsonSource(JsonFactory().createParser(URL("https://app.eurofurence.org/Api/v2/Events")))
    }

    // Subscribe and print some elements.
    observable.skip(40)
            .take(5)
            .subscribe { println("First $it") }

    observable.skip(42)
            .take(5)
            .subscribe { println("Second $it") }
}