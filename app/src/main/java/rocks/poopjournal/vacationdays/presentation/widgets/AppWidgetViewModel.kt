package rocks.poopjournal.vacationdays.presentation.widgets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import rocks.poopjournal.vacationdays.domain.model.WidgetDisplayData
import rocks.poopjournal.vacationdays.domain.usecase.VacationDataUseCase
import javax.inject.Inject

class AppWidgetViewModel @Inject constructor(
    private val vacationDataUseCase: VacationDataUseCase
) : ViewModel() {
    private val _widgetData = MutableStateFlow<WidgetDisplayData?>(null)
    val widgetData: StateFlow<WidgetDisplayData?> = _widgetData

    val vacationStats = widgetData.map { data ->
        data?.let { it.vacationDays to it.remainingVacationDays }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val sickStats = widgetData.map { it?.sickDays }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val countdownToNextVacation = widgetData.map { it?.daysUntilNextVacation }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val lastVacationAndSickCounters = widgetData.map { data ->
        data?.let { it.daysSinceLastVacation to it.daysSinceLastSickDay }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val vacationDaysTaken = widgetData.map { data ->
        data?.vacationDaysTaken
    }.stateIn(viewModelScope, SharingStarted.Eagerly,null)

    val vacationDaysPlanned = widgetData.map {
        it?.vacationDaysPlanned
    }.stateIn(viewModelScope, SharingStarted.Eagerly,null)

    val remainingVacationDays = widgetData.map {
        it?.remainingVacationDays
    }.stateIn(viewModelScope, SharingStarted.Eagerly,null)

    val workDays = widgetData.map {
        it?.workDays
    }.stateIn(viewModelScope, SharingStarted.Eagerly,null)

    init {
        // Collect from use case and expose data as StateFlow
        viewModelScope.launch {
            vacationDataUseCase.widgetData.collectLatest { data ->
                _widgetData.value = data
            }
        }
    }
}