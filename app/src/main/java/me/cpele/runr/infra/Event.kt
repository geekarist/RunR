package me.cpele.runr.infra

import java.util.concurrent.atomic.AtomicBoolean

class Event<T>(private val _value: T) {

    private val isConsumed: AtomicBoolean = AtomicBoolean(false)

    /**
     * If the value was already consumed, return null. If it wasn't consumed, return the value.
     */
    val value: T?
        get() =
            if (isConsumed.getAndSet(true)) null
            else _value
}
