package rocks.poopjournal.vacationdays.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import rocks.poopjournal.vacationdays.data.VacationData
import rocks.poopjournal.vacationdays.domain.model.VacData
import rocks.poopjournal.vacationdays.domain.repo.VacationNumberRepository
import rocks.poopjournal.vacationdays.domain.repo.VacationRepository
import rocks.poopjournal.vacationdays.presentation.ui.utils.ThemeSetting
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val vacationRepository: VacationRepository,
    private val vacationNumberRepository: VacationNumberRepository,
    val themeSetting: ThemeSetting
) : ViewModel() {

    private val formatter = DateTimeFormatter.ofPattern("d/MM/yyyy") // Match the saved format

    val dataFlow: SharedFlow<VacData> =
        combine(
            vacationRepository.getAllData(),
            themeSetting.isExcludeWeekendsFlow,
        ) { data, isExcludeHolidays ->

            val vacationDaysCount = data
                .filter { it.category == "Vacation" }
                .sumOf { calculateDaysBetween(it.startDate, it.endDate, isExcludeHolidays) }

            val sickDaysCount = data
                .filter { it.category == "Sick" }
                .sumOf { calculateDaysBetween(it.startDate, it.endDate, isExcludeHolidays) }

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
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = VacData.Empty
            )


    private fun calculateDaysBetween(
        startDate: String,
        endDate: String?,
        excludeWeekends: Boolean = false
    ): Int {
        val start = LocalDate.parse(startDate, formatter)

        return if (endDate.isNullOrEmpty()) {
            1 // If no end date, count it as 1 day
        } else {
            val end = LocalDate.parse(endDate, formatter)
            val days = ChronoUnit.DAYS.between(start, end).toInt()

            if (excludeWeekends) {
                val startW = start.dayOfWeek
                val endW = end.dayOfWeek
                val daysWithoutWeekends = days - 2 * ((days + startW.value) / 7)

                (daysWithoutWeekends
                        + (if (startW == DayOfWeek.SUNDAY) 1 else 0)
                        + (if (endW == DayOfWeek.SUNDAY) 1 else 0)
                        + 1 // include start and end
                        )
            } else
                days + 1 // Include both start and end
        }
    }

    fun deleteVacation(vacationData: VacationData) {
        viewModelScope.launch {
            vacationRepository.deleteData(vacationData)
        }
    }

    fun restoreVacation(vacation: VacationData) {
        viewModelScope.launch {
            vacationRepository.insertData(vacation) // Re-insert deleted vacation
        }
    }
}