package rocks.poopjournal.vacationdays.presentation.screen.add

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import rocks.poopjournal.vacationdays.data.VacationData
import rocks.poopjournal.vacationdays.domain.repo.VacationRepository
import rocks.poopjournal.vacationdays.presentation.ui.utils.ThemeSetting
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val vacationRepository: VacationRepository
) : ViewModel() {

    @Inject
    lateinit var themeSetting: ThemeSetting

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