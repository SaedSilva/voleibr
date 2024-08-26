package br.dev.saed.voleibr.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.saed.voleibr.model.repositories.db.winner.WinnerRepository
import br.dev.saed.voleibr.ui.state.StatsScreenEvent
import br.dev.saed.voleibr.ui.state.StatsScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StatsViewModel(
    private val winnerRepository: WinnerRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(StatsScreenState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(_uiState, winnerRepository.winners) { _, winners ->
                StatsScreenState(winners)
            }.collect {newState ->
                _uiState.update {
                    newState
                }
            }
        }
    }

    fun onEvent(event: StatsScreenEvent) {
        when (event) {
            is StatsScreenEvent.Load -> {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(winner = winnerRepository.winners.first())
                    }
                }
            }

            is StatsScreenEvent.DeleteTeam -> {
                viewModelScope.launch {
                    winnerRepository.deleteWinner(event.team)
                }
            }
        }
    }
}