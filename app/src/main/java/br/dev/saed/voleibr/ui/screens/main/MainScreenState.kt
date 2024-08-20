package br.dev.saed.voleibr.ui.screens.main

import br.dev.saed.voleibr.model.entities.Team

data class MainScreenState(
    var maxPoints: Int = 12,
    var team1: Team = Team(nome = "Time 1"),
    var team2: Team = Team(nome = "Time 2"),
    val teamsInQueue: List<Team> = ArrayDeque(),
    val vaiA2: Boolean = true,
    val vibrar: Boolean = true,
    val teamToAdd: Team = Team(),
    val winner: Team? = null
) {

    fun testarGanhador(): Team? {
        if (!vaiA2) {
            if (team1.pontos >= maxPoints) {
                if (teamsInQueue.isEmpty()) {
                    team2.pontos = 0
                }
                team1.pontos = 0
                return team1
            } else if (team2.pontos >= maxPoints) {
                if (teamsInQueue.isEmpty()) {
                    team1.pontos = 0
                }
                team2.pontos = 0
                return team2
            }
        } else {
            if (team1.pontos >= maxPoints && team1.pontos >= team2.pontos + 2) {
                println("${team1.nome} ganhou!")
                if (teamsInQueue.isEmpty()) {
                    team2.pontos = 0
                }
                team1.pontos = 0
                return team1
            } else if (team2.pontos >= maxPoints && team2.pontos >= team1.pontos + 2) {
                println("${team2.nome} ganhou!")
                if (teamsInQueue.isEmpty()) {
                    team1.pontos = 0
                }
                team2.pontos = 0
                return team2
            }
        }
        return null
    }
}

sealed class MainScreenEvent {
    data object Team1Scored : MainScreenEvent()
    data object Team1ScoreDecreased : MainScreenEvent()
    data object Team2Scored : MainScreenEvent()
    data object Team2ScoreDecreased : MainScreenEvent()
    data object DecreaseMaxPoints : MainScreenEvent()
    data object IncreaseMaxPoints : MainScreenEvent()
    data object RemoveTeam1: MainScreenEvent()
    data object RemoveTeam2: MainScreenEvent()
    data object ChangeTeams : MainScreenEvent()
    data object SwitchVaiA2 : MainScreenEvent()
    data object SwitchVibrar : MainScreenEvent()
    data object ClearQueue : MainScreenEvent()
    data object ClickedAddTeam : MainScreenEvent()
    data object ResetPoints : MainScreenEvent()
    data class ClickedDeleteTeam(val team: Team) : MainScreenEvent()
    data class OnAddTeamNameChanged(val team: String) : MainScreenEvent()
}
