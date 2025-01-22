package rocks.poopjournal.vacationdays.presentation.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.core.yearMonth
import rocks.poopjournal.vacationdays.presentation.component.ContinuousSelectionHelper.getSelection
import rocks.poopjournal.vacationdays.presentation.ui.theme.primary
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalenderView(
    dateSelected: (startDate: LocalDate, endDate: LocalDate) -> Unit = { _, _ -> },
    isRangeSelection: Boolean = false // Flag to toggle between range or single selection
) {
    val currentMonth = YearMonth.now()
    val startMonth = currentMonth
    val endMonth = currentMonth.plusMonths(12)

    val today = remember { LocalDate.now() }
    var selection by remember { mutableStateOf(DateSelection()) }
    val daysOfWeek = remember { daysOfWeek() }

    MaterialTheme(colorScheme = MaterialTheme.colorScheme.copy(primary = primary)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            Column {
                val state = rememberCalendarState(
                    startMonth = startMonth,
                    endMonth = endMonth,
                    firstVisibleMonth = currentMonth,
                    firstDayOfWeek = firstDayOfWeekFromLocale(),
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
                                isRangeSelection = isRangeSelection
                            ) { day ->
                                if (day.position == DayPosition.MonthDate && (day.date == today || day.date.isAfter(today))) {
                                    selection = if (isRangeSelection) {
                                        ContinuousSelectionHelper.getSelection(
                                            clickedDate = day.date,
                                            dateSelection = selection,
                                        )
                                    } else {
                                        DateSelection(startDate = day.date, endDate = null)
                                    }

                                    // Only call dateSelected if the startDate is non-null (safety check)
                                    selection.startDate?.let { startDate ->
                                        dateSelected(startDate, selection.endDate ?: startDate)
                                    }
                                }
                            }
                        }
                    },
                    monthHeader = { month ->
                        MonthHeader(month)
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
    onClick: (CalendarDay) -> Unit,
    ) {
    val isSelectedStart = day.date == selection.startDate
    val isSelectedEnd = day.date == selection.endDate
    val isInRange = selection.startDate != null && selection.endDate != null &&
            day.date.isAfter(selection.startDate) && day.date.isBefore(selection.endDate)

    // Define the background color based on selection
    val backgroundColor = when {
        isSelectedStart || isSelectedEnd -> MaterialTheme.colorScheme.primary
        isInRange -> MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        else -> Color.Transparent
    }

    // Define text color for the date
    val textColor = when {
        isRangeSelection -> {
            // Text is white for start or end selection in range mode
            if (isSelectedStart || isSelectedEnd) Color.White else MaterialTheme.colorScheme.onBackground
        }
        else -> {
            // Today's date uses primary color, others are onBackground
            if (day.date == today) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
        }
    }

    // Define the dot color for today's date
    val dotColor = if (day.date == today) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.Gray
    }
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .background(color = backgroundColor, shape = CircleShape)
            .clickable(
                enabled = day.position == DayPosition.MonthDate && day.date >= today,
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
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(15.dp))
            Divider()
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
fun DayOfWeek.displayText(uppercase: Boolean = false, narrow: Boolean = false): String {
    val style = if (narrow) TextStyle.NARROW else TextStyle.SHORT
    return getDisplayName(style, Locale.ENGLISH).let { value ->
        if (uppercase) value.uppercase(Locale.ENGLISH) else value
    }
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

@RequiresApi(Build.VERSION_CODES.O)
private val rangeFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

@RequiresApi(Build.VERSION_CODES.O)
fun dateRangeDisplayText(startDate: LocalDate, endDate: LocalDate): String {
    return "Selected: ${rangeFormatter.format(startDate)} - ${rangeFormatter.format(endDate)}"
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun isInDateBetweenSelection(
        inDate: LocalDate,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Boolean {
        if (startDate.yearMonth == endDate.yearMonth) return false
        if (inDate.yearMonth == startDate.yearMonth) return true
        val firstDateInThisMonth = inDate.yearMonth.nextMonth.atStartOfMonth()
        return firstDateInThisMonth in startDate..endDate && startDate != firstDateInThisMonth
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isOutDateBetweenSelection(
        outDate: LocalDate,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Boolean {
        if (startDate.yearMonth == endDate.yearMonth) return false
        if (outDate.yearMonth == endDate.yearMonth) return true
        val lastDateInThisMonth = outDate.yearMonth.previousMonth.atEndOfMonth()
        return lastDateInThisMonth in startDate..endDate && endDate != lastDateInThisMonth
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(heightDp = 800)
@Composable
private fun Example2Preview() {
    CalenderView()
}
