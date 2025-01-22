package rocks.poopjournal.vacationdays.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import rocks.poopjournal.vacationdays.data.VacationData
import rocks.poopjournal.vacationdays.domain.repo.VacationRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val vacationRepository: VacationRepository
) : ViewModel() {
    private val _holidays = MutableStateFlow<List<VacationData>>(emptyList())
    val holidays: StateFlow<List<VacationData>> = _holidays

    init {
        fetchHolidays()
    }

    private fun fetchHolidays() {
        viewModelScope.launch {
            vacationRepository.getAllData().collect { data ->
                    _holidays.value = data
                }
        }
    }

}