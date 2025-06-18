package rocks.poopjournal.vacationdays.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import rocks.poopjournal.vacationdays.data.VacationData
import rocks.poopjournal.vacationdays.domain.repo.VacationRepository
import rocks.poopjournal.vacationdays.presentation.ui.utils.ThemeSetting
import rocks.poopjournal.vacationdays.domain.usecase.VacationDataUseCase
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val vacationRepository: VacationRepository,
    val themeSetting: ThemeSetting,
    val vacationsUseCase: VacationDataUseCase,
) : ViewModel() {

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