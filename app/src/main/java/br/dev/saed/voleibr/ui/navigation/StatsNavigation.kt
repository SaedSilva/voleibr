package br.dev.saed.voleibr.ui.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.dev.saed.voleibr.ui.screens.StatsScreen
import br.dev.saed.voleibr.ui.state.StatsScreenEvent
import br.dev.saed.voleibr.ui.viewmodel.StatsViewModel
import kotlinx.serialization.Serializable

@Serializable
object StatsRoute

fun NavGraphBuilder.statsScreen(
    modifier: Modifier,
    navController: NavHostController,
    statsViewModel: StatsViewModel
) {
    composable<StatsRoute> {
        val statsScreenState by statsViewModel.uiState.collectAsState()
        StatsScreen(
            modifier = modifier,
            uiState = statsScreenState,
            onNavigateToHome = {
                navController.popBackStack(HomeRoute, inclusive = false)
            },
            deleteTeam = { statsViewModel.onEvent(StatsScreenEvent.DeleteTeam(it)) },
            deleteAllTeams = { statsViewModel.onEvent(StatsScreenEvent.DeleteAll) }
        )
    }
}