package br.dev.saed.voleibr.ui.state

import br.dev.saed.voleibr.model.repositories.db.winner.WinnerSearch

data class StatsScreenState (
    val winner: List<WinnerSearch> = emptyList()
)

sealed class StatsScreenEvent {
    data object Load : StatsScreenEvent()
}