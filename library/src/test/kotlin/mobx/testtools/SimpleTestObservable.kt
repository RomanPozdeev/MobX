package mobx.testtools

import mobx.core.observable

class SimpleTestObservable {
    var counter by observable(0)
        private set

    fun inc() {
        mobx.core.Action {
            counter++
        }
    }

    fun reset() {
        counter = 0
    }
}
