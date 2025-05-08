package rocks.poopjournal.vacationdays.presentation.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import rocks.poopjournal.vacationdays.data.VacationData
import rocks.poopjournal.vacationdays.domain.model.VacData
import rocks.poopjournal.vacationdays.domain.model.calculateDaysBetween
import rocks.poopjournal.vacationdays.domain.repo.VacationNumberRepository
import rocks.poopjournal.vacationdays.domain.repo.VacationRepository
import rocks.poopjournal.vacationdays.presentation.ui.utils.ThemeSetting
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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

            VacData.Success(
                vacations = data.sortedBy { LocalDate.parse(it.startDate, formatter) },
                vacationDays = vacationDaysCount,
                sickDays = sickDaysCount,
                vacationsNumber = maxVacationNumber
            )
        }
            .stateIn(
                scope = scope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = VacData.Empty
            )
}