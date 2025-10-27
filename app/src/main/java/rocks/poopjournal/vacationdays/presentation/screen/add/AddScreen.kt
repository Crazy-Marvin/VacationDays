package rocks.poopjournal.vacationdays.presentation.screen.add

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import rocks.poopjournal.vacationdays.R
import rocks.poopjournal.vacationdays.data.VacationData
import rocks.poopjournal.vacationdays.domain.model.VacData
import rocks.poopjournal.vacationdays.presentation.component.CalenderView
import rocks.poopjournal.vacationdays.presentation.component.CustomTab
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddScreen(
    viewModel: AddViewModel = hiltViewModel(),
    navHostController: NavHostController,
    vacationId: Int,
) {

    var selectedTab by remember { mutableIntStateOf(1) }
    val context = LocalContext.current
    val formatter = remember { DateTimeFormatter.ofPattern("d/MM/yyyy") }

    val data by viewModel.vacationsUseCase.vacationsFlow.collectAsStateWithLifecycle(VacData.Empty)
    val editableVacation by viewModel.editableVacation.collectAsStateWithLifecycle(null)

    var vacationName by remember { mutableStateOf("") }
    var startDateString by remember { mutableStateOf("") }
    var endDateString by remember { mutableStateOf("") }
    val emptyVacation = stringResource(R.string.empty_vacation)
    val emptyDate = stringResource(R.string.empty_date)
    val isExceeded = (data as? VacData.Success)?.hasExceededVacationLimit == true
    val isEditMode = vacationId > 0

    val isShowWeekDaysHeader by viewModel.themeSetting.isShowWeekDaysHeaderFlow.collectAsStateWithLifecycle()

    LaunchedEffect(vacationId) {
        if (isEditMode) {
            viewModel.loadVacationForEdit(vacationId)
        } else {
            viewModel.clearEditableVacation()
        }
    }

    LaunchedEffect(editableVacation?.id) {
        editableVacation?.let { vacation ->
            vacationName = vacation.name
            startDateString = vacation.startDate
            endDateString = vacation.endDate ?: ""
            selectedTab = if (vacation.category == "Sick") 0 else 1
        }
    }

    val initialStartDate = remember(editableVacation?.startDate) {
        editableVacation?.startDate
            ?.takeIf { it.isNotEmpty() }
            ?.let { LocalDate.parse(it, formatter) }
    }
    val initialEndDate = remember(editableVacation?.endDate) {
        editableVacation?.endDate
            ?.takeIf { !it.isNullOrEmpty() }
            ?.let { LocalDate.parse(it, formatter) }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        TopBar(
            selectedTab = selectedTab,
            onTabSelected = { index -> selectedTab = index },
            vacationName = vacationName,
            onNameChange = { vacationName = it },
            onCheckClick = onCheckClick@{
                if (vacationName.isEmpty()) {
                    Toast.makeText(context, emptyVacation, Toast.LENGTH_SHORT).show()
                } else if (startDateString.isEmpty()) {
                    Toast.makeText(context, emptyDate, Toast.LENGTH_SHORT).show()
                } else {
                    val category = if (selectedTab == 0) "Sick" else "Vacation"
                    if (isEditMode) {
                        val baseData = editableVacation ?: return@onCheckClick
                        val updatedVacation = baseData.copy(
                            name = vacationName,
                            startDate = startDateString,
                            endDate = endDateString.ifEmpty { null },
                            category = category
                        )
                        viewModel.updateVacation(updatedVacation)
                    } else {
                        val vacationData = VacationData(
                            name = vacationName,
                            startDate = startDateString,
                            endDate = endDateString.ifEmpty { null },
                            category = category
                        )
                        viewModel.addVacation(vacationData)
                    }
                    navHostController.popBackStack()
                }
            },
            onCloseClick = {
                navHostController.popBackStack()
            },
            isSickEnabled = viewModel.themeSetting.isFeatureEnabled,
            isExceedLimit = isExceeded
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            CalenderView(
                holidays = when (val _data = data) {
                    is VacData.Empty -> emptyList()
                    is VacData.Success -> _data.vacations
                },
                isRangeSelection = true,
                dateSelected = { startDate, endDate ->
                    startDateString = startDate.format(formatter)
                    endDateString = endDate?.format(formatter) ?: ""
                },
                showWeekDaysHeader = isShowWeekDaysHeader,
                focusOnDate = initialStartDate ?: LocalDate.now(),
                initialStartDate = initialStartDate,
                initialEndDate = initialEndDate
            )
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
    isExceedLimit: Boolean,
    isSickEnabled: Boolean
) {
    val sick = stringResource(R.string.sick)
    val vacation = stringResource(R.string.vacation)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
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
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = vacationName,
                onValueChange = { onNameChange(it) },
                placeholder = {
                    Text(
                        text = stringResource(R.string.holiday),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                },
                label = {
                    Text(
                        text = stringResource(R.string.name),
                        style = MaterialTheme.typography.labelSmall
                    )
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
            )
        }

        if (isSickEnabled) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomTab(
                    items = listOf(sick, vacation),
                    selectedItemIndex = selectedTab,
                    onClick = { index -> onTabSelected(index) },
                )
            }
        }
        if (isExceedLimit) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.outlineVariant,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_problem),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            stringResource(R.string.exceed_vacation_title),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(start = 40.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            stringResource(R.string.exceed_vacation_description),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}
