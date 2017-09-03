package eu.metatools.kfunnels.base

import eu.metatools.kfunnels.Funneler
import eu.metatools.kfunnels.Module
import eu.metatools.kfunnels.Type
import eu.metatools.kfunnels.base.lists.ListFunneler
import eu.metatools.kfunnels.base.lists.ListNullableFunneler
import eu.metatools.kfunnels.base.sets.SetFunneler
import eu.metatools.kfunnels.base.sets.SetNullableFunneler
import eu.metatools.kfunnels.base.tuples.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashSet
import kotlin.reflect.full.isSubclassOf

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

        // Handle tuples
        @Suppress("unchecked_cast")
        when (type.kClass) {
            Pair::class -> when (type.key.nullable to type.value.nullable) {
                false to false -> return PairFunneler as Funneler<T>
                true to false -> return PairFirstNullableFunneler as Funneler<T>
                false to true -> return PairSecondNullableFunneler as Funneler<T>
                true to true -> return PairNullableFunneler as Funneler<T>
            }
            Triple::class -> return TripleFunneler as Funneler<T>
        }

        // Handle any enum type
        @Suppress("unchecked_cast")
        if (type.kClass.isSubclassOf(Enum::class))
            return EnumFunneler as Funneler<T>

        // Handle generic list types
        @Suppress("unchecked_cast")
        when (type.kClass) {
            MutableCollection::class,
            MutableList::class,
            ArrayList::class,
            Collection::class,
            List::class ->
                if (type.arg.nullable)
                    return ListNullableFunneler.forArrayList as Funneler<T>
                else
                    return ListFunneler.forArrayList as Funneler<T>

            LinkedList::class ->
                if (type.arg.nullable)
                    return ListNullableFunneler.forLinkedList as Funneler<T>
                else
                    return ListFunneler.forLinkedList as Funneler<T>

            Vector::class ->
                if (type.arg.nullable)
                    return ListNullableFunneler.forVector as Funneler<T>
                else
                    return ListFunneler.forVector as Funneler<T>

            Stack::class ->
                if (type.arg.nullable)
                    return ListNullableFunneler.forStack as Funneler<T>
                else
                    return ListFunneler.forStack as Funneler<T>
        }


        // Handle generic set types
        @Suppress("unchecked_cast")
        when (type.kClass) {
        // Immutable set
            MutableSet::class,
            HashSet::class,
            Set::class ->
                if (type.arg.nullable)
                    return SetNullableFunneler.forHashSet as Funneler<T>
                else
                    return SetFunneler.forHashSet as Funneler<T>

            LinkedHashSet::class ->
                if (type.arg.nullable)
                    return SetNullableFunneler.forLinkedHashSet as Funneler<T>
                else
                    return SetFunneler.forLinkedHashSet as Funneler<T>
        }

        @Suppress("unchecked_cast")
        return NoFunneler as Funneler<T>
    }
}