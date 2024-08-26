package br.dev.saed.voleibr

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import br.dev.saed.voleibr.model.repositories.datastore.DataStoreHelper
import br.dev.saed.voleibr.model.repositories.db.TeamDatabase
import br.dev.saed.voleibr.model.repositories.db.team.TeamRepository
import br.dev.saed.voleibr.model.repositories.db.winner.WinnerRepository
import br.dev.saed.voleibr.ui.navigation.ConfigRoute
import br.dev.saed.voleibr.ui.navigation.HomeRoute
import br.dev.saed.voleibr.ui.navigation.StatsRoute
import br.dev.saed.voleibr.ui.navigation.enterTransition
import br.dev.saed.voleibr.ui.navigation.exitTransition
import br.dev.saed.voleibr.ui.screens.ConfigScreen
import br.dev.saed.voleibr.ui.screens.MainScreen
import br.dev.saed.voleibr.ui.screens.StatsScreen
import br.dev.saed.voleibr.ui.state.MainScreenEvent
import br.dev.saed.voleibr.ui.state.MainScreenState
import br.dev.saed.voleibr.ui.state.StatsScreenEvent
import br.dev.saed.voleibr.ui.theme.VoleibrTheme
import br.dev.saed.voleibr.ui.viewmodel.MainViewModel
import br.dev.saed.voleibr.ui.viewmodel.StatsViewModel
import br.dev.saed.voleibr.utils.vibrator

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VoleibrTheme {
                App()
            }
        }
    }
}


@Composable
private fun App(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val db = Room.databaseBuilder(
        context,
        TeamDatabase::class.java,
        "team_database"
    ).fallbackToDestructiveMigration().build()

    val teamDao = db.teamDAO()
    val winnerDao = db.winnerDAO()

    val mainViewModel = MainViewModel(
        DataStoreHelper(context),
        TeamRepository(teamDao),
        WinnerRepository(winnerDao)
    )
    val mainScreenState by mainViewModel.uiState.collectAsState()

    val statsViewModel = StatsViewModel(WinnerRepository(winnerDao))
    val statsScreenState by statsViewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        enterTransition = {
            enterTransition()
        },
        exitTransition = {
            exitTransition()
        },
        popEnterTransition = {
            enterTransition()
        },
        popExitTransition = {
            exitTransition()
        }
    ) {
        composable<HomeRoute> {
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
                onAddTeamNameChanged = { mainViewModel.onEvent(MainScreenEvent.OnAddTeamNameChanged(it)) },
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
        composable<ConfigRoute> {
            ConfigScreen(
                modifier = modifier,
                uiState = mainScreenState,
                onClickSwitchVaiA2 = { mainViewModel.onEvent(MainScreenEvent.SwitchVaiA2) },
                onClickSwitchVibrar = { mainViewModel.onEvent(MainScreenEvent.SwitchVibrar) },
                onNavigateToHome = {
                    navController.popBackStack(HomeRoute, inclusive = false)
                }
            )
        }

        composable<StatsRoute> {
            StatsScreen(
                modifier = modifier,
                uiState = statsScreenState,
                onNavigateToHome = {
                    navController.popBackStack(HomeRoute, inclusive = false)
                },
                load = { statsViewModel.onEvent(StatsScreenEvent.Load) }
            )
        }
    }
}

@Composable
private fun mainScreen(
    modifier: Modifier,
    mainScreenState: MainScreenState,
    mainViewModel: MainViewModel,
    context: Context,
    navController: NavHostController
) {
}

@Preview
@Composable
private fun AppPreview() {
    VoleibrTheme {
        /*App(
            context = LocalContext.current,
            viewModel = MainViewModel(
                DataStoreHelper(LocalContext.current),
                TeamRepository(

                )
            ),
            uiState = MainScreenState()
        )*/
    }
}