package eu.metatools.kfunnels.utils


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
        if (!state.isSome)
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
        if (!state.isSome)
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
