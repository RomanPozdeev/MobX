package mobx.collections

import mobx.core.Observable
import mobx.core.ObservableTrackerHolder
import mobx.core.Observer
import mobx.util.ObserversDelegate

@Suppress("TooManyFunctions")
class ObservableMutableList<E>(
    private val origin: MutableList<E>
) : MutableList<E> by origin, Observable, mobx.core.Changeable {

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

    override fun add(element: E): Boolean {
        return origin.add(element).also {
            observersDelegate.notifyObservers()
        }
    }

    override fun add(index: Int, element: E) {
        return origin.add(index, element).also {
            observersDelegate.notifyObservers()
        }
    }

    override fun addAll(index: Int, elements: Collection<E>): Boolean {
        return origin.addAll(index, elements).also {
            observersDelegate.notifyObservers()
        }
    }

    override fun addAll(elements: Collection<E>): Boolean {
        return origin.addAll(elements).also {
            observersDelegate.notifyObservers()
        }
    }

    override fun clear() {
        origin.clear()
        observersDelegate.notifyObservers()
    }

    override fun contains(element: E): Boolean {
        triggerTracker()
        return origin.contains(element)
    }

    override fun containsAll(elements: Collection<E>): Boolean {
        triggerTracker()
        return origin.containsAll(elements)
    }

    override fun get(index: Int): E {
        triggerTracker()
        return origin[index]
    }

    override fun isEmpty(): Boolean {
        triggerTracker()
        return origin.isEmpty()
    }

    override fun remove(element: E): Boolean {
        return origin.remove(element).also {
            if (it) {
                observersDelegate.notifyObservers()
            }
        }
    }

    override fun removeAll(elements: Collection<E>): Boolean {
        return origin.removeAll(elements).also {
            if (it) {
                observersDelegate.notifyObservers()
            }
        }
    }

    override fun removeAt(index: Int): E {
        return origin.removeAt(index).also {
            observersDelegate.notifyObservers()
        }
    }

    override fun retainAll(elements: Collection<E>): Boolean {
        return origin.retainAll(elements).also { removed ->
            if (removed) {
                observersDelegate.notifyObservers()
            }
        }
    }

    override fun set(index: Int, element: E): E {
        return origin.set(index, element).also {
            observersDelegate.notifyObservers()
        }
    }

    override val size: Int
        get() = origin.size.also {
            triggerTracker()
        }

    override fun indexOf(element: E): Int {
        return origin.indexOf(element).also {
            triggerTracker()
        }
    }

    override fun lastIndexOf(element: E): Int {
        return origin.lastIndexOf(element).also {
            triggerTracker()
        }
    }

    override fun listIterator(): ObservableMutableListIterator<E> {
        return ObservableMutableListIterator(origin.listIterator()).also {
            it.subscribe(iteratorObserver)
        }
    }

    override fun listIterator(index: Int): ObservableMutableListIterator<E> {
        return ObservableMutableListIterator(origin.listIterator(index)).also {
            it.subscribe(iteratorObserver)
        }
    }

    override fun iterator(): ObservableMutableIterator<E> {
        return ObservableMutableIterator(origin.iterator()).also {
            it.subscribe(iteratorObserver)
        }
    }

    private fun triggerTracker() {
        ObservableTrackerHolder.currentTracker?.track(this)
    }

    override fun subscribe(observer: Observer) = observersDelegate.subscribe(observer)
}
