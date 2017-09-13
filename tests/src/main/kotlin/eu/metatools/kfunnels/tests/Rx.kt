package eu.metatools.kfunnels.tests

import com.fasterxml.jackson.core.JsonFactory
import eu.metatools.kfunnels.base.ServiceModule
import eu.metatools.kfunnels.std
import eu.metatools.kfunnels.tools.ReceiverModule
import eu.metatools.kfunnels.tools.stream
import eu.metatools.kfunnels.then
import eu.metatools.kfunnels.tools.json.JsonSource
import eu.metatools.kfunnels.tools.json.get
import java.net.URL


fun main(args: Array<String>) {

    // Compose modules to allow resolving receiver
    val module = (ReceiverModule then ServiceModule).std

    // Create an observable of events
    val observable = module.stream<Event> {
        // A new source is a JSON source for a parser on a URL, original labels are converted.
        JsonSource(JsonFactory().createParser(URL("https://app.eurofurence.org/Api/v2/Events")))
    }

    // Subscribe and print some elements
    observable.skip(40)
            .take(5)
            .subscribe { println("First $it") }

    observable.skip(42)
            .take(5)
            .subscribe { println("Second $it") }

    // A nested JSON data structure
    val nestedAccess = """{"x":1,"y":[1,{"a": [1,2,3,4,5]}]}"""

    // Create an observable of ints that are nested within
    val nestedAccessObservable = module.stream<Int> {
        JsonSource(JsonFactory().createParser(nestedAccess)["y/1/a"])
    }

    // Subscribe to all the integers
    nestedAccessObservable.subscribe { println(it) }


}