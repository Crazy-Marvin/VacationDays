package rocks.poopjournal.vacationdays.presentation.dialog

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import rocks.poopjournal.vacationdays.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryInputModal(
    onCountrySelected: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var state by remember { mutableStateOf<String?>(null)}

    SelectConfirmDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                enabled = !state.isNullOrEmpty(),
                onClick = {
                    onCountrySelected(state!!)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors().copy(
                    disabledContentColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                )
            ) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        },
        title = { Text(stringResource(R.string.load_holidays), color= MaterialTheme.colorScheme.onSecondaryContainer) }
    ) {
        TextField(
            value = state ?: "",
            label = { Text(stringResource(R.string.country_code)) },
            placeholder = { Text(stringResource(R.string.country_code_hint)) },
            onValueChange = { state = it },
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewDatePickerModal() {
    CountryInputModal({}, {})
}