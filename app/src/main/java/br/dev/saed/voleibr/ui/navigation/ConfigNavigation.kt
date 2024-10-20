package br.dev.saed.voleibr.ui.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.dev.saed.voleibr.ui.screens.ConfigScreen
import br.dev.saed.voleibr.ui.state.MainScreenEvent
import br.dev.saed.voleibr.ui.viewmodel.MainViewModel
import kotlinx.serialization.Serializable

@Serializable
object ConfigRoute

fun NavGraphBuilder.configScreen(
    modifier: Modifier,
    mainViewModel: MainViewModel,
    navController: NavHostController
) {
    composable<ConfigRoute> {
        val mainScreenState by mainViewModel.uiState.collectAsState()
        ConfigScreen(
            modifier = modifier,
            uiState = mainScreenState,
            onClickSwitchVaiA2 = { mainViewModel.onEvent(MainScreenEvent.SwitchVaiA2) },
            onClickSwitchVibrar = { mainViewModel.onEvent(MainScreenEvent.SwitchVibrar) },
            onNavigateToHome = { navController.popBackStack(HomeRoute, inclusive = false) }
        )
    }
}