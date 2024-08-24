package br.dev.saed.voleibr.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.saed.voleibr.model.entities.Team
import br.dev.saed.voleibr.model.repositories.datastore.DataStoreHelper
import br.dev.saed.voleibr.model.repositories.db.TeamEntity
import br.dev.saed.voleibr.model.repositories.db.TeamRepository
import br.dev.saed.voleibr.ui.state.MainScreenEvent
import br.dev.saed.voleibr.ui.state.MainScreenState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class MainViewModel(
    private val dataStoreHelper: DataStoreHelper,
    private val repository: TeamRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                dataStoreHelper.maxPointsFlow,
                dataStoreHelper.team1Flow,
                dataStoreHelper.team1PointsFlow,
                dataStoreHelper.team2Flow,
                dataStoreHelper.team2PointsFlow,
                dataStoreHelper.vaiA2Flow,
                dataStoreHelper.vibrarFlow,
                repository.queue
            ) { array ->
                val maxPoints = array[0] as Int
                val team1 = array[1] as String
                val team1Points = array[2] as Int
                val team2 = array[3] as String
                val team2Points = array[4] as Int

                val vaiA2 = array[5] as Boolean
                val vibrar = array[6] as Boolean

                val teamsInQueue = array[7] as List<TeamEntity>

                MainScreenState(
                    maxPoints = maxPoints,
                    team1 = Team(nome = team1, pontos = team1Points),
                    team2 = Team(nome = team2, pontos = team2Points),
                    vaiA2 = vaiA2,
                    vibrar = vibrar,
                    teamsInQueue = teamsInQueue.map { team ->
                        Team(team.id, team.name)
                    }
                )
            }.collect {newState ->
                _uiState.update {
                    it.copy(
                        maxPoints = newState.maxPoints,
                        team1 = newState.team1,
                        team2 = newState.team2,
                        vaiA2 = newState.vaiA2,
                        vibrar = newState.vibrar,
                        teamsInQueue = newState.teamsInQueue
                    )
                }
            }
        }
    }

    fun onEvent(event: MainScreenEvent) {
        when (event) {
            is MainScreenEvent.Team1Scored -> {
                viewModelScope.launch {
                    dataStoreHelper.savePointsTeam1(_uiState.value.team1.pontos + 1)
                    testWinner()
                }
            }

            is MainScreenEvent.Team1ScoreDecreased -> {
                if (_uiState.value.team1.pontos > 0) {
                    viewModelScope.launch {
                        dataStoreHelper.savePointsTeam1(_uiState.value.team1.pontos - 1)
                    }
                }
            }

            is MainScreenEvent.Team2Scored -> {
                viewModelScope.launch {
                    dataStoreHelper.savePointsTeam2(_uiState.value.team2.pontos + 1)
                    testWinner()
                }
            }

            is MainScreenEvent.Team2ScoreDecreased -> {
                if (_uiState.value.team2.pontos > 0) {
                    viewModelScope.launch {
                        dataStoreHelper.savePointsTeam2(_uiState.value.team2.pontos - 1)
                    }
                }
            }

            is MainScreenEvent.DecreaseMaxPoints -> {
                if (_uiState.value.maxPoints > 1) {
                    viewModelScope.launch {
                        dataStoreHelper.saveMaxPoints(_uiState.value.maxPoints - 1)
                    }
                }
            }

            is MainScreenEvent.IncreaseMaxPoints -> {
                if (_uiState.value.maxPoints < 99) {
                    viewModelScope.launch {
                        dataStoreHelper.saveMaxPoints(_uiState.value.maxPoints + 1)
                    }
                }
            }

            is MainScreenEvent.RemoveTeam1 -> {
                viewModelScope.launch {
                    val teamToDataStore = _uiState.value.teamsInQueue.first()
                    repository.removeFirstTeam()

                    dataStoreHelper.saveTeam1(teamToDataStore.nome)
                    dataStoreHelper.savePointsTeam1(teamToDataStore.pontos)
                }
            }

            is MainScreenEvent.RemoveTeam2 -> {
                viewModelScope.launch {
                    val teamToDataStore = _uiState.value.teamsInQueue.first()
                    repository.removeFirstTeam()

                    dataStoreHelper.saveTeam2(teamToDataStore.nome)
                    dataStoreHelper.savePointsTeam2(teamToDataStore.pontos)
                }
            }

            is MainScreenEvent.ChangeTeams -> switchTeams()

            is MainScreenEvent.SwitchVaiA2 -> {
                viewModelScope.launch {
                    dataStoreHelper.saveVaiA2(!_uiState.value.vaiA2)
                }
            }

            is MainScreenEvent.SwitchVibrar -> {
                viewModelScope.launch {
                    dataStoreHelper.saveVibrar(!_uiState.value.vibrar)
                }
            }

            is MainScreenEvent.ClearQueue -> {
                viewModelScope.launch {
                    repository.removeAllTeams()
                }
            }

            is MainScreenEvent.ClickedAddTeam -> {
                if (_uiState.value.teamToAdd.nome.isNotBlank()) {
                    viewModelScope.launch {
                        repository.addTeam(TeamEntity(0, _uiState.value.teamToAdd.nome))
                    }
                }
            }

            is MainScreenEvent.ClickedDeleteTeam -> {
                viewModelScope.launch {
                    repository.removeTeam(TeamEntity(event.team.id, event.team.nome))
                }
            }

            is MainScreenEvent.ResetPoints -> {
                viewModelScope.launch {
                    dataStoreHelper.savePointsTeam1(0)
                    dataStoreHelper.savePointsTeam2(0)
                }
            }

            is MainScreenEvent.OnAddTeamNameChanged -> _uiState.update {
                it.copy(teamToAdd = it.teamToAdd.copy(nome = event.team))
            }
        }
    }

    private fun testWinner() {
        val winner = _uiState.value.testarGanhador()
        if (winner != null) {
            viewModelScope.launch {
                if (_uiState.value.teamsInQueue.isNotEmpty()) {
                    val loser =
                        if (winner == _uiState.value.team1) _uiState.value.team2 else _uiState.value.team1
                    rotateTeams(loser)
                }

                dataStoreHelper.savePointsTeam1(0)
                dataStoreHelper.savePointsTeam2(0)

                _uiState.update {
                    it.copy(winner = winner)
                }
                delay(2000)
                _uiState.update {
                    it.copy(winner = null)
                }
            }
        }
    }

    private fun rotateTeams(loser: Team) {
        viewModelScope.launch {
            repository.addTeam(TeamEntity(loser.id, loser.nome))
            val teamToDataStore = _uiState.value.teamsInQueue.first()
            repository.removeFirstTeam()

            if (dataStoreHelper.team1Flow.first() == loser.nome) {
                dataStoreHelper.saveTeam1(teamToDataStore.nome)
            } else {
                dataStoreHelper.saveTeam2(teamToDataStore.nome)
            }
        }
    }

    private fun switchTeams() {
        viewModelScope.launch {
            val team1 = _uiState.value.team1
            val team2 = _uiState.value.team2

            dataStoreHelper.saveTeam1(team2.nome)
            dataStoreHelper.savePointsTeam1(team2.pontos)

            dataStoreHelper.saveTeam2(team1.nome)
            dataStoreHelper.savePointsTeam2(team1.pontos)
        }
    }
}