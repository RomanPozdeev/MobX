package mobx.collections

import mobx.core.Observable
import mobx.core.ObservableTrackerHolder
import mobx.core.Observer
import mobx.util.ObserversDelegate
import kotlin.collections.MutableMap.MutableEntry

class ObservableMutableMap<K, V>(
    private val origin: MutableMap<K, V>
) : MutableMap<K, V>, Observable, mobx.core.Changeable {

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

    override val values: MutableCollection<V>
        get() = ObservableMutableCollection(origin.values).also {
            it.subscribe(iteratorObserver)
        }

    override val size: Int
        get() {
            triggerTracker()
            return origin.size
        }

    override fun containsKey(key: K): Boolean {
        triggerTracker()
        return origin.containsKey(key)
    }

    override fun containsValue(value: V): Boolean {
        triggerTracker()
        return origin.containsValue(value)
    }

    override val entries: MutableSet<MutableEntry<K, V>>
        get() = ObservableMutableSet(origin.entries).also {
            it.subscribe(iteratorObserver)
        }

    override val keys: MutableSet<K>
        get() = ObservableMutableSet(origin.keys).also {
            it.subscribe(iteratorObserver)
        }

    override fun clear() {
        origin.clear()
        observersDelegate.notifyObservers()
    }

    override fun get(key: K): V? {
        triggerTracker()
        return origin[key]
    }

    override fun isEmpty(): Boolean {
        triggerTracker()
        return origin.isEmpty()
    }

    override fun put(key: K, value: V): V? {
        return origin.put(key, value).also {
            observersDelegate.notifyObservers()
        }
    }

    override fun putAll(from: Map<out K, V>) {
        return origin.putAll(from).also {
            observersDelegate.notifyObservers()
        }
    }

    override fun remove(key: K): V? {
        return origin.remove(key).also {
            if (it != null) {
                observersDelegate.notifyObservers()
            }
        }
    }

    private fun triggerTracker() {
        ObservableTrackerHolder.currentTracker?.track(this)
    }

    override fun subscribe(observer: Observer) = observersDelegate.subscribe(observer)
}
