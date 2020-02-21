package mobx.core

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class TrackedObservableVariable<T>(value: T) : ReadWriteProperty<Any, T> {

    private val observable = TrackedObservable(value)

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return observable.get()
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        observable.set(value)
    }
}
