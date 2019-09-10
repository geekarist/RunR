package me.cpele.runr.domain.adapter

interface SetupStatusRepository {

    var value: Status

    enum class Status {
        DONE,
        TODO
    }
}
