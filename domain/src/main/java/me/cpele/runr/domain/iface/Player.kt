package me.cpele.runr.domain.iface

import me.cpele.runr.domain.bo.PlaylistBo

interface Player {
    suspend fun play(playlist: PlaylistBo)
}