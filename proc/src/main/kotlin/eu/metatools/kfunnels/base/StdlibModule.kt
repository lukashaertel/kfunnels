package eu.metatools.kfunnels.base

import eu.metatools.kfunnels.Funneler
import eu.metatools.kfunnels.Module
import eu.metatools.kfunnels.Type
import eu.metatools.kfunnels.base.lists.ListFunneler
import eu.metatools.kfunnels.base.lists.ListNullableFunneler
import eu.metatools.kfunnels.base.lists.MutableListFunneler
import eu.metatools.kfunnels.base.lists.MutableListNullableFunneler
import eu.metatools.kfunnels.base.sets.MutableSetFunneler
import eu.metatools.kfunnels.base.sets.MutableSetNullableFunneler
import eu.metatools.kfunnels.base.sets.SetFunneler
import eu.metatools.kfunnels.base.sets.SetNullableFunneler
import java.util.*

/**
 * The standard library types are resolved in this module. This includes primitives and list types.
 */
object StdlibModule : Module {
    override fun <T> resolve(type: Type): Funneler<T> {
        // Handle primitive types
        @Suppress("unchecked_cast")
        when (type.primitiveCode) {
            Type.primitiveBoolean ->
                return BooleanFunneler as Funneler<T>

            Type.primitiveByte ->
                return ByteFunneler as Funneler<T>

            Type.primitiveShort ->
                return ShortFunneler as Funneler<T>

            Type.primitiveInt ->
                return IntFunneler as Funneler<T>

            Type.primitiveLong ->
                return LongFunneler as Funneler<T>

            Type.primitiveFloat ->
                return FloatFunneler as Funneler<T>

            Type.primitiveDouble ->
                return DoubleFunneler as Funneler<T>

            Type.primitiveChar ->
                return CharFunneler as Funneler<T>

            Type.primitiveUnit ->
                return UnitFunneler as Funneler<T>

            Type.primitiveString ->
                return StringFunneler as Funneler<T>
        }

        // Handle generic list types
        @Suppress("unchecked_cast")
        when (type.kClass) {
        // Array list, also default implementation of mutable list and mutable collection
            MutableCollection::class,
            MutableList::class,
            ArrayList::class ->
                if (type.arg.nullable)
                    return MutableListNullableFunneler(::ArrayList) as Funneler<T>
                else
                    return MutableListFunneler(::ArrayList) as Funneler<T>

        // Linked list
            LinkedList::class ->
                if (type.arg.nullable)
                    return MutableListNullableFunneler(::LinkedList) as Funneler<T>
                else
                    return MutableListFunneler(::LinkedList) as Funneler<T>

        // Immutable list, also default for immutable collection
            Collection::class,
            List::class ->
                if (type.arg.nullable)
                    return ListNullableFunneler as Funneler<T>
                else
                    return ListFunneler as Funneler<T>
        }


        // Handle generic set types
        @Suppress("unchecked_cast")
        when (type.kClass) {
        // Hash set, also default implementation of mutable set
            MutableSet::class,
            HashSet::class ->
                if (type.arg.nullable)
                    return MutableSetNullableFunneler(::HashSet) as Funneler<T>
                else
                    return MutableSetFunneler(::HashSet) as Funneler<T>

        // Tree set
            TreeSet::class ->
                if (type.arg.nullable)
                    return MutableSetNullableFunneler(::TreeSet) as Funneler<T>
                else
                    return MutableSetFunneler(::TreeSet) as Funneler<T>

        // Immutable set
            Set::class ->
                if (type.arg.nullable)
                    return SetNullableFunneler as Funneler<T>
                else
                    return SetFunneler as Funneler<T>
        }

        @Suppress("unchecked_cast")
        return NoFunneler as Funneler<T>
    }
}