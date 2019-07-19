package me.cpele.runr.domain.iface

import me.cpele.runr.domain.bo.PlaylistBo

interface Player {
    suspend fun connect()
    suspend fun play(playlist: PlaylistBo)
    fun disconnect()
}