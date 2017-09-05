package eu.metatools.kfunnels.tests

import com.fasterxml.jackson.core.JsonFactory
import eu.metatools.kfunnels.Funnelable
import eu.metatools.kfunnels.base.ServiceModule
import eu.metatools.kfunnels.base.onAfterCreate
import eu.metatools.kfunnels.read
import eu.metatools.kfunnels.std
import eu.metatools.kfunnels.then
import eu.metatools.kfunnels.tools.JsonSource
import eu.metatools.kfunnels.tools.ListSource
import javafx.collections.ListChangeListener
import javafx.collections.ListChangeListener.Change
import javafx.collections.ObservableList
import java.net.URL

@Funnelable
data class Event(
        val LastChangeDateTimeUtc: String,
        val Id: String,
        val Slug: String?,
        val Title: String?,
        val SubTitle: String?,
        val Abstract: String?,
        val ConferenceDayId: String?,
        val ConferenceTrackId: String?,
        val ConferenceRoomId: String?,
        val Description: String?,
        val Duration: String?,
        val StartTime: String?,
        val EndTime: String?,
        val StartDateTimeUtc: String?,
        val EndDateTimeUtc: String?,
        val PanelHosts: String?,
        val IsDeviatingFromConBook: Boolean?,
        val BannerImageId: String?,
        val PosterImageId: String?) {
    override fun toString() = Title ?: Id
}

fun main(args: Array<String>) {
    val url = "https://app.eurofurence.org/Api/v2/Events"

    // Make a new module that also supports observable collections
    val module = (FxObservablesModule then ServiceModule).std

    val cloneItems = JsonFactory().createParser(URL(url)).use {
        module.read<List<Event>>(JsonSource(it).onAfterCreate<ObservableList<Event>> {
            // Add a listener to the observable list before items will be sent to it
            it.addListener { c: Change<out Event> ->
                println(c)
            }
        }) {
            // Substitute original type so that an observable list is created instead
            it.sub(List::class, ObservableList::class)
        }
    }

    println("Found: ${cloneItems.size}")
}