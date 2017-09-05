package eu.metatools.kfunnels.tests

import eu.metatools.kfunnels.*
import eu.metatools.kfunnels.base.NoFunneler
import eu.metatools.kfunnels.base.ServiceModule
import eu.metatools.kfunnels.base.lists.ListFunneler
import eu.metatools.kfunnels.base.lists.ListNullableFunneler
import eu.metatools.kfunnels.base.onAfterCreate
import eu.metatools.kfunnels.base.sets.SetFunneler
import eu.metatools.kfunnels.base.sets.SetNullableFunneler
import eu.metatools.kfunnels.tools.ListSink
import eu.metatools.kfunnels.tools.ListSource
import javafx.collections.FXCollections
import javafx.collections.ListChangeListener.Change
import javafx.collections.ObservableList
import javafx.collections.ObservableSet

/**
 * Module that handles [ObservableList] and [ObservableSet].
 */
object FxObservablesModule : Module {
    override fun <T> resolve(type: Type): Funneler<T> {
        @Suppress("unchecked_cast")
        if (type.kClass == ObservableList::class)
            if (type.nullable)
                return ListNullableFunneler { FXCollections.observableList(arrayListOf()) } as Funneler<T>
            else
                return ListFunneler { FXCollections.observableList(arrayListOf()) } as Funneler<T>

        @Suppress("unchecked_cast")
        if (type.kClass == ObservableSet::class)
            if (type.nullable)
                return SetNullableFunneler { FXCollections.observableSet(hashSetOf()) } as Funneler<T>
            else
                return SetFunneler { FXCollections.observableSet(hashSetOf()) } as Funneler<T>

        @Suppress("unchecked_cast")
        return NoFunneler as Funneler<T>
    }

}

fun main(args: Array<String>) {
    // Make a new module that also supports observable collections
    val module = (FxObservablesModule then ServiceModule).std

    // Make a list to serialize
    val items = (1..100).toList()

    // Print the items
    println(items)

    // Serialize to list
    val listItems = ListSink().let {
        module.write(it, items)
        it.reset()
    }

    // Deserialize, also listen to the after-create message
    val cloneItems = module.read<List<Int>>(ListSource(listItems).onAfterCreate<ObservableList<Int>> {
        // Add a listener to the observable list before items will be sent to it
        it.addListener { c: Change<out Int> ->
            println(c)
        }
    }) {
        // Substitute original type so that an observable list is created instead
        it.sub(List::class, ObservableList::class)
    }

    // Print the cloned items
    println(cloneItems)
}