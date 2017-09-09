package eu.metatools.kfunnels.utils

import kotlin.reflect.KClass


/**
 * Gets the [KClass] for the name, also resolves default imported names.
 */
fun kClassFor(name: String) = when (name) {
    "Nothing" -> Nothing::class
    "Any" -> Any::class
    "Boolean" -> Boolean::class
    "Byte" -> Byte::class
    "Short" -> Short::class
    "Int" -> Int::class
    "Long" -> Long::class
    "Float" -> Float::class
    "Double" -> Double::class
    "Char" -> Char::class
    "Unit" -> Unit::class
    "String" -> String::class

// In package kotlin.*
    "Deprecated" -> Deprecated::class
    "DeprecationLevel" -> DeprecationLevel::class
    "DslMarker" -> DslMarker::class
    "ExtensionFunctionType" -> ExtensionFunctionType::class
    "Function" -> Function::class
    "KotlinNullPointerException" -> KotlinNullPointerException::class
    "KotlinVersion" -> KotlinVersion::class
    "Lazy" -> Lazy::class
    "LazyThreadSafetyMode" -> LazyThreadSafetyMode::class
    "NotImplementedError" -> NotImplementedError::class
    "NoWhenBranchMatchedException" -> NoWhenBranchMatchedException::class
    "Pair" -> Pair::class
    "ParameterName" -> ParameterName::class
    "PublishedApi" -> PublishedApi::class
    "ReplaceWith" -> ReplaceWith::class
    "SinceKotlin" -> SinceKotlin::class
    "Suppress" -> Suppress::class
    "Triple" -> Triple::class
    "TypeCastException" -> TypeCastException::class
    "UninitializedPropertyAccessException" -> UninitializedPropertyAccessException::class
    "UnsafeVariance" -> UnsafeVariance::class

// In package kotlin.collections.*
    "AbstractCollection" -> AbstractCollection::class
    "AbstractIterator" -> AbstractIterator::class
    "AbstractList" -> AbstractList::class
    "AbstractMap" -> AbstractMap::class
    "AbstractMutableCollection" -> AbstractMutableCollection::class
    "AbstractMutableList" -> AbstractMutableList::class
    "AbstractMutableMap" -> AbstractMutableMap::class
    "AbstractMutableSet" -> AbstractMutableSet::class
    "AbstractSet" -> AbstractSet::class
    "BooleanIterator" -> BooleanIterator::class
    "ByteIterator" -> ByteIterator::class
    "CharIterator" -> CharIterator::class
    "DoubleIterator" -> DoubleIterator::class
    "FloatIterator" -> FloatIterator::class
    "Grouping" -> Grouping::class
    "IndexedValue" -> IndexedValue::class
    "IntIterator" -> IntIterator::class
    "LongIterator" -> LongIterator::class
    "ShortIterator" -> ShortIterator::class
    "ArrayList" -> ArrayList::class
    "Collection" -> Collection::class
    "HashMap" -> HashMap::class
    "Iterable" -> Iterable::class
    "Iterator" -> Iterator::class
    "LinkedHashMap" -> LinkedHashMap::class
    "LinkedHashSet" -> LinkedHashSet::class
    "List" -> List::class
    "ListIterator" -> ListIterator::class
    "Map" -> Map::class
    "MutableCollection" -> MutableCollection::class
    "MutableIterable" -> MutableIterable::class
    "MutableIterator" -> MutableIterator::class
    "MutableList" -> MutableList::class
    "MutableListIterator" -> MutableListIterator::class
    "MutableMap" -> MutableMap::class
    "MutableSet" -> MutableSet::class
    "RandomAccess" -> RandomAccess::class
    "Set" -> Set::class

    else -> Class.forName(name).kotlin

}
