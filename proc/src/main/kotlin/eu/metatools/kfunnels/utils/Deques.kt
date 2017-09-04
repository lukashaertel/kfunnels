package eu.metatools.kfunnels.utils

import java.util.*

/**
 * Gets the element the top element.
 */
fun <E> List<E>.peekUnder(): E {
    if (size < 2)
        throw EmptyStackException()
    return get(size - 2)
}