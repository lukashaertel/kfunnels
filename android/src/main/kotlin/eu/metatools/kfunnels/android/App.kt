package eu.metatools.kfunnels.android

import android.app.Application
import android.content.Intent
import eu.metatools.kfunnels.base.StdlibModule
import eu.metatools.kfunnels.read
import eu.metatools.kfunnels.tools.PrintSink
import eu.metatools.kfunnels.tools.android.IntentSink
import eu.metatools.kfunnels.tools.android.IntentSource
import eu.metatools.kfunnels.write

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        val intent = Intent()
        IntentSink(intent).let {
            StdlibModule.write(it, 42 to "hello")
        }

        val pair = IntentSource(intent).let {
            StdlibModule.read<Pair<Int, String>>(it)
        }

        println(pair)

        PrintSink.labeled.let {
            StdlibModule.write(it, pair)
        }
    }
}