package mobx.util

internal fun <R> notSame(): (R?, R?, Int) -> Boolean {
    return { old, new, lastId ->
        old !== new || (new is mobx.core.Changeable && new.change != lastId)
    }
}
