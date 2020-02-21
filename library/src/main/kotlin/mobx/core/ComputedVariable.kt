package mobx.core

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ComputedVariable<T>(body: () -> T) : ReadOnlyProperty<Any, T> {

    private val computed = Computed(body)

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return computed.get()
    }
}
