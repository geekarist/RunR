package me.cpele.runr.infra

import java.util.concurrent.atomic.AtomicBoolean

// TODO: Rename to Event
class Consumable<T>(private val value: T) {

    private val isConsumed: AtomicBoolean = AtomicBoolean(false)

    /**
     * If the value was already consumed, return null. If it wasn't consumed, return the value.
     */
    val unconsumed: T?
        get() =
            if (isConsumed.getAndSet(true)) null
            else value
}
