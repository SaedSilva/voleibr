package br.dev.saed.voleibr.ui.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.dev.saed.voleibr.ui.screens.MainScreen
import br.dev.saed.voleibr.ui.state.MainScreenEvent
import br.dev.saed.voleibr.ui.viewmodel.MainViewModel
import br.dev.saed.voleibr.utils.vibrator
import kotlinx.serialization.Serializable


@Serializable
object HomeRoute

fun NavGraphBuilder.mainScreen(
    modifier: Modifier,
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    composable<HomeRoute> {
        val mainScreenState by mainViewModel.uiState.collectAsState()
        val context = LocalContext.current

        MainScreen(
            modifier = modifier,
            uiState = mainScreenState,
            onClickDecreaseMaxPoints = { mainViewModel.onEvent(MainScreenEvent.DecreaseMaxPoints) },
            onClickIncreaseMaxPoints = { mainViewModel.onEvent(MainScreenEvent.IncreaseMaxPoints) },
            onClickTeam1Scored = {
                mainViewModel.onEvent(MainScreenEvent.Team1Scored)
                if (mainViewModel.uiState.value.vibrar) {
                    context.vibrator(1010)
                }
            },
            onClickTeam1ScoreDecrease = { mainViewModel.onEvent(MainScreenEvent.Team1ScoreDecreased) },
            onClickTeam2Scored = {
                mainViewModel.onEvent(MainScreenEvent.Team2Scored)
                if (mainViewModel.uiState.value.vibrar) {
                    context.vibrator(1010)
                }
            },
            onClickTeam2ScoreDecrease = { mainViewModel.onEvent(MainScreenEvent.Team2ScoreDecreased) },
            onClickChangeTeams = { mainViewModel.onEvent(MainScreenEvent.ChangeTeams) },
            onClickClearQueue = { mainViewModel.onEvent(MainScreenEvent.ClearQueue) },
            onAddTeamNameChanged = {
                mainViewModel.onEvent(
                    MainScreenEvent.OnAddTeamNameChanged(
                        it
                    )
                )
            },
            onClickAddTeam = { mainViewModel.onEvent(MainScreenEvent.ClickedAddTeam) },
            onClickDeleteTeam = { mainViewModel.onEvent(MainScreenEvent.ClickedDeleteTeam(it)) },
            onClickResetPoints = { mainViewModel.onEvent(MainScreenEvent.ResetPoints) },
            onClickRemoveTeam1 = { mainViewModel.onEvent(MainScreenEvent.RemoveTeam1) },
            onClickRemoveTeam2 = { mainViewModel.onEvent(MainScreenEvent.RemoveTeam2) },
            onNavigateToConfig = { navController.navigate(ConfigRoute) },
            onClickChangeTeam1Color = { mainViewModel.onEvent(MainScreenEvent.ChangeTeam1Color) },
            onClickChangeTeam2Color = { mainViewModel.onEvent(MainScreenEvent.ChangeTeam2Color) },
            onNavigateToStats = { navController.navigate(StatsRoute) }
        )
    }
}
