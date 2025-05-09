package rocks.poopjournal.vacationdays.presentation.screen.holidays

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import rocks.poopjournal.vacationdays.data.Holiday
import rocks.poopjournal.vacationdays.domain.repo.HolidaysRepository
import rocks.poopjournal.vacationdays.domain.service.holidays.DateNagerAtService
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HolidaysViewModel @Inject constructor(
    private val repository: HolidaysRepository,
    private val remoteHolidaysService: DateNagerAtService,
) : ViewModel() {
    private fun Int.yearsRange() = (this - 30..this + 30).toList()

    var selectedYear = MutableStateFlow(LocalDate.now().year.let { it to it.yearsRange() })

    fun selectYear(year: Int) {
        selectedYear.value = year to year.yearsRange()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val yearsHolidays = selectedYear
        .flatMapLatest { repository.getYearsHolidays(it.first) }
        .shareIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
        )

    @SuppressLint("SimpleDateFormat")
    fun addHoliday(dt: Long, name: String) {
        val yearFormat = SimpleDateFormat("yyyy")
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        viewModelScope.launch {
            repository.insert(Holiday(
                year = yearFormat.format(dt),
                date = dateFormat.format(dt),
                name = name,
            ))
        }
    }

    fun removeHoliday(holiday: Holiday) {
        viewModelScope.launch {
            repository.delete(holiday)
        }
    }

    fun loadCountryHolidays(code: String, year: Int) {
        viewModelScope.launch {
            val response = remoteHolidaysService.getPublicHolidays(year, code)
            if (!response.isSuccessful) {
                return@launch
            }

            response
                .body()
                ?.map {
                    Holiday(name = it.name, date = it.date, year = it.localDate.year.toString())
                }?.also {
                    repository.insertAll(it)
                }
        }

        selectYear(selectedYear.value.first)
    }

}