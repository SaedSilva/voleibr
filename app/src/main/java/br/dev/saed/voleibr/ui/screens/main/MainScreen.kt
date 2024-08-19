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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
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
import br.dev.saed.voleibr.R
import br.dev.saed.voleibr.model.entities.Team
import br.dev.saed.voleibr.ui.theme.VoleibrTheme

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    uiState: MainScreenState,
    onClickDecreaseMaxPoints: () -> Unit = {},
    onClickIncreaseMaxPoints: () -> Unit = {},
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
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.txt_acss_btn_increase_max_points)
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Card {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            text = uiState.team1.nome,
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.size(width = 150.dp, height = 32.dp)
                        )
                        Text(
                            text = uiState.team1.pontos.toString(),
                            fontSize = 32.sp
                        )
                        Row(modifier = Modifier.padding(top = 4.dp)) {
                            Button(
                                onClick = { onClickTeam1ScoreDecrease() },
                                shape = MaterialTheme.shapes.small,
                                modifier = Modifier
                                    .size(width = 40.dp, height = 32.dp)
                                    .padding(end = 4.dp),
                                contentPadding = PaddingValues(2.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.baseline_remove_24),
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp),
                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                                )
                            }

                            Button(
                                onClick = { onClickTeam1Scored() },
                                shape = MaterialTheme.shapes.small,
                                modifier = Modifier
                                    .size(width = 40.dp, height = 32.dp)
                                    .padding(start = 4.dp),
                                contentPadding = PaddingValues(2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                }

                Column() {
                    Button(
                        onClick = { onClickResetPoints() },
                        modifier = Modifier
                            .size(width = 40.dp, height = 40.dp)
                            .padding(bottom = 4.dp),
                        contentPadding = PaddingValues(2.dp),
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
                        modifier = Modifier
                            .size(width = 40.dp, height = 40.dp)
                            .padding(top = 4.dp),
                        contentPadding = PaddingValues(2.dp),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Column{
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(id = R.string.btn_reset_points),
                                modifier = Modifier.size(16.dp)
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = stringResource(id = R.string.btn_reset_points),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Card {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            text = uiState.team2.nome,
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.size(width = 150.dp, height = 32.dp)
                        )
                        Text(
                            text = uiState.team2.pontos.toString(),
                            fontSize = 32.sp
                        )
                        Row(modifier = Modifier.padding(top = 4.dp)) {
                            Button(
                                onClick = { onClickTeam2ScoreDecrease() },
                                shape = MaterialTheme.shapes.small,
                                modifier = Modifier
                                    .size(width = 40.dp, height = 32.dp)
                                    .padding(end = 4.dp),
                                contentPadding = PaddingValues(2.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.baseline_remove_24),
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp),
                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
                                )
                            }

                            Button(
                                onClick = { onClickTeam2Scored() },
                                shape = MaterialTheme.shapes.small,
                                modifier = Modifier
                                    .size(width = 40.dp, height = 32.dp)
                                    .padding(start = 4.dp),
                                contentPadding = PaddingValues(2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
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
                    verticalAlignment = Alignment.CenterVertically
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
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                TextField(
                    value = uiState.teamToAdd.nome,
                    onValueChange = { onAddTeamNameChanged(it) },
                    singleLine = true,
                    label = { Text(text = stringResource(id = R.string.edit_team_name)) },
                    modifier = Modifier.fillMaxWidth(),
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
                    modifier = Modifier.fillMaxWidth()
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
                Button(onClick = { onClickClearQueue() }) {
                    Text(text = stringResource(id = R.string.btn_clear_queue))
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(id = R.string.btn_clear_queue)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                HorizontalDivider()
                uiState.teamsInQueue.forEachIndexed { index, team ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${index + 1} - ${team.nome}",
                            textAlign = TextAlign.Center,
                            fontSize = 28.sp
                        )
                        Button(onClick = { onClickDeleteTeam(team) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(id = R.string.txt_acss_btn_delete_team)
                            )
                        }
                    }
                    HorizontalDivider()
                }
            }
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