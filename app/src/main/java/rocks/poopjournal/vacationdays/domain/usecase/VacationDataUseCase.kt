package rocks.poopjournal.vacationdays.domain.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import rocks.poopjournal.vacationdays.data.VacationData
import rocks.poopjournal.vacationdays.domain.model.VacData
import rocks.poopjournal.vacationdays.domain.model.WidgetDisplayData
import rocks.poopjournal.vacationdays.domain.model.calculateDaysBetween
import rocks.poopjournal.vacationdays.domain.repo.VacationNumberRepository
import rocks.poopjournal.vacationdays.domain.repo.VacationRepository
import rocks.poopjournal.vacationdays.presentation.ui.utils.ThemeSetting
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VacationDataUseCase @Inject constructor(
    scope: CoroutineScope,
    vacationRepository: VacationRepository,
    vacationNumberRepository: VacationNumberRepository,
    themeSetting: ThemeSetting
) {
    private val formatter = DateTimeFormatter.ofPattern("d/MM/yyyy") // Match the saved format

    private fun VacationData.parsedDates(): Pair<LocalDate, LocalDate?> {
        val start = LocalDate.parse(startDate, formatter)
        val end = endDate?.let { LocalDate.parse(it, formatter) }
        return start to end
    }

    val vacationsFlow: SharedFlow<VacData> =
        combine(
            vacationRepository.getAllData(),
            themeSetting.isExcludeWeekendsFlow,
        ) { data, isExcludeHolidays ->

            val vacationDaysCount = data
                .filter { it.category == "Vacation" }
                .sumOf {
                    val (start, end) = it.parsedDates()
                    calculateDaysBetween(start, end, isExcludeHolidays)
                }

            val sickDaysCount = data
                .filter { it.category == "Sick" }
                .sumOf {
                    val (start, end) = it.parsedDates()
                    calculateDaysBetween(start, end, isExcludeHolidays)
                }

            val currentYear = LocalDate.now().year.toString()
            val vacationNumber = vacationNumberRepository.getVacationNumberForYear(currentYear)
            val maxVacationNumber = maxOf(vacationNumber - vacationDaysCount, 0)
            val hasExceeded = vacationDaysCount > vacationNumber

            VacData.Success(
                vacations = data.sortedBy { LocalDate.parse(it.startDate, formatter) },
                vacationDays = vacationDaysCount,
                sickDays = sickDaysCount,
                vacationsNumber = maxVacationNumber,
                excludeHolidays = isExcludeHolidays,
                hasExceededVacationLimit = hasExceeded
            )
        }
            .stateIn(
                scope = scope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = VacData.Empty
            )

    val widgetData: SharedFlow<WidgetDisplayData> =
        combine(
            vacationRepository.getAllData(),
            themeSetting.isExcludeWeekendsFlow,
        ) { data, isExcludeHolidays ->

            val now = LocalDate.now()

            // Vacation Days
            val vacationDays = data.filter { it.category == "Vacation" }
            val vacationDaysCount = vacationDays.sumOf {
                val (start, end) = it.parsedDates()
                calculateDaysBetween(start, end, isExcludeHolidays)
            }

            // Sick Days
            val sickDays = data.filter { it.category == "Sick" }
            val sickDaysCount = data
                .filter { it.category == "Sick" }
                .filter { LocalDate.parse(it.startDate, formatter).isBefore(now) }
                .sumOf {
                    val (start, end) = it.parsedDates()
                    calculateDaysBetween(start, end, isExcludeHolidays)
                }

            // Remaining Vacation
            val currentYear = now.year.toString()
            val vacationNumber = vacationNumberRepository.getVacationNumberForYear(currentYear)
            val remainingVacation = maxOf(vacationNumber - vacationDaysCount, 0)

            val yearStart = LocalDate.of(now.year, 1, 1)
            val yearEnd = LocalDate.of(now.year, 12, 31)

// Count total workdays in year, excluding weekends (and optionally holidays)


            // Future Vacation Countdown
            val upcomingVacation = vacationDays
                .map { LocalDate.parse(it.startDate, formatter) }
                .filter { !it.isBefore(now) } // includes today
                .minOrNull()

            val daysUntilNextVacation = upcomingVacation?.let {
                ChronoUnit.DAYS.between(now, it).toInt()
            }

            // Last Vacation and Sick
            val lastVacation = vacationDays
                .map { LocalDate.parse(it.startDate, formatter) }
                .filter { it.isBefore(now) }
                .maxOrNull()

            val lastSickDay = sickDays
                .map { LocalDate.parse(it.startDate, formatter) }
                .filter { it.isBefore(now) }
                .maxOrNull()

            val daysSinceLastVacation = lastVacation?.let {
                ChronoUnit.DAYS.between(it, now).toInt()
            }

            val daysSinceLastSickDay = lastSickDay?.let {
                ChronoUnit.DAYS.between(it, now).toInt()
            }

            val vacationDaysTaken = vacationDays
                .filter { LocalDate.parse(it.startDate, formatter).isBefore(now) || LocalDate.parse(it.startDate, formatter).isEqual(now) }
                .sumOf {
                    val (start, end) = it.parsedDates()
                    calculateDaysBetween(start, end, isExcludeHolidays)
                }

            val vacationDaysPlanned = vacationDays
                .filter { LocalDate.parse(it.startDate, formatter).isAfter(now) }
                .sumOf {
                    val (start, end) = it.parsedDates()
                    calculateDaysBetween(start, end, isExcludeHolidays)
                }
            val totalWorkingDaysInYear = generateSequence(yearStart) { it.plusDays(1) }
                .takeWhile { !it.isAfter(yearEnd) }
                .count { date ->
                    val isWeekend = date.dayOfWeek == DayOfWeek.SATURDAY || date.dayOfWeek == DayOfWeek.SUNDAY
                    !isWeekend || !isExcludeHolidays
                }

            val totalVacationDays = vacationDaysTaken + vacationDaysPlanned

            val actualWorkDays = maxOf(totalWorkingDaysInYear - totalVacationDays, 0)

            WidgetDisplayData(
                vacationDays = vacationDaysCount,
                sickDays = sickDaysCount,
                remainingVacationDays = remainingVacation,
                daysUntilNextVacation = daysUntilNextVacation,
                daysSinceLastVacation = daysSinceLastVacation,
                daysSinceLastSickDay = daysSinceLastSickDay,
                vacationDaysPlanned = vacationDaysPlanned,
                vacationDaysTaken = vacationDaysTaken,
                workDays = actualWorkDays

            )
        }.stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = WidgetDisplayData.Empty
        )



}

