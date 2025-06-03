package rocks.poopjournal.vacationdays.presentation.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import rocks.poopjournal.vacationdays.domain.model.VacData
import rocks.poopjournal.vacationdays.domain.model.calculateDaysBetween
import rocks.poopjournal.vacationdays.domain.repo.HolidaysRepository
import rocks.poopjournal.vacationdays.domain.repo.VacationNumberRepository
import rocks.poopjournal.vacationdays.domain.repo.VacationRepository
import rocks.poopjournal.vacationdays.presentation.ui.utils.ThemeSetting
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VacationDataUseCase @Inject constructor(
    scope: CoroutineScope,
    vacationRepository: VacationRepository,
    holidaysRepository: HolidaysRepository,
    vacationNumberRepository: VacationNumberRepository,
    themeSetting: ThemeSetting
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    val vacationsFlow: SharedFlow<VacData> =
        combine(
            vacationRepository.getAllData(),
            themeSetting.isExcludeWeekendsFlow
        ) { data, isExcludeWeekends -> data to isExcludeWeekends }
            .flatMapLatest {
                // obtain holidays for all the years in the data
                val (data, excludeWeekends) = it
                data.flatMap {
                    val (start, end) = it.parsedDates
                    if (end != null) listOf(start.year, end.year) else listOf(start.year)
                }
                    .distinct()
                    .asFlow()
                    .flatMapConcat { holidaysRepository.getYearsHolidays(it) }
                    .map { holidays -> Triple(data, excludeWeekends, holidays) }
            }.map {
                val (data, isExcludeWeekends, holidays) = it
                val holidayDates = holidays.map { it.localDate }

                val vacationDaysCount = data
                    .filter { it.category == "Vacation" }
                    .sumOf {
                        val (start, end) = it.parsedDates
                        calculateDaysBetween(start, end, isExcludeWeekends, holidayDates)
                    }

                val sickDaysCount = data
                    .filter { it.category == "Sick" }
                    .sumOf {
                        val (start, end) = it.parsedDates
                        calculateDaysBetween(start, end, isExcludeWeekends, holidayDates)
                    }

                val currentYear = LocalDate.now().year.toString()
                val vacationNumber = vacationNumberRepository.getVacationNumberForYear(currentYear)
                val maxVacationNumber = maxOf(vacationNumber - vacationDaysCount, 0)

                VacData.Success(
                    vacations = data.sortedBy { it.parsedDates.first },
                    vacationDays = vacationDaysCount,
                    sickDays = sickDaysCount,
                    vacationsNumber = maxVacationNumber,
                    holidays = holidays,
                )
            }
            .stateIn(
                scope = scope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = VacData.Empty
            )
}