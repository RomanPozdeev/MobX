package com.example.mobxexample.presentation.utils.mobx

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import mobx.core.Disposable
import mobx.core.ReactionDataCallback
import mobx.core.ReactionSideEffectCallback
import mobx.core.reaction

fun <R> LifecycleOwner.observeChanges(
    dataCallback: ReactionDataCallback<R>,
    effect: ReactionSideEffectCallback<R>
) {
    if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
        return
    }

    var disposable: Disposable? = null
    if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
        disposable = reaction(dataCallback, effect)
    }

    lifecycle.addObserver(
        LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                disposable = reaction(dataCallback, effect)
            } else if (event == Lifecycle.Event.ON_STOP) {
                disposable?.dispose()
            }
        }
    )
}
