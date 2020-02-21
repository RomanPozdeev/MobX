package mobx.core

import mobx.util.notSame

fun autorun(body: () -> Unit): Autorun {
    return Autorun(body)
}

fun <R> reaction(
    dataCallback: () -> R,
    sideEffect: (R) -> Unit,
    changed: ((R?, R?, Int) -> Boolean) = notSame()
): Reaction<R> {
    return Reaction(dataCallback, sideEffect, changed)
}

fun whenThen(predicate: () -> Boolean, sideEffect: () -> Unit): When {
    return When(predicate, sideEffect)
}

fun <T> observable(valueSupplier: () -> T): TrackedObservableVariable<T> {
    return TrackedObservableVariable(valueSupplier())
}

fun <T> observable(value: T): TrackedObservableVariable<T> {
    return TrackedObservableVariable(value)
}

fun <T> computed(body: () -> T): ComputedVariable<T> {
    return ComputedVariable(body)
}

fun action(payload: Any? = null, body: () -> Unit) {
    Action(payload, body)
}

fun spy(onPayload: ActionListener): Disposable {
    return Spy(onPayload)
}
