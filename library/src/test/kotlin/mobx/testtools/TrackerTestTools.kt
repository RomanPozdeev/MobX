package mobx.testtools

import mobx.core.Observable
import mobx.core.ObservableTracker
import mobx.core.ObservableTrackerHolder
import java.util.concurrent.atomic.AtomicInteger

fun trackableMethod(trackCallCount: AtomicInteger, method: () -> Unit) {
    ObservableTrackerHolder.replaceTo(object : ObservableTracker {
        override fun track(observable: Observable) {
            trackCallCount.incrementAndGet()
        }
    }) {
        method()
    }
}
