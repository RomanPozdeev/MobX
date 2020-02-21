package mobx.collections

import mobx.core.Observable
import mobx.core.ObservableTrackerHolder
import mobx.core.Observer
import mobx.util.ObserversDelegate

@Suppress("TooManyFunctions")
class ObservableMutableListIterator<E>(
    private val origin: MutableListIterator<E>
) : MutableListIterator<E>, Observable {

    private val observersDelegate = ObserversDelegate()

    override fun hasPrevious(): Boolean {
        triggerTracker()
        return origin.hasPrevious()
    }

    override fun nextIndex(): Int {
        triggerTracker()
        return origin.nextIndex()
    }

    override fun previous(): E {
        triggerTracker()
        return origin.previous()
    }

    override fun previousIndex(): Int {
        triggerTracker()
        return origin.previousIndex()
    }

    override fun add(element: E) {
        return origin.add(element).also {
            observersDelegate.notifyObservers()
        }
    }

    override fun hasNext(): Boolean {
        triggerTracker()
        return origin.hasNext()
    }

    override fun next(): E {
        triggerTracker()
        return origin.next()
    }

    override fun remove() {
        return origin.remove().also {
            observersDelegate.notifyObservers()
        }
    }

    override fun set(element: E) {
        return origin.set(element).also {
            observersDelegate.notifyObservers()
        }
    }

    override fun subscribe(observer: Observer) = observersDelegate.subscribe(observer)

    private fun triggerTracker() {
        ObservableTrackerHolder.currentTracker?.track(this)
    }
}
