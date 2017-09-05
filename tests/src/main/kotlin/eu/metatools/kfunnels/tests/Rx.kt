package eu.metatools.kfunnels.tests

import eu.metatools.kfunnels.*
import eu.metatools.kfunnels.base.NoFunneler
import eu.metatools.kfunnels.base.listlike.ListlikeFunneler
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.sync.Mutex

object RxModule : Module {
    override fun <T> resolve(type: Type): Funneler<T> {
        @Suppress("unchecked_cast")
        if (type.kClass == Observable::class) {
            check(!type.nullable) { "Nullable items are not supported in RxJava." }
            return ObservableFunneler as Funneler<T>
        }

        @Suppress("unchecked_cast")
        return NoFunneler as Funneler<T>
    }
}

/**
 * Creates an observable, blocks on [emitter] until a subscriber is present.
 */
class ObservableCreator<T> {
    /**
     * Unlocks once the emitter was created.
     */
    private val emitterReady = Mutex(true)

    /**
     * Holds the emitter.
     */
    private lateinit var emitterLocation: ObservableEmitter<T>

    /**
     * Holds the cancellation state.
     */
    private var cancelState = false

    /**
     * Gets the observable.
     */
    val observable = Observable.create<T> {
        // Link cancel into closed state
        it.setCancellable {
            cancelState = true
        }

        // Assign emitter
        runBlocking {
            emitterLocation = it
            emitterReady.unlock()
        }
    }

    /**
     * Gets the emitter, this call will block until someone subscribed to [observable].
     */
    val emitter: ObservableEmitter<T>
        get() {
            runBlocking {
                emitterReady.lock()
                emitterReady.unlock()
            }
            return emitterLocation
        }

    /**
     * True if closed.
     */
    val cancelled get() = cancelState
}

/**
 * Funnels and unfunnels observables.
 */
// TODO: Better writing behavior maybe?
object ObservableFunneler : ListlikeFunneler<ObservableCreator<Any>, Observable<Any>>() {
    override fun combineContinue(source: ObservableCreator<Any>) = !source.cancelled

    override fun create() = ObservableCreator<Any>()

    override fun begin(source: ObservableCreator<Any>): Observable<Any> {
        return source.observable
    }

    override fun end(source: ObservableCreator<Any>): Observable<Any> {
        source.emitter.onComplete()
        return source.observable
    }

    override fun combineBoolean(target: ObservableCreator<Any>, value: Boolean) =
            target.emitter.onNext(value)

    override fun combineByte(target: ObservableCreator<Any>, value: Byte) =
            target.emitter.onNext(value)

    override fun combineShort(target: ObservableCreator<Any>, value: Short) =
            target.emitter.onNext(value)

    override fun combineInt(target: ObservableCreator<Any>, value: Int) =
            target.emitter.onNext(value)

    override fun combineLong(target: ObservableCreator<Any>, value: Long) =
            target.emitter.onNext(value)

    override fun combineFloat(target: ObservableCreator<Any>, value: Float) =
            target.emitter.onNext(value)

    override fun combineDouble(target: ObservableCreator<Any>, value: Double) =
            target.emitter.onNext(value)

    override fun combineChar(target: ObservableCreator<Any>, value: Char) =
            target.emitter.onNext(value)

    override fun combineUnit(target: ObservableCreator<Any>, value: Unit) =
            target.emitter.onNext(value)

    override fun combineString(target: ObservableCreator<Any>, value: String) =
            target.emitter.onNext(value)

    override fun combineElement(target: ObservableCreator<Any>, value: Any) =
            target.emitter.onNext(value)

    override fun uncombineBooleans(source: Observable<Any>): Sequence<Boolean> =
            source.blockingIterable().map { it as Boolean }.asSequence()

    override fun uncombineBytes(source: Observable<Any>): Sequence<Byte> =
            source.blockingIterable().map { it as Byte }.asSequence()

    override fun uncombineShorts(source: Observable<Any>): Sequence<Short> =
            source.blockingIterable().map { it as Short }.asSequence()

    override fun uncombineInts(source: Observable<Any>): Sequence<Int> =
            source.blockingIterable().map { it as Int }.asSequence()

    override fun uncombineLongs(source: Observable<Any>): Sequence<Long> =
            source.blockingIterable().map { it as Long }.asSequence()

    override fun uncombineFloats(source: Observable<Any>): Sequence<Float> =
            source.blockingIterable().map { it as Float }.asSequence()

    override fun uncombineDoubles(source: Observable<Any>): Sequence<Double> =
            source.blockingIterable().map { it as Double }.asSequence()

    override fun uncombineChars(source: Observable<Any>): Sequence<Char> =
            source.blockingIterable().map { it as Char }.asSequence()

    override fun uncombineUnits(source: Observable<Any>): Sequence<Unit> =
            source.blockingIterable().map { it as Unit }.asSequence()

    override fun uncombineStrings(source: Observable<Any>): Sequence<String> =
            source.blockingIterable().map { it as String }.asSequence()

    override fun uncombineElements(source: Observable<Any>): Sequence<Any> =
            source.blockingIterable().asSequence()
}