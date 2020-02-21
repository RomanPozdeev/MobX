package mobx.collections

import mobx.core.Observable
import mobx.core.Observer

class FakeSubscriber(observable: Observable) {
    var subscriberCallCount = 0
        private set

    init {
        observable.subscribe(object : Observer {
            override fun onChange() {
                subscriberCallCount++
            }
        })
    }
}
