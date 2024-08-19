package br.dev.saed.voleibr

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
import androidx.room.Room
import br.dev.saed.voleibr.model.repositories.datastore.DataStoreHelper
import br.dev.saed.voleibr.model.repositories.db.TeamDatabase
import br.dev.saed.voleibr.model.repositories.db.TeamRepository
import br.dev.saed.voleibr.ui.screens.main.MainScreen
import br.dev.saed.voleibr.ui.screens.main.MainScreenEvent
import br.dev.saed.voleibr.ui.screens.main.MainViewModel
import br.dev.saed.voleibr.ui.theme.VoleibrTheme
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

    val db = Room.databaseBuilder(
        context,
        TeamDatabase::class.java,
        "team_database"
    ).build()

    val teamDao = db.teamDAO()

    val viewModel: MainViewModel = MainViewModel(
        DataStoreHelper(context),
        TeamRepository(teamDao)
    )

    val uiState by viewModel.uiState.collectAsState()

    VoleibrTheme {
        MainScreen(
            modifier = modifier,
            uiState = uiState,
            onClickDecreaseMaxPoints = { viewModel.onEvent(MainScreenEvent.DecreaseMaxPoints) },
            onClickIncreaseMaxPoints = { viewModel.onEvent(MainScreenEvent.IncreaseMaxPoints) },
            onClickTeam1Scored = {
                viewModel.onEvent(MainScreenEvent.Team1Scored)
                if (viewModel.uiState.value.vibrar) {
                    context.vibrator(1010)
                }
            },
            onClickTeam1ScoreDecrease = {
                viewModel.onEvent(MainScreenEvent.Team1ScoreDecreased)
            },
            onClickTeam2Scored = {
                viewModel.onEvent(MainScreenEvent.Team2Scored)
                if (viewModel.uiState.value.vibrar) {
                    context.vibrator(1010)
                }
            },
            onClickTeam2ScoreDecrease = {
                viewModel.onEvent(MainScreenEvent.Team2ScoreDecreased)
            },
            onClickSwitchVaiA2 = { viewModel.onEvent(MainScreenEvent.SwitchVaiA2) },
            onClickSwitchVibrar = { viewModel.onEvent(MainScreenEvent.SwitchVibrar) },
            onClickClearQueue = { viewModel.onEvent(MainScreenEvent.ClearQueue) },
            onAddTeamNameChanged = { viewModel.onEvent(MainScreenEvent.OnAddTeamNameChanged(it)) },
            onClickAddTeam = { viewModel.onEvent(MainScreenEvent.ClickedAddTeam) },
            onClickDeleteTeam = { viewModel.onEvent(MainScreenEvent.ClickedDeleteTeam(it)) },
            onClickResetPoints = { viewModel.onEvent(MainScreenEvent.ResetPoints) }
        )
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