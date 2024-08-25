package br.dev.saed.voleibr.ui.state

import br.dev.saed.voleibr.model.entities.Team

sealed class TeamColor(val color: Int) {
    data object One: TeamColor(0xFFF8E287.toInt())
    data object Two: TeamColor(0xFFEEE2BC.toInt())
    data object Three: TeamColor(0xFFC5ECCE.toInt())
    data object Four: TeamColor(0xFFB3E6D1.toInt())
    data object Red: TeamColor(0xFFE57373.toInt())
    data object Blue: TeamColor(0xFF64B5F6.toInt())
    data object Gree: TeamColor(0xFF81C784.toInt())
    data object Yellow: TeamColor(0xFFFFD54F.toInt())
    data object Purple: TeamColor(0xFFBA68C8.toInt())
    data object Cyan: TeamColor(0xFF4DD0E1.toInt())
    data object Orange: TeamColor(0xFFFFB74D.toInt())
    data object Pink: TeamColor(0xFFE57373.toInt())
    data object Brown: TeamColor(0xFF8D6E63.toInt())
    data object Grey: TeamColor(0xFFB0BEC5.toInt())
}

fun intToTeamColor(color: Int): TeamColor {
    return when (color) {
        TeamColor.One.color -> TeamColor.One
        TeamColor.Two.color -> TeamColor.Two
        TeamColor.Three.color -> TeamColor.Three
        TeamColor.Four.color -> TeamColor.Four
        TeamColor.Red.color -> TeamColor.Red
        TeamColor.Blue.color -> TeamColor.Blue
        TeamColor.Gree.color -> TeamColor.Gree
        TeamColor.Yellow.color -> TeamColor.Yellow
        TeamColor.Purple.color -> TeamColor.Purple
        TeamColor.Cyan.color -> TeamColor.Cyan
        TeamColor.Orange.color -> TeamColor.Orange
        TeamColor.Pink.color -> TeamColor.Pink
        TeamColor.Brown.color -> TeamColor.Brown
        TeamColor.Grey.color -> TeamColor.Grey
        else -> TeamColor.One
    }
}

fun randomTeamColor(): TeamColor {
    val colors = listOf(
        TeamColor.One,
        TeamColor.Two,
        TeamColor.Three,
        TeamColor.Four,
        TeamColor.Red,
        TeamColor.Blue,
        TeamColor.Gree,
        TeamColor.Yellow,
        TeamColor.Purple,
        TeamColor.Cyan,
        TeamColor.Orange,
        TeamColor.Pink,
        TeamColor.Brown,
        TeamColor.Grey
    )
    return colors.random()
}

data class MainScreenState(
    var maxPoints: Int = 12,
    var team1: Team = Team(nome = "Time 1"),
    var team1Color: TeamColor = TeamColor.One,
    var team2: Team = Team(nome = "Time 2"),
    var team2Color: TeamColor = TeamColor.Three,
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
    data object ChangeTeam1Color : MainScreenEvent()
    data object ChangeTeam2Color : MainScreenEvent()
    data object SwitchVaiA2 : MainScreenEvent()
    data object SwitchVibrar : MainScreenEvent()
    data object ClearQueue : MainScreenEvent()
    data object ClickedAddTeam : MainScreenEvent()
    data object ResetPoints : MainScreenEvent()
    data class ClickedDeleteTeam(val team: Team) : MainScreenEvent()
    data class OnAddTeamNameChanged(val team: String) : MainScreenEvent()
}
