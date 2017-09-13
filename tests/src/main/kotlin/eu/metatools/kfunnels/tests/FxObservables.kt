package eu.metatools.kfunnels.tests

import com.fasterxml.jackson.core.JsonFactory
import eu.metatools.kfunnels.Funnelable
import eu.metatools.kfunnels.base.ServiceModule
import eu.metatools.kfunnels.base.onAfterCreate
import eu.metatools.kfunnels.read
import eu.metatools.kfunnels.base.std
import eu.metatools.kfunnels.then
import eu.metatools.kfunnels.tools.json.JsonSource
import eu.metatools.kfunnels.tools.json.JsonSourceConfig
import javafx.collections.ListChangeListener.Change
import javafx.collections.ObservableList
import java.net.URL

@Funnelable
data class Event(
        val lastChangeDateTimeUtc: String,
        val id: String,
        val slug: String?,
        val title: String?,
        val subTitle: String?,
        val abstract: String?,
        val conferenceDayId: String?,
        val conferenceTrackId: String?,
        val conferenceRoomId: String?,
        val description: String?,
        val duration: String?,
        val startTime: String?,
        val endTime: String?,
        val startDateTimeUtc: String?,
        val endDateTimeUtc: String?,
        val panelHosts: String?,
        val isDeviatingFromConBook: Boolean?,
        val bannerImageId: String?,
        val posterImageId: String?) {
    override fun toString() = title ?: id
}

fun main(args: Array<String>) {
    val url = "https://app.eurofurence.org/Api/v2/Events"

    // Make a new module that also supports observable collections
    val module = (FxObservablesModule then ServiceModule)

    val cloneItems = JsonFactory().createParser(URL(url)).use {
        module.read<List<Event>>(JsonSource(it, JsonSourceConfig.upperToLower).onAfterCreate<ObservableList<Event>> {
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