package me.cpele.runr.domain.iface

interface PaceRepository {
    fun get(): Int
    fun set(value: Int)
}
