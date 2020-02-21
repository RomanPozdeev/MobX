package mobx.testtools

import mobx.collections.toObservableList
import mobx.core.observable

class ChangeableTestObservable {
    var counter by observable(
        mutableListOf<Int>().toObservableList()
    )
        private set

    fun inc() {
        mobx.core.Action {
            counter.add(1)
        }
    }

    fun reset() {
        counter.clear()
    }
}
