package mobx.collections

import mobx.core.Observable
import mobx.core.ObservableTrackerHolder
import mobx.core.Observer
import mobx.util.ObserversDelegate

@Suppress("TooManyFunctions")
class ObservableMutableCollection<E>(
    private val origin: MutableCollection<E>
) : MutableCollection<E>, Observable, mobx.core.Changeable {

    private val observersDelegate = ObserversDelegate()
    private val iteratorObserver = object : Observer {
        override fun onChange() {
            observersDelegate.notifyObservers()
        }
    }

    override var change: Int = observersDelegate.change
        private set
        get() {
            return observersDelegate.change
        }

    override val size: Int
        get() {
            triggerTracker()
            return origin.size
        }

    override fun contains(element: E): Boolean {
        triggerTracker()
        return origin.contains(element)
    }

    override fun containsAll(elements: Collection<E>): Boolean {
        triggerTracker()
        return origin.containsAll(elements)
    }

    override fun isEmpty(): Boolean {
        triggerTracker()
        return origin.isEmpty()
    }

    override fun add(element: E): Boolean {
        return origin.add(element).also {
            observersDelegate.notifyObservers()
        }
    }

    override fun addAll(elements: Collection<E>): Boolean {
        return origin.addAll(elements).also {
            observersDelegate.notifyObservers()
        }
    }

    override fun clear() {
        return origin.clear().also {
            observersDelegate.notifyObservers()
        }
    }

    override fun iterator(): ObservableMutableIterator<E> {
        return ObservableMutableIterator(origin.iterator()).also {
            it.subscribe(iteratorObserver)
        }
    }

    override fun remove(element: E): Boolean {
        return origin.remove(element).also {
            observersDelegate.notifyObservers()
        }
    }

    override fun removeAll(elements: Collection<E>): Boolean {
        return origin.removeAll(elements).also {
            observersDelegate.notifyObservers()
        }
    }

    override fun retainAll(elements: Collection<E>): Boolean {
        return origin.retainAll(elements).also {
            if (it) {
                observersDelegate.notifyObservers()
            }
        }
    }

    override fun subscribe(observer: Observer) = observersDelegate.subscribe(observer)

    private fun triggerTracker() {
        ObservableTrackerHolder.currentTracker?.track(this)
    }
}
