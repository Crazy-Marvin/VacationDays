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

@HiltViewModel
class AddViewModel @Inject constructor(
    private val vacationRepository: VacationRepository,
    val themeSetting: ThemeSetting,
    val vacationsUseCase: VacationDataUseCase,
) : ViewModel() {


    fun addVacation(vacationData: VacationData) {
        if (vacationData.name.isEmpty()) {
            return
        }
        if(vacationData.startDate.isEmpty()){
            return
        }
        viewModelScope.launch {
            vacationRepository.insertData(vacationData)
        }
    }
}