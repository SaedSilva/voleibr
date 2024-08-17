package br.dev.saed.voleibr.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.dev.saed.voleibr.R
import br.dev.saed.voleibr.ui.theme.VoleibrTheme

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    uiState: MainScreenState,
    onClickDecreaseMaxPoints: () -> Unit = {},
    onClickIncreaseMaxPoints: () -> Unit = {},
    onClickTeam1Scored: () -> Unit = {},
    onClickTeam2Scored: () -> Unit = {},
    onClickSwitchVaiA2: () -> Unit = {},
    onClickSwitchVibrar: () -> Unit = {},
    onAddTeamNameChanged: (String) -> Unit = {},
    onClickAddTeam: () -> Unit = {},
    onClickResetPoints: () -> Unit = {}
) {

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
            Button(onClick = { onClickDecreaseMaxPoints() }) {
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
                    text = stringResource(id = R.string.txt_max_points),
                    style = MaterialTheme.typography.titleSmall,
                    fontSize = 32.sp
                )
                Text(
                    text = uiState.maxPoints.toString(),
                    fontSize = 28.sp
                )
            }

            Button(onClick = { onClickIncreaseMaxPoints() }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }



        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Card {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = uiState.team1.nome, fontSize = 32.sp)
                    Text(text = uiState.team1.pontos.toString(), fontSize = 32.sp)
                    Button(onClick = { onClickTeam1Scored() }) {
                        Image(
                            painter = painterResource(id = R.drawable.bola_volei),
                            contentDescription = null
                        )
                        Text(
                            text = stringResource(id = R.string.txt_score),
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }

            Card {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = uiState.team2.nome, fontSize = 32.sp)
                    Text(text = uiState.team2.pontos.toString(), fontSize = 32.sp)
                    Button(onClick = { onClickTeam2Scored() }) {
                        Image(
                            painter = painterResource(id = R.drawable.bola_volei),
                            contentDescription = null
                        )
                        Text(
                            text = stringResource(id = R.string.txt_score),
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Switch(
                    checked = uiState.vaiA2,
                    onCheckedChange = {
                        onClickSwitchVaiA2()
                    }
                )
                Text(
                    text = stringResource(id = R.string.txt_vai_a_2),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.txt_vibrar),
                    modifier = Modifier.padding(end = 8.dp)
                )
                Switch(
                    checked = uiState.vibrar,
                    onCheckedChange = {
                        onClickSwitchVibrar()
                    }
                )
            }
        }

        Text(text = stringResource(id = R.string.txt_teams_in_queue), fontSize = 24.sp)
        HorizontalDivider()
        uiState.teamsInQueue.forEachIndexed { index, team ->
            Text(
                text = "${index + 1} - ${team.nome}",
                textAlign = TextAlign.Center,
                fontSize = 28.sp,
                modifier = Modifier.fillMaxWidth()
            )
            HorizontalDivider()
        }



        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            TextField(
                value = uiState.teamToAdd.nome,
                onValueChange = {
                    onAddTeamNameChanged(it)
                },
                singleLine = true,
                label = { Text(text = stringResource(id = R.string.edit_team_name)) },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    onClickAddTeam()
                    onAddTeamNameChanged("")
                },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.txt_add_queue),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Button(
            onClick = {
                onClickResetPoints()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(text = stringResource(id = R.string.btn_reset_points))
        }
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    VoleibrTheme {
        MainScreen(
            uiState = MainScreenState()
        )
    }
}