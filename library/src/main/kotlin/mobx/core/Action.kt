package mobx.core

// Creates an action that encapsulates all the mutations happening on the
// observables.
//
// Wrapping mutations inside an action ensures the depending observers
// are only notified when the action completes. This is useful to silent the notifications
// when several observables are being changed together. You will want to run your
// reactions only when all the mutations complete. This also helps in keeping
// the state of your application consistent.
// Even though we are changing 3 observables (`x`, `y` and `total`), the [autorun()]
// is only executed once. This is the benefit of action. It batches up all the change
// notifications and propagates them only after the completion of the action. Actions
// can also be nested inside, in which case the change notification will propagate when
// the top-level action completes.
class Action(payload: Any? = null, body: (() -> Unit)? = null) {
    init {
        ActionManager.runAsAction(payload, body)
    }
}
