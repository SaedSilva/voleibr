package br.dev.saed.voleibr.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.sharp.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import br.dev.saed.voleibr.R
import br.dev.saed.voleibr.model.entities.Team
import br.dev.saed.voleibr.ui.state.MainScreenEvent
import br.dev.saed.voleibr.ui.state.MainScreenState
import br.dev.saed.voleibr.ui.theme.OrbitronFamily
import br.dev.saed.voleibr.ui.theme.VoleibrTheme
import br.dev.saed.voleibr.ui.theme.onPrimaryContainerLight
import br.dev.saed.voleibr.ui.theme.onTertiaryContainerLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    uiState: MainScreenState,
    onEvent: (MainScreenEvent) -> Unit = {},
    onNavigateToStats: () -> Unit = {}
) {
    var showConfigSheet by remember { mutableStateOf(false) }
    var showAddTeamDialog by remember { mutableStateOf(false) }

    if (uiState.winner != null) {
        WinnerDialog(winner = uiState.winner)
    }

    if (showConfigSheet) {
        ConfigBottomSheet(
            uiState = uiState,
            onClickSwitchVaiA2 = { onEvent(MainScreenEvent.SwitchVaiA2) },
            onClickSwitchVibrar = { onEvent(MainScreenEvent.SwitchVibrar) },
            onClickSwitchKeepScreenOn = { onEvent(MainScreenEvent.SwitchKeepScreenOn) },
            onDismiss = { showConfigSheet = false }
        )
    }

    if (showAddTeamDialog) {
        AddTeamDialog(
            uiState = uiState,
            onAddTeamNameChanged = { onEvent(MainScreenEvent.OnAddTeamNameChanged(it)) },
            onClickAddTeam = {
                onEvent(MainScreenEvent.ClickedAddTeam)
                onEvent(MainScreenEvent.OnAddTeamNameChanged(""))
            }
        ) {
            showAddTeamDialog = false
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            TopControlBar(
                uiState = uiState,
                isVaiA2Triggered = uiState.isVaiA2Triggered,
                onClickDecreaseMaxPoints = { onEvent(MainScreenEvent.DecreaseMaxPoints) },
                onClickIncreaseMaxPoints = { onEvent(MainScreenEvent.IncreaseMaxPoints) },
                onClickResetPoints = { onEvent(MainScreenEvent.ResetPoints) },
                onClickChangeTeams = { onEvent(MainScreenEvent.ChangeTeams) },
                onOpenConfig = { showConfigSheet = true },
                onNavigateToStats = onNavigateToStats
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                TeamScoreCard(
                    modifier = Modifier.weight(1f),
                    team = uiState.team1,
                    teamColor = Color(uiState.team1Color.color),
                    contentColor = onPrimaryContainerLight,
                    showRemove = uiState.teamsInQueue.isNotEmpty(),
                    isMatchPoint = uiState.team1MatchPoint,
                    onRemoveTeam = { onEvent(MainScreenEvent.RemoveTeam1) },
                    onChangeColor = { onEvent(MainScreenEvent.ChangeTeam1Color) },
                    onScoreDecrease = { onEvent(MainScreenEvent.Team1ScoreDecreased) },
                    onScored = { onEvent(MainScreenEvent.Team1Scored) }
                )

                Spacer(modifier = Modifier.width(12.dp))

                TeamScoreCard(
                    modifier = Modifier.weight(1f),
                    team = uiState.team2,
                    teamColor = Color(uiState.team2Color.color),
                    contentColor = onTertiaryContainerLight,
                    showRemove = uiState.teamsInQueue.isNotEmpty(),
                    isMatchPoint = uiState.team2MatchPoint,
                    onRemoveTeam = { onEvent(MainScreenEvent.RemoveTeam2) },
                    onChangeColor = { onEvent(MainScreenEvent.ChangeTeam2Color) },
                    onScoreDecrease = { onEvent(MainScreenEvent.Team2ScoreDecreased) },
                    onScored = { onEvent(MainScreenEvent.Team2Scored) }
                )
            }

            QueueBar(
                uiState = uiState,
                onClearQueue = { onEvent(MainScreenEvent.ClearQueue) },
                onDeleteTeam = { onEvent(MainScreenEvent.ClickedDeleteTeam(it)) },
                onAddTeamClick = { showAddTeamDialog = true }
            )
        }
    }
}

