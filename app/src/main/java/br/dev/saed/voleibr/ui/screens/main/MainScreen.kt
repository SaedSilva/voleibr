package br.dev.saed.voleibr.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import br.dev.saed.voleibr.R
import br.dev.saed.voleibr.model.entities.Team
import br.dev.saed.voleibr.ui.theme.OrbitronFamily
import br.dev.saed.voleibr.ui.theme.VoleibrTheme
import br.dev.saed.voleibr.ui.theme.onPrimaryContainerLight
import br.dev.saed.voleibr.ui.theme.onTertiaryContainerLight
import br.dev.saed.voleibr.ui.theme.primaryContainerLight
import br.dev.saed.voleibr.ui.theme.tertiaryContainerLight

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    uiState: MainScreenState,
    onClickDecreaseMaxPoints: () -> Unit = {},
    onClickIncreaseMaxPoints: () -> Unit = {},
    onClickRemoveTeam1: () -> Unit = {},
    onClickRemoveTeam2: () -> Unit = {},
    onClickChangeTeams: () -> Unit = {},
    onClickTeam1Scored: () -> Unit = {},
    onClickTeam1ScoreDecrease: () -> Unit = {},
    onClickTeam2Scored: () -> Unit = {},
    onClickTeam2ScoreDecrease: () -> Unit = {},
    onClickSwitchVaiA2: () -> Unit = {},
    onClickSwitchVibrar: () -> Unit = {},
    onClickClearQueue: () -> Unit = {},
    onAddTeamNameChanged: (String) -> Unit = {},
    onClickAddTeam: () -> Unit = {},
    onClickDeleteTeam: (Team) -> Unit = {},
    onClickResetPoints: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = { /*TODO implements fab*/ },
