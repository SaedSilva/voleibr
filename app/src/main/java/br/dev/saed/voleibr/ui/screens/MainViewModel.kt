package br.dev.saed.voleibr.ui.screens

import androidx.lifecycle.ViewModel
import br.dev.saed.voleibr.model.Team
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MainScreenState(
    var maxPoints: Int = 12,
    var team1: Team = Team("Time 1"),
    var team2: Team = Team("Time 2"),
    val teamsInQueue: ArrayDeque<Team> = arrayOf(Team("Time 3"), Team("Time 4")).toCollection(
        ArrayDeque()
    ),
    val vaiA2: Boolean = true,
    val teamToAdd: Team = Team()
) {
    fun pontos() {
    }

    fun pontuarTime1() {
        team1.pontos++
    }

    fun pontuarTime2() {
        team2.pontos++
    }

    fun adicionarTime(team: Team) {
        teamsInQueue.add(team)
    }

    fun removerTime(index: Int) {
        teamsInQueue.removeAt(index)
    }

    fun testarGanhador(): Team? {
        if (!vaiA2) {
            if (team1.pontos >= maxPoints) {
                if (teamsInQueue.isEmpty()) {
                    team2.pontos = 0
                } else {
                    team2 = teamsInQueue.removeFirst()
                }
                team1.pontos = 0
                return team1
            } else if (team2.pontos >= maxPoints) {
                if (teamsInQueue.isEmpty()) {
                    team1.pontos = 0
                } else {
                    team1 = teamsInQueue.removeFirst()
                }
                team2.pontos = 0
                return team2
            }
        } else {
            if (team1.pontos >= maxPoints && team1.pontos >= team2.pontos + 2) {
                println("${team1.nome} ganhou!")
                if (teamsInQueue.isEmpty()) {
                    team2.pontos = 0
                } else {
                    team2 = teamsInQueue.removeFirst()
                }
                team1.pontos = 0
            } else if (team2.pontos >= maxPoints && team2.pontos >= team1.pontos + 2) {
                println("${team2.nome} ganhou!")
                if (teamsInQueue.isEmpty()) {
                    team1.pontos = 0
                } else {
                    team1 = teamsInQueue.removeFirst()
                }
                team2.pontos = 0
            }
        }
        return null
    }
}

sealed class MainScreenEvent {
    data object Team1Scored : MainScreenEvent()
    data object Team2Scored : MainScreenEvent()
    data object DecreaseMaxPoints : MainScreenEvent()
    data object IncreaseMaxPoints : MainScreenEvent()
    data object SwitchClicked : MainScreenEvent()
    data object ClickedAddTeam : MainScreenEvent()
    data object ResetPoints : MainScreenEvent()
    data class OnMaxPointsChanged(val maxPoints: Int) : MainScreenEvent()
    data class OnTeam1NameChanged(val name: String) : MainScreenEvent()
    data class OnTeam2NameChanged(val name: String) : MainScreenEvent()
    data class OnAddTeamNameChanged(val team: String) : MainScreenEvent()
}

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenState())

    val uiState = _uiState.asStateFlow()

    fun onEvent(event: MainScreenEvent) {
        when (event) {
            is MainScreenEvent.Team1Scored -> {
                _uiState.update {
                    it.copy(team1 = it.team1.copy(pontos = it.team1.pontos + 1))
                }
                testWinner()
            }

            is MainScreenEvent.Team2Scored -> {
                _uiState.update {
                    it.copy(team2 = it.team2.copy(pontos = it.team2.pontos + 1))
                }
                testWinner()
            }

            is MainScreenEvent.DecreaseMaxPoints -> _uiState.update {
                it.copy(maxPoints = it.maxPoints - 1)
            }

            is MainScreenEvent.IncreaseMaxPoints -> _uiState.update {
                it.copy(maxPoints = it.maxPoints + 1)
            }

            is MainScreenEvent.SwitchClicked -> _uiState.update {
                it.copy(vaiA2 = !it.vaiA2)
            }

            is MainScreenEvent.ClickedAddTeam -> _uiState.update {
                it.copy(teamsInQueue = it.teamsInQueue.apply { add(it.teamToAdd) })
            }

            is MainScreenEvent.ResetPoints -> _uiState.update {
                it.copy(team1 = it.team1.copy(pontos = 0), team2 = it.team2.copy(pontos = 0))
            }

            is MainScreenEvent.OnMaxPointsChanged -> _uiState.update {
                it.copy(maxPoints = event.maxPoints)
            }

            is MainScreenEvent.OnTeam1NameChanged -> _uiState.update {
                it.copy(team1 = it.team1.copy(nome = event.name))
            }

            is MainScreenEvent.OnTeam2NameChanged -> _uiState.update {
                it.copy(team2 = it.team2.copy(nome = event.name))
            }

            is MainScreenEvent.OnAddTeamNameChanged -> _uiState.update {
                it.copy(teamToAdd = it.teamToAdd.copy(nome = event.team))
            }
        }
    }

    private fun testWinner() {
        _uiState.value.testarGanhador()?.let {
            _uiState.update { state ->
                state.copy(
                    team1 = state.team1.copy(),
                    team2 = state.team2.copy()
                )
            }
        }
    }
}