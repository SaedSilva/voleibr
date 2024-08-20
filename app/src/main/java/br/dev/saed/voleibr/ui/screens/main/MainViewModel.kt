package br.dev.saed.voleibr.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.saed.voleibr.model.entities.Team
import br.dev.saed.voleibr.model.repositories.datastore.DataStoreHelper
import br.dev.saed.voleibr.model.repositories.db.TeamEntity
import br.dev.saed.voleibr.model.repositories.db.TeamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
            ) {
                val maxPoints = it[0] as Int
                val team1 = it[1] as String
                val team1Points = it[2] as Int
                val team2 = it[3] as String
                val team2Points = it[4] as Int

                val vaiA2 = it[5] as Boolean
                val vibrar = it[6] as Boolean

                val teamsInQueue = it[7] as List<TeamEntity>

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
            }.collect {
                _uiState.value = it
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
        val winner = _uiState.value.testarGanhador()
        if (winner != null) {
            viewModelScope.launch {
                dataStoreHelper.savePointsTeam1(0)
                dataStoreHelper.savePointsTeam2(0)

                val queue = repository.queue.first()
                if (queue.isNotEmpty()) {
                    val loser =
                        if (winner == _uiState.value.team1) _uiState.value.team2 else _uiState.value.team1
                    rotateTeams(loser)
                }
            }
        }
    }

    private fun rotateTeams(loser: Team) {
        viewModelScope.launch {
            repository.addTeam(TeamEntity(loser.id, loser.nome))

            val teamToDataStore = repository.queue.first().first()

            if (dataStoreHelper.team1Flow.first() == loser.nome) {
                dataStoreHelper.saveTeam1(teamToDataStore.name)
            } else {
                dataStoreHelper.saveTeam2(teamToDataStore.name)
            }
            repository.removeFirstTeam()
        }
    }

    private fun switchTeams() {
        viewModelScope.launch {
            val team1 = dataStoreHelper.team1Flow.first()
            val team2 = dataStoreHelper.team2Flow.first()
            dataStoreHelper.saveTeam1(team2)
            dataStoreHelper.saveTeam2(team1)
        }
    }
}