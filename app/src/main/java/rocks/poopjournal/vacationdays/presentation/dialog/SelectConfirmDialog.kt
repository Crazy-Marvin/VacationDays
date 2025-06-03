package rocks.poopjournal.vacationdays.presentation.dialog


import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark

@Composable
fun SelectConfirmDialog(
    onDismissRequest: () -> Unit = {},
    confirmButton: @Composable () -> Unit = {},
    dismissButton: @Composable () -> Unit = {},
    title: @Composable () -> Unit = {},
    body: @Composable () -> Unit = {}
) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.onSecondary,
        onDismissRequest = onDismissRequest,
        title = title,
        text = body,
        confirmButton = confirmButton,
        dismissButton = dismissButton,
    )
}

@PreviewLightDark
@Composable
private fun PreviewSelectConfirmDialog() {
    SelectConfirmDialog()
}