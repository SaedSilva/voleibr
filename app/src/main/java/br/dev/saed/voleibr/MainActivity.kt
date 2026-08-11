package br.dev.saed.voleibr

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import br.dev.saed.voleibr.ui.navigation.HomeRoute
import br.dev.saed.voleibr.ui.navigation.mainScreen
import br.dev.saed.voleibr.ui.navigation.statsScreen
import br.dev.saed.voleibr.ui.theme.VoleibrTheme
import br.dev.saed.voleibr.ui.viewmodel.MainViewModel
import br.dev.saed.voleibr.ui.viewmodel.StatsViewModel
import kotlinx.coroutines.flow.map
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
    val navController = rememberNavController()

    val mainViewModel = koinViewModel<MainViewModel>()

    val statsViewModel = koinViewModel<StatsViewModel>()

    val keepScreenOn = mainViewModel.uiState.collectAsStateWithLifecycle().value.keepScreenOn

    val context = LocalContext.current

    LaunchedEffect(keepScreenOn) {
        if (context is Activity) {
            if (keepScreenOn) {
                context.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            } else {
                context.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        mainScreen(modifier, navController, mainViewModel)
        statsScreen(modifier, navController, statsViewModel)
    }
}
