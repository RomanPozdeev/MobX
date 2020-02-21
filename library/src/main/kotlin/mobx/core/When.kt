package mobx.core

/**
 * Useful for one-off effects. The predicate function is evaluated anytime its dependent observables
 * change. It executes effect function only when the predicate function returns true. When disposes
 * itself after the effect function.
 */
/**
 * TO-DO add when with Promise
 */
typealias WhenPredicate = () -> Boolean

class When(predicate: WhenPredicate, sideEffect: () -> Unit) :
    Disposable {
    // to prevent crash when it is disposed on creation
    private var internalDisposable: Disposable? = null
    private var disposed = false

    private val disposable = Reaction(
        dataCallback = {
            predicate()
        },
        effect = { data ->
            if (data) {
                internalDisposable?.dispose()
                disposed = true
                sideEffect()
            }
        }
    )

    init {
        internalDisposable = disposable
        if (disposed) {
            disposable.dispose()
        }
    }

    override fun dispose() {
        disposable.dispose()
    }
}
