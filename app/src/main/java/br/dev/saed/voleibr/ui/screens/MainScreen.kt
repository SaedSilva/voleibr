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
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { /*TODO*/ }) {
                Icon(painter = painterResource(id = R.drawable.baseline_remove_24), contentDescription = null)
            }

            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Max Pontos:")
                Text(
                    text = uiState.maxPoints.toString()
                )
            }

            Button(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }



        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column {
                Text(text = uiState.team1.nome)
                Text(text = uiState.team1.pontos.toString())
                Button(onClick = { viewModel.onEvent(MainScreenEvent.Team1Scored) }) {
                    Text(text = "Pontuar")
                }
            }

            Column {
                Text(text = uiState.team2.nome)
                Text(text = uiState.team2.pontos.toString())
                Button(onClick = { viewModel.onEvent(MainScreenEvent.Team2Scored) }) {
                    Text(text = "Pontuar")
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Vai a 2")
            Switch(
                checked = uiState.vaiA2,
                onCheckedChange = {
                    viewModel.onEvent(MainScreenEvent.SwitchClicked)
                }
            )

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