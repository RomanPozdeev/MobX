package mobx.collections

fun <E> MutableList<E>.toObservableList(): ObservableMutableList<E> {
    return ObservableMutableList(this)
}

fun <E> MutableSet<E>.toObservableSet(): ObservableMutableSet<E> {
    return ObservableMutableSet(this)
}

fun <K, V> MutableMap<K, V>.toObservableMap(): ObservableMutableMap<K, V> {
    return ObservableMutableMap(this)
}
