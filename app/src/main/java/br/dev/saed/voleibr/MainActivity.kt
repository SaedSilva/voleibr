package br.dev.saed.voleibr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import br.dev.saed.voleibr.ui.navigation.HomeRoute
import br.dev.saed.voleibr.ui.navigation.configScreen
import br.dev.saed.voleibr.ui.navigation.mainScreen
import br.dev.saed.voleibr.ui.navigation.statsScreen
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
    val navController = rememberNavController()

    val mainViewModel = koinViewModel<MainViewModel>()

    val statsViewModel = koinViewModel<StatsViewModel>()

    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        },
        popEnterTransition = {
            EnterTransition.None
        },
        popExitTransition = {
            ExitTransition.None
        }
    ) {
        mainScreen(modifier, navController, mainViewModel)
        configScreen(modifier, mainViewModel, navController)
        statsScreen(modifier, navController, statsViewModel)
    }
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