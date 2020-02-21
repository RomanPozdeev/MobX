package mobx.testtools

import mobx.core.computed
import mobx.core.observable

class ComputedTestObservable {
    var counter by observable { 1 }
        private set

    val triple by computed { counter * 3 }

    fun inc() {
        mobx.core.Action {
            counter++
        }
    }
}
