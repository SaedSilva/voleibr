package br.dev.saed.volei.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.saed.volei.model.repositories.db.winner.WinnerRepository
import br.dev.saed.volei.ui.state.StatsScreenEvent
import br.dev.saed.volei.ui.state.StatsScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StatsViewModel(
    private val winnerRepository: WinnerRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(StatsScreenState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            winnerRepository.winners.collect { winners ->
                _uiState.value = StatsScreenState(winners)
            }
        }
    }

    fun onEvent(event: StatsScreenEvent) {
        when (event) {
            is StatsScreenEvent.DeleteTeam -> {
                viewModelScope.launch {
                    winnerRepository.deleteWinner(event.team)
                }
            }

            StatsScreenEvent.DeleteAll -> {
                viewModelScope.launch {
                    winnerRepository.deleteAll()
                }
            }
        }
    }
}