@Composable
private fun TopControlBar(
    uiState: MainScreenState,
    isVaiA2Triggered: Boolean,
    onClickDecreaseMaxPoints: () -> Unit,
    onClickIncreaseMaxPoints: () -> Unit,
    onClickResetPoints: () -> Unit,
    onClickChangeTeams: () -> Unit,
    onOpenConfig: () -> Unit,
    onNavigateToStats: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()
    val vaiA2Scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(700),
            repeatMode = RepeatMode.Reverse
        )
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onOpenConfig) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(id = R.string.txt_configuracoes),
                    modifier = Modifier.size(28.dp)
                )
            }
            IconButton(onClick = onNavigateToStats) {
                Icon(
                    imageVector = Icons.AutoMirrored.Sharp.List,
                    contentDescription = stringResource(id = R.string.txt_stats),
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = onClickDecreaseMaxPoints,
                shape = MaterialTheme.shapes.small,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_remove_24),
                    contentDescription = stringResource(id = R.string.txt_acss_btn_decrease_max_points),
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(
                modifier = Modifier.padding(horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isVaiA2Triggered) {
                    Text(
                        text = "VAI A 2",
                        fontSize = 18.sp,
                        fontFamily = OrbitronFamily,
                        color = Color(0xFFFFB300),
                        modifier = Modifier.scale(vaiA2Scale)
                    )
                    Text(
                        text = stringResource(id = R.string.txt_max_points) + " " + uiState.maxPoints.toString(),
                        style = MaterialTheme.typography.labelSmall
                    )
                } else {
                    Text(
                        text = uiState.maxPoints.toString(),
                        fontSize = 24.sp,
                        fontFamily = OrbitronFamily
                    )
                    Text(
                        text = stringResource(id = R.string.txt_max_points),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Button(
                onClick = onClickIncreaseMaxPoints,
                shape = MaterialTheme.shapes.small,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.txt_acss_btn_increase_max_points),
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Row {
            IconButton(onClick = onClickResetPoints) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = stringResource(id = R.string.btn_reset_points),
                    modifier = Modifier.size(28.dp)
                )
            }
            IconButton(onClick = onClickChangeTeams) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = stringResource(id = R.string.btn_reset_points),
                        modifier = Modifier.size(14.dp)
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.btn_reset_points),
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TeamScoreCard(
    modifier: Modifier = Modifier,
    team: Team,
    teamColor: Color,
    contentColor: Color,
    showRemove: Boolean,
    isMatchPoint: Boolean,
    onRemoveTeam: () -> Unit,
    onChangeColor: () -> Unit,
    onScoreDecrease: () -> Unit,
    onScored: () -> Unit
) {
    val scoreScale = remember { Animatable(1f) }

    LaunchedEffect(team.pontos) {
        scoreScale.animateTo(1.3f, tween(80))
        scoreScale.animateTo(1f, tween(150))
    }

    Card(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        colors = CardColors(
            containerColor = teamColor,
            contentColor = contentColor,
            disabledContentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.primary
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Box(
            modifier = Modifier.fillMaxSize().clickable { onScored() },
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = team.pontos.toString(),
                    fontSize = 72.sp,
                    fontFamily = OrbitronFamily,
                    color = if (isMatchPoint) Color(0xFFFFB300) else contentColor,
                    modifier = Modifier.scale(scoreScale.value)
                )
                if (isMatchPoint) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Match point",
                            modifier = Modifier.size(20.dp),
                            tint = Color(0xFFFFB300)
                        )
                        Text(
                            text = team.nome,
                            fontSize = 26.sp,
                            textAlign = TextAlign.Center,
                            minLines = 2,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(start = 4.dp, end = 8.dp)
                        )
                    }
                } else {
                    Text(
                        text = team.nome,
                        fontSize = 26.sp,
                        textAlign = TextAlign.Center,
                        minLines = 2,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }

            if (showRemove) {
                IconButton(
                    onClick = onRemoveTeam,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.btn_remove),
                        modifier = Modifier.size(16.dp),
                        tint = contentColor
                    )
                }
            }

            IconButton(
                onClick = onScoreDecrease,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .size(44.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_remove_24),
                    contentDescription = stringResource(id = R.string.txt_acss_btn_decrease_team_points),
                    modifier = Modifier.size(24.dp),
                    colorFilter = ColorFilter.tint(contentColor)
                )
            }

            IconButton(
                onClick = onChangeColor,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Palette,
                    contentDescription = "Mudar cor",
                    modifier = Modifier.size(18.dp),
                    tint = contentColor.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
private fun QueueBar(
    uiState: MainScreenState,
    onClearQueue: () -> Unit,
    onDeleteTeam: (Team) -> Unit,
    onAddTeamClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${stringResource(id = R.string.txt_teams_in_queue)} ${uiState.teamsInQueue.size}",
                fontSize = 16.sp
            )
            Row {
                IconButton(onClick = onAddTeamClick) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = stringResource(id = R.string.txt_add_queue),
                        modifier = Modifier.size(28.dp)
                    )
                }
                Button(
                    onClick = onClearQueue,
                    enabled = uiState.teamsInQueue.isNotEmpty(),
                    shape = MaterialTheme.shapes.small,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.btn_clear_queue),
                        fontSize = 14.sp
                    )
                }
            }
        }

        if (uiState.teamsInQueue.isNotEmpty()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 48.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(uiState.teamsInQueue.size) { index ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Text(
                            text = "${index + 1} - ${uiState.teamsInQueue[index].nome}",
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.widthIn(max = 180.dp)
                        )
                        IconButton(
                            onClick = { onDeleteTeam(uiState.teamsInQueue[index]) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(id = R.string.txt_acss_btn_delete_team),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConfigBottomSheet(
    uiState: MainScreenState,
    onClickSwitchVaiA2: () -> Unit,
    onClickSwitchVibrar: () -> Unit,
    onClickSwitchKeepScreenOn: () -> Unit,
    onDismiss: () -> Unit
) {
    var showSobreDialog by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (showSobreDialog) {
        SobreDialog(onDismissDialog = { showSobreDialog = false })
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.txt_configuracoes),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClickSwitchVaiA2() }
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(id = R.string.txt_vai_a_2))
                Switch(checked = uiState.vaiA2, onCheckedChange = null)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClickSwitchVibrar() }
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(id = R.string.txt_vibrar))
                Switch(checked = uiState.vibrar, onCheckedChange = null)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClickSwitchKeepScreenOn() }
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Manter tela ativa")
                Switch(checked = uiState.keepScreenOn, onCheckedChange = null)
            }

            TextButton(
                onClick = { showSobreDialog = true },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small
            ) {
                Text(text = "Sobre")
            }

            Spacer(modifier = Modifier.padding(bottom = 24.dp))
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
        Card(
            modifier = modifier.size(240.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🏆",
                    fontSize = 48.sp
                )
                Text(
                    text = stringResource(id = R.string.txt_win),
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = winner.nome,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = OrbitronFamily,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun AddTeamDialog(
    modifier: Modifier = Modifier,
    uiState: MainScreenState,
    onAddTeamNameChanged: (String) -> Unit = {},
    onClickAddTeam: () -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(shape = MaterialTheme.shapes.medium) {
            Column(modifier = Modifier.padding(12.dp)) {
                TextField(
                    value = uiState.teamToAdd.nome,
                    onValueChange = { onAddTeamNameChanged(it) },
                    singleLine = true,
                    label = { Text(text = stringResource(id = R.string.edit_team_name)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                    keyboardActions = KeyboardActions {
                        onClickAddTeam()
                        onDismissRequest()
                    },
                    shape = MaterialTheme.shapes.small
                )

                Spacer(modifier = Modifier.padding(4.dp))

                Button(
                    onClick = {
                        onClickAddTeam()
                        onDismissRequest()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = stringResource(id = R.string.txt_add_queue),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AddTeamDialogPreview() {
    VoleibrTheme {
        AddTeamDialog(uiState = MainScreenState())
    }
}

@Preview
@Composable
private fun WinnerDialogPreview() {
    VoleibrTheme {
        WinnerDialog(winner = Team(0, "Time 1", 0))
    }
}

@Preview(device = "spec:parent=pixel_5,orientation=landscape")
@Composable
private fun MainScreenPreview() {
    VoleibrTheme {
        MainScreen(
            uiState = MainScreenState(),
            modifier = Modifier.fillMaxSize()
        )
    }
}
