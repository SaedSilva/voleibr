package br.dev.saed.voleibr.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
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
import br.dev.saed.voleibr.ui.state.MainScreenState
import br.dev.saed.voleibr.ui.theme.VoleibrTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigScreen(
    modifier: Modifier = Modifier,
    uiState: MainScreenState,
    onClickSwitchVaiA2: () -> Unit = {},
    onClickSwitchVibrar: () -> Unit = {},
    onNavigateToHome: () -> Unit = {}
) {
    var sobreDialog by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.txt_configuracoes),
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
        if (sobreDialog) {
            SobreDialog(
                onDismissDialog = { sobreDialog = false }
            )
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
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
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Switch(
                        checked = uiState.vibrar,
                        onCheckedChange = { onClickSwitchVibrar() }
                    )
                    Text(
                        text = stringResource(id = R.string.txt_vibrar),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextButton(
                    shape = MaterialTheme.shapes.small,
                    onClick = {
                        sobreDialog = true
                    }
                ) {
                    Text(text = "Sobre")
                }
            }
        }
    }
}

@Preview
@Composable
private fun ConfigScreenPreview() {
    VoleibrTheme {
        ConfigScreen(
            uiState = MainScreenState()
        )
    }
}

@Composable
fun SobreDialog(
    modifier: Modifier = Modifier,
    onDismissDialog: () -> Unit = {}
) {
    Dialog(onDismissRequest = onDismissDialog) {
        Card {
            Box(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.txt_sobre),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
}

@Preview
@Composable
private fun SobreDialogPreview() {
    VoleibrTheme {
        SobreDialog()
    }
}