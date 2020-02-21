package mobx.testtools

import mobx.core.Observable
import mobx.core.Observer

class TestObservable(val disposable: mobx.core.Disposable, val onSubscribe: () -> Unit) :
    Observable {
    lateinit var observer: Observer

    override fun subscribe(observer: Observer): mobx.core.Disposable {
        onSubscribe()
        this.observer = observer
        return disposable
    }

    fun onChange() {
        observer.onChange()
    }
}
