package rocks.poopjournal.vacationdays.presentation.screen.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import rocks.poopjournal.vacationdays.data.VacationData
import rocks.poopjournal.vacationdays.domain.repo.VacationNumberRepository
import rocks.poopjournal.vacationdays.domain.repo.VacationRepository
import rocks.poopjournal.vacationdays.presentation.ui.utils.ThemeSetting
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val vacationRepository: VacationRepository,
    private val vacationNumberRepository: VacationNumberRepository
) : ViewModel() {

    @Inject
    lateinit var themeSetting: ThemeSetting

    private val _holidays = MutableStateFlow<List<VacationData>>(emptyList())
    val holidays: StateFlow<List<VacationData>> = _holidays

    private val _vacationDays = MutableStateFlow(0)
    val vacationDays: StateFlow<Int> = _vacationDays

    private val _sickDays = MutableStateFlow(0)
    val sickDays: StateFlow<Int> = _sickDays

    private val _totalHolidays = MutableStateFlow(0)
    val totalHolidays: StateFlow<Int> = _totalHolidays

    init {
        fetchHolidays()
        fetchVacationNumber()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchHolidays() {
        viewModelScope.launch {
            vacationRepository.getAllData().collect { data ->
                _holidays.value = data

                val vacationDaysCount = data
                    .filter { it.category == "Vacation" }
                    .sumOf { calculateDaysBetween(it.startDate, it.endDate) }

                val sickDaysCount = data
                    .filter { it.category == "Sick" }
                    .sumOf { calculateDaysBetween(it.startDate, it.endDate) }

                _vacationDays.value = vacationDaysCount
                _sickDays.value = sickDaysCount

                fetchVacationNumber()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateDaysBetween(startDate: String, endDate: String?): Int {
        val formatter = DateTimeFormatter.ofPattern("d/MM/yyyy") // Match the saved format
        val start = LocalDate.parse(startDate, formatter)

        return if (endDate.isNullOrEmpty()) {
            1 // If no end date, count it as 1 day
        } else {
            val end = LocalDate.parse(endDate, formatter)
            ChronoUnit.DAYS.between(start, end).toInt() + 1 // Include both start and end
        }
    }

    private fun fetchVacationNumber() {
        viewModelScope.launch {
            val currentYear = LocalDate.now().year.toString()
            val vacationNumber = vacationNumberRepository.getVacationNumberForYear(currentYear)

            _totalHolidays.value = maxOf(vacationNumber - _vacationDays.value, 0)
        }
    }

    fun deleteVacation(vacationData: VacationData){
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