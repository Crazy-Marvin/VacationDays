package rocks.poopjournal.vacationdays.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import rocks.poopjournal.vacationdays.presentation.ui.theme.MyVacationDays2Theme

@Composable
fun TopBarWithBackButton(
    text: String,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp)
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp),
        ) {
            IconButton(
                onClick = { onClose() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Text(
                text = text,
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewTopBar() {
    MyVacationDays2Theme {
        TopBarWithBackButton("Text", {})
    }
}