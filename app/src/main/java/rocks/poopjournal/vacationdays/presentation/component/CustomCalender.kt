package rocks.poopjournal.vacationdays.presentation.component

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.daysOfWeek
import rocks.poopjournal.vacationdays.data.VacationData
import rocks.poopjournal.vacationdays.presentation.ui.theme.MyVacationDays2Theme
import rocks.poopjournal.vacationdays.presentation.ui.theme.primary
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalenderView(
    dateSelected: (startDate: LocalDate, endDate: LocalDate?) -> Unit = { _, _ -> },
    isRangeSelection: Boolean = false,
    holidays: List<VacationData> = emptyList()
) {
    val currentYear = YearMonth.now().year
    var selectedYear by remember { mutableIntStateOf(currentYear) }

    val startMonth = remember(selectedYear) { YearMonth.of(selectedYear, 1) }
    val endMonth = remember(selectedYear) { YearMonth.of(selectedYear, 12) }

    val today = remember { LocalDate.now() }
    var selection by remember { mutableStateOf(DateSelection()) }

    val years = (currentYear - 30..currentYear + 30).toList()
    var expanded by remember { mutableStateOf(false) }

    MaterialTheme(colorScheme = MaterialTheme.colorScheme.copy(primary = primary)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.TopEnd // Aligns the dropdown to the top-end
                ) {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        Box(
                            modifier = Modifier
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                                .width(150.dp)
                                .clickable { expanded = true }
                                .background(
                                    MaterialTheme.colorScheme.background,
                                    RoundedCornerShape(4.dp)
                                )
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(4.dp)
                                )
                                .padding(10.dp), // Padding for better spacing
                            contentAlignment = Alignment.Center // Centers text
                        ) {
                            Text(
                                text = selectedYear.toString(),
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                textAlign = TextAlign.Center
                            )
                        }

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            containerColor = MaterialTheme.colorScheme.background,
                        ) {
                            years.forEach { year ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            year.toString(),
                                            color = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                    },
                                    onClick = {
                                        selectedYear = year
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                val daysOfWeek = remember { daysOfWeek() }
                val state = rememberCalendarState(
                    startMonth = startMonth,
                    endMonth = endMonth,
                    firstVisibleMonth = startMonth,
                    firstDayOfWeek = daysOfWeek.first(),
                    outDateStyle = OutDateStyle.EndOfRow
                )

                VerticalCalendar(
                    state = state,
                    contentPadding = PaddingValues(bottom = 100.dp),
                    dayContent = { value ->
                        if (value.position == DayPosition.MonthDate) {
                            Day(
                                value,
                                today = today,
                                selection = selection,
                                isRangeSelection = isRangeSelection,
                                holidays = holidays
                            ) { day ->
                                    selection = if (isRangeSelection) {
                                        ContinuousSelectionHelper.getSelection(
                                            clickedDate = day.date,
                                            dateSelection = selection,
                                        )
                                    } else {
                                        DateSelection(startDate = day.date, endDate = null)
                                    }

                                    selection.startDate?.let { startDate ->
                                        if (isRangeSelection) {
                                            dateSelected(startDate, selection.endDate)
                                        } else {
                                            dateSelected(startDate, null)
                                        }
                                    }
                                }
                            }
                    },
                    monthHeader = { month ->
                        MonthHeader(month)
                        DaysOfWeekTitle(daysOfWeek)
                    },
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun Day(
    day: CalendarDay,
    today: LocalDate,
    selection: DateSelection,
    isRangeSelection: Boolean,
    holidays: List<VacationData>,
    onClick: (CalendarDay) -> Unit,
) {
    val isSelectedStart = day.date == selection.startDate
    val isSelectedEnd = day.date == selection.endDate
    val isInRange = selection.startDate != null && selection.endDate != null &&
            day.date.isAfter(selection.startDate) && day.date.isBefore(selection.endDate)

    val backgroundColor = when {
        isRangeSelection && (isSelectedStart || isSelectedEnd) -> MaterialTheme.colorScheme.primary // Highlight selection only in range mode
        isRangeSelection && isInRange -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) // Highlight range
        else -> Color.Transparent
    }

    val textColor = when {
        isRangeSelection && (isSelectedStart || isSelectedEnd) -> MaterialTheme.colorScheme.onSecondaryContainer // Ensure contrast when selected
        day.date == today -> MaterialTheme.colorScheme.surface // Highlight today's date with Surface color
        else -> MaterialTheme.colorScheme.onSecondaryContainer
    }

    val dotColor = when {
        day.date == today -> MaterialTheme.colorScheme.surface // Primary dot for today
        isHoliday(day.date, holidays) -> Color.Gray
        else -> Color.Transparent
    }
    Box(
        modifier = Modifier
            .aspectRatio(1.2f)
            .background(
                color = backgroundColor,
                shape = when {
                    (isSelectedStart && selection.endDate == null) -> CircleShape
                    isSelectedStart -> RoundedCornerShape(50, 0, 0, 50)
                    isSelectedEnd -> RoundedCornerShape(0, 50, 50, 0)
                    else -> RectangleShape
                }
            )
            .clickable(
                enabled = day.position == DayPosition.MonthDate &&
                        day.date.isAfter(today.minusYears(30)),
                onClick = { onClick(day) },
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = day.date.dayOfMonth.toString(),
                color = textColor,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
            )
            if (!isRangeSelection) {
                Text(text = ".", fontWeight = FontWeight.Bold, color = dotColor)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthHeader(calendarMonth: CalendarMonth) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
    ) {

        Column {
            Text(
                textAlign = TextAlign.Center,
                text = calendarMonth.yearMonth.displayText(short = true),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.surface,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Divider()
        }
    }
}

@Composable
fun DaysOfWeekTitle(daysOfWeek: List<DayOfWeek>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                style = MaterialTheme.typography.titleSmall,
                color = when (dayOfWeek) {
                    DayOfWeek.SATURDAY, DayOfWeek.SUNDAY -> MaterialTheme.colorScheme.surface
                    else ->  MaterialTheme.colorScheme.onSecondaryContainer
                },
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun YearMonth.displayText(short: Boolean = false): String {
    return "${this.month.displayText(short = short)} ${this.year}"
}

@RequiresApi(Build.VERSION_CODES.O)
fun Month.displayText(short: Boolean = true): String {
    val style = if (short) TextStyle.SHORT else TextStyle.FULL
    return getDisplayName(style, Locale.ENGLISH)
}


@RequiresApi(Build.VERSION_CODES.O)
data class DateSelection(val startDate: LocalDate? = null, val endDate: LocalDate? = null) {
    val daysBetween by lazy(LazyThreadSafetyMode.NONE) {
        if (startDate == null || endDate == null) {
            null
        } else {
            ChronoUnit.DAYS.between(startDate, endDate)
        }
    }
}

object ContinuousSelectionHelper {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getSelection(
        clickedDate: LocalDate,
        dateSelection: DateSelection,
    ): DateSelection {
        val (selectionStartDate, selectionEndDate) = dateSelection
        return if (selectionStartDate != null) {
            if (clickedDate < selectionStartDate || selectionEndDate != null) {
                DateSelection(startDate = clickedDate, endDate = null)
            } else if (clickedDate != selectionStartDate) {
                DateSelection(startDate = selectionStartDate, endDate = clickedDate)
            } else {
                DateSelection(startDate = clickedDate, endDate = null)
            }
        } else {
            DateSelection(startDate = clickedDate, endDate = null)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(heightDp = 800)
@Composable
private fun Example2Preview() {
    MyVacationDays2Theme {
        CalenderView()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun isHoliday(date: LocalDate, holidays: List<VacationData>): Boolean {
    val holidayRanges = mutableListOf<Pair<LocalDate, LocalDate>>()
    holidays.forEach { holiday ->
        try {
            val formatter = DateTimeFormatter.ofPattern("d/MM/yyyy")
            val holidayStart = LocalDate.parse(holiday.startDate, formatter)
            val holidayEnd = holiday.endDate?.let { LocalDate.parse(it, formatter) } ?: holidayStart
            val mergedHolidayRange = mutableListOf<Pair<LocalDate, LocalDate>>()
            var isMerged = false
            for (range in holidayRanges) {
                val (existingStart, existingEnd) = range
                if (holidayStart.isBefore(existingEnd) && holidayEnd.isAfter(existingStart)) {
                    val mergedStart = minOf(holidayStart, existingStart)
                    val mergedEnd = maxOf(holidayEnd, existingEnd)
                    mergedHolidayRange.add(mergedStart to mergedEnd)
                    isMerged = true
                } else {
                    mergedHolidayRange.add(range)
                }
            }
            if (isMerged) {
                holidayRanges.clear()
                holidayRanges.addAll(mergedHolidayRange)
            } else {
                holidayRanges.add(holidayStart to holidayEnd)
            }

        } catch (e: DateTimeParseException) {
            Log.e("Day", "Invalid date format: ${holiday.startDate}")
        }
    }

    for (range in holidayRanges) {
        val (start, end) = range
        if (!date.isBefore(start) && !date.isAfter(end)) {
            return true
        }
    }

    return false
}
