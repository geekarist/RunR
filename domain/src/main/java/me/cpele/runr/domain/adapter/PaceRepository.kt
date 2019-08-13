package me.cpele.runr.domain.adapter

interface PaceRepository {
    fun get(): Int
    fun set(value: Int)
}
