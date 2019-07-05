package me.cpele.runr.domain.iface

import me.cpele.runr.domain.bo.AuthResponseBo

interface AuthResponseRepository {
    fun save(response: AuthResponseBo?)
    fun load(): AuthResponseBo?
}
