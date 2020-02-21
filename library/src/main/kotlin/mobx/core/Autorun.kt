package mobx.core

/**
 * Useful for long-running side-effects. The effect function executes immediately and
 * also anytime the dependent observables (used within it) change. It returns a Disposable
 * that can be used to cancel anytime
 */
class Autorun(private val effect: () -> Unit) :
    Disposable,
    ObservableTracker,
    Observer {

    private val childObservableDisposables = hashSetOf<Disposable>()
    private val actionNestingListenerDisposable: Disposable
    private var hasPendingBodyInvocation = false
    private var disposed = false

    private val actionNestingListener: Listener = {
        if (hasPendingBodyInvocation) {
            runBody()
        }
    }

    init {
        actionNestingListenerDisposable =
            ActionManager.listenNestingChanges(
                actionNestingListener
            )
        runBody()
    }

    override fun dispose() {
        disposeChildObservables()
        actionNestingListenerDisposable.dispose()
        disposed = true
    }

    override fun track(observable: Observable) {
        val disposable = observable.subscribe(this)
        childObservableDisposables.add(disposable)
    }

    override fun onChange() {
        checkDisposed()
        if (ActionManager.currentNesting == 0) {
            runBody()
        } else {
            hasPendingBodyInvocation = true
        }
    }

    private fun checkDisposed() {
        if (disposed) {
            throw IllegalStateException("autorun is disposed!")
        }
    }

    private fun disposeChildObservables() {
        childObservableDisposables.forEach {
            it.dispose()
        }
        childObservableDisposables.clear()
    }

    private fun runBody() {
        checkDisposed()
        ObservableTrackerHolder.replaceTo(this) {
            disposeChildObservables()
            effect()
        }
    }
}
