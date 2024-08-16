package br.dev.saed.voleibr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.dev.saed.voleibr.ui.screens.MainScreen
import br.dev.saed.voleibr.ui.screens.MainScreenEvent
import br.dev.saed.voleibr.ui.screens.MainViewModel
import br.dev.saed.voleibr.ui.theme.VoleibrTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VoleibrTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    App(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
private fun App(modifier: Modifier = Modifier) {
    val viewModel = MainViewModel()
    
    MainScreen(
        viewModel = viewModel,
        modifier = modifier
    )
}

@Preview
@Composable
private fun AppPreview() {
    VoleibrTheme {
        App()
    }
}