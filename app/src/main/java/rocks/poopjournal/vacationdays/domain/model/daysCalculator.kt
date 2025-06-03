package rocks.poopjournal.vacationdays.domain.model

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit

val DayOfWeek.isWeekend get() = (this == DayOfWeek.SATURDAY) || (this == DayOfWeek.SUNDAY)

fun calculateDaysBetween(
    startDate: LocalDate,
    endDate: LocalDate?,
    excludeWeekends: Boolean = false,
    excludeDates: List<LocalDate>? = null
): Int {
    return if (endDate == null) {
        val shouldExclude = (excludeWeekends && startDate.dayOfWeek.isWeekend) ||
                (excludeDates?.contains(startDate) == true)
        return if (shouldExclude) 0 else 1
    } else {
        val days = ChronoUnit.DAYS.between(startDate, endDate).toInt() + 1

        if (!excludeWeekends && excludeDates.isNullOrEmpty()) {
            return days
        }

        var weekendDays = 0
        if (excludeWeekends) {
            val fullWeeks = days / 7
            weekendDays += fullWeeks * 2

            val remainingDays = days % 7
            val startDayOfWeek = startDate.dayOfWeek.value

            for (i in 0 until remainingDays) {
                val dayOfWeek = (startDayOfWeek + i - 1) % 7 + 1
                if (dayOfWeek == DayOfWeek.SATURDAY.value || dayOfWeek == DayOfWeek.SUNDAY.value) {
                    weekendDays++
                }
            }
        }

        // Count how many of the excludeDates fall within range and are not already excluded as weekends
        val additionalExcluded = excludeDates?.count {
            !it.isBefore(startDate) && !it.isAfter(endDate) &&
                    (!excludeWeekends || !it.dayOfWeek.isWeekend)
        } ?: 0

        return days - weekendDays - additionalExcluded
    }
}

typealias SoftRange = Pair<LocalDate, LocalDate?>
typealias HardRange = Pair<LocalDate, LocalDate>

fun HardRange.isDateBetween(date: LocalDate) = !date.isBefore(first) && !date.isAfter(second)

fun mergeRanges(ranges: List<SoftRange>): List<HardRange> {
    val mergedRanges = mutableListOf<HardRange>()
    ranges.forEach { range ->
        val holidayStart = range.first
        val holidayEnd = range.second ?: range.first
        val subRange = mutableListOf<HardRange>()
        var isMerged = false
        for (range in mergedRanges) {
            val (existingStart, existingEnd) = range
            if (holidayStart.isBefore(existingEnd) && holidayEnd.isAfter(existingStart)) {
                val mergedStart = minOf(holidayStart, existingStart)
                val mergedEnd = maxOf(holidayEnd, existingEnd)
                subRange.add(mergedStart to mergedEnd)
                isMerged = true
            } else {
                subRange.add(range)
            }
        }
        if (isMerged) {
            mergedRanges.clear()
            mergedRanges.addAll(subRange)
        } else {
            mergedRanges.add(holidayStart to holidayEnd)
        }
    }

    return mergedRanges.toList()
}