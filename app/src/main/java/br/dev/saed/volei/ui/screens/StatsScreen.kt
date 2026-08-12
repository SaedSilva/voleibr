package br.dev.saed.volei.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import br.dev.saed.volei.R
import br.dev.saed.volei.model.repositories.db.winner.WinnerSearch
import br.dev.saed.volei.ui.state.StatsScreenEvent
import br.dev.saed.volei.ui.state.StatsScreenState
import br.dev.saed.volei.ui.theme.VoleibrTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun StatsScreen(
    modifier: Modifier = Modifier,
    uiState: StatsScreenState,
    onNavigateToHome: () -> Unit = {},
    onEvent: (StatsScreenEvent) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.txt_stats),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigateToHome() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    if (uiState.winner.isNotEmpty()) {
                        var showDialog by remember { mutableStateOf(false) }
                        TextButton(
                            onClick = { showDialog = true },
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = stringResource(id = R.string.txt_delete_all_teams),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                        if (showDialog) {
                            DeleteTeamDialog(
                                team = stringResource(id = R.string.txt_all_teams),
                                onDismissDialog = {
                                    if (it) onEvent(StatsScreenEvent.DeleteAll)
                                    showDialog = false
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            if (uiState.winner.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = R.string.txt_team),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Start
                    )
                    Text(
                        text = stringResource(id = R.string.txt_wins),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(80.dp)
                    )
                }

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(uiState.winner.size) { index ->
                        val winner = uiState.winner[index]
                        var showDialog by remember { mutableStateOf(false) }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .combinedClickable(
                                    onLongClick = { showDialog = true },
                                    onClick = {}
                                )
                                .background(
                                    color = if (index % 2 == 0)
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                                    else
                                        MaterialTheme.colorScheme.surface,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = winner.name ?: "",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = winner.wins.toString(),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(80.dp)
                            )
                        }

                        if (showDialog) {
                            DeleteTeamDialog(
                                team = winner.name ?: "",
                                onDismissDialog = {
                                    if (it && winner.name != null) {
                                        onEvent(StatsScreenEvent.DeleteTeam(winner.name))
                                    }
                                    showDialog = false
                                }
                            )
                        }
                    }
                }
            } else {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    text = stringResource(id = R.string.txt_no_teams),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
private fun StatsScreenPreview() {
    VoleibrTheme {
        StatsScreen(
            uiState = StatsScreenState(
                winner = listOf(
                    WinnerSearch(id = null, name = "Brasil", wins = 10),
                    WinnerSearch(id = null, name = "EUA", wins = 5),
                    WinnerSearch(id = null, name = "Itália", wins = 15),
                    WinnerSearch(id = null, name = "Japão", wins = 3)
                )
            )
        )
    }
}

@Composable
fun DeleteTeamDialog(
    modifier: Modifier = Modifier,
    team: String,
    onDismissDialog: (Boolean) -> Unit = {}
) {
    Dialog(onDismissRequest = { onDismissDialog(false) }) {
        Card(shape = MaterialTheme.shapes.medium) {
            Column(
                modifier = modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.txt_delete_team)
                )
                Text(text = team)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { onDismissDialog(false) }) {
                        Text(text = stringResource(id = R.string.txt_cancel))
                    }
                    TextButton(onClick = { onDismissDialog(true) }) {
                        Text(text = stringResource(id = R.string.txt_ok))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun DeleteTeamDialogPreview() {
    VoleibrTheme {
        DeleteTeamDialog(team = "Brasil")
    }
}
