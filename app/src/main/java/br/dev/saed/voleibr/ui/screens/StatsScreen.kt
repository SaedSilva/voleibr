package br.dev.saed.voleibr.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import br.dev.saed.voleibr.R
import br.dev.saed.voleibr.model.repositories.db.winner.WinnerSearch
import br.dev.saed.voleibr.ui.state.StatsScreenState
import br.dev.saed.voleibr.ui.theme.VoleibrTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun StatsScreen(
    modifier: Modifier = Modifier,
    uiState: StatsScreenState,
    onNavigateToHome: () -> Unit = {},
    deleteTeam: (String) -> Unit = {},
    deleteAllTeams: () -> Unit = {}
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
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                if (uiState.winner.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(id = R.string.txt_team),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = stringResource(id = R.string.txt_wins),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                } else {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.txt_no_teams),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                }
                LazyColumn {
                    items(uiState.winner.size) { index ->
                        var showDialog by remember { mutableStateOf(false) }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .combinedClickable(
                                    onLongClick = {
                                        showDialog = true
                                    },
                                    onClick = {

                                    }
                                )
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = uiState.winner[index].name!!)
                            Text(text = uiState.winner[index].wins.toString())
                        }
                        if (showDialog) {
                            DeleteTeamDialog(
                                team = uiState.winner[index].name!!,
                                onDismissDialog = {
                                    if (it) {
                                        deleteTeam(uiState.winner[index].name!!)
                                    }
                                    showDialog = false
                                }
                            )
                        }
                    }
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var showDialog by remember { mutableStateOf(false) }
                TextButton(
                    onClick = {
                        showDialog = true
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.txt_delete_all_teams),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                if (showDialog) {
                    DeleteTeamDialog(
                        team = stringResource(id = R.string.txt_all_teams),
                        onDismissDialog = {
                            if (it) {
                                deleteAllTeams()
                            }
                            showDialog = false
                        }
                    )
                }
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
                    WinnerSearch(
                        id = null,
                        name = "Brasil",
                        wins = 10
                    ),
                    WinnerSearch(
                        id = null,
                        name = "EUA",
                        wins = 5
                    )
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
        Card {
            Column(
                modifier = modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.txt_delete_team)
                )
                Text(
                    text = team
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = { onDismissDialog(true) }) {
                        Text(
                            text = stringResource(id = R.string.txt_ok),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    TextButton(onClick = { onDismissDialog(false) }) {
                        Text(
                            text = stringResource(id = R.string.txt_cancel),
                            style = MaterialTheme.typography.bodyMedium
                        )
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