package mobx.core

typealias ComputedBody<R> = () -> R

class Computed<R : Any?>(private val body: ComputedBody<R>) : ObservableTracker,
    Observer {

    private var needRecalculation = true
    private val children = hashSetOf<Observable>()
    private var memoizedResult: R? = null

    fun get(): R {
        if (needRecalculation) {
            ObservableTrackerHolder.replaceTo(this) {
                memoizedResult = body()
            }
        }
        needRecalculation = false

        ObservableTrackerHolder.currentTracker?.let { tracker ->
            children.forEach {
                tracker.track(it)
            }
        }

        return memoizedResult!!
    }

    override fun track(observable: Observable) {
        observable.subscribe(this)
        children.add(observable)
    }

    override fun onChange() {
        needRecalculation = true
    }
}
