package mobx.util

import mobx.core.Observable
import mobx.core.Observer

internal class ObserversDelegate(counterStart: Int = Int.MIN_VALUE) :
    mobx.core.Changeable,
    Observable {
    private val delegate = CommonListenersDelegate<Observer>()

    fun notifyObservers() {
        safeInc()
        delegate.notifyListeners {
            it.onChange()
        }
    }

    private fun safeInc() {
        if (change == Int.MAX_VALUE) {
            change = Int.MIN_VALUE
        }
        change++
    }

    fun subscribe(value: Any?, observer: Observer): mobx.core.Disposable {
        return if (value is Observable) {
            delegate.listeners[observer] ?: value.subscribe(observer).also {
                delegate.listeners[observer] = it
            }
        } else {
            subscribe(observer)
        }
    }

    override fun subscribe(observer: Observer): mobx.core.Disposable {
        return delegate.add(observer)
    }

    override var change: Int = counterStart
        private set
}
