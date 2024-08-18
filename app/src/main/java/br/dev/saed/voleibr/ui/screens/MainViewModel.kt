package br.dev.saed.voleibr.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.saed.voleibr.model.entities.Team
import br.dev.saed.voleibr.model.repositories.datastore.DataStoreHelper
import br.dev.saed.voleibr.model.repositories.db.TeamDAO
import br.dev.saed.voleibr.model.repositories.db.TeamDatabase
import br.dev.saed.voleibr.model.repositories.db.TeamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MainScreenState(
    var maxPoints: Int = 12,
    var team1: Team = Team("Time 1"),
    var team2: Team = Team("Time 2"),
    val teamsInQueue: ArrayDeque<Team> = arrayOf(Team("Time 3"), Team("Time 4")).toCollection(
        ArrayDeque()
    ),
    val vaiA2: Boolean = true,
    val vibrar: Boolean = true,
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
    data object SwitchVaiA2 : MainScreenEvent()
    data object SwitchVibrar: MainScreenEvent()
    data object ClickedAddTeam : MainScreenEvent()
    data object ResetPoints : MainScreenEvent()
    data class OnMaxPointsChanged(val maxPoints: Int) : MainScreenEvent()
    data class OnTeam1NameChanged(val name: String) : MainScreenEvent()
    data class OnTeam2NameChanged(val name: String) : MainScreenEvent()
    data class OnAddTeamNameChanged(val team: String) : MainScreenEvent()
}

class MainViewModel(
    private val dataStoreHelper: DataStoreHelper,
    private val repository: TeamRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenState())

    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val maxPoints = dataStoreHelper.maxPointsFlow.first()
            val team1 = dataStoreHelper.team1Flow.first()
            val pointTeam1 = dataStoreHelper.team1PointsFlow.first()
            val team2 = dataStoreHelper.team2Flow.first()
            val pointTeam2 = dataStoreHelper.team2PointsFlow.first()
            val vaiA2 = dataStoreHelper.vaiA2Flow.first()
            val vibrar = dataStoreHelper.vibrarFlow.first()

            _uiState.update {
                it.copy(
                    maxPoints = maxPoints,
                    team1 = Team(team1, pointTeam1),
                    team2 = Team(team2, pointTeam2),
                    vaiA2 = vaiA2,
                    vibrar = vibrar
                )
            }
        }
    }

    fun onEvent(event: MainScreenEvent) {
        when (event) {
            is MainScreenEvent.Team1Scored -> {
                _uiState.update {
                    it.copy(team1 = it.team1.copy(pontos = it.team1.pontos + 1))
                }
                testWinner()
                viewModelScope.launch {
                    dataStoreHelper.savePointsTeam1(_uiState.value.team1.pontos)
                }
            }

            is MainScreenEvent.Team2Scored -> {
                _uiState.update {
                    it.copy(team2 = it.team2.copy(pontos = it.team2.pontos + 1))
                }
                testWinner()
                viewModelScope.launch {
                    dataStoreHelper.savePointsTeam2(_uiState.value.team2.pontos)
                }
            }

            is MainScreenEvent.DecreaseMaxPoints -> {
                _uiState.update {
                    it.copy(maxPoints = it.maxPoints - 1)
                }
                viewModelScope.launch {
                    dataStoreHelper.saveMaxPoints(_uiState.value.maxPoints)
                }
            }

            is MainScreenEvent.IncreaseMaxPoints -> {
                _uiState.update {
                    it.copy(maxPoints = it.maxPoints + 1)
                }
                viewModelScope.launch {
                    dataStoreHelper.saveMaxPoints(_uiState.value.maxPoints)
                }
            }

            is MainScreenEvent.SwitchVaiA2 -> {
                _uiState.update {
                    it.copy(vaiA2 = !it.vaiA2)
                }
                viewModelScope.launch {
                    dataStoreHelper.saveVaiA2(_uiState.value.vaiA2)
                }
            }

            is MainScreenEvent.SwitchVibrar -> {
                _uiState.update {
                    it.copy(vibrar = !it.vibrar)
                }

                viewModelScope.launch {
                    dataStoreHelper.saveVibrar(_uiState.value.vibrar)
                }
            }

            is MainScreenEvent.ClickedAddTeam -> _uiState.update {
                it.copy(teamsInQueue = it.teamsInQueue.apply { add(it.teamToAdd) })
            }

            is MainScreenEvent.ResetPoints -> {
                _uiState.update {
                    it.copy(team1 = it.team1.copy(pontos = 0), team2 = it.team2.copy(pontos = 0))
                }
                viewModelScope.launch {
                    dataStoreHelper.savePointsTeam1(0)
                    dataStoreHelper.savePointsTeam2(0)
                }
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
        viewModelScope.launch {
            dataStoreHelper.saveTeam1(_uiState.value.team1.nome)
            dataStoreHelper.saveTeam2(_uiState.value.team2.nome)
        }
    }
}