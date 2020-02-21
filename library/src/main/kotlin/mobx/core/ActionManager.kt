package mobx.core

import mobx.util.CommonListenersDelegate

internal typealias Listener = () -> Unit
internal typealias ActionListener = (Any?) -> Unit

internal object ActionManager {
    private val actionDelegate = CommonListenersDelegate<ActionListener>()

    // autorun's ex
    private val listenersDelegate = CommonListenersDelegate<Listener>()

    var currentNesting = 0
        private set

    fun listenNestingChanges(listener: Listener): Disposable {
        return listenersDelegate.add(listener)
    }

    fun listenActions(listener: ActionListener): Disposable {
        return actionDelegate.add(listener)
    }

    fun runAsAction(payload: Any?, action: (() -> Unit)?) {
        actionDelegate.notifyListeners {
            it(payload)
        }
        currentNesting++
        action?.invoke()
        currentNesting--
        listenersDelegate.notifyListeners {
            it()
        }
    }

    fun removeActionListener(onPayload: ActionListener) {
        actionDelegate.remove(onPayload)
    }
}
