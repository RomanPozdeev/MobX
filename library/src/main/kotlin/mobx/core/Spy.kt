package mobx.core

class Spy(private val onPayload: ActionListener) :
    Disposable {

    init {
        ActionManager.listenActions(onPayload)
    }

    override fun dispose() {
        ActionManager.removeActionListener(onPayload)
    }
}
