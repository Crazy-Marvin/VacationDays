package rocks.poopjournal.vacationdays.presentation.screen.holidays

import android.annotation.SuppressLint
import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import rocks.poopjournal.vacationdays.R
import rocks.poopjournal.vacationdays.data.Holiday
import rocks.poopjournal.vacationdays.presentation.component.YearDropDown
import rocks.poopjournal.vacationdays.presentation.ui.theme.MyVacationDays2Theme
import java.time.format.DateTimeFormatter


@Composable
fun HolidaysScreen(
    onClose: () -> Unit = {},
    years: List<Int>,
    selectedYear: Int,
    onYearSelected: (Int) -> Unit,
    onAddHoliday: (Long, String) -> Unit,
    holidays: List<Holiday> = emptyList(),
) {
    var isAddDialogOpen by remember { mutableStateOf(false) }

    val monthdayFormat = DateTimeFormatter.ofPattern("${DateFormat.MONTH} ${DateFormat.DAY}")
    val weekdayFormat = DateTimeFormatter.ofPattern(DateFormat.WEEKDAY)

    Scaffold(
        topBar = { TopBar(onClose = onClose) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isAddDialogOpen = true },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.background
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            YearDropDown(
                selectedYear = selectedYear,
                years = years,
                onYearSelected = onYearSelected
            )
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                if (holidays.isEmpty()) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp)
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Text("No holidays for given year", textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                }
                items(holidays, key = { it.id }) { holiday ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = holiday.name,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                            // Local name maybe ?
//                            Text(
//                                text = holiday.year,
//                                style = MaterialTheme.typography.bodyMedium,
//                                color = MaterialTheme.colorScheme.secondary
//                            )
                        }

                        Column {
                            Text(
                                text = monthdayFormat.format(holiday.localDate),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Right,
                            )

                            Text(
                                text = weekdayFormat.format(holiday.localDate),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.secondary,
                                textAlign = TextAlign.Right,
                            )
                        }
                    }
                }
            }
        }

        if (isAddDialogOpen) {
            AddHolidayDialog(
                onDismissRequest = { isAddDialogOpen = false },
                year = selectedYear,
                onAddHoliday = onAddHoliday
            )
        }
    }
}

@Composable
fun HolidaysScreen(
    viewModel: HolidaysViewModel = hiltViewModel(),
    onClose: () -> Unit,
) {
    val selectedYear by viewModel.selectedYear.collectAsState()
    val holidays by viewModel.yearsHolidays.collectAsStateWithLifecycle(emptyList())

    HolidaysScreen(
        onClose = onClose,
        years = selectedYear.second,
        selectedYear = selectedYear.first,
        onYearSelected = { viewModel.selectYear(it) },
        onAddHoliday = { dt, name -> viewModel.addHoliday(dt, name) },
        holidays = holidays
    )
}


@Composable
private fun TopBar(onClose: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp)
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = { onClose() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Text(
                text = stringResource(id = R.string.holidays),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}


@SuppressLint("SimpleDateFormat")
@Composable
private fun AddHolidayDialog(
    onDismissRequest: () -> Unit,
    year: Int,
    onAddHoliday: (Long, String) -> Unit,
) {
    val displayFormat = SimpleDateFormat("MMMM d")

    var dateValue by remember { mutableStateOf<Long?>(null) }
    var nameValue by remember { mutableStateOf<String?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    SelectConfirmDialog(
        onDismissRequest,
        confirmButton = {
            TextButton(
                enabled = dateValue != null && nameValue != null,
                onClick = {
                    onAddHoliday(dateValue!!, nameValue!!)
                    onDismissRequest()
                },
                modifier = Modifier.padding(8.dp),
            ) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismissRequest() },
                modifier = Modifier.padding(8.dp),
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    ) {
        Text("New Holiday")
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = dateValue?.let { displayFormat.format(it) } ?: "",
            label = { Text("Date") },
            onValueChange = { },
            modifier = Modifier.pointerInput(dateValue) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showDatePicker = true
                    }
                }
            }
        )

        TextField(
            value = nameValue ?: "",
            label = { Text("Name") },
            onValueChange = { nameValue = it }
        )

        if (showDatePicker) {
            DatePickerModal({ dateValue = it }, { showDatePicker = false }, onlyYear = year)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
    onlyYear: Int? = null,
) {
    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableYear(year: Int): Boolean {
                return onlyYear?.let { year == it } ?: true
            }
        }
    )

    SelectConfirmDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun SelectConfirmDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable () -> Unit,
    block: @Composable () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                block()

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Absolute.Right,
                ) {
                    dismissButton()
                    confirmButton()
                }
            }
        }
    }
}

@Composable
@PreviewLightDark
fun PreviewHolidayScreen() {
    MyVacationDays2Theme {
        HolidaysScreen(
            years = (2000..2050).toList(),
            selectedYear = 2025,
            onYearSelected = {},
            onAddHoliday = { _, _ -> },
            holidays = listOf(
                Holiday(id=0, date="2025-01-03", name="Some", year="2025"),
                Holiday(id=1, date="2025-05-02", name="Public", year="2025"),
            )
        )
    }
}