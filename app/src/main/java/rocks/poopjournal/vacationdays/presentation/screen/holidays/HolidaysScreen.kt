package rocks.poopjournal.vacationdays.presentation.screen.holidays

import android.annotation.SuppressLint
import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import rocks.poopjournal.vacationdays.R
import rocks.poopjournal.vacationdays.data.Holiday
import rocks.poopjournal.vacationdays.domain.model.isWeekend
import rocks.poopjournal.vacationdays.presentation.component.ExpandableFAB
import rocks.poopjournal.vacationdays.presentation.component.SwipeToDismissListItem
import rocks.poopjournal.vacationdays.presentation.component.TopBarWithBackButton
import rocks.poopjournal.vacationdays.presentation.component.YearDropDown
import rocks.poopjournal.vacationdays.presentation.dialog.CountryInputModal
import rocks.poopjournal.vacationdays.presentation.dialog.DatePickerModal
import rocks.poopjournal.vacationdays.presentation.dialog.SelectConfirmDialog
import rocks.poopjournal.vacationdays.presentation.ui.theme.MyVacationDays2Theme
import java.time.format.DateTimeFormatter


@Composable
fun HolidaysScreen(
    onClose: () -> Unit = {},
    years: List<Int>,
    selectedYear: Int,
    onYearSelected: (Int) -> Unit,
    onAddHoliday: (Long, String) -> Unit,
    onRemoveHoliday: (Holiday) -> Unit,
    onLoadCountryHolidays: (String, Int) -> Unit,
    holidays: List<Holiday> = emptyList(),
) {
    var isAddDialogOpen by remember { mutableStateOf(false) }
    var isPickCountryDialogOpen by remember { mutableStateOf(false) }

    val monthdayFormat = DateTimeFormatter.ofPattern("${DateFormat.MONTH} ${DateFormat.DAY}")
    val weekdayFormat = DateTimeFormatter.ofPattern(DateFormat.WEEKDAY)

    Scaffold(
        topBar = {
            TopBarWithBackButton(
                text = stringResource(id = R.string.holidays),
                onClose = onClose
            )
        },
        floatingActionButton = {
            ExpandableFAB(
                expandable = true,
                fabIcon = Icons.Default.Add,
                expandedHeight = 128.dp + 32.dp,
            ) { closeFab ->

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    TextButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        onClick = {
                            isAddDialogOpen = true
                            closeFab()
                        }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Icon(
                                imageVector = Icons.Default.Create,
                                contentDescription = "Add Holiday",
                            )
                            Text(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 16.dp),
                                textAlign = TextAlign.Left,
                                text = "Add Holiday"
                            )
                        }
                    }

                    TextButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        onClick = {
                            isPickCountryDialogOpen = true
                            closeFab()
                        }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Back",
                            )
                            Text(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 16.dp),
                                textAlign = TextAlign.Left,
                                text = "Load"
                            )
                        }
                    }
                }
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
                            Text(
                                stringResource(R.string.empty_holidays),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.33f)
                            )
                        }
                    }
                }
                items(holidays, key = { it.id }) { holiday ->
                    val dayColor = when (holiday.localDate.dayOfWeek.isWeekend) {
                        true -> MaterialTheme.colorScheme.error
                        false -> MaterialTheme.colorScheme.onSecondaryContainer
                    }.copy(alpha = 0.6f)

                    SwipeToDismissListItem(
                        onEndToStart = { onRemoveHoliday(holiday) },
                        content = {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(64.dp)
                                    .padding(horizontal = 16.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier.weight(2f),
                                        verticalArrangement = Arrangement.Top
                                    ) {
                                        Text(
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            text = holiday.name,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                    }

                                    Column(
                                        modifier = Modifier.weight(1f),
                                        horizontalAlignment = Alignment.End
                                    ) {
                                        Text(
                                            text = monthdayFormat.format(holiday.localDate),
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                                            textAlign = TextAlign.Right,
                                        )
                                    }
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Text(
                                        text = weekdayFormat.format(holiday.localDate),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = dayColor,
                                        textAlign = TextAlign.Right,
                                    )
                                }
                            }
                        })
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

        if (isPickCountryDialogOpen) {
            CountryInputModal(
                onDismiss = { isPickCountryDialogOpen = false },
                onCountrySelected = {
                    onLoadCountryHolidays(it, selectedYear)
                }
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
    val holidays by viewModel.yearsHolidays.collectAsState(emptyList())

    HolidaysScreen(
        onClose = onClose,
        years = selectedYear.second,
        selectedYear = selectedYear.first,
        onYearSelected = { viewModel.selectYear(it) },
        onAddHoliday = { dt, name -> viewModel.addHoliday(dt, name) },
        onRemoveHoliday = { viewModel.removeHoliday(it) },
        onLoadCountryHolidays = { code, year -> viewModel.loadCountryHolidays(code, year) },
        holidays = holidays
    )
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
                colors = ButtonDefaults.buttonColors().copy(
                    disabledContentColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                )
            ) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismissRequest() },
            ) {
                Text(stringResource(R.string.cancel))
            }
        },
        title = { Text("New Holiday", color= MaterialTheme.colorScheme.onSecondaryContainer) }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
        }

        if (showDatePicker) {
            DatePickerModal({ dateValue = it }, { showDatePicker = false }, onlyYear = year)
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
            onRemoveHoliday = { },
            onLoadCountryHolidays = { _, _ -> },
            holidays = listOf(
                Holiday(id = 0, date = "2025-01-03", name = "Some", year = "2025"),
                Holiday(id = 1, date = "2025-05-02", name = "Public", year = "2025"),
                Holiday(
                    id = 2,
                    date = "2025-05-03",
                    name = "Lorem ipsum and some other very long shit",
                    year = "2025"
                ),
            )
        )
    }
}