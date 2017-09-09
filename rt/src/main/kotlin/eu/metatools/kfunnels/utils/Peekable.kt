package eu.metatools.kfunnels.utils

sealed class Option<T> {
    abstract val isPresent: Boolean
    abstract val value: T
}

data class Some<T>(val t: T) : Option<T>() {
    override val isPresent: Boolean
        get() = true
    override val value: T
        get() = t
}

class None<T>() : Option<T>() {
    override val isPresent: Boolean
        get() = false
    override val value: T
        get() = error("Value is not present.")
}

inline fun <T, U> Option<T>.map(present: (T) -> U, absent: () -> U) =
        if (isPresent)
            present(value)
        else
            absent()


fun <T> Option<T>.orDefault(default: () -> T) =
        if (isPresent)
            value
        else
            default()

/**
 * Provides peekability for non-peekable item sources.
 */
abstract class Peekable<T> {
    /**
     * The element that was peeked last or absent.
     */
    private var state: Option<T> = None()

    /**
     * Prepare the state,
     */
    private fun prepareNext() {
        if (!state.isPresent)
            state = Some(provide())
    }

    fun takeNext(): T {
        prepareNext()
        val result = state.value
        state = None()
        return result
    }

    fun peekNext(): T {
        prepareNext()
        return state.value
    }

    abstract fun provide(): T
}

/**
 * Provides peekability for non-peekable suspended item sources.
 */
abstract class SuspendPeekable<T> {
    /**
     * The element that was peeked last or absent.
     */
    private var state: Option<T> = None()

    /**
     * Prepare the state,
     */
    private suspend fun prepareNext() {
        if (!state.isPresent)
            state = Some(provide())
    }

    suspend fun takeNext(): T {
        prepareNext()
        val result = state.value
        state = None()
        return result
    }

    suspend fun peekNext(): T {
        prepareNext()
        return state.value
    }

    abstract suspend fun provide(): T
}

/**
 * Creates a peekable from the provider.
 */
inline fun <T> peekable(crossinline block: () -> T) = object : Peekable<T>() {
    override fun provide() = block()
}
