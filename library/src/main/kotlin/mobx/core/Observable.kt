package mobx.core

interface Observable {
    fun subscribe(observer: Observer): Disposable
}
