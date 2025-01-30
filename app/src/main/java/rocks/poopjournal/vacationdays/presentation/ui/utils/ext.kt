package rocks.poopjournal.vacationdays.presentation.ui.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
fun generateWeekDaysForMonth(yearMonth: YearMonth): List<List<CalendarDay>> {
    val firstDayOfMonth = yearMonth.atDay(1)
    val lastDayOfMonth = yearMonth.atEndOfMonth()

    // Start from the first day of the week containing the first day of the month
    val startOfCalendar = firstDayOfMonth.with(DayOfWeek.MONDAY)

    // End at the last day of the week containing the last day of the month
    val endOfCalendar = lastDayOfMonth.with(DayOfWeek.SUNDAY)

    val days = mutableListOf<CalendarDay>()

    // Generate all CalendarDay objects between startOfCalendar and endOfCalendar
    var current = startOfCalendar
    while (!current.isAfter(endOfCalendar)) {
        days.add(CalendarDay(date = current, position = DayPosition.MonthDate))
        current = current.plusDays(1)
    }

    // Group into weeks (List of List of CalendarDay)
    return days.chunked(7)
}
