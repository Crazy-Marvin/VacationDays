package rocks.poopjournal.vacationdays.presentation.screen.holidays

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import rocks.poopjournal.vacationdays.data.Holiday
import rocks.poopjournal.vacationdays.domain.repo.HolidaysRepository
import rocks.poopjournal.vacationdays.presentation.usecase.HolidaysUseCase
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HolidaysViewModel @Inject constructor(
    private val repository: HolidaysRepository,
    private val holidaysUseCase: HolidaysUseCase,
) : ViewModel() {

    private fun Int.yearsRange() = (this - 30..this + 30).toList()

    var selectedYear = MutableStateFlow(LocalDate.now().year.let { it to it.yearsRange() })

    fun selectYear(year: Int) {
        selectedYear.value = year to year.yearsRange()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val yearsHolidays = selectedYear
        .map { repository.getYearData(it.first) }
        .flattenConcat()
        .shareIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
        )

    @SuppressLint("SimpleDateFormat")
    fun addHoliday(dt: Long, name: String) {
        val yearFormat = SimpleDateFormat("yyyy")
        val format = SimpleDateFormat("yyyy-MM-dd")
        val dateStr = format.format(dt)

        viewModelScope.launch {
            repository.insertData(Holiday(
                year = yearFormat.format(dt),
                date = format.format(dt),
                name = name,
            ))
        }
    }
}