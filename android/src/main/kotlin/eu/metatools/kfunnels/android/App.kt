package eu.metatools.kfunnels.android

import android.app.Application
import com.fasterxml.jackson.core.JsonFactory
import eu.metatools.kfunnels.Funnelable
import eu.metatools.kfunnels.ModuleProvider
import eu.metatools.kfunnels.base.ServiceModule
import eu.metatools.kfunnels.read
import eu.metatools.kfunnels.std
import eu.metatools.kfunnels.tools.json.JsonSource
import java.net.URL
import java.util.*

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

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Thread.sleep(5000)

        ServiceModule.extra += AndroidModule

        val events = resources.openRawResource(R.raw.events).use {
            JsonFactory().createParser(it).use {
                JsonSource(it).let {
                    ServiceModule.std.read<List<Event>>(it)
                }
            }
        }
        println(events)
    }
}