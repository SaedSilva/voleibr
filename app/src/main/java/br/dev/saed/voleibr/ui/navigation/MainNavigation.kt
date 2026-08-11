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
            onEvent = { event ->
                mainViewModel.onEvent(event)
                if (event is MainScreenEvent.Team1Scored || event is MainScreenEvent.Team2Scored) {
                    if (mainViewModel.uiState.value.vibrar) {
                        context.vibrator(1010)
                    }
                }
            },
            onNavigateToStats = { navController.navigate(StatsRoute) }
        )
    }
}
