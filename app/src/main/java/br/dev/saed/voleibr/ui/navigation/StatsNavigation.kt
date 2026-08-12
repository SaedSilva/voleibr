package br.dev.saed.voleibr.ui.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import br.dev.saed.voleibr.ui.screens.StatsScreen
import br.dev.saed.voleibr.ui.viewmodel.StatsViewModel
import kotlinx.serialization.Serializable

@Serializable
data object StatsRoute : NavKey

fun EntryProviderScope<NavKey>.statsScreen(
    modifier: Modifier,
    navigator: Navigator,
    statsViewModel: StatsViewModel
) {
    entry<StatsRoute> {
        val statsScreenState by statsViewModel.uiState.collectAsState()
        StatsScreen(
            modifier = modifier,
            uiState = statsScreenState,
            onNavigateToHome = { navigator.goBack() },
            onEvent = { statsViewModel.onEvent(it) }
        )
    }
}
