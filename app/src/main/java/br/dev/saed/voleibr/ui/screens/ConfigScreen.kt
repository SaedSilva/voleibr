package br.dev.saed.voleibr.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import br.dev.saed.voleibr.R
import br.dev.saed.voleibr.ui.theme.VoleibrTheme

@Composable
fun SobreDialog(
    modifier: Modifier = Modifier,
    onDismissDialog: () -> Unit = {}
) {
    val git = stringResource(id = R.string.url_github)
    val insta = stringResource(id = R.string.url_instagram)
    val context = LocalContext.current
    val gintent = remember { Intent(Intent.ACTION_VIEW, Uri.parse(git)) }
    val iintent = remember { Intent(Intent.ACTION_VIEW, Uri.parse(insta)) }

    Dialog(onDismissRequest = onDismissDialog) {
        Card(modifier = modifier) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.txt_sobre),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Justify
                )
                Row {
                    TextButton(onClick = {
                        context.startActivity(gintent)
                        onDismissDialog()
                    }) {
                        Text(text = stringResource(id = R.string.txt_github))
                    }
                    TextButton(onClick = {
                        context.startActivity(iintent)
                        onDismissDialog()
                    }) {
                        Text(text = stringResource(id = R.string.txt_instagram))
                    }
                }
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
