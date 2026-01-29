package rocks.poopjournal.vacationdays.presentation.screen.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rocks.poopjournal.vacationdays.data.VacationData
import rocks.poopjournal.vacationdays.domain.repo.VacationRepository
import rocks.poopjournal.vacationdays.domain.usecase.VacationDataUseCase
import rocks.poopjournal.vacationdays.presentation.ui.utils.ThemeSetting
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    private val vacationRepository: VacationRepository,
    val themeSetting: ThemeSetting,
    val vacationsUseCase: VacationDataUseCase,
    ) : ViewModel() {

    private val _uiState = MutableStateFlow(EditUiState())
    val uiState: StateFlow<EditUiState> = _uiState.asStateFlow()
    private val formatter = DateTimeFormatter.ofPattern("d/MM/yyyy")

    fun loadVacation(vacationId: Int) {
        viewModelScope.launch {
            vacationRepository.getData(vacationId)
                .collectLatest { vacation ->
                    _uiState.update {
                        it.copy(
                            vacationId = vacation.id,
                            name = vacation.name,
                            startDate = LocalDate.parse(vacation.startDate,formatter),
                            endDate = vacation.endDate?.let { date ->
                                LocalDate.parse(date, formatter)
                            },
                            category = if (vacation.category == "Sick")
                                VacationCategory.Sick else VacationCategory.Vacation,
                            isInitialized = true
                        )
                    }
                }
        }
    }

    fun onNameChange(name: String) =
        _uiState.update { it.copy(name = name) }

    fun onCategoryChange(category: VacationCategory) =
        _uiState.update { it.copy(category = category) }


    fun onDateSelected(start: LocalDate, end: LocalDate?) =
        _uiState.update { it.copy(startDate = start, endDate = end) }

    fun updateVacation() {
        val state = _uiState.value
        if (state.startDate == null) return

        if (state.name.isEmpty()) {
            return
        }
        viewModelScope.launch {
            vacationRepository.updateData(
                VacationData(
                    id = state.vacationId,
                    name = state.name,
                    startDate = state.startDate.format(formatter),
                    endDate = state.endDate?.format(formatter),
                    category = state.category.name
                )
            )
        }
    }
}