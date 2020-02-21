package mobx.util

internal class CommonListenersDelegate<T> {
    internal val listeners = hashMapOf<T, mobx.core.Disposable>()
    private var iterator: MutableIterator<T>? = null

    fun remove(listener: T) {
        iterator?.remove() ?: listeners.remove(listener)
    }

    fun add(listener: T): mobx.core.Disposable {
        return listeners[listener] ?: object : mobx.core.Disposable {
            override fun dispose() {
                remove(listener)
            }
        }.also {
            listeners[listener] = it
        }
    }

    fun notifyListeners(action: (T) -> Unit) {
        val listenersIterator = listeners.keys.iterator()
        iterator = listenersIterator
        while (listenersIterator.hasNext()) {
            val next = listenersIterator.next()
            action(next)
        }
        iterator = null
    }
}
