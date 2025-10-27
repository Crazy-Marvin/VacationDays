package rocks.poopjournal.vacationdays.presentation.screen.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import rocks.poopjournal.vacationdays.data.VacationData
import rocks.poopjournal.vacationdays.domain.repo.VacationRepository
import rocks.poopjournal.vacationdays.presentation.ui.utils.ThemeSetting
import rocks.poopjournal.vacationdays.domain.usecase.VacationDataUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull

@HiltViewModel
class AddViewModel @Inject constructor(
    private val vacationRepository: VacationRepository,
    val themeSetting: ThemeSetting,
    val vacationsUseCase: VacationDataUseCase,
) : ViewModel() {

    private val _editableVacation = MutableStateFlow<VacationData?>(null)
    val editableVacation = _editableVacation.asStateFlow()

    fun loadVacationForEdit(id: Int) {
        if (id <= 0) {
            return
        }
        viewModelScope.launch {
            val vacation = vacationRepository.getData(id).firstOrNull()
            _editableVacation.value = vacation
        }
    }

    fun clearEditableVacation() {
        _editableVacation.value = null
    }

    fun addVacation(vacationData: VacationData) {
        if (vacationData.name.isEmpty()) {
            return
        }
        if (vacationData.startDate.isEmpty()) {
            return
        }
        viewModelScope.launch {
            vacationRepository.insertData(vacationData)
        }
    }

    fun updateVacation(vacationData: VacationData) {
        if (vacationData.id <= 0) {
            return
        }
        if (vacationData.name.isEmpty()) {
            return
        }
        if (vacationData.startDate.isEmpty()) {
            return
        }
        viewModelScope.launch {
            vacationRepository.updateData(vacationData)
        }
    }
}
