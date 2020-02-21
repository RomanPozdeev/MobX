package mobx.core

internal object ObservableTrackerHolder {
    var currentTracker: ObservableTracker? = null
        private set

    fun replaceTo(target: ObservableTracker, transaction: () -> Unit) {
        val prevTracker = currentTracker
        currentTracker = target
        transaction()
        currentTracker = prevTracker
    }
}
