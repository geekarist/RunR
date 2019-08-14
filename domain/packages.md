# Packages

## me.cpele.runr.domain.entity

Contains the private domain model. Things here must be `internal` or `private`.

## me.cpele.runr.domain.api

Contains what can be used by other modules depending on the domain. Public.

### me.cpele.runr.domain.api.usecase

Contains the public use cases of the domain. They implement business logic.

### me.cpele.runr.domain.api.model

Contains the public data model of the domain.

## me.cpele.runr.adapter

Contains the interfaces implemented by other components that can be called by the domain. Public.