package mobx.collections

import mobx.core.Observable
import mobx.core.ObservableTrackerHolder
import mobx.core.Observer
import mobx.util.ObserversDelegate

@Suppress("TooManyFunctions")
class ObservableMutableIterator<out E>(
    private val origin: MutableIterator<E>
) : MutableIterator<E>, Observable {

    private val observersDelegate = ObserversDelegate()

    override fun hasNext(): Boolean {
        triggerTracker()
        return origin.hasNext()
    }

    override fun next(): E {
        return origin.next().also {
            triggerTracker()
        }
    }

    override fun remove() {
        return origin.remove().also {
            observersDelegate.notifyObservers()
        }
    }

    private fun triggerTracker() {
        ObservableTrackerHolder.currentTracker?.track(this)
    }

    override fun subscribe(observer: Observer) = observersDelegate.subscribe(observer)
}
