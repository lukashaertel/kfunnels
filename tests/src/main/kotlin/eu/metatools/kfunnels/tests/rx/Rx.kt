package eu.metatools.kfunnels.tests.rx

import eu.metatools.kfunnels.*
import eu.metatools.kfunnels.base.NoFunneler
import eu.metatools.kfunnels.base.listlike.ListlikeFunneler
import eu.metatools.kfunnels.base.listlike.ListlikeNullableFunneler
import eu.metatools.kfunnels.base.onAfterCreate
import eu.metatools.kfunnels.tests.utils.useIfClosable
import io.reactivex.Observable

/**
 * Configurable receiver.
 * @property onElement Callback for when an element was read.
 * @property onEnd Callback for when reading has ended.
 * @property shouldContinue Flag to indicate if reading should be continued.
 */
data class Receiver<T>(var onElement: (T) -> Unit, var onEnd: () -> Unit, var shouldContinue: Boolean)

/**
 * Read only funneler, streams a source into a receiver, which should be configured on [Source.onAfterCreate].
 */
object ReceiverFunneler : ListlikeFunneler<Receiver<Any>, Receiver<Any>>() {
    override fun create() = Receiver<Any>({}, {}, true)

    override fun begin(source: Receiver<Any>) = source

    override fun end(source: Receiver<Any>): Receiver<Any> {
        source.onEnd()
        return source
    }

    override fun combineContinue(source: Receiver<Any>) = source.shouldContinue

    override fun combineBoolean(target: Receiver<Any>, value: Boolean) = target.onElement(value)

    override fun combineByte(target: Receiver<Any>, value: Byte) = target.onElement(value)

    override fun combineShort(target: Receiver<Any>, value: Short) = target.onElement(value)

    override fun combineInt(target: Receiver<Any>, value: Int) = target.onElement(value)

    override fun combineLong(target: Receiver<Any>, value: Long) = target.onElement(value)

    override fun combineFloat(target: Receiver<Any>, value: Float) = target.onElement(value)

    override fun combineDouble(target: Receiver<Any>, value: Double) = target.onElement(value)

    override fun combineChar(target: Receiver<Any>, value: Char) = target.onElement(value)

    override fun combineUnit(target: Receiver<Any>, value: Unit) = target.onElement(value)

    override fun combineString(target: Receiver<Any>, value: String) = target.onElement(value)

    override fun combineElement(target: Receiver<Any>, value: Any) = target.onElement(value)

    override fun uncombineBooleans(source: Receiver<Any>): Sequence<Boolean> = error("Reading not supported")

    override fun uncombineBytes(source: Receiver<Any>): Sequence<Byte> = error("Reading not supported")

    override fun uncombineShorts(source: Receiver<Any>): Sequence<Short> = error("Reading not supported")

    override fun uncombineInts(source: Receiver<Any>): Sequence<Int> = error("Reading not supported")

    override fun uncombineLongs(source: Receiver<Any>): Sequence<Long> = error("Reading not supported")

    override fun uncombineFloats(source: Receiver<Any>): Sequence<Float> = error("Reading not supported")

    override fun uncombineDoubles(source: Receiver<Any>): Sequence<Double> = error("Reading not supported")

    override fun uncombineChars(source: Receiver<Any>): Sequence<Char> = error("Reading not supported")

    override fun uncombineUnits(source: Receiver<Any>): Sequence<Unit> = error("Reading not supported")

    override fun uncombineStrings(source: Receiver<Any>): Sequence<String> = error("Reading not supported")

    override fun uncombineElements(source: Receiver<Any>): Sequence<Any> = error("Reading not supported")
}

/**
 * Read only funneler, streams a source into a receiver, which should be configured on [Source.onAfterCreate].
 */
object ReceiverNullableFunneler : ListlikeNullableFunneler<Receiver<Any?>, Receiver<Any?>>() {
    override fun create() = Receiver<Any?>({}, {}, true)

    override fun begin(source: Receiver<Any?>) = source

    override fun end(source: Receiver<Any?>): Receiver<Any?> {
        source.onEnd()
        return source
    }

    override fun combineContinue(source: Receiver<Any?>) = source.shouldContinue

    override fun combineBoolean(target: Receiver<Any?>, value: Boolean?) = target.onElement(value)

    override fun combineByte(target: Receiver<Any?>, value: Byte?) = target.onElement(value)

    override fun combineShort(target: Receiver<Any?>, value: Short?) = target.onElement(value)

    override fun combineInt(target: Receiver<Any?>, value: Int?) = target.onElement(value)

    override fun combineLong(target: Receiver<Any?>, value: Long?) = target.onElement(value)

    override fun combineFloat(target: Receiver<Any?>, value: Float?) = target.onElement(value)

    override fun combineDouble(target: Receiver<Any?>, value: Double?) = target.onElement(value)

    override fun combineChar(target: Receiver<Any?>, value: Char?) = target.onElement(value)

    override fun combineUnit(target: Receiver<Any?>, value: Unit?) = target.onElement(value)

    override fun combineString(target: Receiver<Any?>, value: String?) = target.onElement(value)

    override fun combineElement(target: Receiver<Any?>, value: Any?) = target.onElement(value)

    override fun uncombineBooleans(source: Receiver<Any?>): Sequence<Boolean> = error("Reading not supported")

    override fun uncombineBytes(source: Receiver<Any?>): Sequence<Byte> = error("Reading not supported")

    override fun uncombineShorts(source: Receiver<Any?>): Sequence<Short> = error("Reading not supported")

    override fun uncombineInts(source: Receiver<Any?>): Sequence<Int> = error("Reading not supported")

    override fun uncombineLongs(source: Receiver<Any?>): Sequence<Long> = error("Reading not supported")

    override fun uncombineFloats(source: Receiver<Any?>): Sequence<Float> = error("Reading not supported")

    override fun uncombineDoubles(source: Receiver<Any?>): Sequence<Double> = error("Reading not supported")

    override fun uncombineChars(source: Receiver<Any?>): Sequence<Char> = error("Reading not supported")

    override fun uncombineUnits(source: Receiver<Any?>): Sequence<Unit> = error("Reading not supported")

    override fun uncombineStrings(source: Receiver<Any?>): Sequence<String> = error("Reading not supported")

    override fun uncombineElements(source: Receiver<Any?>): Sequence<Any?> = error("Reading not supported")
}

/**
 * Resolves the [ReceiverFunneler] and [ReceiverNullableFunneler].
 */
object ReceiverModule : Module {
    override fun <T> resolve(type: Type): Funneler<T> {
        @Suppress("unchecked_cast")
        if (type.kClass == Receiver::class) {
            if (type.arg.nullable)
                return ReceiverNullableFunneler as Funneler<T>
            else
                return ReceiverFunneler as Funneler<T>

        }

        @Suppress("unchecked_cast")
        return NoFunneler as Funneler<T>
    }
}

/**
 * Uses a source provider to create a repeatable observable.
 */
inline fun <E> Module.stream(crossinline sourceProvider: () -> Source) =
        Observable.create<E> { emitter ->
            sourceProvider().useIfClosable {
                read<Receiver<E>>(it.onAfterCreate<Receiver<E>> {
                    // Wire emitter cancellation into receiver
                    emitter.setCancellable { it.shouldContinue = false }

                    // Write receiver emissions into emitter
                    it.onElement = { emitter.onNext(it) }
                    it.onEnd = { emitter.onComplete() }
                })
            }
        }