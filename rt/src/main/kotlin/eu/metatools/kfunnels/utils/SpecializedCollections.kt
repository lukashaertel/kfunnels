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

fun Stack<Int>.incTop() {
    push(pop() + 1)
}

fun Stack<Int>.decTop() {
    push(pop() - 1)
}

fun Stack<Int>.topZero() = peek() == 0