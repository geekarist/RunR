package me.cpele.runr.domain.adapter

import me.cpele.runr.domain.api.model.Auth

interface AuthResponseRepository {
    fun save(response: Auth?)
    fun load(): Auth?
}
