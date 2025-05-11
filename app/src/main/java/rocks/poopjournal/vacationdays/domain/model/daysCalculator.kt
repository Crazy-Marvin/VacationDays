package rocks.poopjournal.vacationdays.domain.model

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit

private fun DayOfWeek.isWeekend() = (this == DayOfWeek.SATURDAY) || (this == DayOfWeek.SUNDAY)

fun calculateDaysBetween(
    startDate: LocalDate,
    endDate: LocalDate?,
    excludeWeekends: Boolean = false
): Int {
    return if (endDate == null) {
        if (excludeWeekends && startDate.dayOfWeek.isWeekend()) 0 else 1 // If no end date, count it as 1 day
    } else {
        val days = ChronoUnit.DAYS.between(startDate, endDate).toInt() + 1

        if (!excludeWeekends) {
            return days
        }

        // Calculate number of complete weeks
        val fullWeeks = days / 7
        var weekendDays = fullWeeks * 2

        // Handle remaining days after full weeks
        val remainingDays = days % 7
        val startDayOfWeek = startDate.dayOfWeek.value

        for (i in 0 until remainingDays) {
            val dayOfWeek = (startDayOfWeek + i - 1) % 7 + 1
            if (dayOfWeek == DayOfWeek.SATURDAY.value || dayOfWeek == DayOfWeek.SUNDAY.value) {
                weekendDays++
            }
        }

        return days - weekendDays
    }
}