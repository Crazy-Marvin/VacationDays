package rocks.poopjournal.vacationdays.presentation.screen.add

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import rocks.poopjournal.vacationdays.data.VacationData
import rocks.poopjournal.vacationdays.presentation.component.CalenderView
import rocks.poopjournal.vacationdays.presentation.component.CustomTab
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddScreen(viewModel: AddViewModel = hiltViewModel(), navHostController: NavHostController) {

    var selectedTab by remember { mutableIntStateOf(1) }

    var vacationName by remember { mutableStateOf("") }
    var startDateString by remember { mutableStateOf("") }
    var endDateString by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(
            selectedTab = selectedTab,
            onTabSelected = { index -> selectedTab = index },
            vacationName = vacationName,
            onNameChange = { vacationName = it },
            onCheckClick = {
                val vacationData = VacationData(
                    name = vacationName,
                    startDate = startDateString,
                    endDate = endDateString.ifEmpty { null },
                    category = if (selectedTab == 0) "Sick" else "Vacation"
                )
                viewModel.addVacation(vacationData)
                navHostController.popBackStack()
            },
            onCloseClick = {
                navHostController.popBackStack()
            },
            isSickEnabled = viewModel.themeSetting.isFeatureEnabled
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            CalenderView(isRangeSelection = true, dateSelected = { startDate, endDate ->
                startDateString = startDate.format(DateTimeFormatter.ofPattern("d/MM/yyyy"))
                endDateString = endDate?.format(DateTimeFormatter.ofPattern("d/MM/yyyy")) ?: ""
            })
        }
    }


}

@Composable
private fun TopBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    vacationName: String,
    onNameChange: (String) -> Unit,
    onCheckClick: () -> Unit,
    onCloseClick: () -> Unit,
    isSickEnabled : Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(196.dp)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        // Top row with Close and Check buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { onCloseClick() }) {
                Icon(
                    imageVector = Icons.Rounded.Clear,
                    contentDescription = "Clear",
                    tint = Color.White
                )
            }
            IconButton(onClick = { onCheckClick() }) { // Call onCheckClick when Check button is clicked
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Save",
                    tint = Color.White
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = vacationName,
                onValueChange = { onNameChange(it) },
                placeholder = {
                    Text(text = "Holiday", style = MaterialTheme.typography.bodyLarge, color = Color.White)
                },
                label = {
                    Text(text = "Name", style = MaterialTheme.typography.labelSmall)
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                    placeholderColor = Color.White,
                    textColor = Color.White,
                    cursorColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp) // Ensure proper height for the text field
            )
        }

        if(isSickEnabled) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomTab(
                    items = listOf("Sick", "Vacation"),
                    selectedItemIndex = selectedTab,
                    onClick = { index -> onTabSelected(index) },
                )
            }
        }
    }
}

