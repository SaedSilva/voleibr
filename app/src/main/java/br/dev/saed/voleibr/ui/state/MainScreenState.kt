package br.dev.saed.voleibr.ui.state

import br.dev.saed.voleibr.model.entities.Team

enum class TeamColor(val color: Int) {
    One(0xFFF8E287.toInt()),
    Two(0xFFEEE2BC.toInt()),
    Three(0xFFC5ECCE.toInt()),
    Four(0xFFB3E6D1.toInt()),
    Red(0xFFE57373.toInt()),
    Blue(0xFF64B5F6.toInt()),
    Green(0xFF81C784.toInt()),
    Yellow(0xFFFFD54F.toInt()),
    Purple(0xFFBA68C8.toInt()),
    Cyan(0xFF4DD0E1.toInt()),
    Orange(0xFFFFB74D.toInt()),
    Pink(0xFFF06292.toInt()),
    Brown(0xFF8D6E63.toInt()),
    Grey(0xFFB0BEC5.toInt()),
    Lime(0xFFC0CA33.toInt()),
    Coral(0xFFFF7043.toInt()),
    Indigo(0xFF5C6BC0.toInt()),
    Teal(0xFF009688.toInt()),
    Amber(0xFFFFB300.toInt()),
    Sky(0xFF29B6F6.toInt()),
    Emerald(0xFF43A047.toInt()),
    Lavender(0xFFAB47BC.toInt()),
    Mint(0xFF80CBC4.toInt()),
    Navy(0xFF1565C0.toInt()),
    Ruby(0xFFD32F2F.toInt()),
    Olive(0xFF827717.toInt()),
    Slate(0xFF607D8B.toInt()),
    Peach(0xFFFFAB91.toInt());
}

fun intToTeamColor(color: Int): TeamColor {
    return TeamColor.entries.find { it.color == color } ?: TeamColor.One
}

fun randomTeamColor(): TeamColor {
    return TeamColor.entries.random()
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
    val keepScreenOn: Boolean = true,
    val teamToAdd: Team = Team(),
    val winner: Team? = null
) {

    val isVaiA2Triggered: Boolean
        get() = vaiA2 && winner == null &&
                team1.pontos >= maxPoints - 1 && team2.pontos >= maxPoints - 1

    val team1MatchPoint: Boolean
        get() = if (vaiA2) {
            team1.pontos >= maxPoints - 1 && team1.pontos >= team2.pontos + 1
        } else {
            team1.pontos >= maxPoints - 1
        }

    val team2MatchPoint: Boolean
        get() = if (vaiA2) {
            team2.pontos >= maxPoints - 1 && team2.pontos >= team1.pontos + 1
        } else {
            team2.pontos >= maxPoints - 1
        }

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
    data object SwitchKeepScreenOn : MainScreenEvent()
    data object ClearQueue : MainScreenEvent()
    data object ClickedAddTeam : MainScreenEvent()
    data object ResetPoints : MainScreenEvent()
    data class ClickedDeleteTeam(val team: Team) : MainScreenEvent()
    data class OnAddTeamNameChanged(val team: String) : MainScreenEvent()
}
