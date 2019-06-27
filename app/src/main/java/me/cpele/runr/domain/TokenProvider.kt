package me.cpele.runr.domain

interface TokenProvider {
    suspend fun get(): String
}