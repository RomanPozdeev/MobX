package mobx.core

import mobx.util.notSame

/**
 * Useful for long-running side-effects. It executes the effect function only when
 * the data returned by the tracker function is different. In other words Reaction waits for
 * a change in the observables before any side-effect are run. It also gives back a Disposable
 * to cancel the effect prematurely
 */
typealias ReactionSideEffectCallback<R> = (R) -> Unit
typealias ReactionDataCallback<R> = () -> R

class Reaction<R>(
    dataCallback: ReactionDataCallback<R>,
    effect: ReactionSideEffectCallback<R>,
    changed: ((R?, R?, Int) -> Boolean) = notSame()
) : Disposable {
    private var lastData: R? = null
    private var lastId = Int.MIN_VALUE

    private val disposable = Autorun {
        val data = dataCallback()
        if (changed(lastData, data, lastId)) {
            effect(data)
            if (data is Changeable) {
                lastId = data.change
            }
            lastData = data
        }
    }

    override fun dispose() {
        disposable.dispose()
    }
}
