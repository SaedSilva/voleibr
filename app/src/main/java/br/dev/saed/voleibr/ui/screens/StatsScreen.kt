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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
    load: () -> Unit = {},
    deleteTeam: (String) -> Unit = {}
) {
    load()
    var deleteTeamDialog by remember {
        mutableStateOf(false)
    }

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
                .fillMaxSize()
        ) {
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
            LazyColumn {
                items(uiState.winner.size) { index ->
                    if (deleteTeamDialog) {
                        DeleteTeamDialog(team = uiState.winner[index].name!!,
                            onOkButton = {
                                deleteTeam(uiState.winner[index].name!!)
                                deleteTeamDialog = false
                            },
                            onCancelButton = {
                                deleteTeamDialog = false
                            })
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .combinedClickable(
                                onLongClick = {
                                    deleteTeamDialog = true
                                },
                                onClick = {}
                            )
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = uiState.winner[index].name!!)
                        Text(text = uiState.winner[index].wins.toString())
                    }
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
    onOkButton: (Boolean) -> Unit = {},
    onCancelButton: (Boolean) -> Unit = {}
) {
    Dialog(onDismissRequest = { onCancelButton(false) }) {
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
                    TextButton(onClick = { onOkButton(true) }) {
                        Text(
                            text = stringResource(id = R.string.txt_ok),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    TextButton(onClick = { onCancelButton(false) }) {
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