package eu.metatools.kfunnels.utils

/**
 * An value of either [A] or [B].
 */
sealed class Either<A, B> {
    /**
     * The actual value.
     */
    abstract val value: Any?

    /**
     * True if left (value of [A]).
     */
    abstract val isLeft: Boolean

    /**
     * True if right (value of [B]).
     */
    abstract val isRight: Boolean
}

/**
 * An instance of an [A] value for [Either].
 */
data class Left<A, B>(val left: A) : Either<A, B>() {
    override val value get() = left

    override val isLeft get() = true
    override val isRight get() = false
}

/**
 * An instance of a [B] value for [Either].
 */
data class Right<A, B>(val right: B) : Either<A, B>() {
    override val value get() = right

    override val isLeft get() = false
    override val isRight get() = true
}

/**
 * A pending resolution of [Either].
 */
sealed class ResolvedLeft<A, B, T>

/**
 * [Either] resolved as *had left*.
 */
data class SomeLeft<A, B, T>(val it: T) : ResolvedLeft<A, B, T>()

/**
 * [Either] resolved as *didn't have left*.
 */
data class NoneLeft<A, B, T>(val right: B) : ResolvedLeft<A, B, T>()

/**
 * A pending resolution of [Either].
 */
sealed class ResolvedRight<A, B, T>

/**
 * [Either] resolved as *had right*.
 */
data class SomeRight<A, B, T>(val it: T) : ResolvedRight<A, B, T>()

/**
 * [Either] resolved as *didn't have right*.
 */
data class NoneRight<A, B, T>(val left: A) : ResolvedRight<A, B, T>()

/**
 * Prepares a resolution of both [Left] and [Right].
 */
inline infix fun <A, B, T> Either<A, B>.onLeft(block: (A) -> T): ResolvedLeft<A, B, T> =
        when (this) {
            is Left<A, B> -> SomeLeft<A, B, T>(block(left))
            is Right<A, B> -> NoneLeft<A, B, T>(right)
        }

/**
 * Prepares a resolution of both [Left] and [Right].
 */
inline infix fun <A, B, T> Either<A, B>.onRight(block: (B) -> T): ResolvedRight<A, B, T> =
        when (this) {
            is Right<A, B> -> SomeRight<A, B, T>(block(right))
            is Left<A, B> -> NoneRight<A, B, T>(left)
        }

/**
 * Finishes a resolution of both [Left] and [Right].
 */
inline infix fun <A, B, T> ResolvedRight<A, B, T>.onLeft(block: (A) -> T) =
        when (this) {
            is SomeRight<A, B, T> -> it
            is NoneRight<A, B, T> -> block(left)
        }

/**
 * Finishes a resolution of both [Left] and [Right].
 */
inline infix fun <A, B, T> ResolvedLeft<A, B, T>.onRight(block: (B) -> T) =
        when (this) {
            is SomeLeft<A, B, T> -> it
            is NoneLeft<A, B, T> -> block(right)
        }

/**
 * Returns [Some] if the receiver is [Left], [None] otherwise.
 */
operator fun <A, B> Either<A, B>.component1() =
        when (this) {
            is Left<A, B> -> Some(left)
            is Right<A, B> -> None<A>()
        }

/**
 * Returns [Some] if the receiver is [Right], [None] otherwise.
 */
operator fun <A, B> Either<A, B>.component2() =
        when (this) {
            is Right<A, B> -> Some(right)
            is Left<A, B> -> None<B>()
        }

/**
 * Folds with the appropriate method.
 */
inline fun <A, B, U> Either<A, B>.fold(onLeft: (A) -> U, onRight: (B) -> U) =
        when (this) {
            is Left<A, B> -> onLeft(left)
            is Right<A, B> -> onRight(right)
        }