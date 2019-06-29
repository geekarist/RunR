package me.cpele.runr.domain

interface AuthResponseRepository {
    fun save(response: AuthResponseBo?)
    fun load(): AuthResponseBo?
}
