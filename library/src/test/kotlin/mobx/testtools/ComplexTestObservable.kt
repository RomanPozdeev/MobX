package mobx.testtools

import mobx.core.observable

class ComplexTestObservable {
    var counter1 by observable(0)
        private set
    var counter2 by observable(1)
        private set

    var counter3 by observable { 1 }
        private set

    fun inc() {
        mobx.core.Action {
            counter1++
            counter2++
            counter3++
        }
    }

    override fun toString(): String {
        return "counter1 $counter1 counter2$counter2"
    }
}