//                containerColor = MaterialTheme.colorScheme.primary
//            ) {
//                Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
//            }
//        }
    ) { innerPadding ->
        if (uiState.winner != null) {
            WinnerDialog(winner = uiState.winner)
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
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
                        contentDescription = stringResource(id = R.string.txt_acss_btn_decrease_max_points)
                    )
                }

                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.txt_max_points),
                        style = MaterialTheme.typography.titleSmall,
                        fontSize = 32.sp
                    )
                    Text(
                        text = uiState.maxPoints.toString(),
                        fontSize = 28.sp,
                        fontFamily = OrbitronFamily
                    )
                }

                Button(onClick = { onClickIncreaseMaxPoints() }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.txt_acss_btn_increase_max_points)
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.size(width = 150.dp, height = 260.dp)) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = uiState.teamsInQueue.isNotEmpty(),
                        shape = MaterialTheme.shapes.small,
                        onClick = { onClickRemoveTeam1() }
                    ) {
                        Text(text = stringResource(id = R.string.btn_remove))
                        Icon(
                            imageVector = Icons.Default.Delete, contentDescription = stringResource(
                                id = R.string.btn_remove
                            )
                        )
                    }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardColors(
                            containerColor = primaryContainerLight,
                            contentColor = onPrimaryContainerLight,
                            disabledContentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = uiState.team1.pontos.toString(),
                                fontSize = 64.sp,
                                fontFamily = OrbitronFamily
                            )
                            Text(
                                text = uiState.team1.nome,
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.size(width = 125.dp, height = 64.dp)
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { onClickTeam1ScoreDecrease() },
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier
                                .size(width = 50.dp, height = 40.dp)
                                .padding(end = 4.dp),
                            contentPadding = PaddingValues(2.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.baseline_remove_24),
                                contentDescription = stringResource(id = R.string.txt_acss_btn_decrease_team_points),
                                modifier = Modifier.size(32.dp),
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                            )
                        }
                        Button(
                            onClick = { onClickTeam1Scored() },
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier
                                .size(width = 50.dp, height = 40.dp)
                                .padding(start = 4.dp),
                            contentPadding = PaddingValues(2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(id = R.string.txt_acss_btn_score_team_1),
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier.size(width = 40.dp, height = 225.dp),
                    verticalArrangement = Arrangement.Center,
                ) {
                    Button(
                        onClick = { onClickResetPoints() },
                        contentPadding = PaddingValues(0.dp),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = stringResource(id = R.string.btn_reset_points),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Button(
                        onClick = { onClickChangeTeams() },
                        contentPadding = PaddingValues(0.dp),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Column {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(id = R.string.btn_reset_points),
                                modifier = Modifier.size(15.dp)
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = stringResource(id = R.string.btn_reset_points),
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                }

                Column(modifier = Modifier.size(width = 150.dp, height = 260.dp)) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = uiState.teamsInQueue.isNotEmpty(),
                        shape = MaterialTheme.shapes.small,
                        onClick = { onClickRemoveTeam2() }
                    ) {
                        Text(text = stringResource(id = R.string.btn_remove))
                        Icon(
                            imageVector = Icons.Default.Delete, contentDescription = stringResource(
                                id = R.string.btn_remove
                            )
                        )
                    }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardColors(
                            containerColor = tertiaryContainerLight,
                            contentColor = onTertiaryContainerLight,
                            disabledContentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = uiState.team2.pontos.toString(),
                                fontSize = 64.sp,
                                fontFamily = OrbitronFamily
                            )
                            Text(
                                text = uiState.team2.nome,
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.size(width = 125.dp, height = 64.dp)
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { onClickTeam2ScoreDecrease() },
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier
                                .size(width = 50.dp, height = 40.dp)
                                .padding(end = 4.dp),
                            contentPadding = PaddingValues(2.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.baseline_remove_24),
                                contentDescription = stringResource(id = R.string.txt_acss_btn_decrease_team_points),
                                modifier = Modifier.size(32.dp),
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                            )
                        }
                        Button(
                            onClick = { onClickTeam2Scored() },
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier
                                .size(width = 50.dp, height = 40.dp)
                                .padding(start = 4.dp),
                            contentPadding = PaddingValues(2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(id = R.string.txt_acss_btn_score_team_2),
                                modifier = Modifier.size(32.dp)
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
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Switch(
                        checked = uiState.vaiA2,
                        onCheckedChange = { onClickSwitchVaiA2() }
                    )
                    Text(
                        text = stringResource(id = R.string.txt_vai_a_2),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.txt_vibrar),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Switch(
                        checked = uiState.vibrar,
                        onCheckedChange = { onClickSwitchVibrar() }
                    )
                }
            }



            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                TextField(
                    value = uiState.teamToAdd.nome,
                    onValueChange = { onAddTeamNameChanged(it) },
                    singleLine = true,
                    label = { Text(text = stringResource(id = R.string.edit_team_name)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                    keyboardActions = KeyboardActions {
                        onClickAddTeam()
                        onAddTeamNameChanged("")
                    }
                )

                Button(
                    onClick = {
                        onClickAddTeam()
                        onAddTeamNameChanged("")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.txt_add_queue),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.txt_teams_in_queue),
                    fontSize = 24.sp
                )
                Button(
                    onClick = { onClickClearQueue() },
                    enabled = uiState.teamsInQueue.isNotEmpty()
                ) {
                    Text(text = stringResource(id = R.string.btn_clear_queue))
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(id = R.string.btn_clear_queue)
                    )
                }
            }

            LazyColumn {
                items(uiState.teamsInQueue.size) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${it + 1} - ${uiState.teamsInQueue[it].nome}",
                            textAlign = TextAlign.Center,
                            fontSize = 28.sp
                        )
                        Button(onClick = { onClickDeleteTeam(uiState.teamsInQueue[it]) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(id = R.string.txt_acss_btn_delete_team)
                            )
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun WinnerDialog(
    modifier: Modifier = Modifier,
    winner: Team,
    onDismissRequest: () -> Unit = {}
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(modifier = modifier.size(200.dp)) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Ganhador: ",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = winner.nome,
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
private fun WinnerDialogPreview() {
    VoleibrTheme {
        WinnerDialog(winner = Team(0, "Time 1", 0))
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