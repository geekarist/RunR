package me.cpele.runr.domain.iface

import me.cpele.runr.domain.entities.AuthResponse

interface AuthResponseRepository {
    fun save(response: AuthResponse?)
    fun load(): AuthResponse?
}
