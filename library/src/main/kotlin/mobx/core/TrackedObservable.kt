package mobx.core

import mobx.util.ObserversDelegate

class TrackedObservable<T>(
    private var value: T
) : Observable {
    private val observersDelegate = ObserversDelegate()

    fun get(): T {
        val tracker = ObservableTrackerHolder.currentTracker
        tracker?.track(this)
        return value
    }

    fun set(value: T) {
        if (this.value !== value) {
            this.value = value
            observersDelegate.notifyObservers()
        }
    }

    override fun subscribe(observer: Observer): Disposable {
        return observersDelegate.subscribe(value, observer)
    }
}
