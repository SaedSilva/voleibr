package br.dev.saed.voleibr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import br.dev.saed.voleibr.model.repositories.DataStoreHelper
import br.dev.saed.voleibr.ui.screens.MainScreen
import br.dev.saed.voleibr.ui.screens.MainScreenEvent
import br.dev.saed.voleibr.ui.screens.MainViewModel
import br.dev.saed.voleibr.ui.theme.VoleibrTheme
import br.dev.saed.voleibr.utils.vibrator

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VoleibrTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    App(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
private fun App(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModel = MainViewModel(DataStoreHelper(context))
    val uiState by viewModel.uiState.collectAsState()
    VoleibrTheme {
        MainScreen(
            modifier = modifier,
            uiState = uiState,
            onClickDecreaseMaxPoints = { viewModel.onEvent(MainScreenEvent.DecreaseMaxPoints) },
            onClickIncreaseMaxPoints = { viewModel.onEvent(MainScreenEvent.IncreaseMaxPoints) },
            onClickTeam1Scored = {
                viewModel.onEvent(MainScreenEvent.Team1Scored)
                context.vibrator(1010)
            },
            onClickTeam2Scored = {
                viewModel.onEvent(MainScreenEvent.Team2Scored)
                context.vibrator(1010)
            },
            onClickSwitchVaiA2 = { viewModel.onEvent(MainScreenEvent.SwitchVaiA2) },
            onClickSwitchVibrar = { viewModel.onEvent(MainScreenEvent.SwitchVibrar) },
            onAddTeamNameChanged = { viewModel.onEvent(MainScreenEvent.OnAddTeamNameChanged(it)) },
            onClickAddTeam = { viewModel.onEvent(MainScreenEvent.ClickedAddTeam) },
            onClickResetPoints = { viewModel.onEvent(MainScreenEvent.ResetPoints) }
        )
    }
}

@Preview
@Composable
private fun AppPreview() {
    VoleibrTheme {
        App()
    }
}