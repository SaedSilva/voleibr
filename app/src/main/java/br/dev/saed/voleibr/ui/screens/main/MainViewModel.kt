package br.dev.saed.voleibr.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.saed.voleibr.model.entities.Team
import br.dev.saed.voleibr.model.repositories.datastore.DataStoreHelper
import br.dev.saed.voleibr.model.repositories.db.TeamEntity
import br.dev.saed.voleibr.model.repositories.db.TeamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
            val maxPoints = dataStoreHelper.maxPointsFlow.first()
            val team1 = dataStoreHelper.team1Flow.first()
            val pointTeam1 = dataStoreHelper.team1PointsFlow.first()
            val team2 = dataStoreHelper.team2Flow.first()
            val pointTeam2 = dataStoreHelper.team2PointsFlow.first()
            val vaiA2 = dataStoreHelper.vaiA2Flow.first()
            val vibrar = dataStoreHelper.vibrarFlow.first()
            val teams = repository.queue.first()

            _uiState.update {
                it.copy(
                    maxPoints = maxPoints,
                    team1 = Team(nome = team1, pontos = pointTeam1),
                    team2 = Team(nome = team2, pontos = pointTeam2),
                    vaiA2 = vaiA2,
                    vibrar = vibrar,
                    teamsInQueue = teams.map { team ->
                        Team(id = team.id, nome = team.name)
                    }
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

            is MainScreenEvent.Team1ScoreDecreased -> {
                if (_uiState.value.team1.pontos > 0) {
                    _uiState.update {
                        it.copy(team1 = it.team1.copy(pontos = it.team1.pontos - 1))
                    }
                    viewModelScope.launch {
                        dataStoreHelper.savePointsTeam1(_uiState.value.team1.pontos)
                    }
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

            is MainScreenEvent.Team2ScoreDecreased -> {
                if (_uiState.value.team2.pontos > 0) {
                    _uiState.update {
                        it.copy(team2 = it.team2.copy(pontos = it.team2.pontos - 1))
                    }
                    viewModelScope.launch {
                        dataStoreHelper.savePointsTeam2(_uiState.value.team2.pontos)
                    }
                }
            }

            is MainScreenEvent.DecreaseMaxPoints -> {
                _uiState.update {
                    if (it.maxPoints > 1) {
                        it.copy(maxPoints = it.maxPoints - 1)
                    } else {
                        it
                    }
                }
                viewModelScope.launch {
                    dataStoreHelper.saveMaxPoints(_uiState.value.maxPoints)
                }

            }

            is MainScreenEvent.IncreaseMaxPoints -> {
                _uiState.update {
                    if (it.maxPoints < 99) {
                        it.copy(maxPoints = it.maxPoints + 1)
                    } else {
                        it
                    }
                }
                viewModelScope.launch {
                    dataStoreHelper.saveMaxPoints(_uiState.value.maxPoints)
                }
            }

            is MainScreenEvent.ChangeTeams -> switchTeams()

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

            is MainScreenEvent.ClearQueue -> {
                viewModelScope.launch {
                    repository.removeAllTeams()
                }
                _uiState.update {
                    it.copy(teamsInQueue = emptyList())
                }
            }

            is MainScreenEvent.ClickedAddTeam -> {
                viewModelScope.launch {
                    repository.addTeam(TeamEntity(0, _uiState.value.teamToAdd.nome))
                }
                _uiState.update {
                    it.copy(teamsInQueue = it.teamsInQueue + it.teamToAdd)
                }
            }

            is MainScreenEvent.ClickedDeleteTeam -> {
                viewModelScope.launch {
                    repository.removeTeam(TeamEntity(event.team.id, event.team.nome))
                    val teams = repository.queue.first()
                    _uiState.update {
                        it.copy(teamsInQueue = teams.map { team ->
                            Team(team.id, team.name)
                        })
                    }
                }
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
        val winner = _uiState.value.testarGanhador()
        if (winner != null) {
            _uiState.update {
                it.copy(team1 = it.team1.copy(pontos = 0), team2 = it.team2.copy(pontos = 0))
            }
            viewModelScope.launch {
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
            val teams = repository.queue.first()

            _uiState.update {
                it.copy(
                    team1 = Team(nome = dataStoreHelper.team1Flow.first(), pontos = 0),
                    team2 = Team(nome = dataStoreHelper.team2Flow.first(), pontos = 0),
                    teamsInQueue = teams.map { team ->
                        Team(team.id, team.name)
                    }
                )
            }
        }
    }

    private fun switchTeams() {
        viewModelScope.launch {
            val team1 = dataStoreHelper.team1Flow.first()
            val team2 = dataStoreHelper.team2Flow.first()
            dataStoreHelper.saveTeam1(team2)
            dataStoreHelper.saveTeam2(team1)
            _uiState.update {
                it.copy(
                    team1 = Team(nome = team2, pontos = it.team2.pontos),
                    team2 = Team(nome = team1, pontos = it.team1.pontos)
                )
            }
        }
    }
}