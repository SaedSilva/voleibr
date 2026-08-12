package br.dev.saed.voleibr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.keepScreenOn
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import br.dev.saed.voleibr.ui.navigation.HomeRoute
import br.dev.saed.voleibr.ui.navigation.Navigator
import br.dev.saed.voleibr.ui.navigation.mainScreen
import br.dev.saed.voleibr.ui.navigation.rememberNavigationState
import br.dev.saed.voleibr.ui.navigation.statsScreen
import br.dev.saed.voleibr.ui.navigation.toEntries
import br.dev.saed.voleibr.ui.theme.VoleibrTheme
import br.dev.saed.voleibr.ui.viewmodel.MainViewModel
import br.dev.saed.voleibr.ui.viewmodel.StatsViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VoleibrTheme {
                App(modifier = Modifier.fillMaxSize())
            }
        }
    }
}


@Composable
private fun App(
    modifier: Modifier = Modifier
) {
    val navigationState = rememberNavigationState(
        startRoute = HomeRoute,
        topLevelRoutes = setOf(HomeRoute)
    )
    val navigator = remember { Navigator(navigationState) }

    val mainViewModel = koinViewModel<MainViewModel>()

    val statsViewModel = koinViewModel<StatsViewModel>()

    val keepScreenOn = mainViewModel.uiState.collectAsStateWithLifecycle().value.keepScreenOn

    val entryProvider = entryProvider {
        mainScreen(modifier, navigator, mainViewModel)
        statsScreen(modifier, navigator, statsViewModel)
    }

    NavDisplay(
        modifier = if (keepScreenOn) Modifier.keepScreenOn() else Modifier,
        entries = navigationState.toEntries(entryProvider),
        onBack = { navigator.goBack() }
    )
}
