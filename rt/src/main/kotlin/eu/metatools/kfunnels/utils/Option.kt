package eu.metatools.kfunnels.utils

/**
 * An optional value [T].
 */
sealed class Option<T> {
    /**
     * True if there is some value.
     */
    abstract val isSome: Boolean

    /**
     * True if there is no value.
     */
    abstract val isNone: Boolean

    /**
     * The value, or raises an error if none.
     */
    abstract val value: T
}

/**
 * An instance of an actual value for [Option].
 */
data class Some<T>(override val value: T) : Option<T>() {
    override val isSome get() = true
    override val isNone get() = false
}

/**
 * An instance of no value for [Option].
 */
class None<T> : Option<T>() {
    override val isSome get() = false
    override val isNone get() = true
    override val value: T
        get() = error("Value is not present.")
}

/**
 * A pending resolution of [Some].
 */
sealed class ResolvedSome<A, T>

/**
 * [Some] resolved as *had some*.
 */
class SomeSome<A, T>(val it: T) : ResolvedSome<A, T>()

/**
 * [Some] resolved as *had none*.
 */
class NoneSome<A, T> : ResolvedSome<A, T>()

/**
 * A pending resolution of [None].
 */
sealed class ResolvedNone<A, T>

/**
 * [None] resolved as *had some*.
 */
class SomeNone<A, T>(val it: T) : ResolvedNone<A, T>()

/**
 * [None] resolved as *had none*.
 */
class NoneNone<A, T>(val value: A) : ResolvedNone<A, T>()

/**
 * Prepares a resolution of both [Some] and [None].
 */
inline infix fun <A, T> Option<A>.onSome(block: (A) -> T): ResolvedSome<A, T> =
        when (this) {
            is Some<A> -> SomeSome<A, T>(block(value))
            is None<A> -> NoneSome<A, T>()
        }

/**
 * Prepares a resolution of both [Some] and [None].
 */
inline infix fun <A, T> Option<A>.onNone(block: () -> T): ResolvedNone<A, T> =
        when (this) {
            is None<A> -> SomeNone<A, T>(block())
            is Some<A> -> NoneNone<A, T>(value)
        }

/**
 * Finishes a resolution of both [Some] and [None].
 */
inline infix fun <A, T> ResolvedNone<A, T>.onSome(block: (A) -> T) =
        when (this) {
            is SomeNone<A, T> -> it
            is NoneNone<A, T> -> block(value)
        }

/**
 * Finishes a resolution of both [Some] and [None].
 */
inline infix fun <A, T> ResolvedSome<A, T>.onNone(block: () -> T) =
        when (this) {
            is SomeSome<A, T> -> it
            is NoneSome<A, T> -> block()
        }

/**
 * Folds with the appropriate method.
 */
inline fun <T, U> Option<T>.fold(onSome: (T) -> U, onNone: () -> U) =
        when (this) {
            is Some<T> -> onSome(value)
            is None<T> -> onNone()
        }