package br.dev.saed.voleibr.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import br.dev.saed.voleibr.R
import br.dev.saed.voleibr.ui.theme.VoleibrTheme

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { viewModel.onEvent(MainScreenEvent.DecreaseMaxPoints) }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_remove_24),
                    contentDescription = null
                )
            }

            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Max Pontos:",
                    style = MaterialTheme.typography.titleSmall,
                    fontSize = 32.sp
                )
                Text(
                    text = uiState.maxPoints.toString(),
                    fontSize = 28.sp
                )
            }

            Button(onClick = { viewModel.onEvent(MainScreenEvent.IncreaseMaxPoints) }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }



        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = uiState.team1.nome, fontSize = 24.sp)
                Text(text = uiState.team1.pontos.toString(), fontSize = 32.sp)
                Button(onClick = { viewModel.onEvent(MainScreenEvent.Team1Scored) }) {
                    Text(text = "Pontuar")
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = uiState.team2.nome, fontSize = 24.sp)
                Text(text = uiState.team2.pontos.toString(), fontSize = 32.sp)
                Button(onClick = { viewModel.onEvent(MainScreenEvent.Team2Scored) }) {
                    Text(text = "Pontuar")
                }
            }
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Switch(
                checked = uiState.vaiA2,
                onCheckedChange = {
                    viewModel.onEvent(MainScreenEvent.SwitchClicked)
                }
            )
            Text(text = "Vai a 2", modifier = Modifier.padding(start = 8.dp))
        }

        Text(text = "Times na fila:")
        HorizontalDivider()
        for (team in uiState.teamsInQueue) {
            Text(
                text = team.nome,
                fontSize = 24.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            HorizontalDivider()
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = uiState.teamToAdd.nome,
                onValueChange = {
                    viewModel.onEvent(MainScreenEvent.OnAddTeamNameChanged(it))
                },
                singleLine = true,
                label = { Text(text = "Nome do time") }
            )
            Button(
                onClick = {
                    viewModel.onEvent(MainScreenEvent.ClickedAddTeam)
                    viewModel.onEvent(MainScreenEvent.OnAddTeamNameChanged(""))
                }
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = null)
            }
        }

        Button(
            onClick = {
                viewModel.onEvent(MainScreenEvent.ResetPoints)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(text = "Reiniciar")
        }
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    VoleibrTheme {
        MainScreen(viewModel = MainViewModel())
    }
}