package rocks.poopjournal.vacationdays.presentation.screen.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import rocks.poopjournal.vacationdays.data.VacationData
import rocks.poopjournal.vacationdays.domain.repo.VacationRepository
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val vacationRepository: VacationRepository
) : ViewModel() {

    fun addVacation(vacationData: VacationData) {
        if (vacationData.name.isEmpty() || vacationData.startDate.isEmpty()) {
            return
        }
        viewModelScope.launch {
            vacationRepository.insertData(vacationData)
        }
    }
}