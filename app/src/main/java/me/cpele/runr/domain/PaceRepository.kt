package me.cpele.runr.domain

interface PaceRepository {
    fun get(): Int
    fun set(value: Int)
}
